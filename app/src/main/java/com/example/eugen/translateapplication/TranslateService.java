package com.example.eugen.translateapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Eugen on 13.02.2018.
 */

public interface TranslateService {
    @FormUrlEncoded
    @POST("tr.json/translate")
    Call<Response> getTranslate(@Field("key") String key,@Field("text") String text,@Field("lang") String lang);
}
