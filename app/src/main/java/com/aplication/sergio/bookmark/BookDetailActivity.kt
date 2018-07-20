package com.aplication.sergio.bookmark

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aplication.sergio.bookmark.model.db.Book
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper
import kotlinx.android.synthetic.main.activity_book_detail.*
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val bounds = ChangeBounds()
        bounds.duration = 1000
        window.sharedElementEnterTransition = bounds*/
        setContentView(R.layout.activity_book_detail)
        val extras = intent.extras
        if (extras != null) {
            var book: Book = extras.getSerializable("Book") as Book
            Log.i("DETAil", book.toString())
            UrlImageViewHelper.setUrlDrawable(thumbImageView, book.thumbnail)
            titleContainer.text = book.title
            authorContainer.text = book.authors.toString()
            genderContainer.text = book.gender.toString()
            overViewContainer.text = book.description
            ratingBar.rating = book.totalPoints / book.numberOfVotes

            val rBar = ratingBar as RatingBar
            ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                book.sumPoints(rBar.rating)
                book.save()
            }

        } else {
            UrlImageViewHelper.setUrlDrawable(thumbImageView, "http://books.google.com/books/content?id=EJ0YAAAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        }

    }

}
