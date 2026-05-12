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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.models.ClaimedItem
import com.jacob.erudi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyClaimedItems(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val userEmail = FirebaseAuth.getInstance().currentUser?.email

    var myClaimedItems by remember { mutableStateOf<List<ClaimedItem>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<ClaimedItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("claimed_items")
            .whereEqualTo("claimerEmail", userEmail)
            .get()
            .addOnSuccessListener { result ->
                myClaimedItems = result.documents.mapNotNull { doc ->
                    doc.toObject(ClaimedItem::class.java)?.copy(id = doc.id)
                }
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Claims History", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppDeepBlue,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = SurfaceGray
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AppDeepBlue)
            } else if (myClaimedItems.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Text("No claims found", color = Color.Gray, fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(myClaimedItems) { item ->
                        MyClaimedItemCard(item = item, onDeleteClick = {
                            itemToDelete = it
                            showDeleteDialog = true
                        })
                    }
                }
            }
        }

        if (showDeleteDialog && itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = DestructiveRed) },
                title = { Text("Retract Claim?") },
                text = { Text("This will remove your claim for ${itemToDelete?.itemName}. You will need to submit a new claim if you change your mind.") },
                confirmButton = {
                    Button(
                        onClick = {
                            db.collection("claimed_items").document(itemToDelete!!.id).delete()
                                .addOnSuccessListener {
                                    myClaimedItems = myClaimedItems.filter { it.id != itemToDelete!!.id }
                                    showDeleteDialog = false
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DestructiveRed)
                    ) { Text("Confirm Delete") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel", color = Color.Gray) }
                },
                shape = RoundedCornerShape(28.dp)
            )
        }
    }
}

@Composable
fun MyClaimedItemCard(item: ClaimedItem, onDeleteClick: (ClaimedItem) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Header: Status Badge
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Claim Pending",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppDeepBlue.copy(alpha = 0.7f)
                )
                Icon(Icons.Default.MoreVert, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.LightGray)
            }

            // Image
            if (item.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(8.dp)).padding(horizontal = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = item.itemName, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = item.category, fontSize = 14.sp, color = AppDeepBlue)

                Spacer(modifier = Modifier.height(12.dp))

                ClaimDetailRow(Icons.Default.LocationOn, "Found at", item.locationFound)
                ClaimDetailRow(Icons.Default.Person, "Finder", item.originalOwnerEmail)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onDeleteClick(item) },
                        colors = ButtonDefaults.textButtonColors(contentColor = DestructiveRed)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove Claim", fontWeight = FontWeight.Bold)
                    }
                    
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ClaimDetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(6.dp))
        Text("$label: ", fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
fun MyClaimedPreview() {
    MyClaimedItems(rememberNavController())
}
