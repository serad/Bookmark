package com.aplication.sergio.bookmark.fragments


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aplication.sergio.bookmark.R
import com.aplication.sergio.bookmark.adapters.BookAdapter
import com.aplication.sergio.bookmark.model.db.Book

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.support.v7.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_book_list.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BookListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_book_list, container, false)
        val rView = v.rView as RecyclerView
        rView.setAdapter(null)
        val args = arguments
        var position = 0
        if(args!!.get("position") != null) position = args.get("position") as Int
        var type: String

        when (position) {
            -1 -> {
                type = "topFavorites"
                this.favouriteList(rView, type)
            }
            0 -> {
                type = "favoriteList"
                this.userList(rView, type)
            }
            1 -> {
                type = "discardedList"
                this.userList(rView, type)
            }
            2 -> {
                type = "readList"
                this.userList(rView, type)
            }
        }



        return v
    }

    fun favouriteList(rView: RecyclerView, type: String)
    {
        val bookReference = FirebaseDatabase.getInstance().getReference("books")

        var topBookList  =  bookReference.orderByChild("totalPoints").limitToLast(3)
        topBookList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favouriteList = getBookList(snapshot)
                val adapter = BookAdapter(activity!!, favouriteList)
                rView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                rView.adapter = adapter
                val orientation : Int = getResources().getConfiguration().orientation

                rView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    rView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
                }
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    rView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun userList(rView: RecyclerView, type: String)
    {
        val usersReference = FirebaseDatabase.getInstance().getReference("users")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid

        var currentUsersReference = usersReference.child(uid).child(type)
        currentUsersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favouriteList = getBookList(snapshot)

                val adapter = BookAdapter(activity!!, favouriteList)
                rView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                rView.adapter = adapter
                val orientation : Int = getResources().getConfiguration().orientation

                rView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    rView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
                }
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    rView.layoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    fun getBookList( dataSnapshot: DataSnapshot  ): ArrayList<Book> {
        var bookList = ArrayList<Book>()
        bookList.clear()
        for (singleSnapshot in dataSnapshot.children) {
            var book = singleSnapshot.getValue( Book::class.java )

            if(book != null ) bookList.add( book )
        }
        return bookList
    }

    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }

}
