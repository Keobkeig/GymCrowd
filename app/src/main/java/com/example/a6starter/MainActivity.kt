package com.example.a6starter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a6starter.data.entities.CrowdData
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.entities.GymSummary
import com.example.a6starter.ui.screens.main.LoginScreen
import com.example.a6starter.ui.screens.main.ProfileScreen
import com.example.a6starter.ui.theme.A6StarterTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@kotlinx.serialization.Serializable
sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object LoginScreen : Screen("LoginScreen")
    object ProfileScreen : Screen("ProfileScreen")
}


fun NavBackStackEntry.toScreen(): Screen? =
    when (destination.route) {
        Screen.HomeScreen.route -> Screen.HomeScreen
        Screen.LoginScreen.route -> Screen.LoginScreen
        Screen.ProfileScreen.route -> Screen.ProfileScreen
        else -> null
    }


data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            A6StarterTheme {
                val tabs = listOf(
                    NavItem(
                        label = "Home",
                        icon = dumbbell(),
                        screen = Screen.HomeScreen,
                    ),
                    NavItem(
                        label = "Login",
                        icon = login(),
                        screen = Screen.LoginScreen,
                    ),
                    NavItem(
                        label = "Profile",
                        icon = Icons.Filled.Person,
                        screen = Screen.ProfileScreen,
                    )
                )
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState().value

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar {
                        tabs.map { item ->
                            NavigationBarItem(
                                selected = navBackStackEntry?.destination?.route == item.screen.route,
                                onClick = {
                                    navController.navigate(item.screen.route)
                                },
                                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                                label = { Text(text = item.label) }
                            )
                        }
                    }
                }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
                            composable(Screen.LoginScreen.route) {
                                LoginScreen(
                                    username = { username = it },
                                    password = { password = it }
                                )
                            }
                            composable(Screen.LoginScreen.route) {
                                LoginScreen(
                                    username = { username = it },
                                    password = { password = it }
                                )
                            }
                            composable(Screen.ProfileScreen.route) { ProfileScreen() }
                        }
                    }
                }
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymCard(gym: Gym) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), // Adjust height as needed,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(226, 190, 241) //colors of container
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Gym details
            Text(
                text = gym.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Location: ${gym.location}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Type: ${gym.type}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )

            // Display crowd data if available
            if (gym.crowdData != null && gym.crowdData.isNotEmpty()) {
                val crowdData = gym.crowdData.first() //TODO: Assuming we're displaying the first entry
                Text(
                    text = "Occupancy: ${crowdData.occupancy}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Percentage Full: ${crowdData.percentageFull?.let { "${it}%" } ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Last Updated: ${crowdData.lastUpdated.formatDateTime()}",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start
                )
            } else {
                Text(
                    text = "Crowd data not available",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    return this.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
    return this.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GymGridPreview() { //This is our previewable function
    A6StarterTheme {

        val sampleCrowdData = listOf(
            CrowdData(
                crowdId = 1,
                gym = GymSummary(gymId = 1, name = "Fitness Hub"),
                occupancy = 50,
                percentageFull = 75.0,
                lastUpdated = LocalDateTime.now()
            )
        )

        // Mock data matching the JSON structure of the Gym class
        val sampleGyms = listOf(
            Gym(
                gymId = 1,
                name = "Fitness Hub",
                location = "123 Main St",
                type = "Public",
                createdAt = LocalDateTime.now(),
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 2,
                name = "Peak Performance",
                location = "456 Elm St",
                type = "Private",
                createdAt = LocalDateTime.now(),
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 3,
                name = "Iron Paradise",
                location = "789 Maple Ave",
                type = "Public",
                createdAt = LocalDateTime.now(),
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 4,
                name = "The Fit Factory",
                location = "101 Oak Rd",
                type = "Private",
                createdAt = LocalDateTime.now(),
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 5,
                name = "Sweat Zone",
                location = "202 Pine Ln",
                type = "Public",
                createdAt = LocalDateTime.now(),
                crowdData = null
            ),
            Gym(
                gymId = 6,
                name = "Wellness Center",
                location = "303 Cedar Dr",
                type = "Public",
                createdAt = LocalDateTime.now(),
                crowdData = null
            ),
            Gym(
                gymId = 7,
                name = "Muscle Mansion",
                location = "404 Birch Blvd",
                type = "Private",
                createdAt = LocalDateTime.now(),
                crowdData = null
            ),
            Gym(
                gymId = 8,
                name = "Health Haven",
                location = "505 Walnut St",
                type = "Public",
                createdAt = LocalDateTime.now(),
                crowdData = null
            )
        )
        GymGrid(gyms = sampleGyms)
    }
}

@Composable
fun dumbbell(): ImageVector {
    return ImageVector.vectorResource(id = R.drawable.baseline_fitness_center_24)
}

@Composable
fun login(): ImageVector {
    return ImageVector.vectorResource(id = R.drawable.baseline_login_24)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    A6StarterTheme {
        Greeting("Android")
    }
}