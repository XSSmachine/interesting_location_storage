package org.unizd.rma.kovacevic.presentation.map

import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import org.unizd.rma.kovacevic.common.ScreenViewState
import org.unizd.rma.kovacevic.presentation.home.locations
import org.unizd.rma.kovacevic.presentation.map.state.LocationState

@Composable
fun MapScreen(
    state: MapViewModel.MapState,

) {

    // Set properties using MapProperties which you can use to recompose the map
    val cameraPositionState = rememberCameraPositionState()

    when(state.clusterItems){
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }
        is ScreenViewState.Success -> {
            Box(
                modifier = Modifier.padding(bottom=60.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
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
                        )
                    }
                    MarkerInfoWindow(
                        state = rememberMarkerState(position = LatLng(49.1, -122.5)),
                        snippet = "Some stuff",
                        onClick = {
                            // This won't work :(
                            System.out.println("Mitchs_: Cannot be clicked")
                            true
                        },
                        draggable = true
                    )
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

    // Center camera to include all the Zones.
    LaunchedEffect(true) {

            cameraPositionState.centerOnLocation(lat = locations[locations.size-1].latitude,lng= locations[locations.size-1].longitude)


    }
}

/**
 * If you want to center on a specific location.
 */
private suspend fun CameraPositionState.centerOnLocation(
    lat:Double,lng:Double
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(lat, lng),
        15f
    ),
)