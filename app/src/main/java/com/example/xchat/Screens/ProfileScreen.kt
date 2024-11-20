package com.example.xchat.Screens

import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.privacysandbox.tools.core.model.Type
import com.example.xchat.CommonDivider
import com.example.xchat.CommonImage
import com.example.xchat.CommonProgressBar
import com.example.xchat.DestinationScreen
import com.example.xchat.LCViewModel
import com.example.xchat.navigateTo

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ProfileScreen(
    vm : LCViewModel,
    navController: NavController
) {
    var inProgress by remember { mutableStateOf(vm.inprogess.value) }
    var selectedItem by remember { mutableStateOf(BottomNavigationItem.PROFILE) } // State for selected item

    if (inProgress) {
        CommonProgressBar()
    }
    Log.d("TAG", "ProfileScreen: $inProgress")
    val userdata by vm.userData.observeAsState()
    var name by rememberSaveable { mutableStateOf(userdata?.name ?: "") }
    var number by rememberSaveable { mutableStateOf(userdata?.number ?: "") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    var isKeyboardVisible by remember { mutableStateOf(false) }
    DisposableEffect(view) {
        val listener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val insets = view.rootWindowInsets
            // Check if the IME (keyboard) is visible
            isKeyboardVisible = insets.isVisible(android.view.WindowInsets.Type.ime())
        }
        view.addOnLayoutChangeListener(listener)

        onDispose {
            view.removeOnLayoutChangeListener(listener)
        }
    }



    Scaffold(
        bottomBar = {
            if(!isKeyboardVisible) {
                BottomNavigationMenu(
                    selectedItem = selectedItem,
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // this padding adjusts for the bottom bar
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .imePadding()
//            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                vm = vm,
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onSave = {
                    keyboardController?.hide()
                    // we need to update and save file
                    vm.createOrUpdateProfle(
                        name = name,
                        number = number
                    )
                    inProgress = false
                },
                onBack = {
                    navigateTo(navController = navController, route = DestinationScreen.ChatList.route)
                },
                onLogout = {
                    vm.logout()
                    navigateTo(navController = navController, route = DestinationScreen.Login.route)
                }
            )
//            BottomNavigationMenu(
//                selectedItem = selectedItem,
//                navController = navController,
//
//                )
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier,
    vm : LCViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange : (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogout: ()->Unit) {
    val userdata by vm.userData.observeAsState()
    Column {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Back", modifier = Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save", modifier = Modifier.clickable {
                onSave.invoke()
            })
        }
        CommonDivider()
        ProfileImage(imageUrl = userdata?.imageUrl,vm = vm)
        CommonDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Name", modifier= Modifier.width(100.dp))
            TextField(value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ))


        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Number", modifier= Modifier.width(100.dp))
            TextField(value = number, onValueChange = onNumberChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }
        CommonDivider()
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.Center) {
                Text(text = "LogOut",
                    modifier = Modifier.clickable {
                        onLogout.invoke()
                    })
            }



    }
}

@Composable
fun ProfileImage(imageUrl : String?, vm : LCViewModel) {

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {

        uri->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column (modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card (shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ){
                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")
        }
    }
}

