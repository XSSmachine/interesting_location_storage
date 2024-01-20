package org.unizd.rma.kovacevic.presentation.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.unizd.rma.kovacevic.common.ScreenViewState
import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.data.local.model.LocationCategory
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
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
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
            .fillMaxWidth()
            .padding(4.dp),
        shape = shape,
        onClick = {onLocationClicked(location.id)}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = location.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = location.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ){
                IconButton(onClick = { onDeleteLocation(location.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        )

                }
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
        title="Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = Date(),
        category = LocationCategory.GRADSKI_PROSTORI,
        imagePath = "res/drawable/example_pic.jpg",
        latitude = 0.0,
        longitude = 0.0

    ),
    Location(
        title="Jetpack Compose",
        content = "Test example",
        createdDate = Date(),
        category = LocationCategory.POVIJESNA_MJESTA,
        imagePath = "res/drawable/example_pic.jpg",
        latitude = 0.0,
        longitude = 0.0

    ),
    Location(
        title="Room Database",
        content = placeHolderText + placeHolderText,
        createdDate = Date(),
        category = LocationCategory.GRADSKI_PROSTORI,
        imagePath = "res/drawable/example_pic.jpg",
        latitude = 0.0,
        longitude = 0.0

    )

)