package com.example.xchat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.xchat.Screens.ChatListScreen
import com.example.xchat.Screens.LoginScreen
import com.example.xchat.Screens.ProfileScreen
import com.example.xchat.Screens.SignUpScreen
import com.example.xchat.Screens.SingleChatScreen
import com.example.xchat.Screens.SingleStatusScreen
import com.example.xchat.Screens.StatusScreen
import com.example.xchat.DestinationScreen
import com.example.xchat.ui.theme.XChatTheme
import dagger.hilt.android.AndroidEntryPoint





@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            XChatTheme {
                Scaffold (modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ){
                    MyApp(modifier = Modifier.padding(it))
                }
            }
        }
    }


}


@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var vm : LCViewModel = hiltViewModel()
    ChatAppNavigation(navController = navController , vm = vm)
}