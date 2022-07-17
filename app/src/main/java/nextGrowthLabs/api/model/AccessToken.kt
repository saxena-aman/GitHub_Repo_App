package nextGrowthLabs.api.model

import com.google.gson.annotations.SerializedName

class AccessToken {
    @SerializedName("access_token")
    private lateinit var accessToken:String
    @SerializedName("token_type")
    private lateinit var tokenType:String

    public fun getAccessToken(): String {
        return accessToken
    }
    public fun getTokenType(): String {
        return tokenType
    }

}
