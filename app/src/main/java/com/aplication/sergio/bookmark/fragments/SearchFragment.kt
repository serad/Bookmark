package com.aplication.sergio.bookmark.fragments



import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aplication.sergio.bookmark.R
import com.aplication.sergio.bookmark.model.db.Book
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import com.aplication.sergio.bookmark.FetchBook
import com.aplication.sergio.bookmark.MainActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : Fragment() {

     private var book: Book? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_search, container, false)

        readBook(arguments)

        if (book !== null) {

            UrlImageViewHelper.setUrlDrawable(v.imageViewer, book!!.thumbnail)
            v.titleContainer.text = book!!.title
            v.authorContainer.text = book!!.authors.toString()
            if ( book!!.descriptionLength() > 150) {
                Log.i("LOG", book.toString())
                val span = SpannableString("View More")
                span.setSpan( ForegroundColorSpan( ContextCompat.getColor( context!!,R.color.brown)),0, span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )
                span.setSpan(UnderlineSpan(), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                val text = book!!.description.substring(0,150)+"... "
                v.overViewContainer.text = TextUtils.concat(text, span)
            } else {
                v.overViewContainer.text = book!!.description
            }
            v.favorite_button.setOnClickListener({ _ ->
                this.setFavorite(book)
            })
            v.discard_button.setOnClickListener({ _ ->
                this.setDiscard(book)
            })
            v.read_button.setOnClickListener({ _ ->
                this.setRead(book)
            })

        } else {
            UrlImageViewHelper.setUrlDrawable(imageViewer, "http://books.google.com/books/content?id=EJ0YAAAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api")
        }
        return v
    }

    private fun readBook(bundle: Bundle?) {
        if (bundle != null) {
            this.book =  bundle.getSerializable("book") as Book
        }
    }

    private fun setFavorite(book: Book?){

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val usersReference = FirebaseDatabase.getInstance().getReference("users")
        usersReference.child(uid).child("favoriteList").child(book!!.googleId).setValue( mapOf( "googleId" to book.googleId, "title" to book.title) )
        val queryString = "subject:" + this.getGender()
        FetchBook( activity as AppCompatActivity , MainActivity.SEARCH_FRAGMENT).execute( queryString )
    }

    private fun setDiscard(book: Book?){

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val usersReference = FirebaseDatabase.getInstance().getReference("users")
       // val key = usersReference.child(uid).child("discardedList").push().key
        usersReference.child(uid).child("discardedList").child(book!!.googleId).setValue( mapOf( "googleId" to book.googleId, "title" to book.title) )
        val queryString = "subject:" + this.getGender()
        FetchBook(activity as AppCompatActivity , MainActivity.SEARCH_FRAGMENT).execute( queryString )
    }

    private fun setRead(book: Book?){

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val usersReference = FirebaseDatabase.getInstance().getReference("users")
        usersReference.child(uid).child("readList").child(book!!.googleId).setValue( mapOf( "googleId" to book.googleId, "title" to book.title) )
        val queryString = "subject:" + this.getGender()
        FetchBook(activity as AppCompatActivity , MainActivity.SEARCH_FRAGMENT).execute( queryString )
    }

    private fun getGender(): String{
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val genders = preferences.getString("genders", "")
        val gendersArray = ArrayList<String>(genders.split(','))
        val randomGenerator = Random()
        return gendersArray[(randomGenerator.nextInt(gendersArray.size))]
    }

    companion object {
        fun newInstance( book: Book): SearchFragment {
            val f = SearchFragment()
            val args = Bundle()
            args.putSerializable("book", book)
            f.arguments = args
            return f
        }
    }





}
