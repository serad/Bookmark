package com.aplication.sergio.bookmark.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.aplication.sergio.bookmark.*
import com.aplication.sergio.bookmark.model.db.Book
import com.aplication.sergio.bookmark.AsyncResponse
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.EditText




class HomeFragment : Fragment(), AsyncResponse {
    val TAG = "HOME"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.findViewById<Button>(R.id.suggestion).setOnClickListener{ goToDailySuggestion() }
        rootView.findViewById<Button>(R.id.publicYour).setOnClickListener{ goToPublicYour() }
        rootView.findViewById<Button>(R.id.favourite).setOnClickListener{ goToFavourites() }
        return rootView
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    //this override the implemented method from asyncTask
    override fun processFinish(output: Book) {
        Log.i("LIBRO", output.toString());

        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
    }
    fun goToPublicYour() {
        startActivity(Intent(activity, BookDetailActivity::class.java))
    }

    fun goToFavourites() {


        val nextFrag = ListFragment.newInstance()
        val args = Bundle()
        args.putInt("top", 1)
        nextFrag.arguments = args
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_home, nextFrag, "findThisFragment")
                .commit()

    }
    fun goToDailySuggestion() {
        /* Metodo para ocultar teclado al buscar
        *var inputManager: InputMethodManager = activity!!.getSystemService(
        *        Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        *inputManager.hideSoftInputFromWindow( activity!!.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        */

        var connManager: ConnectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var netInfo: NetworkInfo? = connManager.activeNetworkInfo;

        if(netInfo != null && netInfo.isConnected && true ){
            var queryString: String = "El imperio final"
            Log.i(TAG, "Searching")
            val intent = Intent(this.context, BookDetailActivity::class.java)
           startActivity(intent)

        } else {
            Log.i(TAG, "Network error")
            Toast.makeText( activity, "Please check our connection",Toast.LENGTH_LONG).show()
        }

    }

}