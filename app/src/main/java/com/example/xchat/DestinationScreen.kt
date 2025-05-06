package com.example.xchat

sealed class DestinationScreen(var route : String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("Login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}"){
        fun createRoute(chatId: String) = "singleChat/$chatId"
    }
    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}"){
        fun createRoute(userId: String) = "singleStatus/ $userId"
    }
}