package com.example.xchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.xchat.Screens.ChatListScreen
import com.example.xchat.Screens.LoginScreen
import com.example.xchat.Screens.ProfileScreen
import com.example.xchat.Screens.SignUpScreen
import com.example.xchat.Screens.SingleChatScreen
import com.example.xchat.Screens.SingleStatusScreen
import com.example.xchat.Screens.StatusScreen
import com.example.xchat.ui.theme.XChatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route : String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("Login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}"){
        fun createRoute(id: String) = "singlechat/$id"
    }
    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}"){
        fun createRoute(userId: String) = "singleStatus/ $userId"
    }
}



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            XChatTheme {
                Surface (modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        var vm : LCViewModel = hiltViewModel()
        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
            composable(DestinationScreen.SignUp.route){
                SignUpScreen(navController ,vm)
            }
            composable(DestinationScreen.ChatList.route){
                ChatListScreen(vm = vm, navController = navController)
            }
            composable(DestinationScreen.SingleChat.route){
                val chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(navController,vm, chatId)
                }
            }
            composable(DestinationScreen.Login.route){
                LoginScreen(vm = vm, navController = navController)
            }
            composable(DestinationScreen.StatusList.route){
                StatusScreen(vm = vm, navController = navController)
            }
            composable(DestinationScreen.Profile.route){
                ProfileScreen(vm = vm, navController= navController)
            }
            composable(DestinationScreen.SingleStatus.route){
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    SingleStatusScreen(vm = vm, navController = navController, userId = it)
                }

            }

        }
    }
    @Preview
    @Composable
    private fun hello() {
        ChatAppNavigation()

    }
}
