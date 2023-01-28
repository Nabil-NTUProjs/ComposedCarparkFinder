# ComposedCarparkFinder
Find Me Carparks is an application that searches for and displays public carparks in Singapore, sourced from data.gov.sg APIs. The app is built with Jetpack Compose as part of NTU's SC2006 Software Engineering module, and aims to demonstrate use of the Model-View-Viewmodel architecture, with dependency injection, API services and creation of a local database. 

To try out this application, use the latest stable version of [Android Studio](https://developer.android.com/studio), and clone this repository. 

## Features

The application contains several screens, the main map screen containing all carpark markers, the carpark list and search screen, and the carpark detail screen. It also supports light and dark themes, as well as multiple screen sizes.

The user is able to bookmark commonly-visited carparks for later reference, view live occupancy levels, as well as set a navigation route to any carpark.

## Screenshots
![screenshot1](https://user-images.githubusercontent.com/19179827/215279336-29662d34-3716-45af-b358-02c88e766cdc.png)


### App Scaffolding

Package `com.teamtwo.carparkfinderapp` 

`MainActivity.kt` is the application entry point. Each application screen is implemented using Composables and navigation between them is handled using Android Jetpack's [Navigation](https://developer.android.com/guide/navigation) component. The application's navigation graph and destinations are configured here, creating a NavHost container.

#### Map

Package `com.teamtwo.carparkfinderapp.presentation.map`

The package contains the map screen, its associated viewmodel and states. It is the default start destination of the application, and displays all carparks overlaid on a [Google Maps Composable](https://developers.google.com/maps/documentation/android-sdk/maps-compose) after retrieving their entries from the application [Room](https://developer.android.com/training/data-storage/room) database.

#### Carpark List

Package `com.teamtwo.carparkfinderapp.presentation.carparklist`

The package contains the carpark list screen, and its associated viewmodel. It will display all carparks retrieved from the database in a scrolling list. The user will navigate to this screen by clicking on the bottom bar found in the map screen.


#### Carpark Details

Package `com.teamtwo.carparkfinderapp.presentation.carparkdetail`

The package contains the carpark details screen, and its associated viewmodel. It will display all values from the [Carpark](https://github.com/Sonvanelle/ComposedCarparkFinder/blob/master/app/src/main/java/com/teamtwo/carparkfinderapp/domain/model/Carpark.kt) data class. Live availability numbers of the selected carpark can be queried from this screen, as well as bookmark creation.

###   Data

Data is handled in the `com.teamtwo.carparkfinderapp.data` package, broken down into two other packages:

- `remote` contains the Retrofit API requests, which converts the retrieved JSON data into a Java interface format.
- `local` contains the Room database entities, storing the Carpark and Availability data on-device.

The `dependencyinjection` module will create and link the databases with the APIs, and then provide them as instances to the appropriate viewmodels.

#

This project uses a library by [cgcai](https://github.com/cgcai/SVY21) to handle conversion of SVY21 coordinates to Lat/Lon.
