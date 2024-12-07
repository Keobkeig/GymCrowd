package com.example.a6starter.ui.screens.main


import android.os.Build import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a6starter.GymGrid
import com.example.a6starter.data.remote.GymApi

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
fun LoginScreen(
    navController: NavController,
    username: (String) -> Unit,
    password: (String) -> Unit,
    onSignIn: (String, String, String, String) -> Unit,
    onLogin: (String, String) -> Unit // Callback to handle login
) {
    var uname by remember { mutableStateOf("") }
    var pword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showSignUpFields by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show Name and Email fields only when creating a new account
        if (showSignUpFields) {
            Name(onValueChange = { name = it })
            Email(onValueChange = { email = it })
        } else {
            UserName(value = uname, onValueChange = { uname = it })
            Password(value = pword, onValueChange = { pword = it })
        }

        // Action button (Login or Sign Up)
        Button(
            onClick = {
                if (showSignUpFields) {
                    if (name.isNotBlank() && email.isNotBlank()) {
                        onSignIn(name, email, uname, pword)
                        navController.navigate("HomeScreen")
                    }
                } else {
                    if (uname.isNotBlank() && pword.isNotBlank()) {
                        onLogin(uname, pword)
                        navController.navigate("HomeScreen")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 190, 241)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(if (showSignUpFields) "Sign Up" else "Login")
        }

        // Toggle between Sign Up and Login
        Button(
            onClick = {
                if (showSignUpFields) {
                    name = ""
                    email = ""
                }
                showSignUpFields = !showSignUpFields
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 190, 241)  // Custom color
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showSignUpFields) "Go to Login" else "Sign Up")
        }
    }
}

@Composable
fun Name(onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        placeholder = { Text("Enter Name") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun Email(onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        placeholder = { Text("Enter Email") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun Password(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun UserName(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter Username") },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
