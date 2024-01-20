package org.unizd.rma.kovacevic.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.data.local.model.LocationCategory
import org.unizd.rma.kovacevic.domain.use_cases.AddUseCase
import org.unizd.rma.kovacevic.domain.use_cases.GetLocationByIdUseCase
import java.util.*

class DetailViewModel @AssistedInject constructor(
    private val addusecase: AddUseCase,
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
    @Assisted private val locationID:Long
): ViewModel() {
    var state by mutableStateOf(DetailState())
        private set
    val isFormNotBlank:Boolean
        get() = state.title.isNullOrEmpty()  &&
                state.content.isNotEmpty() &&
                state.imagePath.isNotEmpty()
    private val location:Location
        get() = state.run {
            Location(
                id=id,
                title=title,
                content=content,
                category=category,
                createdDate=createdDate,
                imagePath=imagePath,
                latitude=latitude,
                longitude=longitude
            )
        }

    init {
        initialize()
    }

    private fun initialize(){
        val isUpdatingLocation = locationID != -1L
        state = state.copy(isUpdatingLocation = isUpdatingLocation)
        if(isUpdatingLocation) {
            getLocationById()
        }
    }

    private fun getLocationById() = viewModelScope.launch {
        getLocationByIdUseCase(locationID).collectLatest { location ->
            state = state.copy(
                id = location.id,
                title = location.title,
                content = location.content,
                category = location.category,
                createdDate = location.createdDate,
                imagePath = location.imagePath,
                latitude = location.latitude,
                longitude = location.longitude,

            )
        }
    }

    fun onTitleChange(title:String){
        state = state.copy(title=title)
    }
    fun onContentChange(content:String){
        state = state.copy(content=content)
    }
    fun onCategoryChange(category: LocationCategory){
        state = state.copy(category=category)
    }

    fun addOrUpdateLocation() = viewModelScope.launch {
        addusecase(location=location)
    }
}

data class DetailState(
    val id:Long = 0,
    val title:String = "",
    val content:String="",
    val category: LocationCategory = LocationCategory.PRIRODNE_LJEPOTE,
    val createdDate: Date = Date(),
    val imagePath:String = "",
    val latitude:Double = 0.0,
    val longitude : Double = 0.0,
    val isUpdatingLocation: Boolean = false
    )

class DetailedViewModelFactory(
    private val locationId: Long,
    private val assistedFactory: DetailAssistedFactory
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(locationId) as T
    }
}


@AssistedFactory
interface DetailAssistedFactory{
    fun create(locationID: Long): DetailViewModel
}