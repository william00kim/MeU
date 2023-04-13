package org.example.ada.fcm

import okhttp3.ResponseBody
import org.example.ada.fcm.Repo.Companion.CONTENT_TYPE
import org.example.ada.fcm.Repo.Companion.SERVER_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotiAPI {
    //서버에 메세지에 보내라는 것을 명령
    @Headers("Authorization: key = $SERVER_KEY", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>
}