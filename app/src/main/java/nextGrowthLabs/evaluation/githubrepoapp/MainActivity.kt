package nextGrowthLabs.evaluation.githubrepoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val Logout_from_google: Button = findViewById(R.id.Logout_from_google)
        Logout_from_google.setOnClickListener {
            signOut()
            checkCurrentUser()
        }
    }
    private fun signOut() {
        Firebase.auth.signOut()
    }
    override fun onBackPressed() {
        // super.onBackPressed()
        return
    }
    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null)
        {
            Toast.makeText(applicationContext, "Still Logged In", Toast.LENGTH_SHORT).show()
        }
        else
        {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}