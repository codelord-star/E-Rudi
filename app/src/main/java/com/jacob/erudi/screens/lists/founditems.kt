package com.jacob.erudi.screens.lists

import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.models.FoundItem
import com.jacob.erudi.navigation.ROUTE_MYCLAIMS
import com.jacob.erudi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundItemsList(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    var foundItems by remember { mutableStateOf<List<FoundItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("found_items")
            .get()
            .addOnSuccessListener { result ->
                foundItems = result.documents.mapNotNull { doc ->
                    doc.toObject(FoundItem::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Found Items Catalog", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppDeepBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppDeepBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(foundItems) { item ->
                    FoundItemCard(
                        item = item,
                        onClaimClick = { selectedItem ->
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val claimerEmail = currentUser?.email ?: ""

                            val claimedItem = hashMapOf(
                                "itemName" to selectedItem.itemName,
                                "category" to selectedItem.category,
                                "description" to selectedItem.description,
                                "locationFound" to selectedItem.foundLocation,
                                "dateFound" to selectedItem.dateFound,
                                "imageUrl" to selectedItem.imageUrl,
                                "originalOwnerEmail" to selectedItem.email,
                                "claimerEmail" to claimerEmail
                            )

                            FirebaseFirestore.getInstance()
                                .collection("claimed_items")
                                .add(claimedItem)
                                .addOnSuccessListener {
                                    Toast.makeText(navController.context, "Claim Submitted", Toast.LENGTH_SHORT).show()
                                    navController.navigate(ROUTE_MYCLAIMS)
                                }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FoundItemCard(item: FoundItem, onClaimClick: (FoundItem) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image Section
            if (item.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Content Section
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.itemName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Badge(containerColor = AppDeepBlue.copy(alpha = 0.1f)) {
                        Text(item.category, color = AppDeepBlue, modifier = Modifier.padding(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Detail Rows
                ItemInfoRow(Icons.Default.LocationOn, "Location", item.foundLocation)
                ItemInfoRow(Icons.Default.DateRange, "Date Found", item.dateFound)
                
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 8.dp),
                    maxLines = 2
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

                // Footer Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Reported by:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(item.email, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                    }
                    
                    Button(
                        onClick = { onClaimClick(item) },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Claim")
                    }
                }
            }
        }
    }
}

@Composable
fun ItemInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label: ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
    }
}

@Preview(showBackground = true)
@Composable
fun FoundItemsPreview() {
    FoundItemsList(rememberNavController())
}
