package org.unizd.rma.kovacevic.presentation.detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import org.unizd.rma.kovacevic.data.local.model.LocationCategory

class DropdownMenuStateHolder {

    var enabled by mutableStateOf(false)
    var value by mutableStateOf("")
    var selectedIndex by mutableStateOf(-1)
    var size by mutableStateOf(Size.Zero)
    val icon: ImageVector
    @Composable get() = if (enabled){
        Icons.Default.ArrowDropUp
    }else{
        Icons.Default.ArrowDropDown
    }
    val items = LocationCategory.values().map { it.name }

    fun onEnabled(newValue:Boolean){
        enabled = newValue
    }
    fun onSelectedIndex(newValue:Int){
        selectedIndex = newValue
        value = items[selectedIndex]
    }
    fun onSize(newValue:Size){
        size = newValue
    }


}

@Composable
fun rememberDropdownMenuStateHolder() = remember {
    DropdownMenuStateHolder()
}