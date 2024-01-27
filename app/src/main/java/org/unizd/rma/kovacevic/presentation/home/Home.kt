package org.unizd.rma.kovacevic.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.unizd.rma.kovacevic.BitmapConverter
import org.unizd.rma.kovacevic.common.ScreenViewState
import org.unizd.rma.kovacevic.data.local.model.Location
import java.util.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onDeleteLocation: (Long) -> Unit,
    onLocationClicked: (Long) -> Unit
){
    when(state.locations){
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }
        is ScreenViewState.Success -> {
            val locations = state.locations.data
            HomeDetail(
                locations = locations,
                modifier = modifier,
                onDeleteLocation = onDeleteLocation,
                onLocationClicked = onLocationClicked)
        }

        is ScreenViewState.Error -> {
            Text(
                text = state.locations.message ?: "Unknown Error",
                color = MaterialTheme.colors.error
                )
        }
    }
}


@Composable
private fun HomeDetail(
    locations:List<Location>,
    modifier: Modifier,
    onDeleteLocation:(Long) -> Unit,
    onLocationClicked:(Long) -> Unit
){

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier.padding(bottom = 50.dp)
    ){
        itemsIndexed(locations){ index, item ->
            LocationCard(
                index = index,
                location = item,
                onDeleteLocation = onDeleteLocation,
                onLocationClicked = onLocationClicked)

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationCard(
    index:Int,
    location: Location,
    onDeleteLocation:(Long) -> Unit,
    onLocationClicked:(Long) -> Unit
){

    val isEvenIndex = index % 2 == 0
    val shape = when{
        isEvenIndex -> {
            RoundedCornerShape(
                topStart = 50f,
                bottomEnd = 50f
            )
        }
        else -> {
            RoundedCornerShape(
                topEnd = 50f,
                bottomStart = 50f
            )
        }
    }
    Card (
        modifier = Modifier
            .width(105.dp)
            .padding(4.dp),
        shape = shape,
        onClick = {onLocationClicked(location.id)}
    ) {
        Column(
            modifier = Modifier
                .width(105.dp)
                .padding(8.dp)
        ) {
            Text(
                modifier=Modifier.width(185.dp),
                text = location.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.size(0.dp))

            Text(
                text = location.category +" | "+ location.createdDate,
                fontWeight = FontWeight.Medium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                modifier=Modifier.width(190.dp),
                text = location.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.size(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(
                    modifier = Modifier.size(25.dp),
                    onClick = { onDeleteLocation(location.id) }) {
                    Icon(

                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )

                }
            }
        }

        Column(modifier = Modifier
            .size(180.dp)
            .padding(start = 200.dp, bottom = 5.dp).fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            var prevImage = location.imagePath
            var newBitmap = BitmapConverter.converterStringToBitmap(prevImage)
            if (newBitmap != null) {
                Image(
                    bitmap = newBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .size(360.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun prevHome(){
    HomeScreen(
        state = HomeState(
            locations = ScreenViewState.Success(locations)
        ),
        onDeleteLocation = {},
        onLocationClicked = {})
}

val placeHolderText =
    "Ovdje je neki random tekst koji sam napisao da se ispiše u prikazu"

val locations = listOf(
    Location(
        title ="Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = "23-11-2025",
        category = "GRADSKI PROSTORI",
        imagePath = "res/drawable/example_pic.jpg",
        latitude = 0.0,
        longitude = 0.0

    ),
    Location(
        title ="Jetpack Compose",
        content = "Test example",
        createdDate = "23-11-2025",
        category = "POVIJESNA_MJESTA",
        imagePath = "res/drawable/example_pic.jpg",
        latitude = 0.0,
        longitude = 0.0

    ),
    Location(
        title ="Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = "23-11-2025",
        category = "GRADSKI PROSTORI",
        imagePath = "res/drawable/example_pic.jpg",
        latitude = 0.0,
        longitude = 0.0

    )

)