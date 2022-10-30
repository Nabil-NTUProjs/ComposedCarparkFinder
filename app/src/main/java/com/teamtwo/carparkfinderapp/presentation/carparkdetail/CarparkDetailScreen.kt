package com.teamtwo.carparkfinderapp.presentation.carparkdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.teamtwo.carparkfinderapp.domain.model.Availability
import com.teamtwo.carparkfinderapp.domain.model.Carpark
import kotlinx.coroutines.launch

@Composable
fun CarparkDetailScreen(
    carparkId: Int,
    navController: NavController,
    viewModel: CarparkDetailViewModel = hiltNavGraphViewModel()
) {

    // get the carpark state holder (with dummy state) from viewModel and load carpark
    val cParkState by remember {
        viewModel.cparkState
    }
    // get the availability state holder (with dummy state) from viewModel and load
    val availabilityState by remember {
        viewModel.availabilityState
    }

    // load the proper state of the selected carpark
    viewModel.loadCarpark(carparkId)
    // load the availability of the current carpark
    viewModel.loadAvailability(cParkState.carParkNo)

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    Box(modifier = Modifier.background(
        MaterialTheme.colors.primary)
        .fillMaxSize()
        .navigationBarsPadding()) {
        // data is loaded: proceed with composing the ui elements
        if (cParkState.id != -1) {
            DrawDetailScreen(cParkState, availabilityState, navController)
        } else { // draw loading screen
            CircularProgressIndicator(color = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.Center))
            Text("Loading...", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
    }

}

@Composable
fun DrawDetailScreen(
    cParkState: Carpark,
    availabilityState : Availability,
    navController: NavController,
    viewModel: CarparkDetailViewModel = hiltNavGraphViewModel()
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var toggleAvailability by remember { viewModel.toggleAvailability }

    // place the current database bookmarked status into a state so it can recompose the FAB
    var fabStatus by remember {
        mutableStateOf(cParkState.isBookmarked)
    }

    // load the availability of the current carpark
    viewModel.loadAvailability(cParkState.carParkNo)

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    // draw the map header based on carpark
                    MapHeader(
                        cParkState,
                        this@BoxWithConstraints.maxHeight,
                        viewModel
                    )

                    // floating action buttons
                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        // if not bookmarked, FAB will offer to SET bookmark
                        if(fabStatus == 0) {
                            ExtendedFloatingActionButton(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                icon = { Icon(Icons.Filled.BookmarkAdd,"", tint = MaterialTheme.colors.primaryVariant) },
                                text = { Text("BOOKMARK", color = MaterialTheme.colors.primaryVariant) },
                                elevation = FloatingActionButtonDefaults.elevation(5.dp),
                                backgroundColor = Color.Yellow,
                                onClick = {
                                    // function to modify the bookmark status
                                    coroutineScope.launch {
                                        viewModel.setBookmark(1, cParkState.id)
                                    }
                                    fabStatus = 1
                                }
                            )
                        }
                        // if is bookmarked, FAB will offer to REMOVE bookmark
                        if(fabStatus == 1) {
                            ExtendedFloatingActionButton(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                icon = { Icon(Icons.Filled.BookmarkAdded,"") },
                                text = { Text("BOOKMARK", color = MaterialTheme.colors.primaryVariant) },
                                elevation = FloatingActionButtonDefaults.elevation(5.dp),
                                backgroundColor = Color.Red,
                                onClick = {
                                    // function to modify the bookmark status
                                    coroutineScope.launch {
                                        viewModel.setBookmark(0, cParkState.id)
                                    }
                                    fabStatus = 0
                                }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        ExtendedFloatingActionButton(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = {
                                toggleAvailability = true
                            },
                            elevation = FloatingActionButtonDefaults.elevation(5.dp),
                            backgroundColor = MaterialTheme.colors.secondary,
                            text = {
                                // Inner content including an icon and a text label
                                Icon(
                                    Icons.Filled.LocalParking,
                                    contentDescription = "",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("CHECK LOTS", fontSize = 12.sp)
                            }
                        )
                    }

                    if (toggleAvailability) {
                        // open dialog box
                        AvailabilityDialog(availabilityState, viewModel)
                    }
                    ProfileContent(
                        cParkState,
                        this@BoxWithConstraints.maxHeight
                    )
                }
            }
        }
    }
}

// dialog that shows carpark availability from the api
@Composable
fun AvailabilityDialog(
    availabilityState: Availability,
    viewModel: CarparkDetailViewModel
) {

    var toggleAvailability by remember { viewModel.toggleAvailability }

    AlertDialog(
        onDismissRequest = {
            toggleAvailability = false
        },
        title = { Text("Live Availability - Carpark " + availabilityState.carparkNumber) },
        text = {
            Box {
                Column {

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Available Lots: " + availabilityState.availableLots,
                        modifier = Modifier.height(24.dp),
                        style = MaterialTheme.typography.body1
                    )

                    Text(
                        "Total Lots: " + availabilityState.totalLots,
                        modifier = Modifier.height(24.dp),
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Lot Type: " + availabilityState.lotType,
                        modifier = Modifier.height(24.dp),
                        style = MaterialTheme.typography.caption
                    )

                    Text(
                        "Retrieved: " + availabilityState.updateDateTime,
                        modifier = Modifier.height(24.dp),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { toggleAvailability = false }
                ) {
                    Text("Dismiss", color = Color.White)
                }
            }
        }
    )
}

// generates a composable map zoomed into a marker representing the carpark
@Composable
fun MapHeader(
    cpark: Carpark,
    containerHeight: Dp,
    viewModel: CarparkDetailViewModel
) {

    val pos = LatLng(cpark.lat, cpark.lng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pos, 15f)
    }
    GoogleMap(
        // getting the container height of parent in order to size the map to 1/2
        modifier = Modifier
            .heightIn(max = containerHeight / 2)
            .fillMaxWidth(),
        cameraPositionState = cameraPositionState
    ) {
        MarkerInfoWindowContent(
            state = MarkerState(position = pos) ,
            title = cpark.carParkNo,
            snippet = "Touch bottom window for directions",
        )
    }
}

@Composable
fun ProfileContent(
    cPark: Carpark,
    containerHeight: Dp
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        // prints big carpark name
        Title(cPark)

        // prints individual properties of the carpark
        ProfileProperty("Address", cPark.address)
        ProfileProperty("Carpark Type", cPark.carParkType)
        if (cPark.carParkDecks != 0) // don't need to print carpark decks if there are none
            ProfileProperty("Carpark Decks", cPark.carParkDecks.toString())
        if (cPark.gantryHeight.toInt() != 0) // don't need to print gantry if there are none
            ProfileProperty("Gantry Height", cPark.gantryHeight.toString()+"m")
        ProfileProperty("Parking System", cPark.parkingSystem)
        ProfileProperty("Short-Term Parking", cPark.shortTermParking)
        ProfileProperty("Free Parking", cPark.freeParking)
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun Title(
    cpark: Carpark
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp)) {
        Text(
            text = cpark.carParkNo,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

// composable for creating a label and property, called in ProfileContent with Carpark parameters
@Composable
fun ProfileProperty(label: String, value: String) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider(modifier = Modifier.padding(bottom = 4.dp))
        // property label
        Text(
            text = label,
            modifier = Modifier.height(16.dp),
            style = MaterialTheme.typography.caption,
        )
        // property name
        Text(
            text = value,
            modifier = Modifier.height(16.dp),
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Visible
        )
    }
}