package com.example.mviwithcomposeexample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mviwithcomposeexample.presentation.detail.navigation.productDetailRoute
import com.example.mviwithcomposeexample.presentation.productlist.navigation.productListRoute
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): MainAppState {
    return remember(navController, coroutineScope) {
        MainAppState(navController, coroutineScope)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: Boolean
        @Composable get() = when (currentDestination?.route) {
            productListRoute -> true
            else -> false
        }

    val titleTopAppBar: Int
        @Composable get() {
            val splitRoute = currentDestination?.route?.split("/")
            val currentRoute = splitRoute?.get(0)
            return when (currentRoute) {
                productListRoute -> R.string.title_product_list
                productDetailRoute -> R.string.title_product_detail
                else -> R.string.title_empty
            }
        }

}