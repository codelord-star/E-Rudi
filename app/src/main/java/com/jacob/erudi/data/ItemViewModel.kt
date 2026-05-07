package com.jacob.erudi.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ItemViewModel : ViewModel() {

    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun uploadLostItem(

        itemName: String,
        category: String,
        description: String,
        location: String,
        dateLost: String,
        imageUri: Uri?,
        fullName: String,
        email: String,
        phone: String

    ) {
        if (imageUri == null) return

        val imageRef = storage.reference.child(
            "lost_items/${System.currentTimeMillis()}.jpg"
        )

        imageRef.putFile(imageUri)

            .addOnSuccessListener {

                imageRef.downloadUrl.addOnSuccessListener { uri ->

                    val imageUrl = uri.toString()

                    val lostItem = hashMapOf(

                        "itemName" to itemName,
                        "category" to category,
                        "description" to description,
                        "location" to location,
                        "dateLost" to dateLost,
                        "imageUrl" to imageUrl,
                        "fullName" to fullName,
                        "email" to email,
                        "phone" to phone

                    )

                    firestore.collection("lost_items")
                        .add(lostItem)

                        .addOnSuccessListener {

                            Log.d("FIRESTORE", "Lost item uploaded")

                        }

                        .addOnFailureListener {

                            Log.e("FIRESTORE", it.message.toString())

                        }

                }

            }

            .addOnFailureListener {

                Log.e("STORAGE", it.message.toString())

            }

    }
}