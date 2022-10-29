package com.teamtwo.carparkfinderapp.presentation.carparklist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.teamtwo.carparkfinderapp.domain.model.Carpark


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CarparkListScreen(
    navController: NavController,
    viewModel: CarparkListViewModel = hiltNavGraphViewModel(),
    modifier: Modifier
) {
    val scaffoldState = rememberScaffoldState()
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    Box(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                ListBottomBar(viewModel, navController)
            },
            modifier = Modifier.navigationBarsPadding().statusBarsPadding()
        ) {
            Column {
                SearchBar(
                    hint = "Search...",
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    viewModel.searchCarparkList(it)
                }
                Spacer(modifier = Modifier.height(16.dp))
                CarparkList(navController = navController)
            }
        }
    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    // state variables

    var text by remember {
        mutableStateOf("")
    }

    // if we pass in a string, this state will be true, false otherwise
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it     // assign new string to text state
                onSearch(it)  // trigger search with the new string
            },
            maxLines = 1,
            singleLine = true,
            //
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    // hide hint (via state) when text field is focused
                    isHintDisplayed = it.isFocused != true
                }
        )
        // if text field is not focused, display hint
        if(isHintDisplayed){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun ListBottomBar(
    viewModel: CarparkListViewModel = hiltNavGraphViewModel(),
    navController: NavController
) {

    BottomNavigation(elevation = 10.dp) {
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Map, "")
        },
            label = { Text(text = "Map") },
            selected = false,
            onClick = {
                navController.navigate("map_screen")
            }
        )
    }
}

@Composable
fun CarparkList(
    navController: NavController,
    viewModel: CarparkListViewModel = hiltNavGraphViewModel()
) {
    // grab states from viewModel
    val cparkList by remember { viewModel.cparkList }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val listSize = cparkList.size

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(listSize) {
            index -> ListEntry(cparkList[index], navController)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().navigationBarsPadding().statusBarsPadding(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.Center))
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadCarparkList()
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column(horizontalAlignment = CenterHorizontally) {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun ListEntry(
    entry: Carpark,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CarparkListViewModel = hiltNavGraphViewModel()
) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {

        Column(modifier = Modifier
            .clickable { navController.navigate("carpark_detail_screen/${entry.id-1}") }
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Text(text = entry.address, style = typography.h6)
            Row {
                Text(text = "VIEW", style = typography.caption)
                if (entry.isBookmarked == 1)
                {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(imageVector = Icons.Default.Star,"")
                }
            }

        }
    }
}