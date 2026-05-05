package com.jacob.erudi.screens.services

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ReportLostItem(navController: NavHostController){

}

@Preview(showBackground = true)
@Composable
fun ReportLostItemPreview(){
    ReportLostItem(rememberNavController())
}