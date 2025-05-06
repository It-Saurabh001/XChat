package com.example.xchat.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import com.example.xchat.CommonProgressBar
import com.example.xchat.CommonRow
import com.example.xchat.CommonRows
import com.example.xchat.DestinationScreen
import com.example.xchat.LCViewModel
import com.example.xchat.TitleText
import com.example.xchat.navigateTo


@Composable
fun ChatListScreen(
    navController: NavController ,
    vm: LCViewModel
) {

    extracted(navController,vm)
}
@Composable
fun extracted(navController: NavController, vm:LCViewModel) {

    var selectedItem by remember { mutableStateOf(BottomNavigationItem.CHATLIST) } // State for selected item
    val inProgress = vm.inProcessChats
    if (inProgress.value){
        CommonProgressBar()

    }else{
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember {mutableStateOf(false)}
        val onFabClick: ()-> Unit = {showDialog.value = true}
        val onDismiss:()->Unit = {showDialog.value = false}
        val onAddChat : (String)->Unit = {
            Log.d("TAG", "extracted: $it")
            vm.onAddChat(it)
//            Log.d("TAG", "extracted: ${vm.onAddChat(it)}")
            showDialog.value = false
        }
        Scaffold(
            floatingActionButton = {
                FAB(showDialog = showDialog.value,onFabClick = onFabClick,onDismiss = onDismiss,onAddChat = onAddChat

            )
                Log.d("TAG", "extracted: $onAddChat")},
            content = {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                    
                    TitleText(txt = "Chats")
                    
                    if(chats.isEmpty()){
                        Log.d("TAG", "extracted: $chats")
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Text(text = "No Chats Available")
                        }
                    }
                    else{
                        Log.d("TAG", "chat screen is not empty")
//                        LazyColumn(modifier = Modifier.weight(1f)) {
//                            items(chats){chat->
//
//                                val chatUser = if(chat.user1.userId == userData?.userId){
//                                    chat.user2
//                                }else{
//                                    chat.user1
//                                }
//                                Log.d("TAG", "chatuserid : ${chatUser.userId}")
//                                CommonRow(imageUrl = chatUser.imageUrl, name =chatUser.name ) {
//                                    chat.chatId?.let { chatId->
//                                //                                        chatId ->
//                                //                                        navController.navigate("${DestinationScreen.SingleChat.route}/${chatId}")
//                                        Log.d("TAG","chatid is : ${chatId}")
//                                        navigateTo(navController,
////                                            DestinationScreen.Profile.route
//                                            DestinationScreen.SingleChat.createRoute(chatId = chatId)
//                                        )
//                                    }
//
//                                }
//
//                            }
//                        }
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(chats){chat->

                                val chatUser = if(chat.user1.userId == userData?.userId){
                                    chat.user2
                                }else{
                                    chat.user1
                                }
                                Log.d("TAG", "chatuserid : ${chatUser.userId}")
                                CommonRows(imageUrl = chatUser.imageUrl, name =chatUser.name ,chatId = chat.chatId) {
                                        Log.d("TAG","chatid is : ${it}")
                                        navigateTo(navController,
//                                            DestinationScreen.Profile.route
                                            DestinationScreen.SingleChat.createRoute(chatId = it)
                                        )


                                }

                            }
                        }

                        //

                    }
                    BottomNavigationMenu(
                        selectedItem = selectedItem,
                        navController =navController
                    )
                }
            }
        )
    }

}

@Composable
fun FAB (
    showDialog: Boolean,
    onFabClick : ()->Unit,
    onDismiss: ()-> Unit,
    onAddChat : (String) -> Unit
    ){
    val addChatNumber = remember{ mutableStateOf("") }
    if(showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatNumber.value = ""
        }, confirmButton = {
            Button(
                onClick = { onAddChat(addChatNumber.value) },
                modifier = Modifier
//                    .background(Color.Blue)
//                    .clip(RoundedCornerShape(10))


                ) {
                Text(
                    text = "Add Chat",
                    modifier = Modifier.size(40.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }

        },
            title = {
                Text(text = "Add Chat")
            },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    label = {Text(text = "Add Number")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1

                )
            }
        )
    }

        FloatingActionButton(onClick = { onFabClick() },
            containerColor = MaterialTheme.colorScheme.secondary,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 40.dp)) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White )

        }

}
