package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN=22
    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("656872401218-86bucjmn7e986kif79j4lkfq6mi9is3l.apps.googleusercontent.com")
            .requestEmail()
            .build()
       googleSignInClient=GoogleSignIn.getClient(this,gso)


    }
    fun signIn(view:android.view.View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.w("Fire22","Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken:String){
        val auth= FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    Log.d("Fire22","signInWithCredential:success")

                    SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,true)
                    val user=auth.currentUser
                startActivity(Intent(this, MainActivity::class.java))
                    Log.d("Fire22","firebaseAuthWithGoogle:${user?.displayName}")

                }else{
                    Log.w("Fire22","signInWithCredential:failure",task.exception)

                }

            }
    }

}
