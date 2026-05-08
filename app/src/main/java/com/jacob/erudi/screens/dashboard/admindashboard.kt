package com.jacob.erudi.screens.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.R
import com.jacob.erudi.data.AuthViewModel
import com.jacob.erudi.navigation.ROUTE_LOGIN
import com.jacob.erudi.navigation.ROUTE_CLAIMED
import com.jacob.erudi.navigation.ROUTE_FOUNDITEMS
import com.jacob.erudi.navigation.ROUTE_LOSTITEMS
import com.jacob.erudi.navigation.ROUTE_PROFILE
import com.jacob.erudi.navigation.ROUTE_RETURNED

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavHostController){
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
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
                            text = "ADMINISTRATOR DASHBOARD"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        viewModel.signout()
                        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(ROUTE_LOGIN){
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "sign out"
                        )
                    }
                }
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
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Home,
                            contentDescription = "home icon") },
                        label = { Text("HOME",
                            color = Color.White) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {navController.navigate(ROUTE_PROFILE)},
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
        var fullname by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            viewModel.getCurrentUserName {
                fullname = it
            }
        }
        Text(text = "Welcome, $fullname")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerpadding),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(modifier = Modifier
                    .width(170.dp)
                    .padding(16.dp)
                    .height(150.dp)
                    .clickable{navController.navigate(ROUTE_LOSTITEMS)},
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red,
                        contentColor = Color.Black,
                    )
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("LOST ITEMS",
                            fontWeight = FontWeight.Bold)
                        Text("View the list of items reported as lost")
                    }
                }
                Card(modifier = Modifier
                    .width(170.dp)
                    .padding(16.dp)
                    .height(150.dp)
                    .clickable{navController.navigate(ROUTE_FOUNDITEMS)},
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black,
                    )
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("FOUND ITEMS",
                            fontWeight = FontWeight.Bold)
                        Text("View the list of items found misplaced but have been recovered")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) { }
            Card(modifier = Modifier
                .width(170.dp)
                .padding(16.dp)
                .height(150.dp)
                .clickable{navController.navigate(ROUTE_CLAIMED)},
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black,
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("CLAIMED ITEMS",
                        fontWeight = FontWeight.Bold)
                    Text("View the list of items that have been claimed")
                }
            }
            Card(modifier = Modifier
                .width(170.dp)
                .padding(16.dp)
                .height(150.dp)
                .clickable{navController.navigate(ROUTE_RETURNED)},
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray,
                    contentColor = Color.Black,
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("RETURNED ITEMS",
                        fontWeight = FontWeight.Bold)
                    Text("View the list of items that have been returned to their owners")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboard(rememberNavController())
}