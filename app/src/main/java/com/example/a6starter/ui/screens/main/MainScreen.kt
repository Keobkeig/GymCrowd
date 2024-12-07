package com.example.a6starter.ui.screens.main


import android.os.Build import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a6starter.GymGrid

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val gyms = viewModel.gymsFlow.collectAsState(initial = emptyList())
    val errorMessage = viewModel.errorMessage.collectAsState(initial = null)

    if (gyms.value.isNotEmpty()) {
        GymGrid(gyms = gyms.value)
    } else if (errorMessage.value != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage.value ?: "An error occurred.")
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading gyms...")
        }
    }
}

@Composable
fun ProfileScreen(){

}

@Composable
fun LoginScreen(username: (String) -> Unit, password: (String) -> Unit) {
    var uname by remember { mutableStateOf("") }
    var pword by remember { mutableStateOf("") }
    var showNameScreen by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (showNameScreen) {
                Name()
                Email()
            }


            TextField(
                value = uname,
                onValueChange = {
                    uname = it
                    username(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Enter Username") }
            )


            TextField(
                value = pword,
                onValueChange = {
                    pword = it
                    password(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Enter Password") },
                visualTransformation = PasswordVisualTransformation()
            )


            Button(
                onClick = {
                },
                enabled = uname.isNotBlank() && pword.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Login")
            }


            Button(
                onClick = {
                    showNameScreen = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Sign Up")
            }
        }
    }
}


@Composable
fun Name() {
    var text by remember { mutableStateOf("") }


    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        placeholder = { Text("Enter Name") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Composable
fun Email() {
    var text by remember { mutableStateOf("") }


    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        placeholder = { Text("Enter Email") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
