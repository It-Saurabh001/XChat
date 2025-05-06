package com.example.xchat

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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

@Composable
fun ChatAppNavigation(navController: NavHostController,vm:LCViewModel) {

    NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
        composable(DestinationScreen.SignUp.route){
            SignUpScreen(navController ,vm)
        }
        composable(DestinationScreen.ChatList.route){
            ChatListScreen(vm = vm, navController = navController)
        }
        composable(
            DestinationScreen.SingleChat.route // "singleChat/{chatId}"
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            chatId?.let {
                SingleChatScreen(navController = navController, vm = vm, chatId = it)
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