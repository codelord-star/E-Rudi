package com.jacob.erudi.screens.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.models.ClaimedItem
import com.jacob.erudi.navigation.ROUTE_RETURNED
import com.jacob.erudi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimedItems(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    var claimedItems by remember { mutableStateOf<List<ClaimedItem>>(emptyList()) }
    var showReturnDialog by remember { mutableStateOf(false) }
    var itemToReturn by remember { mutableStateOf<ClaimedItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("claimed_items")
            .get()
            .addOnSuccessListener { result ->
                claimedItems = result.documents.mapNotNull { doc ->
                    doc.toObject(ClaimedItem::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verified Claims", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ProgressGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundMint
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = ProgressGreen)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(claimedItems) { item ->
                        ClaimedItemCard(
                            item = item,
                            onReturnClick = {
                                itemToReturn = it
                                showReturnDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showReturnDialog && itemToReturn != null) {
            AlertDialog(
                onDismissRequest = { showReturnDialog = false },
                icon = { Icon(Icons.Default.Verified, contentDescription = null, tint = ProgressGreen) },
                title = { Text("Finalize Return") },
                text = { Text("Confirming this will move '${itemToReturn!!.itemName}' to the Returned Archives. This signifies the owner has received their item.") },
                confirmButton = {
                    Button(
                        onClick = {
                            val returnedData = hashMapOf(
                                "itemName" to itemToReturn!!.itemName,
                                "category" to itemToReturn!!.category,
                                "description" to itemToReturn!!.description,
                                "locationFound" to itemToReturn!!.locationFound,
                                "dateFound" to itemToReturn!!.dateFound,
                                "imageUrl" to itemToReturn!!.imageUrl,
                                "originalOwnerEmail" to itemToReturn!!.originalOwnerEmail,
                                "claimerEmail" to itemToReturn!!.claimerEmail
                            )

                            db.collection("returned_items").add(returnedData).addOnSuccessListener {
                                db.collection("claimed_items").document(itemToReturn!!.id).delete().addOnSuccessListener {
                                    showReturnDialog = false
                                    navController.navigate(ROUTE_RETURNED)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ProgressGreen)
                    ) { Text("Confirm Return") }
                },
                dismissButton = {
                    @Suppress("DEPRECATION")
                    TextButton(onClick = { showReturnDialog = false }) { Text("Cancel", color = Color.Gray) }
                }
            )
        }
    }
}

@Composable
fun ClaimedItemCard(
    item: ClaimedItem,
    onReturnClick: (ClaimedItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Success Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ProgressGreen.copy(alpha = 0.08f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = ProgressGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                @Suppress("DEPRECATION")
                Text("CLAIM PENDING RETURN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ProgressGreen)
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (item.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        @Suppress("DEPRECATION")
                        Text(item.itemName, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = DeepTeal)
                        Surface(
                            color = BackgroundMint,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = item.category,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 12.sp,
                                color = ProgressGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logistics Grid
                Row(modifier = Modifier.fillMaxWidth()) {
                    ClaimInfoBlock(Modifier.weight(1f), Icons.Default.Place, "Found At", item.locationFound)
                    ClaimInfoBlock(Modifier.weight(1f), Icons.Default.History, "Found On", item.dateFound)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Email Context
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    EmailLine(label = "Finder", email = item.originalOwnerEmail)
                    Spacer(modifier = Modifier.height(4.dp))
                    EmailLine(label = "Claimant", email = item.claimerEmail)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onReturnClick(item) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ProgressGreen)
                ) {
                    Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    @Suppress("DEPRECATION")
                    Text("Mark as Successfully Returned", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ClaimInfoBlock(modifier: Modifier, icon: ImageVector, label: String, value: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DeepTeal)
        }
    }
}

@Composable
fun EmailLine(label: String, email: String) {
    Row {
        @Suppress("DEPRECATION")
        Text("$label: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Text(email, fontSize = 11.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun ClaimedPreview() {
    ClaimedItems(rememberNavController())
}
