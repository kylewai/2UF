package com.example.kylewai.a2uf;

import com.example.kylewai.a2uf.volleypojo.UFData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UfApiService {
    @GET("apix/soc/schedule/?category=RES&term=2201")
    Call<UFData> listCourses();
}
