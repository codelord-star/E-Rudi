package com.jacob.erudi.screens.myreports

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.data.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoundItemScreen(
    navController: NavController,
    itemId: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "search icon",
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "UPDATE FOUND ITEM"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
            )
        }
    ) {
            innerpadding->
        val viewModel: ItemViewModel = viewModel()
        val db = FirebaseFirestore.getInstance()

        var itemName by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var foundLocation by remember { mutableStateOf("") }
        var dateFound by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var currentImageUrl by remember { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            imageUri = uri
        }

        // FETCH CURRENT ITEM DATA
        LaunchedEffect(Unit) {

            db.collection("found_items")
                .document(itemId)
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {

                        itemName = document.getString("itemName") ?: ""

                        category = document.getString("category") ?: ""

                        description = document.getString("description") ?: ""

                        foundLocation = document.getString("location") ?: ""

                        dateFound = document.getString("dateFound") ?: ""

                        currentImageUrl = document.getString("imageUrl") ?: ""
                    }
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerpadding)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                value = itemName,
                onValueChange = {
                    itemName = it
                },
                label = {
                    Text("Item Name")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = category,
                onValueChange = {
                    category = it
                },
                label = {
                    Text("Category")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = foundLocation,
                onValueChange = {
                    foundLocation = it
                },
                label = {
                    Text("Location Found")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dateFound,
                onValueChange = {
                    dateFound = it
                },
                label = {
                    Text("Date Found")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            AsyncImage(
                model = imageUri ?: currentImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CHANGE IMAGE BUTTON
            Button(
                onClick = {
                    launcher.launch("image/*")
                }
            ) {
                Text("Change Image")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // CASE 1: No new image selected → keep existing image
                    if (imageUri == null) {

                        db.collection("found_items")
                            .document(itemId)
                            .update(
                                mapOf(
                                    "itemName" to itemName,
                                    "category" to category,
                                    "description" to description,
                                    "foundLocation" to foundLocation,
                                    "dateFound" to dateFound,
                                    "imageUrl" to currentImageUrl
                                )
                            )
                            .addOnSuccessListener {
                                navController.popBackStack()
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirestoreUpdate", e.message.toString())
                            }

                    } else {

                        // CASE 2: New image selected → upload to Cloudinary first
                        viewModel.uploadImageToCloudinary(
                            context = navController.context,
                            imageUri = imageUri!!,

                            onSuccess = { newImageUrl ->

                                db.collection("found_items")
                                    .document(itemId)
                                    .update(
                                        mapOf(
                                            "itemName" to itemName,
                                            "category" to category,
                                            "description" to description,
                                            "foundLocation" to foundLocation,
                                            "dateFound" to dateFound,
                                            "imageUrl" to newImageUrl
                                        )
                                    )
                                    .addOnSuccessListener {
                                        navController.popBackStack()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FirestoreUpdate", e.message.toString())
                                    }
                            },

                            onError = { errorMessage ->
                                Log.e("CloudinaryUpload", errorMessage)
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Update Item")
            }
        }
    }
}