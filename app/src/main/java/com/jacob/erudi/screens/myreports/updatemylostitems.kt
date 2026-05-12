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
fun EditLostItemScreen(
    navController: NavController,
    itemId: String
) {
    val viewModel: ItemViewModel = viewModel()
    val db = FirebaseFirestore.getInstance()

    var itemName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var dateLost by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var currentImageUrl by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    // FETCH CURRENT DATA
    LaunchedEffect(Unit) {
        db.collection("lost_items").document(itemId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                itemName = document.getString("itemName") ?: ""
                category = document.getString("category") ?: ""
                description = document.getString("description") ?: ""
                location = document.getString("location") ?: ""
                dateLost = document.getString("dateLost") ?: ""
                currentImageUrl = document.getString("imageUrl") ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Lost Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FormIndigo,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = FormBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Image Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                AsyncImage(
                    model = imageUri ?: currentImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Overlay Button for Image change
                Surface(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                    color = FormIndigo,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Photo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Inputs
            LostEditField(value = itemName, onValueChange = { itemName = it }, label = "Item Name", icon = Icons.Default.Label)
            LostEditField(value = category, onValueChange = { category = it }, label = "Category", icon = Icons.Default.Category)
            LostEditField(value = location, onValueChange = { location = it }, label = "Location Lost", icon = Icons.Default.MyLocation)
            LostEditField(value = dateLost, onValueChange = { dateLost = it }, label = "Date Lost", icon = Icons.Default.CalendarToday)
            LostEditField(value = description, onValueChange = { description = it }, label = "Description", icon = Icons.Default.Notes, singleLine = false)

            Spacer(modifier = Modifier.height(32.dp))

            // Final Action
            Button(
                onClick = {
                    isSaving = true
                    val updateMap = mutableMapOf<String, Any>(
                        "itemName" to itemName,
                        "category" to category,
                        "description" to description,
                        "location" to location,
                        "dateLost" to dateLost
                    )

                    if (imageUri == null) {
                        db.collection("lost_items").document(itemId).update(updateMap)
                            .addOnSuccessListener { navController.popBackStack() }
                            .addOnFailureListener { isSaving = false }
                    } else {
                        viewModel.uploadImageToCloudinary(
                            context = navController.context,
                            imageUri = imageUri!!,
                            onSuccess = { url ->
                                updateMap["imageUrl"] = url
                                db.collection("lost_items").document(itemId).update(updateMap)
                                    .addOnSuccessListener { navController.popBackStack() }
                            },
                            onError = { isSaving = false }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FormIndigo),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Update Report", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LostEditField(
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
        leadingIcon = { Icon(icon, contentDescription = null, tint = FormIndigo, modifier = Modifier.size(20.dp)) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FormIndigo,
            focusedLabelColor = FormIndigo,
            cursorColor = FormIndigo
        )
    )
}
