package org.unizd.rma.kovacevic.presentation.map



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.unizd.rma.kovacevic.common.ScreenViewState
import org.unizd.rma.kovacevic.domain.use_cases.GetAllLocationsUseCase
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

