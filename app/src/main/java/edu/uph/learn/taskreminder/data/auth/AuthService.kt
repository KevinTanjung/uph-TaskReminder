package edu.uph.learn.taskreminder.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Registers a new user and establishes an authenticated session.
     *
     * Firebase automatically signs in the user after successful registration.
     *
     * @return The newly registered Firebase user.
     * @throws Exception if Firebase registration fails.
     */
    suspend fun register(
        email: String,
        password: String
    ): FirebaseUser {
        // TODO: add passwordConfirmation, validation and field-level error handling
        val authResult = auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        ).await()

        return authResult.user
            ?: throw IllegalStateException(
                "Registration succeeded without returning a user."
            )
    }

    suspend fun login(
        email: String,
        password: String
    ): FirebaseUser {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()

        return authResult.user
            ?: throw IllegalStateException(
                "Login succeeded without returning a user."
            )
    }

    /**
     * Returns the currently authenticated user, if any.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Indicates whether a user is currently authenticated.
     */
    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Ends the current authenticated session.
     */
    fun signOut() {
        auth.signOut()
    }


    fun addAuthStateListener(
        onAuthStateChanged: (FirebaseUser?) -> Unit
    ): FirebaseAuth.AuthStateListener {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            onAuthStateChanged(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        return listener
    }

    fun removeAuthStateListener(
        listener: FirebaseAuth.AuthStateListener
    ) {
        auth.removeAuthStateListener(listener)
    }
}
