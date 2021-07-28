import json
import torch 
from transformers.modeling_electra import ElectraModel, ElectraPreTrainedModel
from transformers import ElectraTokenizer
from typing import Union, Optional

import numpy as np
from transformers.pipelines import ArgumentHandler
from transformers import (
    Pipeline,
    PreTrainedTokenizer,
    ModelCard
)
import torch.nn as nn
from torch.nn import BCEWithLogitsLoss

class MultiLabelPipeline(Pipeline):
	def __init__(
		self,
		model: Union["PreTrainedModel", "TFPreTrainedModel"],
		tokenizer: PreTrainedTokenizer,
		modelcard: Optional[ModelCard] = None,
		framework: Optional[str] = None,
		task: str = "",
		args_parser: ArgumentHandler = None,
		device: int = -1,
		binary_output: bool = False,
		threshold: float = 0.3):
		super().__init__(
			model=model,
			tokenizer=tokenizer,
			modelcard=modelcard,
			framework=framework,
			args_parser=args_parser,
			device=device,
			binary_output=binary_output,
			task=task)

		self.threshold = threshold

	def __call__(self, *args, **kwargs):
		outputs = super().__call__(*args, **kwargs)
		scores = 1 / (1 + np.exp(-outputs))  # Sigmoid
		results = []
		for item in scores:
			labels = []
			scores = []
			for idx, s in enumerate(item):
				if s > self.threshold:
					labels.append(self.model.config.id2label[idx])
					scores.append(s)
			results.append({"labels": labels, "scores": scores})
		return results


class ElectraForMultiLabelClassification(ElectraPreTrainedModel):
	def __init__(self, config):
		super().__init__(config)
		self.num_labels = config.num_labels
		self.electra = ElectraModel(config)
		self.dropout = nn.Dropout(config.hidden_dropout_prob)
		self.classifier = nn.Linear(config.hidden_size, self.config.num_labels)
		self.loss_fct = BCEWithLogitsLoss()

		self.init_weights()

	def forward(
            self,
            input_ids=None,
            attention_mask=None,
            token_type_ids=None,
            position_ids=None,
            head_mask=None,
            inputs_embeds=None,
            labels=None,
    ):
		discriminator_hidden_states = self.electra(
		input_ids, attention_mask, token_type_ids, position_ids, head_mask, inputs_embeds
        )
        
		pooled_output = discriminator_hidden_states[0][:, 0]
		pooled_output = self.dropout(pooled_output)
		logits = self.classifier(pooled_output)
		outputs = (logits,) + discriminator_hidden_states[1:]  # add hidden states and attention if they are here

		if labels is not None:
			loss = self.loss_fct(logits, labels)
			outputs = (loss,) + outputs
		
		return outputs  # (loss), logits, (hidden_states), (attentions)

def serverless_pipeline(context, model_path='./model'):
	
	tokenizer = ElectraTokenizer.from_pretrained(model_path)
	model = ElectraForMultiLabelClassification.from_pretrained(model_path)
	
	print("토크나이저,모델 로드 성공")
	goemotions = MultiLabelPipeline(model=model, tokenizer=tokenizer, threshold=0.3)
	print("파이프라인 연결")

	# 문장을 여러개로 쪼갠 뒤에 문장마다 들어있는 모든 감정을 추출하도록 처리

	print(context)

	return goemotions(context)
	
	

def handler(event, context):
	try:
		print(event, context)
		body = json.loads(event['body'])
		answer = serverless_pipeline(body['context'])
		print("answer :", answer)
		
		if len(answer[0]) == 0:
			result = ""
		else:
			result = answer[0]['labels'].pop()

		return {
			"statusCode" : 200,
			"headers": {
				'Content-Type': 'application/json',
				'Access-Control-Allow-Origin': '*',
				'Access-Control-Allow-Credentials': True
			},
			"body": json.dumps({'answer': result})
		}
	except Exception as e:
		print(repr(e))
		return {
			"statusCode": 500,
			"headers": {
				'Content-Type': 'application/json',
				'Access-Control-Allow-Origin': '*',
				"Access-Control-Allow-Credentials": True
			},
			"body": json.dumps({"error": repr(e)})
		}
	
			
