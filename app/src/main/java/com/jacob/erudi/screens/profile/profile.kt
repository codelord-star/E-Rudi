package com.jacob.erudi.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.R

@Composable
fun ProfileScreen(navController: NavHostController){
    Scaffold() {
        innerpadding->
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
//                    Text()

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.labelMedium
                    )
//                    Text()

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Phone Number",
                        style = MaterialTheme.typography.labelMedium
                    )
//                    Text()

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Role",
                        style = MaterialTheme.typography.labelMedium
                    )
//                    Text()
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

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
    ProfileScreen(rememberNavController())
}