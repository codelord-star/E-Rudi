package com.jacob.erudi.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ReturnedItems(navController : NavHostController){

}

@Preview(showBackground = true)
@Composable
fun ReturnedPreview(){
    ReturnedItems(rememberNavController())
}