package org.unizd.rma.kovacevic.presentation.detail

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import org.unizd.rma.kovacevic.BitmapConverter

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
        onImageTaken = viewModel::onImageTaken,
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
    category: String,
    imagePath:String,
    isFormNotEmpty:Boolean,
    onTitleChange:(String) -> Unit,
    onContentChange:(String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageTaken: (String) -> Unit,
    onBtnClick:()-> Unit,
    onNavigate:() -> Unit,
) {
    val stateHolder = rememberDropdownMenuStateHolder()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        TopSection(
            title = title, 
            onNavigate =onNavigate,
            onTitleChange = onTitleChange,
            ) 
        Spacer(modifier = Modifier.size(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End

            ) {
//                TextField(
//                    value = category,
//                    onValueChange = onCategoryChange)

//                DropdownTextField(
//                    category = category,
//                    onCategoryChange = onCategoryChange,
//                    items = listOf(
//                        "PRIRODNE LJEPOTE",
//                        "POVIJESNA MJESTA",
//                        "GRADSKI PROSTORI"
//                    ))
                DropdownMenu(
                    stateHolder = stateHolder,
                    category=category,
                    onCategoryChange = onCategoryChange
                )

                AnimatedVisibility(isFormNotEmpty) {
                    IconButton(onClick = onBtnClick) {
                        val icon = if (isUpdatingLocation) Icons.Default.Update
                        else Icons.Default.Check
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )

                    }
                }
            }


        Log.d("DetailScreen", "isFormNotEmpty: $isFormNotEmpty")
        Spacer(modifier = Modifier.size(12.dp))
        ImageTaker(
            onImageTaken = { imagePath ->
                onImageTaken(imagePath)
            }
        )
//        FloatingActionButton(onClick = {
//
//        }) {
//            Icon(imageVector = Icons.Default.Camera,contentDescription = null)
//        }
        Spacer(modifier = Modifier.size(12.dp))
        LocationTextField(
            modifier = Modifier.weight(1f),
            value = content,
            onValueChange = onContentChange,
            label = "Content"
        )



    }
}

@Composable
fun DropdownTextField(
    category: String,
    onCategoryChange: (String) -> Unit,
    items: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    OutlinedTextField(
        value = category,
        onValueChange = {  },
        label = { Text("Category") },
        readOnly = true,
        trailingIcon = {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        },
        modifier = Modifier.clickable { expanded = true }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        items.forEachIndexed { index,item ->
            DropdownMenuItem(onClick = {
                selectedIndex = index
                onCategoryChange(items.get(selectedIndex))
                expanded = false
            }) {
                Text(text = item)
            }
        }
    }
}



@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
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
fun DropdownMenu(
    stateHolder: DropdownMenuStateHolder,
    category: String,
    onCategoryChange: (String) ->Unit,
) {
    Column {
        Box {
            OutlinedTextField(
                value = stateHolder.value,
                onValueChange = {onCategoryChange(it)},
                label = { Text(text = "Category") },
                readOnly = true,
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
                color = Color.LightGray,
                textAlign = labelAlign,
                modifier= modifier.fillMaxWidth()
            )
        }
        )
}


@Composable
fun ImageTaker(
    onImageTaken: (String) -> Unit,
               ) {
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { newImage ->
            bitmap = newImage
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }

    Column {
        bitmap?.let {
            val imagePath = BitmapConverter.converterBitmapToString(it)
            onImageTaken.invoke(imagePath)
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(360.dp)
            )
        }

        val context = LocalContext.current

        TextButton(
            onClick = {
                // Checks if the permission is granted
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    // The permission is already granted
                    cameraLauncher.launch()
                } else {
                    // Launches the permission request
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
        ) {
            Text(
                text = "Use camera"
            )
        }
    }
}


