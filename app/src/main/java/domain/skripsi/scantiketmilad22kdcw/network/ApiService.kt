package domain.skripsi.scantiketmilad22kdcw.network

import domain.skripsi.scantiketmilad22kdcw.model.Model
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("get-tickets")
    fun getTickets(
        @Field("search") search: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("edit-tickets")
    fun editTicket(
        @Field("ticket_id") ticketId: String,
        @Field("status_id") statusId: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("scan-ticket-attendance")
    fun scanTicketAttend(
        @Field("nra_campus") nraCampus: String, // "99.xix,78"
        @Field("status_id") statusId: String,
    ): Call<Model.ResponseModel>

    @POST("get-data")
    fun getData(): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("campus") campus: String,
    ): Call<Model.ResponseModel>

}