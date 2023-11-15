package com.ezgikara.gathereality.data.repository

import com.ezgikara.gathereality.common.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth) {
    fun isUserLoggedIn() = firebaseAuth.currentUser != null         //splash kontrol
    fun getUserId() = firebaseAuth.currentUser?.uid.orEmpty()
    suspend fun signIn(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Invalid login credentials")
            }
        } catch (e: Exception) {
            Resource.Error("Account not found")
        }
    }
    suspend fun signUp(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Invalid login credentials")
            }
        } catch (e: Exception) {
            Resource.Error("Invalid login credentials")
        }
    }
}