package com.ppb.travelanywhere.services.api

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FireAuth : FireConsole() {



    suspend fun registerUser(username: String, email: String, password: String) : Boolean {
        val user = User(
            username = username,
            email = email,
            password = password,
            user_auth_status = stringMap[false]!!
        )

        val isUserExist = checkIfUserExists(username, email)
        if (!isUserExist) {
            addUser(user)
            Log.d("FireAuth", "User added")
        }
        return !isUserExist
    }

    suspend fun login(username: String, email: String, password: String) : Boolean {
        val isUserExist = checkIfUserExists(username, email)
        if (isUserExist) {
            val id = getUserId(username, email)
            return validateUser(id = id, password = password)
        }
        return false
    }

    fun logout(id: String) {
        updateUserStatus(id, stringMap[false]!!)
    }


    suspend fun checkUserStatus(id: String) : Boolean {
        return suspendCoroutine { continuation ->
            usersRef.whereEqualTo(field_ID, id).get(Source.SERVER).addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.w("FireAuth", "User not exist")
                    continuation.resume(false)
                } else {
                    val user = documents.toObjects(User::class.java)[0]
                    Log.d("FireAuth", "User exist status ${user.user_auth_status} -> ${booleanMap[user.user_auth_status]}")
                    Log.d("FireAuth", "User exist status $user")
                    continuation.resume(booleanMap[user.user_auth_status]!!)
                }
            }.addOnFailureListener { exception ->
                Log.e("FireAuth", "Error checking user status :", exception)
                continuation.resumeWithException(exception)
            }
        }
    }


    suspend fun getUserId(username: String, email: String): String {
        return suspendCoroutine { continuation ->
            usersRef.whereEqualTo(field_username, username).get(Source.SERVER).addOnSuccessListener { resultUsername ->
                if (resultUsername.isEmpty) {
                    usersRef.whereEqualTo(field_email, email).get(Source.SERVER).addOnSuccessListener { resultEmail ->
                        if (resultEmail.isEmpty) {
                            Log.w("FireAuth", "User not exist")
                            continuation.resume("")
                        } else {
                            val user = resultEmail.toObjects(User::class.java)[0]
                            continuation.resume(user.id)
                        }
                    }.addOnFailureListener {
                        Log.e("FireAuth", "Error getting user id from email:", it)
                        continuation.resumeWithException(it)
                    }
                } else {
                    Log.d("FireAuth", "User exist")
                    val user = resultUsername.toObjects(User::class.java)[0]
                    continuation.resume(user.id)
                }
            }.addOnFailureListener {
                Log.e("FireAuth", "Error getting user id from username:", it)
                continuation.resumeWithException(it)
            }
        }
    }



    /**
     *  Private Section
     *  WARNING : CREATE UPDATE DELETE
     */



    private suspend fun checkIfUserExists(username: String, email: String) : Boolean {
        return suspendCoroutine { continuation ->
            var isUserExist : Boolean? = null
            usersRef.whereEqualTo(field_username, username).get(Source.SERVER).addOnSuccessListener {resultUsername ->
                if (resultUsername.isEmpty) {
                    usersRef.whereEqualTo(field_email, email).get(Source.SERVER).addOnSuccessListener { resultEmail ->
                        if (resultEmail.isEmpty) {
                            isUserExist = false
                            continuation.resume(isUserExist!!)
                            Log.i("FireAuth", "User not exist")
                        } else {
                            isUserExist = true
                            continuation.resume(isUserExist!!)
                            Log.d("FireAuth", "User exist")
                        }
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
                } else {
                    isUserExist = true
                    continuation.resume(isUserExist!!)
                    Log.d("FireAuth", "User exist")
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    private fun addUser(user: User) {
        usersRef.add(user).addOnSuccessListener { documentReference ->
            user.id = documentReference.id
            documentReference.set(user).addOnFailureListener {
                Log.e("FireAuth", "Error adding user id :", it)
            }
        }
    }

    private suspend fun validateUser(id:String, password: String) : Boolean {
        return suspendCoroutine {continuation ->
            usersRef.whereEqualTo(field_ID, id).get(Source.SERVER).addOnSuccessListener {
                if (it.isEmpty) {
                    continuation.resume(false)
                } else {
                    val user = it.toObjects(User::class.java)[0]
                    if (user.password == password) {
                        updateUserStatus(user.id, stringMap[true]!!, "Validation")
                        usersRef.document(user.id).update(field_updatedAt, FieldValue.serverTimestamp())
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }

        }
    }

    private fun updateUserStatus(id: String, newStatus: String, msg : String = "") {
        usersRef.document(id).update(field_isActive, newStatus)
            .addOnSuccessListener {
                Log.d("FireAuth", "User status updated $msg")
            }
            .addOnFailureListener { e ->
                Log.w("FireAuth", "Error updating user status", e)
            }
    }








    private val booleanMap = mapOf(
        "log_out" to false,
        "log_in" to true
    )

    private val stringMap = mapOf(
        false to "log_out",
        true to "log_in"
    )

    data class User(
        var id : String = "",
        var username : String = "",
        var email : String = "",
        var password : String = "",
        var role : String = "Role_User",
        var user_auth_status : String = "log_out",
        val createdAt : Any? = FieldValue.serverTimestamp(),
        var updatedAt : Any? = FieldValue.serverTimestamp()
    )

    private val field_ID = "id"
    private val field_username = "username"
    private val field_email = "email"
    private val field_password = "password"
    private val field_role = "role"
    private val field_isActive = "user_auth_status"
    private val field_createdAt = "createdAt"
    private val field_updatedAt = "updatedAt"
}