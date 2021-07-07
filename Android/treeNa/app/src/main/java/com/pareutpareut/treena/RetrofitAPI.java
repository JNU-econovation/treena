package com.pareutpareut.treena;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface RetrofitAPI {
    @POST("qa")
    Call<Emotion> getEmotionList(@Body Emotion emotion);
}

