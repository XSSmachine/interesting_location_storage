package org.unizd.rma.kovacevic.presentation.detail

import android.graphics.Bitmap
import android.util.Log
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
import org.unizd.rma.kovacevic.BitmapConverter
import org.unizd.rma.kovacevic.MainActivity.Companion.lastKnownLocation
import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.domain.use_cases.AddUseCase
import org.unizd.rma.kovacevic.domain.use_cases.GetLocationByIdUseCase
import java.text.SimpleDateFormat
import java.util.*


class DetailViewModel @AssistedInject constructor(
    private val addusecase: AddUseCase,
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
    @Assisted private val locationID:Long
): ViewModel() {



    var state by mutableStateOf(DetailState())
        private set
    val isFormNotBlank: Boolean
        get() = state.title.isNotEmpty() &&
                state.content.isNotEmpty() &&
                state.imagePath.isNotEmpty()
    private val location: Location
        get() = state.run {
            Location(
                id,
                title,
                content,
                category,
                createdDate,
                imagePath,
                latitude,
                longitude
            )
        }

    init {
        initialize()
    }

    private fun initialize() {
        val isUpdatingLocation = locationID != -1L
        state = state.copy(isUpdatingLocation = isUpdatingLocation)
        if (isUpdatingLocation) {
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

    fun onTitleChange(title: String) {
        state = state.copy(title = title)
    }

    fun onContentChange(content: String) {
        state = state.copy(content = content)
    }

    fun onCategoryChange(category: String) {
        state = state.copy(category = category)
    }

    fun onImageTaken(imagePath: Bitmap) {
        Log.d("DetailScreen", "LocationDataaaa: ${lastKnownLocation?.latitude}")
        state = state.copy(imagePath = BitmapConverter.converterBitmapToString(imagePath))
    }

    fun onLocationFetch() {
        if (lastKnownLocation != null) {
            state = state.copy(
                latitude = lastKnownLocation!!.latitude,
                longitude = lastKnownLocation!!.longitude
            )
        }

    }

    fun onScreenDialogDismissed(newValue: Boolean) {
        state = state.copy(isScreenDialogDismissed = newValue)
    }

    fun addOrUpdateLocation() = viewModelScope.launch {
        addusecase(location = location)
    }

    companion object {
        fun provideMainViewModelFactory(
            factory: Factory,
            locationID: Long
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(locationID) as T
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(locationID: Long): DetailViewModel
    }

}

data class DetailState(
    val id:Long = 0,
    val title:String = "",
    val content:String="",
    val category: String = "",
    val createdDate: String = formatDate(Date()),
    val imagePath: String = "",
    val latitude:Double = 0.0,
    val longitude: Double = 0.0,
    val isUpdatingLocation: Boolean = false,
    val isScreenDialogDismissed: Boolean = true
    ){
    companion object {
        private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        private fun formatDate(date: Date): String {
            return dateFormatter.format(date)
        }
    }
}




