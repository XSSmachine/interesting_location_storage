package org.unizd.rma.kovacevic.presentation.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.unizd.rma.kovacevic.data.local.model.LocationCategory

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    locationId: Long,
    assistedFactory: DetailAssistedFactory,
    navigateUp: () -> Unit
) {
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailedViewModelFactory(
            locationId = locationId,
            assistedFactory = assistedFactory
        )
    )

    val state = viewModel.state
    DetailScreen(
        modifier = modifier,
        isUpdatingLocation = state.isUpdatingLocation,
        title = state.title,
        content = state.content,
        category = state.category,
        imagePath = state.imagePath,
        isFormNotEmpty = viewModel.isFormNotBlank,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onCategoryChange = viewModel::onCategoryChange,
        onBtnClick = {
            viewModel.addOrUpdateLocation()
            navigateUp()
        },
        onNavigate = navigateUp
        )
}

@Composable
 fun DetailScreen(
    modifier: Modifier,
    isUpdatingLocation:Boolean,
    title:String,
    content: String,
    category: LocationCategory,
    imagePath:String,
    isFormNotEmpty:Boolean,
    onTitleChange:(String) -> Unit,
    onContentChange:(String) -> Unit,
    onCategoryChange:(LocationCategory) -> Unit,
    onBtnClick:()-> Unit,
    onNavigate:() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        TopSection(
            title = title, 
            onNavigate=onNavigate,
            category = category, 
            onTitleChange = onTitleChange, 
            onCategoryChange = onCategoryChange,
            ) 
        Spacer(modifier = Modifier.size(12.dp))
        AnimatedVisibility(isFormNotEmpty) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End

            ) {
                IconButton(onClick = onBtnClick) {
                    val icon = if (isUpdatingLocation) Icons.Default.Update
                    else Icons.Default.Check
                    Icon(
                        imageVector = icon ,
                        contentDescription = null)

                }
            }

        }
        Spacer(modifier = Modifier.size(12.dp))
        DropdownMenuSpinner()
        Spacer(modifier = Modifier.size(12.dp))
        LocationTextField(
            modifier = Modifier.weight(1f),
            value = content,
            onValueChange = onContentChange,
            label = "Content",
        )
    }
}

@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    category: LocationCategory,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (LocationCategory) -> Unit,
    onNavigate: () -> Unit
) {
    Row(
        modifier= modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = onNavigate) {
            Icon(
                imageVector = Icons.Default.ArrowBack ,
                contentDescription = null)

        }
        LocationTextField(
            modifier = Modifier.weight(1f),
            value = title,
            onValueChange = onTitleChange,
            label = "Title",
            labelAlign = TextAlign.Center
        )

        
    }
}

@Composable
fun DropdownMenuSpinner() {
    val options = listOf(LocationCategory.values())
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(options[0]) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Selected option: $selectedOption")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        setSelectedOption(option)
                        setExpanded(false)
                    }) {
                        Text(text = option.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationTextField(
    modifier: Modifier,
    value:String,
    onValueChange:(String) -> Unit,
    label: String,
    labelAlign: TextAlign? = null
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier=modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledPlaceholderColor = Color.Transparent,
            placeholderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
            ),
        placeholder = {
            Text(
                text = "Insert $label",
                textAlign = labelAlign,
                modifier= modifier.fillMaxWidth()
            )
        }
        )
}