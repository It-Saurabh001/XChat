package com.example.xchat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter

fun navigateTo(navController: NavController, route: String){
    navController.navigate(route){
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun CommonProgressBar() {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f)) // Apply alpha to the background
            .clickable(enabled = false) {}
            , verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        CircularProgressIndicator()
    }
}

@Composable
fun checkSignedIn(vm : LCViewModel, navController: NavController){
    // function check whether user is signedup or not
    // if not -> remain on SignUpScreen
    // if loggedin -> directly moved on Chatlist Scree
    // in whatsapp after loggedin on opening application direct chatlist screen open not signup screen
    // so same rule followed here

    var signIn = vm.signIn.value
    val alreadySignedIn = remember { mutableStateOf(false) } // State to check if the navigation has already occurred

    LaunchedEffect(signIn) {
        if (signIn && !alreadySignedIn.value) {
            alreadySignedIn.value = true
            navController.navigate(DestinationScreen.ChatList.route) {
                // Optional: Clear back stack if needed
                popUpTo(DestinationScreen.SignUp.route) { inclusive = true }
            }
        } else if (!signIn) {
            Log.d("TAG", "checkSignedIn: Not signed or logged in")
        }
    }

}

@Composable
fun CommonDivider() {

    HorizontalDivider(
        modifier = Modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )

}

@Composable
fun CommonImage(
    data:String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScale : ContentScale = ContentScale.Crop
)  {
        AsyncImage(model = data, contentDescription = null, modifier= modifier, contentScale= contentScale)
}


@Composable
fun TitleText(txt : String){
    Text(text =txt,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CommonRow(imageUrl : String?, name : String? , onItemClick: () -> Unit) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .clickable { onItemClick.invoke() },
        verticalAlignment = Alignment.CenterVertically){
        CommonImage(data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Text(
            text = name?:"---",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )

    }

}@Composable
fun CommonRows(imageUrl : String?, name : String? ,chatId: String, onItemClick: (String) -> Unit) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .clickable { onItemClick(chatId) },
        verticalAlignment = Alignment.CenterVertically){
        CommonImage(data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Text(
            text = name?:"---",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )

    }

}
