package com.jacob.erudi.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.erudi.navigation.ROUTE_LOGIN
import com.jacob.erudi.navigation.ROUTE_REGISTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {
    // Define a professional Gradient
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A237E), Color(0xFF3949AB), Color(0xFF5C6BC0))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("eRudi", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(brush = gradientBrush)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Hero Icon
            Surface(
                modifier = Modifier.size(160.dp),
                shape = RoundedCornerShape(40.dp),
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Logo",
                    modifier = Modifier.padding(30.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "eRudi",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            
            Text(
                text = "Ripoti... Irudi kwa mwenyewe",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(ROUTE_REGISTER) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign Up", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { navController.navigate(ROUTE_LOGIN) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 2.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign In", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    InfoRow("🔍 Report and recover lost items instantly.")
                    InfoRow("🤝 Connect directly with community owners.")
                    InfoRow("🛡️ Fast, secure, and reliable recovery.")
                }
            }
        }
    }
}

@Composable
fun InfoRow(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color(0xFF333333),
        modifier = Modifier.padding(vertical = 4.dp),
        textAlign = TextAlign.Start,
        lineHeight = 20.sp
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home(rememberNavController())
}
