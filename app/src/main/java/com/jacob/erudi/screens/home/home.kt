package com.jacob.erudi.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.R
import com.jacob.erudi.navigation.ROUTE_LOGIN
import com.jacob.erudi.navigation.ROUTE_REGISTER

@Composable
fun HomeScreen(navController: NavHostController){
    Scaffold() {
        innerpadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon1),
                contentDescription = "app logo",
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "E-Rudi",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ripoti...Irudi kwa mwenyewe",
                fontFamily = FontFamily.Serif,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {navController.navigate(ROUTE_REGISTER)},
                ) {
                    Text(
                        text = "Sign up",
                    )
                }
                Button(
                    onClick = {navController.navigate(ROUTE_LOGIN)},
                ) {
                    Text(
                        text = "Sign in"
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            //ABOUT
            Text(
                text = "",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    HomeScreen(rememberNavController())
}
