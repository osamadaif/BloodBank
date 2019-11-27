package com.osama.daif.bloodbank.data.api;

import com.osama.daif.bloodbank.data.model.city.GeneralResponse;
import com.osama.daif.bloodbank.data.model.login.Login;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.data.model.register.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Call<Login> getLogin(@Field("phone") String phone,
                         @Field("password") String password);

    @POST("signup")
    @FormUrlEncoded
    Call<Register> getRegister(@Field("name") String name,
                               @Field("email") String email,
                               @Field("birth_date") String birth_date,
                               @Field("city_id") int city_id,
                               @Field("phone") String phone,
                               @Field("donation_last_date") String donation_last_date,
                               @Field("password") String password,
                               @Field("password_confirmation") String password_confirmation,
                               @Field("blood_type_id") int blood_type_id
    );

    @GET("blood-types")
    Call<GeneralResponse> getBloodTypes();

    @GET("governorates")
    Call<GeneralResponse> getGovernorates();

    @GET("cities")
    Call<GeneralResponse> getCities(@Query("governorate_id") int governorate_id);

    @GET("posts")
    Call<Posts> getAllPosts(@Query("api_token") String apiToken,
                            @Query("page") int page);

    @GET("posts")
    Call<Posts> getPostsFilter(@Query("api_token") String apiToken,
                               @Query("page") int page,
                               @Query("keyword") String keyword,
                               @Query("category_id") int category_id);

    @GET("categories")
    Call<GeneralResponse> getCategories();


    @POST("post-toggle-favourite")
    @FormUrlEncoded
    Call<Login> getPostToggleFavourite(@Field("post_id") int post_id,
                                       @Field("api_token") String apiToken);


}
