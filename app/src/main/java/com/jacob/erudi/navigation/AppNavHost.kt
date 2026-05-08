package com.jacob.erudi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.screens.login.Login
import com.jacob.erudi.screens.dashboard.AdminDashboard
import com.jacob.erudi.screens.dashboard.UserDashboard
import com.jacob.erudi.screens.home.Home
import com.jacob.erudi.screens.lists.FoundItemsList
import com.jacob.erudi.screens.lists.LostItemsList
import com.jacob.erudi.screens.myreports.MyFoundItems
import com.jacob.erudi.screens.myreports.MyLostItems
import com.jacob.erudi.screens.profile.Profile
import com.jacob.erudi.screens.register.Register
import com.jacob.erudi.screens.report.ReportFoundItem
import com.jacob.erudi.screens.report.ReportLostItem
import com.jacob.erudi.screens.splash.Splash

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String=ROUTE_HOME
){
    NavHost(navController=navController,
        startDestination=startDestination
    ){
        composable ("splash"){
            Splash(navController)
        }
        composable (ROUTE_HOME){
            Home(navController)
        }
        composable (ROUTE_REGISTER){
            Register(navController)
        }
        composable (ROUTE_LOGIN){
            Login(navController)
        }
        composable (ROUTE_ADMINDASHBOARD){
            AdminDashboard(navController)
        }
        composable (ROUTE_USERDASHBOARD){
            UserDashboard(navController)
        }
        composable (ROUTE_PROFILE){
            Profile(navController)
        }
        composable (ROUTE_REPORTLOSTITEM){
            ReportLostItem(navController)
        }
        composable(ROUTE_REPORTFOUNDITEM) {
            ReportFoundItem(navController)
        }
        composable(ROUTE_LOSTITEMS) {
            LostItemsList(navController)
        }
        composable(ROUTE_FOUNDITEMS) {
            FoundItemsList(navController)
        }
        composable(ROUTE_MYREPORTS) {

        }
        composable(ROUTE_MYLOSTITEMS) {
            MyLostItems(navController)
        }
        composable(ROUTE_MYFOUNDITEMS) {
            MyFoundItems(navController)
        }
        composable(ROUTE_CLAIMED) {

        }
    }
}