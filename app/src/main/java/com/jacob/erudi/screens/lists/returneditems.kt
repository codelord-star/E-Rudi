package com.jacob.erudi.screens.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.models.ReturnedItem
import com.jacob.erudi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnedItems(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    var returnedItems by remember { mutableStateOf<List<ReturnedItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("returned_items")
            .get()
            .addOnSuccessListener { result ->
                returnedItems = result.documents.mapNotNull { doc ->
                    doc.toObject(ReturnedItem::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Returned Archives", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ArchiveCharcoal,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = SoftSilver
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ArchiveCharcoal)
            }
        } else if (returnedItems.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                Text("No archived returns yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(returnedItems) { item ->
                    ReturnedItemCard(item)
                }
            }
        }
    }
}

@Composable
fun ReturnedItemCard(item: ReturnedItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Status Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE3F2FD)) // Light blue tint for "History"
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF1976D2))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "OFFICIALLY RETURNED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF1976D2)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.itemName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = ArchiveCharcoal
                        )
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = SuccessGold
                        )
                    }

                    if (item.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Grid
                Row(modifier = Modifier.fillMaxWidth()) {
                    ArchiveDetailItem(Modifier.weight(1f), "Location", item.locationFound)
                    ArchiveDetailItem(Modifier.weight(1f), "Date", item.dateFound)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

                // The Trail (Finder -> Claimant)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoftSilver, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text("TRANSACTION LOG", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    TrailRow(label = "From", email = item.originalOwnerEmail)
                    Spacer(modifier = Modifier.height(4.dp))
                    TrailRow(label = "To", email = item.claimerEmail)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = item.description,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun ArchiveDetailItem(modifier: Modifier, label: String, value: String) {
    Column(modifier = modifier) {
        Text(label, fontSize = 11.sp, color = Color.Gray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ArchiveCharcoal)
    }
}

@Composable
fun TrailRow(label: String, email: String) {
    Row {
        Text("$label: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ArchiveCharcoal)
        Text(email, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun ReturnedPreview() {
    ReturnedItems(rememberNavController())
}
