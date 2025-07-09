//package com.example.wa.data.repository
//
//import android.app.Activity
//import android.util.Log
//import com.example.wa.R
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.tasks.await
//
//
//class AuthRepository {
//
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    // REGISTRAZIONE CON EMAIL E PASSWORD
//    fun registerWithEmail(email: String, password: String): Task<AuthResult> {
//        return auth.createUserWithEmailAndPassword(email, password)
//    }
//
//    // LOGIN CON EMAIL E PASSWORD
//    fun loginWithEmail(email: String, password: String): Task<AuthResult> {
//        return auth.signInWithEmailAndPassword(email, password)
//    }
//
//    // LOGOUT
//    fun logout() {
//        auth.signOut()
//    }
//
//    // UTENTE ATTUALE
//    fun getCurrentUser(): FirebaseUser? {
//        return auth.currentUser
//    }
//
//    // RESET PASSWORD via email
//    fun sendPasswordResetEmail(email: String): Task<Void> {
//        return auth.sendPasswordResetEmail(email)
//    }
//
//    // LOGIN CON GOOGLE: ottieni client
//    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(activity.getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        return com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(activity, gso)
//    }
//
//    // LOGIN CON GOOGLE: firebaseAuth con token Google
//    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        return auth.signInWithCredential(credential)
//    }
//
//    // AGGIORNA IL PROFILO UTENTE (displayName, photoUrl)
//    fun updateUserProfile(displayName: String?, photoUrl: String?) : Task<Void>? {
//        val user = auth.currentUser ?: return null
//        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
//            .setDisplayName(displayName)
//            .setPhotoUri(photoUrl?.let { android.net.Uri.parse(it) })
//            .build()
//        return user.updateProfile(profileUpdates)
//    }
//
//    // SALVA I DATI UTENTE SU FIRESTORE (nome, email, eventuali altri campi)
//    fun saveUserData(uid: String, displayName: String, email: String): Task<Void> {
//        val userData = hashMapOf(
//            "displayName" to displayName,
//            "email" to email
//            // aggiungi altri campi se vuoi, tipo avatar, timestamp ecc.
//        )
//        return firestore.collection("users").document(uid).set(userData)
//    }
//
//    // OPZIONALE: recupera dati utente da Firestore
//    fun getUserData(uid: String) = firestore.collection("users").document(uid).get()
//
//}
