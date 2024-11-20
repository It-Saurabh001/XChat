package com.example.xchat.Screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xchat.CommonImage
import com.example.xchat.LCViewModel

enum class State {
    INITIAL, ACTIVE, COMPLETED
}

@Composable
fun SingleStatusScreen(
    vm: LCViewModel,
    navController: NavController,
    userId: String
) {
    val filteredStatuses by vm.status.collectAsState(emptyList())
    Log.d("TAG", "SingleStatusScreen: Filtered statuses: $filteredStatuses")

//    val statuses = filteredStatuses.filter { it.user.userId == userId }
    val statuses = filteredStatuses.filter { status ->
        // here it.user.userId and userId got trimmed due to some whitespace leading return false even after it seems like same
        val userIdTrimmed = status.user.userId?.trim() ?: ""
        val userIdToMatchTrimmed = userId.trim()

        val isValid = userIdTrimmed == userIdToMatchTrimmed
        Log.d("TAG", "Filtering Status: userId=$userIdTrimmed, userIdToMatch=$userIdToMatchTrimmed, isValid=$isValid")
        isValid





    }

    Log.d("TAG", "SingleStatusScreen: Filtered statuses : $statuses")
//    val statuses = vm.status.value.filter {
//        it.user.userId == userId
//    }
    if (statuses.isNotEmpty()) {
        Log.d("TAG", "SingleStatusScreen: screen working properly")
        val currentStatus = remember {
            mutableStateOf(0)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            CommonImage(
                data = statuses[currentStatus.value].imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                statuses.forEachIndexed { index, status ->
                    customProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .height(7.dp)
                            .padding(1.dp),
                        state = if (currentStatus.value < index) State.INITIAL else if (currentStatus.value == index) State.ACTIVE else State.COMPLETED
                    ) {
                        if(currentStatus.value < statuses.size -1) currentStatus.value++ else navController.popBackStack()
                    }
                }

            }

        }
    }
    else{
        Log.d("TAG", "SingleStatusScreen: screen not working")
        println("no status available")
//        Column (modifier = Modifier
//            .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center) {
//            Text(text = "No Status Available")
//        }
    }
}

@Composable
fun customProgressIndicator(modifier: Modifier, state: State, onComplete: () -> Unit) {
    var progress = if (state == State.INITIAL) 0f else 1f
    if (state == State.ACTIVE) {
        val toggleState = remember { mutableStateOf(false) }
        LaunchedEffect(toggleState) {
            toggleState.value = true
        }
        val p: Float by animateFloatAsState(
            if (toggleState.value){
                1f
            } else {
                0f
            },
            animationSpec = tween(5000),
            finishedListener = { onComplete.invoke() })
        progress = p
    }
    LinearProgressIndicator(modifier = modifier, color = Color.Red, progress = progress)
}