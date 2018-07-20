package com.aplication.sergio.bookmark

import com.aplication.sergio.bookmark.model.db.Book

interface AsyncResponse {
    fun processFinish(output: Book)
}