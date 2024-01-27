package org.unizd.rma.kovacevic.presentation.detail

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import org.unizd.rma.kovacevic.BitmapConverter

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    locationId: Long,
    assistedFactory: DetailViewModel.Factory,
    navigateUp: () -> Unit
) {
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailViewModel.provideMainViewModelFactory(

            assistedFactory,
                    locationId,
        )
    )

    val state = viewModel.state
    DetailScreenEntry(
        state=state,
        modifier = modifier,
        isUpdatingLocation = state.isUpdatingLocation,
        isFormNotEmpty = viewModel.isFormNotBlank,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onCategoryChange = viewModel::onCategoryChange,
        onImageTaken = viewModel::onImageTaken,
        onLocationFetched = viewModel::onLocationFetch,
        onDialogDismissed = viewModel::onScreenDialogDismissed,
        onBtnClick = {
            viewModel.addOrUpdateLocation()
            navigateUp()
        },
        onNavigate = navigateUp
        )
}

@Composable
private fun DetailScreenEntry(
    modifier: Modifier,
    state: DetailState,
    isUpdatingLocation:Boolean,
    isFormNotEmpty:Boolean,
    onTitleChange:(String) -> Unit,
    onContentChange:(String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageTaken: (Bitmap) -> Unit,
    onLocationFetched: () -> Unit,
    onDialogDismissed:(Boolean) -> Unit,
    onBtnClick:()-> Unit,
    onNavigate:() -> Unit,
) {
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var convertedImage by remember{
        mutableStateOf("")
    }
    val items = listOf(
                        "PRIRODNE LJEPOTE",
                        "POVIJESNA MJESTA",
                        "GRADSKI PROSTORI"
                    )
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        TopSection(
            modifier= Modifier.background(color= Color(0xFF36A2EB)),
            title = state.title,
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
                TextField(
                    modifier = Modifier.weight(2f),
                    value = state.category,
                    readOnly = true,
                    onValueChange = {
                        onCategoryChange.invoke(it)
                    },
                    label = {Text(text = "Category")},
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onDialogDismissed.invoke(!state.isScreenDialogDismissed)
                            }
                            )
                    }
                )
                if(!state.isScreenDialogDismissed){
                    Popup(
                        onDismissRequest = {
                            onDialogDismissed.invoke(!state.isScreenDialogDismissed)
                        }
                    ){
                        Surface(modifier=Modifier.padding(16.dp)) {
                            Column {
                                items.forEach{
                                    Text(text = it,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                onCategoryChange.invoke(it)
                                                onDialogDismissed(!state.isScreenDialogDismissed)
                                            }
                                        )
                                }
                            }

                        }
                    }
                }


                AnimatedVisibility(isFormNotEmpty) {
                    IconButton(onClick = onBtnClick) {
                        onLocationFetched()
                        val icon = if (isUpdatingLocation) Icons.Default.Update
                        else Icons.Default.Check
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )

                    }
                }
            }


        Log.d("DetailScreen", "isFormNotEmpty: ${state.imagePath}")
        Spacer(modifier = Modifier.size(12.dp))

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

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
                ){
            if(isUpdatingLocation){
                var prevImage = state.imagePath
                var newBitmap = BitmapConverter.converterStringToBitmap(prevImage)
                if (newBitmap != null) {
                    Image(
                        bitmap = newBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(360.dp)
                    )
                }
            }else{
            bitmap?.let {
                convertedImage = BitmapConverter.converterBitmapToString(bitmap!!)
                convertedImage = state.imagePath
                onImageTaken.invoke(bitmap!!)
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(360.dp)
                ) }
            }

            val context = LocalContext.current

            if(!isUpdatingLocation){
            Button(
                modifier = Modifier.padding(horizontal = 165.dp, vertical = 4.dp),
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
                Icon(imageVector = Icons.Default.Camera,contentDescription = null)
            }}
        }
        Spacer(modifier = Modifier.size(12.dp))
        LocationTextField(
            modifier = Modifier.weight(2f).fillMaxWidth(),
            value = state.content,
            onValueChange = onContentChange,
            label = "Content"
        )
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
        verticalAlignment = Alignment.CenterVertically,

    ){
        IconButton(onClick = onNavigate) {
            Icon(
                imageVector = Icons.Default.ArrowBack ,
                modifier = Modifier.background(color = Color.Transparent),
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
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledPlaceholderColor = Color.Transparent,
            placeholderColor = Color.Transparent,
            focusedLabelColor = Color.Transparent
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


