package com.aplication.sergio.bookmark

import android.support.v4.app.Fragment
import android.content.Context
import android.content.res.Resources
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.aplication.sergio.bookmark.fragments.SearchFragment
import com.aplication.sergio.bookmark.helper.SQLHelper
import com.aplication.sergio.bookmark.model.db.Book
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class FetchBook : AsyncTask<String, Void, String> {
    private val TAG = "FETCH"
    private val activity: AppCompatActivity
    private var fromFragment: Int? = null
    var delegate: AsyncResponse? = null

    constructor(activity: AppCompatActivity, fromFragment: Int )
    {
        this.activity = activity
        this.fromFragment = fromFragment
    }

    override fun doInBackground(vararg params: String?): String? {
        return NetworkUtils.getBookInfo( params[0] )
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        try {
            var jsonObject = JSONObject(result)
            var itemArray: JSONArray = jsonObject.optJSONArray ("items")

            for (i in 0..(itemArray.length() - 1)) {
                var book: JSONObject = itemArray.getJSONObject(i)
                var googleId: String? = null
                var title: String? = null
                var authors: String? = null
                var bookImage: String?
                var description: String?
                var gender: String?
                var volumeInfo: JSONObject = book.getJSONObject("volumeInfo")
                var imagesLinks: JSONObject = volumeInfo.optJSONObject("imageLinks")

                try {
                    googleId = book.optString("id")
                    title = volumeInfo.optString("title")
                    authors = volumeInfo.optString("authors").replace("[", "").replace("]", "")
                    bookImage = imagesLinks.optString("thumbnail")
                    description = volumeInfo.optString("description")
                    gender = volumeInfo.optString("categories")
                    val nextBook = Book(googleId, title, authors.split(","), bookImage, description, gender.split(","), 0f, 0)
                    nextBook.save()
                    if( this.fromFragment != null){
                        when( this.fromFragment ){
                            MainActivity().searchFragment -> {
                                val searchFragment = SearchFragment.newInstance(nextBook)
                                val transaction = this.activity.supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.container, searchFragment)
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }
                            200 -> Log.i(TAG, "DETAIL CONTEXT")
                            else -> Log.i(TAG, "NO CONTEXT")
                        }
                     /*   var i: Intent = Intent(this.context, BookDetailActivity::class.java)
                        i.putExtra("Book", book)
                        this.context.startActivity(i)*/
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (title != null && authors != null && googleId != null) {
                    Log.i(TAG, "Id: $googleId,  Titulo: $title, autores: $authors")
                }
            }
        } catch (e: Exception) {

        }
    }

}
