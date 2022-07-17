package nextGrowthLabs.evaluation.githubrepoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import nextGrowthLabs.api.model.AccessToken
import nextGrowthLabs.api.service.GithubClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var clientId = "20818c2ddf606775b250"
    private var clientSecret = "554cd6a9e8bcc1a479a535bb991d43d540b597a6"
    private var redirectUri = "githubrepo://callback"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val Google_Sign_in_button: Button = findViewById(R.id.Google_Sign_in_button)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
        Google_Sign_in_button.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(checkCurrentUser()) {
            updateUI(currentUser)
        }
        else {googleSignInClient.signOut()}
    }
    override fun onRestart() {
        super.onRestart()
        if(checkCurrentUser()) {updateUI(auth.currentUser) }
        else {googleSignInClient.signOut()}
    }

    /*override fun onResume() {
        super.onResume()
        val uri = intent.data
        if(uri!=null && uri.toString().startsWith(redirectUri))
        {
            val code = uri.getQueryParameter("code")
            val builder = Retrofit.Builder()
                .baseUrl("https://github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val client = builder.create(GithubClient::class.java)
            val accessTokenCall = client.getAccessToken(
                clientId,
                clientSecret,
                code.toString()
            )
            accessTokenCall.enqueue(object : Callback<AccessToken>{
                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    Toast.makeText(applicationContext, "yay!", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Toast.makeText(applicationContext, "no!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }*/
    private fun checkCurrentUser(): Boolean{
        val user = Firebase.auth.currentUser
        return user != null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize?client_id=$clientId&scope=repo&redirect_uri=$redirectUri"))
                    startActivity(intent)
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(applicationContext, "Error in Logging In !!!", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}