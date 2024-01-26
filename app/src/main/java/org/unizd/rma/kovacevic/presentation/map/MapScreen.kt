package org.unizd.rma.kovacevic.presentation.map

import android.content.Context
import android.graphics.drawable.Icon
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import org.unizd.rma.kovacevic.MainActivity
import org.unizd.rma.kovacevic.common.ScreenViewState

@Composable
fun MapScreen(
    state: MapViewModel.MapState,
    onLocationClicked: (Long) -> Unit

) {

    // Set properties using MapProperties which you can use to recompose the map
    val cameraPositionState = rememberCameraPositionState()

    when(state.clusterItems){
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }
        is ScreenViewState.Success -> {
            Box(
                modifier = Modifier.padding(bottom=56.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    state.clusterItems.data.forEach { position ->
                        Marker(

                            state = MarkerState(
                                position = LatLng(
                                    position.latitude,
                                    position.longitude
                                ),

                                ),
                            title = position.title,
                            tag = position.category,
                            onInfoWindowLongClick ={ onLocationClicked(position.id) }
                        )
                    }
//                    MarkerInfoWindow(
//                        state = rememberMarkerState(position = LatLng(49.1, -122.5)),
//                        snippet = "Some stuff",
//                        onClick = {
//                            // This won't work :(
//                            System.out.println("Mitchs_: Cannot be clicked")
//                            true
//                        },
//                        draggable = true
//                    )
                }
            }
        }

        is ScreenViewState.Error -> {
            Text(
                text = state.clusterItems.message ?: "Unknown Error",
                color = MaterialTheme.colors.error
            )
        }
    }




    fun calculateZoneLatLng(state: MapViewModel.MapState): LatLng {
        return when (val clusterItems = state.clusterItems) {
            is ScreenViewState.Success -> {
                val latLngs = clusterItems.data
                    .map { LatLng(it.latitude, it.longitude) }
                LatLng(latLngs[latLngs.size-1].latitude, latLngs[latLngs.size-1].longitude)
            }
            else -> {
                // Handle other states like Loading or Error
                // For now, returning a default value
                LatLng(44.119371, 15.231365)
            }
        }
    }

    // Center camera to include all the Zones.
    LaunchedEffect(state.clusterItems) {
        if (state.clusterItems!=null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    calculateZoneLatLng(state),
                    15f
                ),
            )
        }
    }

    // Center camera to include all the Zones.
//    LaunchedEffect(true) {
//
//            cameraPositionState.centerOnLocation(lat = locations.get(si),lng= locations[locations.size-1].longitude)
//
//
//    }
}

/**
 * If you want to center on a specific location.
 */
private suspend fun CameraPositionState.centerOnLocation(
    lat:Double,lng:Double
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(lat, lng),
        5f
    ),
)

