package com.jacob.erudi.screens.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.jacob.erudi.R
import com.jacob.erudi.data.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportLostItem(navController: NavHostController){
    val viewModel: ItemViewModel = viewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon1),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "E-Rudi"
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
        //for image upload
        val context = LocalContext.current

        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedImageUri = uri
        }

        var itemName by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }
        var dateLost by remember { mutableStateOf("") }

        var expanded by remember { mutableStateOf(false) }

        val categories = listOf("Electronics", "Clothing", "Documents", "Accessories", "Others")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerpadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "REPORT LOST ITEM",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                category = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location Lost") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dateLost,
                onValueChange = { dateLost = it },
                label = { Text("Date Lost") },
                placeholder = { Text("DD/MM/YYYY") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 🔹 Image Placeholder
            Button(
                onClick = {
                    // TODO: Open image picker
                    launcher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Image")
            }
            //show selected image preview
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Submit Button
            Button(
                onClick = {
                    viewModel.uploadLostItem(

                        itemName = itemName,

                        category = category,

                        description = description,

                        location = location,

                        dateLost = dateLost,

                        imageUri = selectedImageUri,

                        fullName = "Jacob Hagee",

                        email = "jacob@gmail.com",

                        phone = "0712345678"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Report")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportLostItemPreview(){
    ReportLostItem(rememberNavController())
}