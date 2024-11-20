package com.example.xchat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xchat.CommonProgressBar
import com.example.xchat.DestinationScreen
import com.example.xchat.LCViewModel
import com.example.xchat.R
import com.example.xchat.checkSignedIn
import com.example.xchat.navigateTo

@Composable
fun SignUpScreen(navController: NavController, vm : LCViewModel)
{
    // before signup check wether user already signedin or not
    // if yes jump to chatlist screen
    // if not remain on signup screen
    checkSignedIn(vm = vm, navController = navController )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val nameState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
            }

            val numberState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
            }
            val passwordState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
            }
            val emailState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
            }
            val focus = LocalFocusManager.current

            Image(
                painter = painterResource(id = R.drawable.bubble_chat),
                contentDescription = null,modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)

            )
            Text(
                text = "Sign Up",fontSize = 30.sp,fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = nameState.value, onValueChange = {
                    nameState.value = it
                },label = { Text(text = "Name") },modifier = Modifier.padding(8.dp)
            )


            OutlinedTextField(
                value = numberState.value, onValueChange = {
                    numberState.value = it
                },label = { Text(text = "Number") },modifier = Modifier.padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


            OutlinedTextField(
                value = emailState.value, onValueChange = {
                    emailState.value = it
                },label = { Text(text = "Email") },modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = passwordState.value, onValueChange = {
                    passwordState.value = it
                },label = { Text(text = "Password") },modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {
                    vm.signUp(nameState.value.text,numberState.value.text,emailState.value.text,passwordState.value.text)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "SIGN UP")

            }

            Text(text = "Already a User ? Go to login",
                color = Color.Green,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    }
            )

        }


    }
    if (vm.inprogess.value) {
        CommonProgressBar()
    }

}