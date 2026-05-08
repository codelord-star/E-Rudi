package com.jacob.erudi.screens.myreports

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.screens.lists.FoundItemCard
import com.jacob.erudi.models.FoundItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFoundItems(navController : NavHostController){
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

                        Text("MY FOUND ITEMS")
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

        var myFoundItems by remember {
            mutableStateOf<List<FoundItem>>(emptyList())
        }

        LaunchedEffect(Unit) {

            db.collection("found_items")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { result ->

                    val items = result.documents.mapNotNull { document ->
                        val item = document.toObject(FoundItem::class.java)

                        item?.copy(id = document.id)
                    }

                    myFoundItems = items
                }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(innerpadding)
        ) {

            items(myFoundItems) { item ->
                FoundItemCard(item)
            }
        }
    }
}

@Composable
fun FoundItemCard(item: FoundItem) {

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
                    contentDescription = "Found item image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "Text: ${item.itemName}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Category: ${item.category}")

            Text(text = "Found at: ${item.foundLocation}")

            Text(text = "Date Found: ${item.dateFound}")

            Text(text = "Description: ${item.description}")

            Text(text = "Contact: ${item.email}")

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White
                    )) {
                    Text("Update")
                }
                Button(onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )) {
                    Text("Delete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyFoundItemsPreview(){
    MyFoundItems(rememberNavController())
}