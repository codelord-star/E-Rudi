package com.jacob.erudi.screens.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.models.ClaimedItem
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyClaimedItems(navController : NavHostController){
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

                        Text("MY CLAIMS")
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
        val db = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email

        var myClaimedItems by remember {
            mutableStateOf<List<ClaimedItem>>(emptyList())
        }
        var showDeleteDialog by remember {
            mutableStateOf(false)
        }

        var itemToDelete by remember {
            mutableStateOf<ClaimedItem?>(null)
        }

        LaunchedEffect(Unit) {

            db.collection("claimed_items")
                .whereEqualTo(
                    "claimerEmail",
                    userEmail
                )
                .get()
                .addOnSuccessListener { result ->

                    val items = result.documents.mapNotNull { document ->

                        val item =
                            document.toObject(
                                ClaimedItem::class.java
                            )

                        item?.copy(id = document.id)
                    }

                    myClaimedItems = items
                }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
        ) {

            items(myClaimedItems) { item ->
                MyClaimedItemCard(
                    item=item,
                    onDeleteClick = { selectedItem ->
                        itemToDelete = selectedItem
                        showDeleteDialog = true
                    }
                )
            }
        }
        if (showDeleteDialog && itemToDelete != null) {

            AlertDialog(

                onDismissRequest = {
                    showDeleteDialog = false
                    itemToDelete = null
                },

                title = {
                    Text("Confirm Delete")
                },

                text = {
                    Text("Are you sure you want to delete this claim?")
                },

                confirmButton = {

                    Button(
                        onClick = {

                            FirebaseFirestore.getInstance()
                                .collection("claimed_items")
                                .document(itemToDelete!!.id)
                                .delete()
                                .addOnSuccessListener {

                                    myClaimedItems =
                                        myClaimedItems.filter {

                                            it.id != itemToDelete!!.id
                                        }

                                    showDeleteDialog = false
                                    itemToDelete = null
                                }
                        }
                    ) {
                        Text("Delete")
                    }
                },

                dismissButton = {

                    OutlinedButton(
                        onClick = {
                            showDeleteDialog = false
                            itemToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun MyClaimedItemCard(
    item: ClaimedItem,
    onDeleteClick: (ClaimedItem) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            if (item.imageUrl.isNotEmpty()) {

                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
            Text(text = item.itemName)

            Text(text = "Category: ${ item.category }")

            Text(
                text = "Found at: ${item.locationFound}"
            )

            Text(
                text = "Date Found: ${item.dateFound}"
            )

            Text(text = "Decsription: ${ item.description }")

            Text(
                text = "Original finder: ${item.originalOwnerEmail}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Claimed by: ${item.claimerEmail}"
            )

            Button(
                onClick = {onDeleteClick(item)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
                ) {
                Text("Delete claim")
            }
        }
    }
}

@Preview
@Composable
fun MyClaimedPreview(){
    MyClaimedItems(rememberNavController())
}