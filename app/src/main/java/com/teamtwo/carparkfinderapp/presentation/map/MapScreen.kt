package com.teamtwo.carparkfinderapp.presentation.map

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.teamtwo.carparkfinderapp.R
import com.teamtwo.carparkfinderapp.domain.model.Carpark

/*
* This class contains Composable functions responsible for drawing UI elements
* for the Map screen. It will call upon the MapsViewModel to execute functions
* using data belonging to another layer.
* The map screen contains the Google Maps API composable, bottom navigation bar,
* floating action button to open the bookmark list, as well as the UI for the bookmark list.
*/

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MapsViewModel = hiltNavGraphViewModel(),
    mCurrentLocation: Location?,
) {
    val scaffoldState = rememberScaffoldState()
    val MapState by viewModel.mapState.collectAsState()

    // beginning state for the map
    val singapore = mCurrentLocation?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(1.35, 103.87)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 12f)
    }
    // carpark list for displaying markers
    val cparkList by remember { viewModel.cparkList }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    Box(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.navigationBarsPadding(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    // button will trigger event to change state (if false, assert true and open bookmark)
                    viewModel.onEvent(MapEvent.ToggleBookmarkView)
                }) {
                    Icon(
                        imageVector = Icons.Default.Bookmarks,
                        contentDescription = "Open bookmarks"
                    )
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomBar(viewModel, navController)
            }

        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                cameraPositionState = cameraPositionState,
                // to ensure map properties reflect changes in state
                properties = MapState.properties,
                // prevent the controls from overlapping with the FAB
                uiSettings = MapState.uiSettings,

            ) {
                // place all the markers from the corresponding list to the screen
                cparkList.forEach {carpark ->
                    MarkerInfoWindowContent(
                        state = MarkerState(
                            position = LatLng(carpark.lat, carpark.lng)
                        ),
                        title = carpark.address,
                        infoWindowAnchor = Offset(0.0f, 0.0f)
                    )
                }
            }
            /*
            ScaleBar(
                modifier = Modifier
                    .padding(top = 5.dp, end = 15.dp)
                    .align(Alignment.TopEnd),
                cameraPositionState = cameraPositionState
            )
            */

            // open bookmark depending on state (after button onClick)
            if (MapState.isBookmarkView) {
                viewModel.loadBookmarkedCarparkList()
                bookmarkDialog(navController)
            }
        }
    }
}

/*
* This composable will define and style the bottom navigation bar, as
* well as set the navigation link to switch views to the carpark list screen.
 */

@Composable
fun BottomBar(
    viewModel: MapsViewModel = hiltNavGraphViewModel(),
    navController: NavController
) {

    BottomNavigation(
        elevation = 10.dp
    ) {
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.ManageSearch,"")
        },
            label = { Text(text = "Search") },
            selected = false,
            onClick = {
                navController.navigate("carpark_list_screen")
            }
        )
    }
}

/*
* This composable will provide the bookmark list in the form of a pop-up
* dialog. It will rely on MapsViewModel to retrieve data from the database
* and populate the states containing the list of carparks.
 */

@Composable
private fun bookmarkDialog(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MapsViewModel = hiltNavGraphViewModel(),
) {
    // grab states from viewModel
    val cparkBookmarkList by remember { viewModel.cparkBookmarkList }
    val listSize = cparkBookmarkList.size

    AlertDialog(
        onDismissRequest = {
            viewModel.onEvent(MapEvent.ToggleBookmarkView)
        },
        title = { Text(stringResource(R.string.savedBookmarks) + ": " + listSize.toString()) },
        text = {
            BoxWithConstraints {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                    .heightIn(max = 400.dp, min = 100.dp )
                    .fillMaxWidth(),
                    contentPadding = PaddingValues(4.dp))
                {
                    items(listSize) {
                        index -> BookmarkListItem(navController, cparkBookmarkList[index])
                    }
                }
            }
        },
        modifier = modifier,
        buttons = {
            Row(
                modifier = Modifier.padding(all = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onEvent(MapEvent.ToggleBookmarkView) }
                ) {
                    Text("Dismiss")
                }
            }
        }
    )
}

@Composable
private fun BookmarkListItem(
    navController: NavController,
    cpark: Carpark
) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
        elevation = 2.dp,
    ) {
        Row(Modifier.clickable {

        }) {
            Column(modifier = Modifier
                .clickable { navController.navigate("carpark_detail_screen/${cpark.id-1}") }
                .padding(16.dp)
                .fillMaxWidth()
            ) {
                Text(text = cpark.address, style = MaterialTheme.typography.body2)
                Text(text = "VIEW", style = MaterialTheme.typography.caption)
            }
        }
    }
}