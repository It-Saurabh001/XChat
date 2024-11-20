package com.example.xchat.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xchat.CommonDivider
import com.example.xchat.CommonProgressBar
import com.example.xchat.CommonRow
import com.example.xchat.DestinationScreen
import com.example.xchat.LCViewModel
import com.example.xchat.TitleText
import com.example.xchat.navigateTo

@Composable
fun StatusScreen(

    vm:LCViewModel,
    navController: NavController,
) {
    var selectedItem by remember { mutableStateOf(BottomNavigationItem.STATUSLIST) } // State for selected item

    val statuses by vm.status.collectAsState(initial = emptyList())
    val userData = vm.userData.value
    val inProcess = vm.inProgressStatus.value
    if (inProcess){
        CommonProgressBar()
    }else{

        val myStatuses = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatuses = statuses.filter {
            it.user.userId != userData?.userId
        }
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri ->
            uri?.let {
                vm.uploadStatus(uri)
            }

        }


        Scaffold (
            floatingActionButton = {
                FAB {
                    launcher.launch("image/*")
                }
            },
            content = {
                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(it)){
                    TitleText(txt = "Status")
                    if (statuses.isEmpty()){
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center){
                            Text(text = "No Status Available")

                        }
                    }else {
                        if (myStatuses.isNotEmpty()) {
                            CommonRow(
                                imageUrl = myStatuses[0].user.imageUrl,
                                name = myStatuses[0].user.name
                            ) {
                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.SingleStatus.createRoute(myStatuses[0].user.userId!!)
                                )

                            }
                            CommonDivider()
                            val uniqueUsers = otherStatuses.map { it.user }.toSet().toList()
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(uniqueUsers) { user ->
                                    CommonRow(imageUrl = user.imageUrl, name = user.name) {
                                        navigateTo(
                                            navController = navController,
                                            DestinationScreen.SingleStatus.createRoute(user.userId!!)
                                        )

                                    }

                                }

                            }
                        }
                    }

                    BottomNavigationMenu(selectedItem = selectedItem,
                        navController = navController,
                        )

                }
            }
        )
    }
}
@Composable
fun FAB(
    onFabClick:()->Unit
){
    FloatingActionButton(onClick = {onFabClick() },
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    )
    {
        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Add Status", tint = Color.White)
    }
}