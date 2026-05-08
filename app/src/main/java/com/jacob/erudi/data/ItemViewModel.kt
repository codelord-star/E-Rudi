package com.jacob.erudi.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jacob.erudi.models.FoundItem
import com.jacob.erudi.navigation.ROUTE_FOUNDITEMS
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ItemViewModel : ViewModel() {
    private val cloudName = "dosbf5vye"
    private val uploadPreset = "lost_items"

    fun uploadLostItem(
        itemName: String,
        category: String,
        description: String,
        location: String,
        dateLost: String,
        imageUri: Uri?,
        context: Context
    ) {

        Log.d("VM", "uploadLostItem called")

        if (imageUri == null) {
            Log.e("UPLOAD", "Image URI is null")
            return
        }

        // -----------------------------
        // 1. Get logged-in user
        // -----------------------------
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val uid = user?.uid
        val email = user?.email

        if (uid == null || email == null) {
            Log.e("AUTH", "User not logged in")
            return
        }

        // -----------------------------
        // 2. Get user profile (name + phone)
        // -----------------------------
        val database = FirebaseDatabase.getInstance().reference
                // -----------------------------
                // 3. Upload image to Cloudinary
                // -----------------------------
                uploadImageToCloudinary(
                    context = context,
                    imageUri = imageUri,
                    onSuccess = { imageUrl ->

                        Log.d("CLOUDINARY", "Image uploaded")

                        // -----------------------------
                        // 4. Save to Firestore
                        // -----------------------------
                        val lostItem = hashMapOf(
                            "itemName" to itemName,
                            "category" to category,
                            "description" to description,
                            "location" to location,
                            "dateLost" to dateLost,
                            "imageUrl" to imageUrl,

                            "email" to email,
                            "uid" to uid,
                            "timestamp" to System.currentTimeMillis()
                        )

                        FirebaseFirestore.getInstance()
                            .collection("lost_items")
                            .add(lostItem)
                            .addOnSuccessListener {
                                Log.d("FIRESTORE", "Item uploaded successfully")
                            }
                            .addOnFailureListener {
                                Log.e("FIRESTORE", it.message.toString())
                            }

                    },
                    onError = {
                        Log.e("CLOUDINARY", it)
                    }
                )
    }

    // -----------------------------
    // CLOUDINARY UPLOAD FUNCTION
    // -----------------------------
    private fun uploadImageToCloudinary(
        context: Context,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        try {

            val file = File.createTempFile("upload", ".jpg")

            val inputStream = context.contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
                .addFormDataPart("upload_preset", uploadPreset)
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
                .post(requestBody)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    onError(e.message ?: "Upload failed")
                }

                override fun onResponse(call: Call, response: Response) {

                    val body = response.body?.string()

                    val imageUrl = Regex("\"secure_url\":\"(.*?)\"")
                        .find(body ?: "")?.groupValues?.get(1)

                    if (imageUrl != null) {
                        onSuccess(imageUrl)
                    } else {
                        onError("Failed to get image URL")
                    }
                }
            })

        } catch (e: Exception) {
            onError(e.message ?: "Error occurred")
        }
    }

    fun uploadFoundItem(
        itemName: String,
        category: String,
        description: String,
        foundLocation: String,
        dateFound: String,
        imageUri: Uri?,
        context: Context
    ) {

        Log.d("VM", "uploadFoundItem called")

        if (imageUri == null) {
            Log.e("UPLOAD", "Image URI is null")
            return
        }

        // -----------------------------
        // 1. Get logged-in user
        // -----------------------------
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val uid = user?.uid
        val email = user?.email

        if (uid == null || email == null) {
            Log.e("AUTH", "User not logged in")
            return
        }

        // -----------------------------
        // 2. Upload image to Cloudinary
        // -----------------------------
        uploadImageToCloudinary(
            context = context,
            imageUri = imageUri,

            onSuccess = { imageUrl ->

                Log.d("CLOUDINARY", "Image uploaded")

                // -----------------------------
                // 3. Save to Firestore
                // -----------------------------
                val foundItem = hashMapOf(

                    "itemName" to itemName,
                    "category" to category,
                    "description" to description,
                    "foundLocation" to foundLocation,
                    "dateFound" to dateFound,
                    "imageUrl" to imageUrl,

                    "email" to email,
                    "uid" to uid,
                    "timestamp" to System.currentTimeMillis()

                )

                FirebaseFirestore.getInstance()
                    .collection("found_items")
                    .add(foundItem)

                    .addOnSuccessListener {
                        Log.d("FIRESTORE", "Found item uploaded successfully")
                    }

                    .addOnFailureListener {
                        Log.e("FIRESTORE", it.message.toString())
                    }
            },

            onError = {
                Log.e("CLOUDINARY", it)
            }
        )
    }
}
