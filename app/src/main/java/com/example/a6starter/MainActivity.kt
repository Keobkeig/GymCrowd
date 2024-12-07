package com.example.a6starter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.a6starter.data.entities.CrowdData
import com.example.a6starter.data.entities.Exercise
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.ui.screens.main.ExerciseScreenViewModel
import com.example.a6starter.ui.screens.main.LoginScreen
import com.example.a6starter.ui.screens.main.MainScreen
import com.example.a6starter.ui.screens.main.MainScreenViewModel
import com.example.a6starter.ui.screens.main.ProfileScreen
import com.example.a6starter.ui.theme.A6StarterTheme
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Serializable
sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object LoginScreen : Screen("LoginScreen")
    object ExerciseScreen : Screen("ExerciseScreen")
}


fun NavBackStackEntry.toScreen(): Screen? =
    when (destination.route) {
        Screen.HomeScreen.route -> Screen.HomeScreen
        Screen.LoginScreen.route -> Screen.LoginScreen
        Screen.ExerciseScreen.route -> Screen.ExerciseScreen
        else -> null
    }


data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
                        screen = Screen.ExerciseScreen,
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
                        NavHost(
                            navController = navController,
                            startDestination = Screen.HomeScreen.route
                        ) {
                            composable(Screen.HomeScreen.route) { MainScreen() } //Change to mainscreen once it's fully implemented
                            composable(Screen.LoginScreen.route) {
                                LoginScreen(
                                    username = { username = it },
                                    password = { password = it }

                                )
                            }
                            composable(Screen.ExerciseScreen.route) { ExerciseScreen() }
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
            .height(180.dp), // Adjust height as needed,
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
                text = gym.location,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            // Display crowd data if available
            if (gym.crowdData != null && gym.crowdData.isNotEmpty()) {
                val crowdData =
                    gym.crowdData.first()
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
//                Text(
//                    text = "Last Updated: ${crowdData.lastUpdated}",
//                    style = MaterialTheme.typography.bodySmall,
//                    textAlign = TextAlign.Start
//                )
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
                occupancy = 50,
                percentageFull = 75.0,
                lastUpdated = LocalDateTime.now().toString(),
                gym = 1
            )
        )

        // Mock data matching the JSON structure of the Gym class
        val sampleGyms = listOf(
            Gym(
                gymId = 1,
                name = "Fitness Hub",
                location = "123 Main St",
                type = "Public",
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 2,
                name = "Peak Performance",
                location = "456 Elm St",
                type = "Private",
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 3,
                name = "Iron Paradise",
                location = "789 Maple Ave",
                type = "Public",
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 4,
                name = "The Fit Factory",
                location = "101 Oak Rd",
                type = "Private",
                crowdData = sampleCrowdData
            ),
            Gym(
                gymId = 5,
                name = "Sweat Zone",
                location = "202 Pine Ln",
                type = "Public",
                crowdData = null
            ),
            Gym(
                gymId = 6,
                name = "Wellness Center",
                location = "303 Cedar Dr",
                type = "Public",
                crowdData = null
            ),
            Gym(
                gymId = 7,
                name = "Muscle Mansion",
                location = "404 Birch Blvd",
                type = "Private",
                crowdData = null
            ),
            Gym(
                gymId = 8,
                name = "Health Haven",
                location = "505 Walnut St",
                type = "Public",
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

private const val LOADING_KEY = "LOADING"

@Composable
fun ExerciseScreen(viewModel: ExerciseScreenViewModel = hiltViewModel()) {
    val lazyListState = rememberLazyListState()

    val viewState by viewModel.exerciseScreenViewState.collectAsState()
    val loadingCircleVisible by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.any { it.key == LOADING_KEY }
        }
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow { loadingCircleVisible }.onEach {
            viewModel.loadNextPage()
        }.launchIn(coroutineScope)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        ExerciseList(
            favorites = viewState.favorites,
            allExercises = viewState.allExercises,
            lazyListState = lazyListState,
            onFavorite = viewModel::addFavoriteExercise,
            onUnfavorite = viewModel::removeFavoriteExercise
        )
    }
}

@Composable
fun ExerciseList(
    favorites: List<Exercise>,
    allExercises: List<Exercise>,
    lazyListState: LazyListState,
    onFavorite: (Int) -> Unit,
    onUnfavorite: (Int) -> Unit
) {
    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle(title = "Favorites")
        }
        if (favorites.isNotEmpty()) {
            items(favorites, key = { "favorite-${it.exerciseId}" }) { exercise ->
                ExerciseBox(
                    exercise = exercise,
                    isFavorite = true,
                    onFavoriteToggle = { onUnfavorite(exercise.exerciseId) }
                )
            }
        }

        item {
            SectionTitle(title = "All Exercises")
        }
        items(allExercises, key = { "all-${it.exerciseId}" }) { exercise ->
            ExerciseBox(
                exercise = exercise,
                isFavorite = favorites.any { it.exerciseId == exercise.exerciseId },
                onFavoriteToggle = { onFavorite(exercise.exerciseId) }
            )
        }

        item(key = LOADING_KEY) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
        }
    }
}

@Composable
fun ExerciseBox(
    exercise: Exercise,
    isFavorite: Boolean,
    onFavoriteToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color.Black else Color.Gray,
                    modifier = Modifier
                        .clickable {
                            try {
                                onFavoriteToggle(exercise.exerciseId)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                println("Error toggling favorite: ${e.message}")
                            }
                        }
                        .size(24.dp)
                )
            }
            Text(
                text = "Target: ${exercise.target}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (exercise.secondaryMuscles != null) {
                Text(
                    text = "Secondary Muscles: ${exercise.secondaryMuscles}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Text(
                text = "Body Part: ${exercise.bodyPart}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Equipment: ${exercise.equipment ?: "None"}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Instructions: ${exercise.instructions}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        color = Color.Black
    )
}
