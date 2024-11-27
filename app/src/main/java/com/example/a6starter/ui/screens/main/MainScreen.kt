package com.example.a6starter.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val LOADING_KEY = "LOADING"

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    Text(
        "TODO: Create your main screen here, note that you can access the viewModel from " +
                "the composable parameter ($viewModel)"
    )
    val lazyListState = rememberLazyListState()
    // To see when we need to load more data, we create a stateful variable based on the lazy list
    //  state. It is true if our loading circle is visible.
    val loadingCircleVisible by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.any { it.key == LOADING_KEY }
        }
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // We then use snapshotFlow to convert this stateful variable into a flow, so this way
        //  we can observe and react to its changes.
        snapshotFlow { loadingCircleVisible }.onEach {
            // TODO call load next page here
        }.launchIn(coroutineScope)
    }

    LazyColumn(state = lazyListState) {
        items(TODO("Add your list items here")) {

        }
        item(key = LOADING_KEY) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProfileScreen(){

}

@Composable
fun LoginScreen(){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Column{
            Row {
                var text by remember { mutableStateOf("") }
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Enter Username here") }
                )
            }
            Row{
                var text by remember { mutableStateOf("") }
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Enter Password here") }
                )
            }
        }

    }
}