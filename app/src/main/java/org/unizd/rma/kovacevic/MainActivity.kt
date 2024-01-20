package org.unizd.rma.kovacevic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.unizd.rma.kovacevic.presentation.detail.DetailAssistedFactory
import org.unizd.rma.kovacevic.presentation.home.HomeViewModel
import org.unizd.rma.kovacevic.presentation.map.MapViewModel
import org.unizd.rma.kovacevic.presentation.navigation.LocationNavigation
import org.unizd.rma.kovacevic.presentation.navigation.Screens
import org.unizd.rma.kovacevic.presentation.navigation.navigateToSingleTop
import org.unizd.rma.kovacevic.ui.theme.InterestingLocationsStorageTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var assistedFactory: DetailAssistedFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterestingLocationsStorageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onSurface
                ) {
                    LocationApp()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LocationApp(){
        val homeViewModel:HomeViewModel = viewModel()
        val mapViewModel:MapViewModel = viewModel()
        val navController = rememberNavController()
        var currentTab by remember {
            mutableStateOf(TabScreen.Home)
        }
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    content = {
                        Row(
                            horizontalArrangement =  Arrangement.Center
                        ) {
                            InputChip(
                                selected = currentTab == TabScreen.Home,
                                onClick = {
                                    currentTab = TabScreen.Home
                                    navController.navigateToSingleTop(
                                        route = Screens.Home.name
                                    )
                                },
                                label = {
                                    Text("Home")

                                },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                                }
                            )
                            Spacer(modifier = Modifier.Companion.size(12.dp))
                            InputChip(
                                selected = currentTab == TabScreen.Home,
                                onClick = {
                                    currentTab = TabScreen.Map
                                    navController.navigateToSingleTop(
                                        route = Screens.Map.name
                                    )
                                },
                                label = {
                                    Text("Map")

                                },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Map, contentDescription = null)
                                }
                            )
                        }

                    }

                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigateToSingleTop(
                        route = Screens.Detail.name
                    )
                }) {
                    Icon(imageVector = Icons.Default.Add,contentDescription = null)
                }
            }
        ) {
            LocationNavigation(
                modifier = Modifier.padding(it),
                navHostController = navController,
                homeViewModel = homeViewModel,
                mapViewModel = mapViewModel,
                assistedFactory = assistedFactory
            )
        }

    }
}

enum class TabScreen{
    Home,Map
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InterestingLocationsStorageTheme {
        Greeting("Android")
    }
}