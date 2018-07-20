package com.aplication.sergio.bookmark.model.db

import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

data class Book(val googleId: String, val title: String, val authors: List<String>, val thumbnail: String, val description: String, val gender: List<String>, var totalPoints: Float, var numberOfVotes: Int):Serializable  {
    constructor(): this( "", "", emptyList<String>(),"","", emptyList<String>(), 0f, 0)
    fun descriptionLength(): Int{
        return this.description.length
    }
    fun save(){
        val bookReference = FirebaseDatabase.getInstance().getReference("books")
        bookReference.child(this.googleId).setValue(this)
    }

    fun sumPoints( points: Float)
    {
        this.totalPoints+= points
        this.numberOfVotes++
    }


}