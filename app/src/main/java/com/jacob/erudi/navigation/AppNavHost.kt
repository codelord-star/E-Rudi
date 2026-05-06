package com.jacob.erudi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.screens.dashboard.AdminDashboard
import com.jacob.erudi.screens.dashboard.UserDashboard
import com.jacob.erudi.screens.home.HomeScreen
import com.jacob.erudi.screens.login.LoginScreen
import com.jacob.erudi.screens.profile.ProfileScreen
import com.jacob.erudi.screens.register.RegisterScreen
import com.jacob.erudi.screens.report.ReportFoundItem
import com.jacob.erudi.screens.report.ReportLostItem
import com.jacob.erudi.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String=ROUTE_SPLASH
){
    NavHost(navController=navController,
        startDestination=startDestination
    ){
        composable (ROUTE_SPLASH){
            SplashScreen(navController)
        }
        composable (ROUTE_HOME){
            HomeScreen(navController)
        }
        composable (ROUTE_REGISTER){
            RegisterScreen(navController)
        }
        composable (ROUTE_LOGIN){
            LoginScreen(navController)
        }
        composable (ROUTE_ADMINDASHBOARD){
            AdminDashboard(navController)
        }
        composable (ROUTE_USERDASHBOARD){
            UserDashboard(navController)
        }
        composable (ROUTE_PROFILE){
            ProfileScreen(navController)
        }
        composable (ROUTE_REPORTLOSTITEM){
            ReportLostItem(navController)
        }
        composable(ROUTE_REPORTFOUNDITEM) {
            ReportFoundItem(navController)
        }
        composable(ROUTE_LOSTITEMS) {

        }
        composable(ROUTE_FOUNDITEMS) {

        }
        composable(ROUTE_MYREPORTS) {

        }
        composable(ROUTE_CLAIMED) {

        }
    }
}