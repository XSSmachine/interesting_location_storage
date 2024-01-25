package org.unizd.rma.kovacevic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import org.unizd.rma.kovacevic.presentation.detail.DetailScreen
import org.unizd.rma.kovacevic.presentation.detail.DetailViewModel
import org.unizd.rma.kovacevic.presentation.home.HomeScreen
import org.unizd.rma.kovacevic.presentation.home.HomeViewModel
import org.unizd.rma.kovacevic.presentation.map.MapScreen
import org.unizd.rma.kovacevic.presentation.map.MapViewModel
import org.unizd.rma.kovacevic.presentation.map.state.LocationState

enum class Screens{
    Home,Detail,Map
}

@Composable
fun LocationNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    mapViewModel: MapViewModel,
    assistedFactory: DetailViewModel.Factory
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Home.name
    ){
        composable(route = Screens.Home.name){
            val state by homeViewModel.state.collectAsState()
            HomeScreen(
                state = state,
                onDeleteLocation = homeViewModel::deleteLocation,
                onLocationClicked = {
                    navHostController.navigateToSingleTop(
                        route = "${Screens.Detail.name}?id=$it"
                    )
                })
        }
        composable(route = Screens.Map.name){
            val state by mapViewModel.state.collectAsState()
            MapScreen(
                state = state

                )
        }
        composable(
            route="${Screens.Detail.name}?id={id}",
            arguments = listOf(navArgument("id"){
                NavType.LongType
                defaultValue=-1L
            })
        ){backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: -1L
            DetailScreen(
                locationId = id,
                assistedFactory = assistedFactory,
                navigateUp = {navHostController.navigateUp()}
                )

        }
    }

}

fun NavHostController.navigateToSingleTop(route:String){
    navigate(route){
        popUpTo(graph.findStartDestination().id){saveState=true}
        launchSingleTop = true
        restoreState = true
    }
}