package com.teamtwo.carparkfinderapp

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.teamtwo.carparkfinderapp.presentation.carparkdetail.CarparkDetailScreen
import com.teamtwo.carparkfinderapp.presentation.carparklist.CarparkListScreen
import com.teamtwo.carparkfinderapp.presentation.map.MapScreen
import com.teamtwo.carparkfinderapp.theme.MapsComposeGuideTheme
import dagger.hilt.android.AndroidEntryPoint

/*
* This is the standard Android activity class which contains and launches the application,
* similar to main() in java and other programming paradigms. MainActivity will load first, and
* the rest of the application is loaded from there.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MapsComposeGuideTheme {

                var locationFromGps: Location? by remember { mutableStateOf(null) }

                // request for location permissions
                val permissionsState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )

                val context = LocalContext.current
                val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
                val locationCallback = remember {
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            locationFromGps = locationResult.lastLocation
                        }
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current

                // callback that is cleaned after use, used to display the permissions request
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if(event == Lifecycle.Event.ON_START) {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                // display messages based on permissions granted
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    permissionsState.permissions.forEach { perm ->
                        when(perm.permission) {
                            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                                when {
                                    perm.hasPermission -> {
                                        Text(text = "Coarse location permission granted.")
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    perm.shouldShowRationale -> {
                                        Text(text = "Coarse location required for map display.")
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    !perm.hasPermission && !perm.shouldShowRationale -> {
                                        Text("Coarse location permission is permanently denied, please" +
                                                "enable it in the application settings.")
                                    }
                                }
                            }
                            Manifest.permission.ACCESS_FINE_LOCATION -> {
                                when {
                                    perm.hasPermission -> {
                                        Text(text = "Fine location permission granted.")
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    perm.shouldShowRationale -> {
                                        Text(text = "Fine location required for map display.")
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    !perm.hasPermission && !perm.shouldShowRationale -> {
                                        Text("Fine location permission is permanently denied, please" +
                                                "enable it in the application settings.")
                                    }
                                }
                            }
                        }
                    }

                    Text(text = "Fine location permission granted.")
                }

                // reference to nav controller - pass this to all the screen composables
                // in order to enable Compose navigation.
                val navController = rememberNavController()

                NavHost(navController = navController,
                    startDestination = "map_screen",
                ) {
                    // specify all the composables that make up the app screens here
                    // this route leads to the default map screen, and is the starting route
                    composable("map_screen") {
                        MapScreen(navController = navController, modifier = Modifier, mCurrentLocation = locationFromGps)
                    }
                    // this route leads to the carpark list and search screen
                    composable("carpark_list_screen") {
                        CarparkListScreen(navController = navController, modifier = Modifier)
                    }
                    // this route leads to the specific detail screen of a carpark
                    // entry, specified by its unique id in the database
                    composable("carpark_detail_screen/{carparkId}",
                        arguments = listOf(
                            navArgument("carparkId") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        // extract arguments from the nav call, pass to composable call
                        val carparkId = remember {
                            // default to 0 if null value gotten
                            it.arguments?.getInt("carparkId") ?: 1
                        }
                        CarparkDetailScreen(
                            navController = navController,
                            carparkId = carparkId,
                        )
                    }
                }
            }
        }
    }
}
