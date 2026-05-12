package com.jacob.erudi.screens.report

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.jacob.erudi.data.ItemViewModel
import com.jacob.erudi.navigation.ROUTE_FOUNDITEMS
import com.jacob.erudi.navigation.ROUTE_MYFOUNDITEMS
import com.jacob.erudi.ui.theme.*

// Consistent Theme Palette
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFoundItem(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ItemViewModel = viewModel()
    val scrollState = rememberScrollState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    var itemName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var foundLocation by remember { mutableStateOf("") }
    var dateFound by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Electronics", "Clothing", "Documents", "Accessories", "Others")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Found Item", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppDeepBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppSurface)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Instruction
            Text(
                text = "Item Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppDeepBlue,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Please provide accurate information to help the owner find their item.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp).align(Alignment.Start)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Item Name
                    ReportTextField(itemName, { itemName = it }, "Item Name", Icons.Default.Info)

                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, tint = AppDeepBlue) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AppDeepBlue)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Location
                    ReportTextField(foundLocation, { foundLocation = it }, "Location Found", Icons.Default.LocationOn)

                    // Date
                    ReportTextField(dateFound, { dateFound = it }, "Date Found (DD/MM/YYYY)", Icons.Default.DateRange)

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Detailed Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AppDeepBlue)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Image Upload Section
            Text(
                text = "Item Image",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppDeepBlue,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Preview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        TextButton(onClick = { launcher.launch("image/*") }) {
                            Text("Change Image", color = AppDeepBlue)
                        }
                    } else {
                        IconButton(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier.size(80.dp).background(AppSurface, CircleShape)
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = AppDeepBlue, modifier = Modifier.size(32.dp))
                        }
                        Text("Tap to upload a clear photo", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    viewModel.uploadFoundItem(
                        itemName = itemName,
                        category = category,
                        description = description,
                        foundLocation = foundLocation,
                        dateFound = dateFound,
                        imageUri = selectedImageUri,
                        context = context,

                        onSuccess = {

                            Toast.makeText(
                                context,
                                "Report submitted successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.navigate(ROUTE_MYFOUNDITEMS)
                        },

                        onError = { error ->

                            Toast.makeText(
                                context,
                                error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppDeepBlue)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SUBMIT REPORT", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ReportTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = AppDeepBlue) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AppDeepBlue)
    )
}

@Preview(showBackground = true)
@Composable
fun ReportFoundItemPreview() {
    ReportFoundItem(rememberNavController())
}
