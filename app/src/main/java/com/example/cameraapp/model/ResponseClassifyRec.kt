package com.example.cameraapp.model

class ResponseClassifyRec(

        val default_face_matching_classification: Boolean,
        val error: String,
        val error_code: Int,
        val message: String,
        ) {

    override fun toString(): String {
        return "User(error='$error', error_code = $error_code, message = '$message')"
    }
}


