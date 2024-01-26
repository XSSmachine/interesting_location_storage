package org.unizd.rma.kovacevic

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import org.unizd.rma.kovacevic.MainActivity.Companion.lastKnownLocation
import org.unizd.rma.kovacevic.presentation.detail.DetailViewModel
import org.unizd.rma.kovacevic.presentation.home.HomeViewModel
import org.unizd.rma.kovacevic.presentation.map.MapViewModel
import org.unizd.rma.kovacevic.presentation.map.state.LocationState
import org.unizd.rma.kovacevic.presentation.navigation.LocationNavigation
import org.unizd.rma.kovacevic.presentation.navigation.Screens
import org.unizd.rma.kovacevic.presentation.navigation.navigateToSingleTop
import org.unizd.rma.kovacevic.ui.theme.InterestingLocationsStorageTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
companion object{
    var lastKnownLocation by mutableStateOf<android.location.Location?>(null)
}
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    if (location != null) {
                        // Location data is available
                        Log.d("MainActivity", "Last known location: $location")
                        // Update lastKnownLocation
                        lastKnownLocation = location
                    } else {
                        // Last known location is null
                        Log.e("MainActivity", "Last known location is null")
                    }
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getDeviceLocation(fusedLocationClient)
            }
        }

    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            getDeviceLocation(fusedLocationClient)
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }





    @Inject lateinit var assistedFactory: DetailViewModel.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement =  Arrangement.SpaceBetween
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
                                    Text("Home",color= Color.Black)

                                },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.Black)
                                }
                            )
                            Spacer(modifier = Modifier.Companion.size(12.dp))
                            AnimatedVisibility(DetailViewModel.counter!=0) {
                            InputChip(
                                selected = currentTab == TabScreen.Home,
                                onClick = {

                                    currentTab = TabScreen.Map
                                    navController.navigateToSingleTop(
                                        route = Screens.Map.name
                                    )
                                },
                                label = {
                                    Text("Map",color= Color.Black)

                                },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Map, contentDescription = null, tint = Color.Black)
                                }
                            )}
                            Spacer(modifier = Modifier.Companion.size(125.dp))
                                FloatingActionButton(onClick = {
                                    navController.navigateToSingleTop(
                                        route = Screens.Detail.name
                                    )
                                }) {
                                    Icon(imageVector = Icons.Default.Add,contentDescription = null)
                                }

                        }

                    }
                )
            },



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