package com.example.a6starter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.entities.GymEntity
import com.example.a6starter.ui.screens.main.MainScreen
import com.example.a6starter.ui.theme.A6StarterTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A6StarterTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

data class GymEntity(
    val gymId: Int,
    val name: String,
    val location: String
)

@Composable
fun GymGrid(gyms: List<Gym>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gyms) { gym ->
            GymCard(gym)
        }
    }
}

@Composable
fun GymCard(gym: Gym) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Adjust height as needed
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = gym.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = gym.location,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GymGridPreview() {
    A6StarterTheme {
        val sampleGyms = listOf(
            Gym(
                gymId = 1, name = "Fitness Hub", location = "123 Main St",
                type = "",
                createdAt = LocalDateTime.now(),
            ),
            Gym(gymId = 2, name = "Peak Performance", location = "456 Elm St",
                type = "",
                createdAt = LocalDateTime.now(),),
            Gym(gymId = 3, name = "Iron Paradise", location = "789 Maple Ave",
                type = "",
                createdAt = LocalDateTime.now(),),
            Gym(gymId = 4, name = "The Fit Factory", location = "101 Oak Rd",
                type = "",
                createdAt = LocalDateTime.now(),),
            Gym(gymId = 5, name = "Sweat Zone", location = "202 Pine Ln",
                type = "",
                createdAt = LocalDateTime.now(),),
            Gym(gymId = 6, name = "Wellness Center", location = "303 Cedar Dr",
                type = "",
                createdAt = LocalDateTime.now(),),
            Gym(gymId = 7, name = "Muscle Mansion", location = "404 Birch Blvd",
                type = "",
                createdAt = LocalDateTime.now(),),
            Gym(gymId = 8, name = "Health Haven", location = "505 Walnut St",
                type = "",
                createdAt = LocalDateTime.now(),)
        )
        GymGrid(gyms = sampleGyms)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    A6StarterTheme {
        Greeting("Android")
    }
}