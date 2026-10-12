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
        val authResult = auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        ).await()

        return authResult.user
            ?: throw IllegalStateException(
                "Registration succeeded without returning a user."
            )
    }

    /**
     * Authenticates an existing user and establishes an authenticated session.
     *
     * @param email The email address associated with the user's account.
     * @param password The user's account password.
     * @return The authenticated Firebase user.
     * @throws Exception if Firebase authentication fails.
     * @throws IllegalStateException if authentication succeeds without returning a user.
     */
    suspend fun login(
        email: String,
        password: String
    ): FirebaseUser {
        val authResult = auth.signInWithEmailAndPassword(
            email.trim(),
            password
        ).await()

        return authResult.user
            ?: throw IllegalStateException(
                "Login succeeded without returning a user."
            )
    }

    /**
     * Sends a password-reset email to the specified email address.
     *
     * Firebase sends an email containing instructions that allow the user
     * to reset their account password.
     *
     * @param email The email address associated with the user's account.
     * @throws Exception if Firebase fails to send the password-reset email.
     */
    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email.trim()).await()
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

    /**
     * Registers a listener that receives updates whenever the authentication state changes.
     *
     * The listener is immediately notified of the current authentication state
     * when it is registered.
     *
     * @param onAuthStateChanged Callback invoked with the current Firebase user,
     * or null if no user is authenticated.
     * @return The registered listener, which can be used to remove it later.
     */
    fun addAuthStateListener(
        onAuthStateChanged: (FirebaseUser?) -> Unit
    ): FirebaseAuth.AuthStateListener {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            onAuthStateChanged(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        return listener
    }

    /**
     * Unregisters an authentication state listener to stop receiving updates.
     *
     * @param listener The listener previously registered using
     * [addAuthStateListener].
     */
    fun removeAuthStateListener(
        listener: FirebaseAuth.AuthStateListener
    ) {
        auth.removeAuthStateListener(listener)
    }
}
