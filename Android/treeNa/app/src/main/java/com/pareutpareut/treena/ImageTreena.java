package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ImageTreena extends AppCompatActivity {
    long diaryNumber;
    int type;
    int id;
    ArrayList<EmotionCommentVO> commentList;
    String ment;
    String imageUrl;
    int treeLevel;
    String[][] imageUrlList = {{"https://postfiles.pstatic.net/MjAyMTA3MjlfMjk3/MDAxNjI3NTY5NTUzMTAz.tHQkyWj6HKs_9DqWg7G7SzlPSSZIOO8LNb3IR_6k4qkg.isURwZoao0V3ilRre9HocQXpDOoV879duT_3K8h8wmog.JPEG.hahahafb/mat1.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTY3/MDAxNjI3NTY5NDkwNDEz.-KWYD7q28xUVf1LqxueWmKqpXEUFykHJtjCqTG1FKu8g.365cLan1D0z32gGtNmKkwlur2OHbAlbtG8YrGtQiIHcg.JPEG.hahahafb/mat2.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTI5/MDAxNjI3NTcwNjA5NzYx.ryIXKHbswAXy-C9dz7wBJV-5HRUw8LJJHIpY9LgIPpcg.pQ_2XcfSOX8vIeo56kxXuZIVW3aFxlu9y5-TNPcA0dIg.JPEG.hahahafb/sunny3.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjU5/MDAxNjI3NTcwNjA5NzI4.XoTj3wgK6f_Rf_ridYxRUWiw6n84wapSLH6l47TnHzYg.oofamGFzfJq98RYSbH2C_G3wyvNZZUOEL4Qv0sVH5uUg.JPEG.hahahafb/sunny4.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjk1/MDAxNjI3NTY5Mzg5ODMw.bohfK72jTSDBJZGz1tSDrHb1tGcn2LXhoU1MFKT_DuAg.FTFtsqha3BD0JInhJ4JJsfyFdbI9bjhqpL4QyeesmmIg.JPEG.hahahafb/hug5.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfNzIg/MDAxNjI3NTY5Mzg5NzE2.3-rZb5-zn1_2DSMv5l6TQ5hUKn98CiFWYvCVZaVa3qkg.ulH-l9qTW07IQQhiajIJ8neQ4jQSoWTJG3uPXa15smQg.JPEG.hahahafb/hug6.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTY1/MDAxNjI3NTY5Mzg5ODQ1.s1irV3yvEo-vf4-8Tkpew0RehOdWPpmPmWpvd2qgQRAg.z17OPOGwq6xrrrAZoDWT8K4tjeQlGa1OIO-X8kdziSog.JPEG.hahahafb/hug7.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjA0/MDAxNjI3NTY5Mzg5ODU4.Ep7051aHq5dLElIOa3OSQHvb4IT40qijQsgu9iXHbLog.L1KwkRirLDVpVjf0zUgIZwS8gN-5j5b2Rci42yJCBPEg.JPEG.hahahafb/hug8.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjA1/MDAxNjI3NTY5Mzg5ODUy.jU6QeEN6IQVtt5b2m6yvZRhtzgzfF8zpdKKFgNIXksEg.jlBSmQDoDtXxc0odRIGkjvd2i-wPb9GvUC6x4iK0vOEg.JPEG.hahahafb/hug9.jpg?type=w966"},
            {"https://postfiles.pstatic.net/MjAyMTA3MjlfMjk3/MDAxNjI3NTY5NTUzMTAz.tHQkyWj6HKs_9DqWg7G7SzlPSSZIOO8LNb3IR_6k4qkg.isURwZoao0V3ilRre9HocQXpDOoV879duT_3K8h8wmog.JPEG.hahahafb/mat1.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTY3/MDAxNjI3NTY5NDkwNDEz.-KWYD7q28xUVf1LqxueWmKqpXEUFykHJtjCqTG1FKu8g.365cLan1D0z32gGtNmKkwlur2OHbAlbtG8YrGtQiIHcg.JPEG.hahahafb/mat2.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTIz/MDAxNjI3NTY5NDkwNDgy.TNtbjD0vvw_-XiswlNBMypTJwAkZnUXapgkgZ3x-3fgg.Fx-npVOg65Y02qfUSeO41nlUfxIOTcE6-Mzcqti07_gg.JPEG.hahahafb/mat3.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTA5/MDAxNjI3NTY5NDkwNDky.HT6oP2mCsFcWZk7wgqctYg355FXv5hd2FT95qvK7vdAg.PDFH8_yjRE0KLsj6RrfPtJzO_1DMUJ8mbmntML2O_68g.JPEG.hahahafb/mat4.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTUy/MDAxNjI3NTY5NDkwNDg1.FpQA4mi_nryjqq0cg_iqJORWGKBMLkbzyw03n4RTb0sg.XmUHswLZa9yTZt4fSDjlEFfCfZ0e-L0Sx3aLBdMYZbAg.JPEG.hahahafb/mat5.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjQ4/MDAxNjI3NTY5NDkwNDY3.70Wj7_QhDpZtg6GPFjqccvYhHusB4yxH5xui45wbLv8g.8GO0Uzw62ML67cv7FQVRIUMq1ELMomzhrIsVmFBSmSAg.JPEG.hahahafb/mat6.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjA2/MDAxNjI3NTY5NDkwNjgy.wD_8KMVR35DomnI3aaxZwLqhrgcWPoQccILYS1-bmf8g.sdV_2-PeUg-l0BH_JI3SV0qyGRugPQYiSPQB6CE36mgg.JPEG.hahahafb/mat7.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjIw/MDAxNjI3NTY5NDkwNzcw.yurCYn309bPvnC1xFR4oTGzsYoBBDL336uM4oeK9VWMg.B48WBp6xwdTiikBnTauaDvbAOWSrSskLWLhzrrdlKA4g.JPEG.hahahafb/mat8.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjk5/MDAxNjI3NTY5NDkwODU4.BXSA6lLaNZvs02CJswtH5gql48X6YSVoqvmlPPNVQmQg.uTLl-0m4YOIsb6Ek0Tu6fZ8vpYcxZmxkbcKGgiEd8tsg.JPEG.hahahafb/mat9.jpg?type=w966"},
            {"https://postfiles.pstatic.net/MjAyMTA3MjlfMTk2/MDAxNjI3NTcwNjA5NjUz.xK12FKhGZdJgjtWvvTpswS5ivHA-pb7oZiBR0pNPFdcg.dnIJoutUNOsBARzxZeIuvlAWJ_saIOB3jr2iv-QJjwwg.JPEG.hahahafb/sunny1.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTc4/MDAxNjI3NTcwNjA5NzU0.6UhZyW0KRhonxfUKJOoe4pFT_vHWA_8KOfLyzgniuKMg.0__KntzRRLvzafdlgTF3TqF98HHoLSnUHPbWs1H_sN4g.JPEG.hahahafb/sunny2.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTI5/MDAxNjI3NTcwNjA5NzYx.ryIXKHbswAXy-C9dz7wBJV-5HRUw8LJJHIpY9LgIPpcg.pQ_2XcfSOX8vIeo56kxXuZIVW3aFxlu9y5-TNPcA0dIg.JPEG.hahahafb/sunny3.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjU5/MDAxNjI3NTcwNjA5NzI4.XoTj3wgK6f_Rf_ridYxRUWiw6n84wapSLH6l47TnHzYg.oofamGFzfJq98RYSbH2C_G3wyvNZZUOEL4Qv0sVH5uUg.JPEG.hahahafb/sunny4.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjc2/MDAxNjI3NTcwNjA5NzQ4.piQPBLSMlE_Bv2eHJGlXNkG65vgUMTHKsdx8va_v0_Yg.UFx2Nvdbh38UE_vEKo2hxdnkyQywclFLi45i2a7eCnwg.JPEG.hahahafb/sunny5.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjA0/MDAxNjI3NTcwNjA5Nzgy.IZOu5YZyFLrR-0ID54ZNvkBsTOru0G96BX9jJkiZSMMg.H7lBxZLDBjjJl8FJUUbNWV2LvrSVKI_9b9Bt7RX42hUg.JPEG.hahahafb/sunny6.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTk3/MDAxNjI3NTcwNjEwMDg5.tIZc1bnlsl-rU2CDUMB8NrA8zlKYn3Ua5Vg8d2Tlyj0g.E4hk4OcTmRk_RVQwAFtUfZN4p_4pim5qKka2w3LlWVIg.JPEG.hahahafb/sunny7.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTU3/MDAxNjI3NTcwNjEwMTM2.CwEKltKp_KrvvxA-pqnbKIFlvJ1u_4TSdpDtLVCw_8kg.eG_gC5kxjfPo4ax4RSpoT4CNEfKuu2m5cAgVbFdKxsgg.JPEG.hahahafb/sunny8.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjE1/MDAxNjI3NTcwNjEwMjMx.Mw0onWFggdHV9MhSbpKnid7nDssesNJD2_mqeYJg8ukg.yrpjj0FQVg2cK_ZVCpJ1mcqkJ_PecDBiTdMMAjnfv8og.JPEG.hahahafb/sunny9.jpg?type=w966"},
            {"https://postfiles.pstatic.net/MjAyMTA3MzBfMjM4/MDAxNjI3NTcxNTk1MDI1.j_cPdxMpLCna7L7iBrndSUgJmB-kafmGndINdGi89dwg.5q1LywdZQ7PTribz3wI1s6dZwHezq_XihIN6aY-inZgg.JPEG.hahahafb/back1.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfNTYg/MDAxNjI3NTcxNTk1MTM4.1K7VsEV6NS9LvV25boXPaL-Q3gsZnnhLG2T44cvA6Rog.5j7lLyq1vZY52ZTBxq5zysxkCDSwUz20sokqoKccMKEg.JPEG.hahahafb/back2.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfNDIg/MDAxNjI3NTcxNTk1MDg0.muLO1AgX8ifGgukZZ2HhpahFFCMKWEqkUAskk7LyUCYg.K8yphT5J8L10P_Wb5U2qSPnqZX9FvbJkfcQKgISRBb8g.JPEG.hahahafb/back3.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjQ2/MDAxNjI3NTcxNTk1MTEw.DBJ20Gby0KviDlTNwwa-IxL3ur1lSvlPUeRp73DG200g.FjyVQxBhHUHmKosRqri241_BdD2j8DRU78oTHA1pOx8g.JPEG.hahahafb/back4.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjgz/MDAxNjI3NTcxNTk1MTMw.VJ956d5Pxf3IJS9Uvs8YM0Y0fbClvKUsTSepEJOEWRgg.Bd-TFNvUNgfTg52fHis0IbOK4UAzvVruelLkKszvVaYg.JPEG.hahahafb/back5.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMTQ2/MDAxNjI3NTcxNTk1MTQy.Zzuj-zXQlMIKbs9t8Lb28kIGmmgGA_lz9Xyr7O-IcF8g.Cygvst0mUL5IJAMeJ7tXsK1wgAt5JaF7-4oQlcB4iMYg.JPEG.hahahafb/back6.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjM5/MDAxNjI3NTcxNTk1NDE4.qxgyvt4_xId8vqvt7K5zSMZ-2plIyoZncyYllyDP5h0g.cr8ooVfhrOggkv6Uy0hcgJXM8kEJdHSdfs6iKqA7qwcg.JPEG.hahahafb/back7.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjQ5/MDAxNjI3NTcxNTk1NDg3.CllEMGuGVwUHt9GINyrpsndc03cHksLcL4T_RQz2nsQg._yO2AU6ZCTj_3hml1QYKqfWi4VVKtuXI40ibHt4iBxwg.JPEG.hahahafb/back8.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMTAy/MDAxNjI3NTcxNTk1NTMy.BxQLDJRx7J6gInjylfcCt92hjWduZyQF0NJV5XmHqJsg.QBMTzbEbR3a8-hXxXEI5stocjR838Fqktd-F2Pw34rog.JPEG.hahahafb/back9.jpg?type=w966"},
            {"https://postfiles.pstatic.net/MjAyMTA3MzBfMjIw/MDAxNjI3NTcxNDg0MzAz.WVfCnnI6UPKSbpWDJMl8Evblz0w9LpcS1YqaCEF4omUg.Mn2AzXj_54A_GmEdehcM0V8IAQA4v5BzbYimoGc7VU0g.JPEG.hahahafb/water1.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjU1/MDAxNjI3NTcxNDg0MzY2.aJjahQlHICwJQAAlrj_CHQs2A5YDd_cml-YXnPAQ3BIg.SIg_dZrLJMkumNPNizRenAX07XeQmZiy0FGzRxhH_zYg.JPEG.hahahafb/water2.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMTI0/MDAxNjI3NTcxNDg0Mzk2.wsk6iF5tCSd8G1giAYxgA_ftcuFpPTPg-Fq0HvE5xZMg.qu_H8hmizKClwAPqQIXDx4WnP7XZW4TRlLrFb7ZDrc0g.JPEG.hahahafb/water3.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjIy/MDAxNjI3NTcxNDg0NDI5.bi7C9SYh2wyLf3DhcHRVJCgWdZA35tpUCov8jBgGe1kg.x1dIDyGPMeUjKxarDw_7zEWu76WGk2Wi_wvxV40n8fIg.JPEG.hahahafb/water4.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMzgg/MDAxNjI3NTcxNDg0NDA2.RW5b_LBffABU-LUlwfH9acgn61gSToBpGW9H4DVWKDYg.C1DiQmYqjH3Hl7I3ygNuFeeGZH41NQKrXnisHZZfgTAg.JPEG.hahahafb/water5.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjcw/MDAxNjI3NTcxNDg0NDI0.TwhdEhuxC0MWFvtC_rgoMeLdG2Wokj-lKRPnH1Ys390g.hq5xvrlll8BaaLsD-Tfp_vR2rAJ2w0mtrQskpRh53kgg.JPEG.hahahafb/water6.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMjA1/MDAxNjI3NTcxNDg0NzMy.1iSdhB-igYqEQRD-c1ioKjQk5B-Ag34vyRsjN9f6j6Qg.oa3xTW9I5SjsRcLJSvJHw9r1Z_xWFyJsmc4Wx-sSVF8g.JPEG.hahahafb/water7.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMTY4/MDAxNjI3NTcxNDg0ODQ1.DGR-9LTPgR_xeBx5V5HvpBTdcFJ9rmLsZ98qt9N5V_4g.MM-Mi84l-q2BlyltcoDFVdDUXgrX7S1q4j8qcpZ0Rj4g.JPEG.hahahafb/water8.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MzBfMTE1/MDAxNjI3NTcxNDg0OTIw.3Da2QTE0cUxnXw8MFOeVqfeij2q4szvw13VY80wXrUIg.37I-2kDjbSmGjOJKUeknq2D3DqvHUwADJRXJRtRdKW4g.JPEG.hahahafb/water9.jpg?type=w966"},
            {"https://postfiles.pstatic.net/MjAyMTA3MjlfMTM0/MDAxNjI3NTY5NTgzMzM1.N1rBudltVY-kUPF16sbEeMtM-OMA2Yu2AGEoGrhcDAQg.zND4wWN-NdRpDc52t8SQTMeChA2dsduYQB3lNpzUHQEg.JPEG.hahahafb/rainny1.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjg1/MDAxNjI3NTY5NTgzNDY0.OyjJ8UiJ-hZzNmY9RNMdGahEoWt4wjRtqjrfXFtxgPQg.4bF8C5fs5iESAZK9zDX0xtIexGOPezAhprRmLXYo1WAg.JPEG.hahahafb/rainny2.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTAx/MDAxNjI3NTY5NTgzNDQ4.UUS5pmg-ZBV-pP13n7pQXFVmawVZtT0Qi_GkG0btDzog.dfuyUJkWGDgnmvF5zCpjwzZgqLgNueR8OXb1gIFeyiMg.JPEG.hahahafb/rainny3.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjky/MDAxNjI3NTY5NTgzMzk3.bjeuZ_yRQCRhtYWrVfB8P2aTtHnX4lqc3iCnBZnPM9Mg.Yr4XjYjG691K4CH7zpgpc9La_OfJq8jS3aGVWMT6Tqkg.JPEG.hahahafb/rainny4.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfOTUg/MDAxNjI3NTcwNTM3NzUz.v7Pd36DojPk4QMjQzi8xxFhB3P__9jlgC7W6TFWL0Yog.qwPM9oDCVsKU7l7oOPXlPR3wSBTaM18NlDkTKIocwA4g.JPEG.hahahafb/rainny5.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMTY4/MDAxNjI3NTY5NTgzNDI4.HnN3Wll8fE58yGmZ3S5Jr37rbM5779kvt27M6EmY4dYg.6qdKZ8GWVKE80KpgdCeiUxmxhDkjfgw31Ce-_OFioUQg.JPEG.hahahafb/rainny6.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjAg/MDAxNjI3NTY5NTgzNjc1.8h5cOlIjQJD7-9uLh1YfD8eJ-JewSsaUwnynFGSb9pIg.d3i5peSlL1LfoRYAxK1VHotOstZsmd2tq31oDfsaqS4g.JPEG.hahahafb/rainny7.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfMjIg/MDAxNjI3NTY5NTgzODI4.X5XcwM-Gc4T3LS-0j866NRXLNre7tnQIdCX0dUmaS_8g.PEklfFGQeNYNW1WgWcHJhDe0pSQZk-cbKwR3t-eCAlog.JPEG.hahahafb/rainny8.jpg?type=w966","https://postfiles.pstatic.net/MjAyMTA3MjlfNjQg/MDAxNjI3NTY5NTgzOTE5.-D-zuZeRBwC4MyH38C9D8ZDIrqMcz4V78fdRI1vueTUg.nxVJTRFQ8r8fjMVfIcEc-9uKF07Qsy7S4OG977WGS8og.JPEG.hahahafb/rainny9.jpg?type=w966"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_treena);
        ImageView imageView = findViewById(R.id.imageView_treena);
        TextView textView = findViewById(R.id.textView_ment);

        Intent intent = getIntent();
        String emotion = intent.getExtras().getString("emotion");
        imageUrl = "";
        ment = "";
        commentList = new ArrayList<>();
        commentList = getCommentList();
        type = 0;
        diaryNumber = intent.getExtras().getLong("number");
        id = 0;
        Log.d("emotion", "treena get "+String.valueOf(diaryNumber));


        Log.d("emotion", "tree level : "+String.valueOf(treeLevel));
        //받아온 감정 타입 체크
        for (int i = 0; i < commentList.size(); i++) {
            if (emotion.equals(commentList.get(i).getName())) {
                type = commentList.get(i).getType();
                Log.d("emotion", "type : " + String.valueOf(type));
                id = i;
                break;
            }
        }
        getTreeLevel();
        int mentRandom = (int) ((Math.random() * 10000) % (commentList.get(id).getResponse().size()));
        Log.d("emotion", "랜덤 멘트 : " + String.valueOf(mentRandom));
        ment = commentList.get(id).getResponse().get(mentRandom);
        textView.setText(ment);

        if (type == 0) {
            int imageRandom = (int) ((Math.random() * 10000) % 2);
            Log.d("emotion"," 랜덤 숫자: "+String.valueOf(imageRandom));
            imageUrl = imageUrlList[imageRandom+3][treeLevel];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        } else if (type == 1) {
            int imageRandom = (int) ((Math.random() * 10000) % 3);
            Log.d("emotion"," 랜덤 숫자: "+String.valueOf(imageRandom));
            imageUrl = imageUrlList[imageRandom][treeLevel];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        } else if (type == -1) {
            imageUrl = imageUrlList[5][treeLevel];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("emotion", "timer 작동");
                Intent intent1 = new Intent(ImageTreena.this, MainActivity.class);
                intent1.putExtra("number", diaryNumber);
                startActivity(intent1);
            }
        };
        timer.schedule(timerTask, 6000);
    }

    private ArrayList<EmotionCommentVO> getCommentList() {
        ArrayList<EmotionCommentVO> list = new ArrayList<>();
        Gson gson = new Gson();

        try {
            InputStream is = getAssets().open("emotion_result 2.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("emotions");

            int index = 0;

            while (index < jsonArray.length()) {
                EmotionCommentVO commentVO = gson.fromJson(jsonArray.get(index).toString(), EmotionCommentVO.class);
                list.add(commentVO);

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void getTreeLevel() {
        if (diaryNumber <= 2) {
            Log.d("emotion", "tree level : "+String.valueOf(treeLevel));
            treeLevel=0;
        }
        if (diaryNumber > 2 && diaryNumber <= 7) {
            Log.d("emotion", "tree level : "+String.valueOf(treeLevel));
            treeLevel = 1;
        }
        if (diaryNumber > 7 && diaryNumber <= 14) {
            Log.d("emotion", "tree level : "+String.valueOf(treeLevel));
            treeLevel = 2;
        }
        if (diaryNumber > 14 && diaryNumber <= 21) {
            Log.d("emotion", "tree level : "+String.valueOf(treeLevel));
            treeLevel = 3;
        }
        if (diaryNumber > 21 && diaryNumber <= 28) {
          treeLevel = 4;
        }
        if (diaryNumber > 28 && diaryNumber <= 35) {
            treeLevel =  5;
        }
        if (diaryNumber > 35 && diaryNumber <= 42) {
            treeLevel = 6;
        }
        if (diaryNumber > 42 && diaryNumber <= 49) {
           treeLevel = 7;
        }
        if (diaryNumber > 49 && diaryNumber <= 56) {
           treeLevel = 8;
        }
    }
}