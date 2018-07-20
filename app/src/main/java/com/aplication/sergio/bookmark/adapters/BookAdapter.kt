package com.aplication.sergio.bookmark.adapters


import android.content.Intent

import android.support.v7.widget.RecyclerView;
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import com.aplication.sergio.bookmark.BookDetailActivity
import com.aplication.sergio.bookmark.R
import com.aplication.sergio.bookmark.model.db.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper

import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.util.Pair



class BookAdapter (private val c : FragmentActivity, private val list :  ArrayList<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var authorsTextView: TextView
        var thumbImageView : ImageView

        init {
            titleTextView = itemView.findViewById(R.id.titleContainer) as TextView
            authorsTextView = itemView.findViewById(R.id.authors) as TextView
            thumbImageView = itemView.findViewById(R.id.thumbImageView) as ImageView
           // overflowImageView = itemView.findViewById(R.id.overflow) as ImageView
        }
    }
    override fun onCreateViewHolder(parent : ViewGroup, type : Int) : BookAdapter.ViewHolder{
        Log.i("TAG4", "PASO")
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_view_book, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder : BookAdapter.ViewHolder, position : Int){
        Log.i("TAG4", list.get(position).toString())
        val book = list.get(position)
        val bookReference = FirebaseDatabase.getInstance().getReference("books")
        val currentBookReference = bookReference.child(book.googleId)
        holder.titleTextView.text = book.title
        currentBookReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val book = snapshot.getValue( Book::class.java )
                holder.titleTextView.text = book!!.title
                holder.authorsTextView.text = book.authors.toString()
                UrlImageViewHelper.setUrlDrawable(holder.thumbImageView, book.thumbnail)

                holder.itemView.setOnClickListener {

                    val intent = Intent(it.context,  BookDetailActivity::class.java)

                    val p1 = Pair.create<View,String>(holder.titleTextView, "title")
                    val p2 = Pair.create<View,String>(holder.thumbImageView, "thumbImage")
                    val p3 = Pair.create<View,String>(holder.authorsTextView, "authors")

                    val options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(c, p1, p2, p3)
                    intent.putExtra("Book", book)

                    c.startActivity(intent, options.toBundle())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

     //   holder.countTextView.text = "${book.title} songs"
     //   holder.thumbImageView.setImageResource(book.title);
      //  holder.listView.
      //  });
      //  holder.overflowImageView.setOnClickListener{showPopupMenu(holder.overflowImageView)};
    }
    override fun getItemCount() : Int{
        return list.size
    }

}

interface RecyclerViewOnItemClickListener {

    fun onClick(v: View, position: Int)
}