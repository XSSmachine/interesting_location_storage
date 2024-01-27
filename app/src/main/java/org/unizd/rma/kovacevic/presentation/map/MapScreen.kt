package org.unizd.rma.kovacevic.presentation.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import org.unizd.rma.kovacevic.common.ScreenViewState

@Composable
fun MapScreen(
    state: MapViewModel.MapState,
    onLocationClicked: (Long) -> Unit

) {
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
                    Marker(
                        state = rememberMarkerState(position = LatLng(44.119371, 15.231365)),
                        title = "Author: Karlo Kovačević",
                        tag = "Default value",
                        draggable = false
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

    fun calculateZoneLatLng(state: MapViewModel.MapState): LatLng {
        return when (val clusterItems = state.clusterItems) {
            is ScreenViewState.Success -> {
                val latLngs = clusterItems.data
                    .map { LatLng(it.latitude, it.longitude) }
                if (latLngs.isNotEmpty()) {
                    LatLng(latLngs.last().latitude, latLngs.last().longitude)
                } else {
                    // Handle the case when there are no cluster items
                    LatLng(44.119371, 15.231365) // Default coordinates
                }
            }
            else -> {
                // Handle other states like Loading or Error
                // For now, returning a default value
                LatLng(44.119371, 15.231365)
            }
        }
    }

    // Center camera to last added location.
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

