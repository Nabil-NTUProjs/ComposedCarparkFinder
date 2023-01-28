# ComposedCarparkFinder
Find Me Carparks is an application that searches for and displays public carparks in Singapore, sourced from data.gov.sg APIs. The app is built with Jetpack Compose as part of NTU's SC2006 Software Engineering module, and aims to demonstrate use of the Model-View-Viewmodel architecture, with dependency injection, API services and creation of a local database. 

To try out this application, use the latest stable version of [Android Studio](https://developer.android.com/studio), and clone this repository. 

## Features

The application contains several screens, the main map screen containing all carpark markers, the carpark list and search screen, and the carpark detail screen. It also supports light and dark themes, as well as multiple screen sizes.

### App Scaffolding

Package `com.teamtwo.carparkfinderapp` 

`MainActivity.kt` is the application entry point. Each application screen is implemented using Composables and navigation between them is handled using Android Jetpack's [Navigation](https://developer.android.com/guide/navigation) component. The application's navigation graph and destinations are configured here, creating a NavHost container.

### Map

Package `com.teamtwo.carparkfinderapp.presentation.map`

The package contains the map screen, its associated viewmodel and states. It is the default start destination of the application, and displays all carparks overlaid on a [Google Maps Composable](https://developers.google.com/maps/documentation/android-sdk/maps-compose) after retrieving their entries from the application [Room](https://developer.android.com/training/data-storage/room) database.

### Carpark List

Package `com.teamtwo.carparkfinderapp.presentation.carparklist`

The package contains the carpark list screen, and its associated viewmodel. It will display all carparks retrieved from the database in a scrolling list. The user will navigate to this screen by clicking on the bottom bar found in the map screen.


### Carpark Details

###   Data
