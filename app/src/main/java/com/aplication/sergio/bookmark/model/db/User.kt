package com.aplication.sergio.bookmark.model.db

import com.google.firebase.database.*
import java.io.Serializable

data class User (var userId: String, var userName: String, var favoriteList: Map<String,String>,
            var readList: Map<String,String>, var discardedList: Map<String,String>): Serializable {

    constructor(): this( "", "", mapOf(), mapOf(),mapOf())

    fun save(){
        val usersReference = FirebaseDatabase.getInstance().getReference("users")
        usersReference.child(this.userId).setValue(this)
    }

}