package com.example.data.repository

import android.util.Log
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthenticationRepository {
    private val TAG = this::class.simpleName
    override suspend fun registerUser(email: String, password: String):
            UseCaseResponse<String> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()?.let { response ->
                response.user?.let { user ->
                    return UseCaseResponse.Success(user.uid)
                }
            }
            return UseCaseResponse.Failure(ErrorMessage.GENERAL)
        } catch (networkException: FirebaseNetworkException) {
            networkException.getError(ErrorMessage.NO_NETWORK)
        } catch (emailException: FirebaseAuthUserCollisionException) {
            emailException.getError(ErrorMessage.INVALID_EMAIL)
        }
    }

    override suspend fun loginUser(email: String, password: String):
            UseCaseResponse<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()?.let {
                return UseCaseResponse.Success(Unit)
            }
            return UseCaseResponse.Failure(ErrorMessage.GENERAL)
        } catch (networkException: FirebaseNetworkException) {
            networkException.getError(ErrorMessage.NO_NETWORK)
        } catch (invalidUserException: FirebaseAuthInvalidUserException) {
            invalidUserException.getError(ErrorMessage.INCORRECT_ACCOUNT)
        } catch (invalidUserException: FirebaseAuthInvalidCredentialsException) {
            invalidUserException.getError(ErrorMessage.INCORRECT_ACCOUNT)
        } catch (exception: Exception) {
            exception.getError(ErrorMessage.GENERAL)
        }
    }

    override fun getIdUserLoggedIn() =
        firebaseAuth.currentUser?.uid?.let {
            UseCaseResponse.Success(it)
        } ?: run {
            UseCaseResponse.Failure(ErrorMessage.GENERAL)
        }

    private fun Exception.getError(error: ErrorMessage): UseCaseResponse.Failure {
        Log.v(TAG, "Authentication failure. Exception: ${this.message}")
        return UseCaseResponse.Failure(error)
    }
}