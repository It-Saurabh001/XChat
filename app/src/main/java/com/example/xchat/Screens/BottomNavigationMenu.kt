package com.example.xchat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xchat.DestinationScreen
import com.example.xchat.R
import com.example.xchat.navigateTo

enum class BottomNavigationItem(val icon:Int, val navDestination: DestinationScreen){

    CHATLIST(R.drawable.chat,DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status,DestinationScreen.StatusList),
    PROFILE(R.drawable.profile, DestinationScreen.Profile)
}

@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationItem,
    navController: NavController,

) {

    Row (modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(top = 4.dp)
        .background(Color.White)){

        // icon can show using loop at once
        // values() replace by entries
        for(item in BottomNavigationItem.values()){
            Image(painter = painterResource(id = item.icon), contentDescription = null,
                modifier = Modifier.size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(navController,item.navDestination.route)
                    },
                colorFilter = if (item == selectedItem)
                    ColorFilter.tint(color = Color.Blue)
                else
                    ColorFilter.tint(Color.Gray)

            )
        }

    }

}