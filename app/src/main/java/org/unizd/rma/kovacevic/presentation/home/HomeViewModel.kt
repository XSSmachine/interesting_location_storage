package org.unizd.rma.kovacevic.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.unizd.rma.kovacevic.common.ScreenViewState
import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.domain.use_cases.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val addUseCase: AddUseCase
):ViewModel() {
    private val _state:MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state:StateFlow<HomeState> = _state.asStateFlow()

    init {
        getAllLocations()
    }


    private fun getAllLocations(){
        getAllLocationsUseCase()
            .onEach {
                _state.value = HomeState(locations = ScreenViewState.Success(it))
            }
            .catch {
                _state.value = HomeState(locations = ScreenViewState.Error(it.message))
            }
            .launchIn(viewModelScope)
    }

    fun deleteLocation(locationId: Long) = viewModelScope.launch {
        deleteLocationUseCase(locationId)
    }
}


data class HomeState(
    val locations:ScreenViewState<List<Location>> = ScreenViewState.Loading,

)