package org.unizd.rma.kovacevic.presentation.map


//google map + entity markers
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.model.polygonOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.unizd.rma.kovacevic.common.ScreenViewState
import org.unizd.rma.kovacevic.domain.use_cases.GetAllLocationsUseCase
import org.unizd.rma.kovacevic.presentation.home.HomeState

import org.unizd.rma.kovacevic.presentation.navigation.Screens
import org.unizd.rma.kovacevic.presentation.navigation.navigateToSingleTop
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase
): ViewModel() {


    private val _state:MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val state:StateFlow<MapState> = _state.asStateFlow()

    init {
        getAllLocations()
    }


    private fun getAllLocations(){
        getAllLocationsUseCase()
            .onEach {
                _state.value = MapState(clusterItems = ScreenViewState.Success(it))
            }
            .catch {
                _state.value = MapState(clusterItems = ScreenViewState.Error(it.message))
            }
            .launchIn(viewModelScope)
    }

    data class MapState(
        val clusterItems: ScreenViewState<List<org.unizd.rma.kovacevic.data.local.model.Location>> = ScreenViewState.Loading,
    )











}

