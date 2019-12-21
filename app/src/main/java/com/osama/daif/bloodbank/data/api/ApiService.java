package com.osama.daif.bloodbank.data.api;

import com.osama.daif.bloodbank.data.model.city.GeneralResponse;
import com.osama.daif.bloodbank.data.model.contactUs.ContactUs;
import com.osama.daif.bloodbank.data.model.donation.Donation;
import com.osama.daif.bloodbank.data.model.donationDetails.DonationDetails;
import com.osama.daif.bloodbank.data.model.login.Login;
import com.osama.daif.bloodbank.data.model.notification.Notification;
import com.osama.daif.bloodbank.data.model.notificationCount.NotificationCount;
import com.osama.daif.bloodbank.data.model.notificationSetting.NotificationSetting;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.data.model.register.Register;
import com.osama.daif.bloodbank.data.model.settings.Settings;

import java.util.List;

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
    Call<Posts> getPostToggleFavourite(@Field("post_id") int post_id,
                                       @Field("api_token") String apiToken);

    @GET("donation-requests")
    Call<Donation> getAllDonations(@Query("api_token") String apiToken,
                                   @Query("page") int page);

    @GET("donation-requests")
    Call<Donation> getAllDonationsRequestsFilter(@Query("api_token") String apiToken,
                                                 @Query("blood_type_id") int blood_type_id,
                                                 @Query("city_id") int city_id,
                                                 @Query("page") int page);

    @GET("post")
    Call<Posts> getPostDetails(@Query("api_token") String apiToken,
                               @Query("post_id") int post_id,
                               @Query("page") int page);

    @POST("donation-request/create")
    @FormUrlEncoded
    Call<Donation> createDonationRequest(@Field("api_token") String apiToken,
                                         @Field("patient_name") String patientName,
                                         @Field("patient_age") int patientAge,
                                         @Field("blood_type_id") int bloodTypeId,
                                         @Field("bags_num") int bagsNum,
                                         @Field("hospital_name") String hospitalName,
                                         @Field("hospital_address") String hospitalAddress,
                                         @Field("city_id") int cityId,
                                         @Field("phone") String phone,
                                         @Field("notes") String notes,
                                         @Field("latitude") String latitude,
                                         @Field("longitude") String longitude);

    @POST("profile")
    @FormUrlEncoded
    Call<Register> editProfile(@Field("name") String name,
                               @Field("email") String email,
                               @Field("birth_date") String birth_date,
                               @Field("city_id") int city_id,
                               @Field("phone") String phone,
                               @Field("donation_last_date") String donation_last_date,
                               @Field("password") String password,
                               @Field("password_confirmation") String password_confirmation,
                               @Field("blood_type_id") int blood_type_id,
                               @Field("api_token") String apiToken);

    @POST("profile")
    @FormUrlEncoded
    Call<Register> getProfile(@Field("api_token") String apiToken);

    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationSetting> getNotificationSetting(@Field("api_token") String apiToken);

    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationSetting> setNotificationSetting(@Field("api_token") String apiToken,
                                                     @Field("governorates[]") List<Integer> governorates,
                                                     @Field("blood_types[]") List<Integer> bloodTypes);

    @GET("my-favourites")
    Call<Posts> getFavouritesPosts(@Query("api_token") String apiToken);

    @POST("donation-request/create")
    @FormUrlEncoded
    Call<Donation> createNewDonation(@Field("api_token") String apiToken,
                                     @Field("patient_name") String patient_name,
                                     @Field("patient_age") String patient_age,
                                     @Field("blood_type_id") int blood_type_id,
                                     @Field("bags_num") String bags_num,
                                     @Field("hospital_name") String hospital_name,
                                     @Field("hospital_address") String hospital_address,
                                     @Field("city_id") int city_id,
                                     @Field("phone") String phone,
                                     @Field("notes") String notes,
                                     @Field("latitude") double latitude,
                                     @Field("longitude") double longitude
    );

    @GET("notifications")
    Call<Notification> getNotifications(@Query("api_token") String apiToken,
                                        @Query("page") int page);

    @GET("donation-request")
    Call<DonationDetails> getDonationDetails(@Query("api_token") String apiToken,
                                             @Query("donation_id") int donation_id);

    @GET("notifications-count")
    Call<NotificationCount> getNotificationsCount(@Query("api_token") String apiToken);

    @GET("settings")
    Call<Settings> getSetting(@Query("api_token") String apiToken);

    @POST("contact")
    @FormUrlEncoded
    Call<ContactUs> setContactUsMessage(@Field("api_token") String apiToken,
                                        @Field("title") String title,
                                        @Field("message") String message);
}
