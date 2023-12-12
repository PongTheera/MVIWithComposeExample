@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mviwithcomposeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mviwithcomposeexample.ui.theme.MVIWithComposeExampleTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MVIWithComposeExampleTheme {

                MviExampleProductApp()

            }
        }
    }

    @Composable
    private fun MviExampleProductApp(
        appState: MainAppState = rememberAppState(),
    ) {

        var currentTopAppBarAction by rememberSaveable {
            mutableStateOf(TopAppBarActionEnum.ACTION_IDLE)
        }

        Scaffold(
            modifier = Modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                AppToolbar(
                    appState = appState,
                    title = getString(appState.titleTopAppBar),
                    actionIcon = Icons.Default.AddCircle,
                    navigationIcon = Icons.Default.ArrowBack,
                    onBackPressed = {
                        appState.navController.popBackStack()
                    }, onActionClick = {
                        currentTopAppBarAction = TopAppBarActionEnum.ACTION_ADD_PRODUCT
                    }
                )

                MainAppHost(
                    appState = appState,
                    currentTopAppBarAction = currentTopAppBarAction,
                    onAddProductSuccess = {
                        currentTopAppBarAction = TopAppBarActionEnum.ACTION_IDLE
                    },
                )

            }

        }
    }
}


@Composable
fun AppToolbar(
    title: String,
    navigationIcon: ImageVector,
    onBackPressed: () -> Unit,
    actionIcon: ImageVector,
    onActionClick: () -> Unit,
    appState: MainAppState,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, color = Color.White) },
        navigationIcon = {
            if (!appState.currentTopLevelDestination) {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(navigationIcon, contentDescription = null, tint = Color.White)
                }
            }
        },
        actions = {
            if (appState.currentTopLevelDestination) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}