package com.jacob.erudi.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.R
import com.jacob.erudi.navigation.ROUTE_FOUNDITEMS
import com.jacob.erudi.navigation.ROUTE_MYREPORTS
import com.jacob.erudi.navigation.ROUTE_REPORTFOUNDITEM
import com.jacob.erudi.navigation.ROUTE_REPORTLOSTITEM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard(navController: NavHostController){
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
                actions = {
//                    IconButton(onClick = {}) {
//                        Icon(
//                            Icons.Default.ExitToApp,
//                            contentDescription = "sign out"
//                        )
//                    }
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
//                    NavigationBarItem(
//                        selected = true,
//                        onClick = {navController.navigate(ROUTE_USERDASHBOARD)},
//                        icon = { Icon(Icons.Default.Home,
//                            contentDescription = "home icon") },
//                        label = { Text("HOME",
//                            color = Color.White) }
//                    )
//                    NavigationBarItem(
//                        selected = false,
//                        onClick = {navController.navigate(ROUTE_PROFILE)},
//                        icon = { Icon(Icons.Default.Person,
//                            contentDescription = "person icon") },
//                        label = { Text("PROFILE ",
//                            color = Color.White) }
//                    )
                }
            }
        }
    ) {
        innerpadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerpadding),
        ) {
            Card(modifier = Modifier
                .width(200.dp)
                .padding(16.dp)
                .height(150.dp)
                .clickable{navController.navigate(ROUTE_REPORTLOSTITEM)},
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
                    Text("REPORT LOST ITEM",
                        fontWeight = FontWeight.Bold)
                    Text("Report an item you have lost")
                }
            }
            Card(modifier = Modifier
                .width(200.dp)
                .padding(16.dp)
                .height(150.dp)
                .clickable{navController.navigate(ROUTE_REPORTFOUNDITEM)},
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
                    Text("REPORT FOUND ITEM",
                        fontWeight = FontWeight.Bold)
                    Text("Report an item you have found misplaced")
                }
            }
            Card(modifier = Modifier
                .width(200.dp)
                .padding(16.dp)
                .height(150.dp)
                .clickable{navController.navigate(ROUTE_FOUNDITEMS)},
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.Black,
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("FOUND ITEMS",
                        fontWeight = FontWeight.Bold)
                    Text("View a list of items that have been found")
                }
            }
            Card(modifier = Modifier
                .width(200.dp)
                .padding(16.dp)
                .height(150.dp)
                .clickable{navController.navigate(ROUTE_MYREPORTS)},
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.Black,
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("MY REPORTS",
                        fontWeight = FontWeight.Bold)
                    Text("View a list of items that you reported lost")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun UserDashboardPreview(){
    UserDashboard(rememberNavController())
}