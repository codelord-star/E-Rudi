package com.jacob.erudi.screens.myreports

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MyLostItems(navController : NavHostController){

}

@Preview(showBackground = true)
@Composable
fun MyLostItemsPreview(){
    MyLostItems(rememberNavController())
}