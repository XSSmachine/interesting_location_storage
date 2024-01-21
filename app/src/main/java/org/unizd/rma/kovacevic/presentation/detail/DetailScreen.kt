package org.unizd.rma.kovacevic.presentation.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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
private fun DetailScreen(
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
    val stateHolder = rememberDropdownMenuStateHolder()
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
        DropdownMenu(stateHolder = stateHolder)
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
fun DropdownMenu(stateHolder: DropdownMenuStateHolder) {
    Column {
        Box {
            OutlinedTextField(
                value = stateHolder.value,
                onValueChange = {},
                label = { Text(text = "Category") },
                trailingIcon = {
                    Icon(
                        imageVector = stateHolder.icon,
                        contentDescription = null,
                        Modifier.clickable {
                            stateHolder.onEnabled(!(stateHolder.enabled))
                        })
                },
                modifier = Modifier.onGloballyPositioned {
                    stateHolder.onSize(it.size.toSize())
                }
            )
            androidx.compose.material.DropdownMenu(
                expanded = stateHolder.enabled,
                onDismissRequest = {
                    stateHolder.onEnabled(false)

                },
                modifier = Modifier
                    .width(with(LocalDensity.current)
                    {stateHolder.size.width.toDp()})
            ) {
                    stateHolder.items.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            stateHolder.onSelectedIndex(index)
                            stateHolder.onEnabled(false)
                        }) {
                            Text(text = s)
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