package com.ppb.travelanywhere.services.api

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class FireConsole {
    internal val firebase = Firebase.firestore

    private val _usersCollectionRef = firebase.collection("users")
    val usersRef
        get() = _usersCollectionRef

}