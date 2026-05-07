package com.jacob.erudi.screens.profile

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.auth.User
import com.jacob.erudi.R
import com.jacob.erudi.data.AuthViewModel
import com.jacob.erudi.models.AppUser
import com.jacob.erudi.navigation.ROUTE_PROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavHostController){
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

                        Text(
                            text = "eRudi"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Blue,
                contentColor = Color.White,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(
                            Icons.Default.Home,
                            contentDescription = "home icon") },
                        label = { Text("HOME",
                            color = Color.White) }
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Person,
                            contentDescription = "person icon") },
                        label = { Text("PROFILE ",
                            color = Color.White) }
                    )
                }
            }
        }
    ) {
        innerpadding->
        val viewModel: AuthViewModel = viewModel()
        var user by remember { mutableStateOf<AppUser?>(null) }

        LaunchedEffect(Unit) {
            viewModel.getUserData {
                user = it
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MY PROFILE",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Image(
                painter = painterResource(id = R.drawable.profile2),
                contentDescription = "profile",
                modifier = Modifier
                    .size(250.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Full Name",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text("${user?.fullname}")

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text("${user?.email}")

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Phone Number",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text("${user?.phone}")

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Role",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text("${user?.role}")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //  Logout Button
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview(){
    Profile(rememberNavController())
}