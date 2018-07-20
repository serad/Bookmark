package com.aplication.sergio.bookmark

import android.net.Uri
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NetworkUtils {
    companion object {
        val TAG = "NET"
        val BOOK_API_URL = "https://www.googleapis.com/books/v1/volumes?"
        val QUERY_PARAM = "q"
        val MAX_RESULTS = "maxResults"
        val PRINT_TYPE = "printType"
        var LANGUAGE = "langRestrict"
        var START_INDEX = "startIndex"

        fun getBookInfo(queryString: String?): String? {

            var urlConnection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            var bookJsonString: String? = null;
            //return bookJsonString;
            Log.d(TAG, queryString)
            try {
                val randomGenerator = Random()
                val builtUri: Uri = Uri.parse(BOOK_API_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .appendQueryParameter(START_INDEX, (randomGenerator.nextInt(300)).toString())
                        .appendQueryParameter(MAX_RESULTS, "1")
                        .appendQueryParameter(LANGUAGE, "eng")
                        .appendQueryParameter(PRINT_TYPE, "books").build()
                Log.d(TAG, builtUri.toString())
                val requestURL = URL(builtUri.toString())
                urlConnection = requestURL.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                urlConnection.connect()

                val inputStream: InputStream = urlConnection.inputStream
                val buffer = StringBuffer()

                reader = BufferedReader( InputStreamReader(inputStream))


                var lines = reader.readLines()
                for (line in lines)
                {
                    buffer.append(line)
                }

                if( buffer.isEmpty() )
                {
                    return null
                }

                bookJsonString = buffer.toString()

            } catch( ex: Exception){
                ex.printStackTrace()
            }
            finally {
                if( urlConnection!=null )
                {
                    urlConnection.disconnect()
                }

                if( reader != null )
                {
                    try {
                        reader.close()
                    } catch (ioe: IOException)
                    {
                        ioe.printStackTrace()
                    }
                }
                Log.d(TAG, bookJsonString)

                return  bookJsonString
            }
        }

    }
}