package nextGrowthLabs.api.service

import nextGrowthLabs.api.model.AccessToken
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface GithubClient {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(@Field("client_id") clientId: String,
                        @Field("client_secret") clientSecret: String,
                        @Field("code") code:String) : Call<AccessToken>
}