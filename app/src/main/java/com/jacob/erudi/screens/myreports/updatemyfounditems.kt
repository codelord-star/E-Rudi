package com.jacob.erudi.screens.myreports

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.jacob.erudi.data.ItemViewModel
import com.jacob.erudi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoundItemScreen(
    navController: NavController,
    itemId: String
) {
    val viewModel: ItemViewModel = viewModel()
    val db = FirebaseFirestore.getInstance()

    var itemName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var foundLocation by remember { mutableStateOf("") }
    var dateFound by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var currentImageUrl by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    LaunchedEffect(Unit) {
        db.collection("found_items").document(itemId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                itemName = document.getString("itemName") ?: ""
                category = document.getString("category") ?: ""
                description = document.getString("description") ?: ""
                foundLocation = document.getString("foundLocation") ?: "" // Corrected key check
                dateFound = document.getString("dateFound") ?: ""
                currentImageUrl = document.getString("imageUrl") ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Found Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FormEmerald,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = FormBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                AsyncImage(
                    model = imageUri ?: currentImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Surface(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                    color = FormEmerald,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Photo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            StyledEditField(value = itemName, onValueChange = { itemName = it }, label = "Item Name", icon = Icons.Default.Edit)
            StyledEditField(value = category, onValueChange = { category = it }, label = "Category", icon = Icons.Default.Category)
            StyledEditField(value = foundLocation, onValueChange = { foundLocation = it }, label = "Location Found", icon = Icons.Default.Place)
            StyledEditField(value = dateFound, onValueChange = { dateFound = it }, label = "Date Found", icon = Icons.Default.DateRange)
            StyledEditField(value = description, onValueChange = { description = it }, label = "Description", icon = Icons.Default.Description, singleLine = false)

            Spacer(modifier = Modifier.height(32.dp))

            // Update Button
            Button(
                onClick = {
                    isUpdating = true
                    val updateData = mutableMapOf<String, Any>(
                        "itemName" to itemName,
                        "category" to category,
                        "description" to description,
                        "foundLocation" to foundLocation,
                        "dateFound" to dateFound
                    )

                    if (imageUri == null) {
                        db.collection("found_items").document(itemId).update(updateData)
                            .addOnSuccessListener { navController.popBackStack() }
                    } else {
                        viewModel.uploadImageToCloudinary(
                            context = navController.context,
                            imageUri = imageUri!!,
                            onSuccess = { newUrl ->
                                updateData["imageUrl"] = newUrl
                                db.collection("found_items").document(itemId).update(updateData)
                                    .addOnSuccessListener { navController.popBackStack() }
                            },
                            onError = { isUpdating = false }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FormEmerald),
                enabled = !isUpdating
            ) {
                if (isUpdating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StyledEditField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = FormEmerald, modifier = Modifier.size(20.dp)) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FormEmerald,
            focusedLabelColor = FormEmerald,
            cursorColor = FormEmerald
        )
    )
}
