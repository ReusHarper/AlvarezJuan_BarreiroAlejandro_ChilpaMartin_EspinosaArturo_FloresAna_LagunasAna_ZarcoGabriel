package com.example.proyectorelampago

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    private val callbackManager = CallbackManager.Factory.create()
    private  val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        // SplashScreen
        setTheme(R.style.AppTheme)

        // Instanciacion
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Analytics events
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        // Setup
        setup()
        session()
    }
    override fun onStart(){
        super.onStart()

        authLayout.visibility = View.VISIBLE

    }
    private fun session(){
        val prefs = getsharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email= prefs.getString("email", null)
        val provider= prefs.getString("provider", null)

        if(email != null && provider != null){
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title = "Autenticación"

        signInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?:"")
                        } else {
                            showAlert()
                        }
                    }
            } else {
                showAlertNull()
            }

        }

        logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?:"--")
                        } else {
                            showAlert()
                        }
                    }
            } else {
                showAlertNull()
            }
        }

        facebookImageButton.setOnClickListener{
            LoginManager.getInstance().registerCallback(callbackManager,
                object :FacebookCallback<LoginResult>{

                    override fun onSuccess(result: LoginResult?) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException?) {
                        TODO("Not yet implemented")
                    }
                })
        }


        gmailImageButton.setOnClickLister{

            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val googleClient:GoogleSignInClient! = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN )
        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autentificación del usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showAlertNull() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Ingrese todos los datos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showAlertRegister() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("Registro completado con éxito.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showHome(email: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }


        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task:TaskGoogleSignInAccount>! = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account:GoogleSignInAccount? = task.getResult(ApiException::class.java)

                if(account!= null) {
                    val credential = GoofleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential){
                        if (it.isSuccessful){
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException){
                showAlert()
            }

        }

        return super.equals(other)
    }
}
