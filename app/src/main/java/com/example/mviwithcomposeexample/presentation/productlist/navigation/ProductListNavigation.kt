package com.example.mviwithcomposeexample.presentation.productlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mviwithcomposeexample.TopAppBarActionEnum
import com.example.mviwithcomposeexample.enterTransition
import com.example.mviwithcomposeexample.exitTransition
import com.example.mviwithcomposeexample.popEnterTransition
import com.example.mviwithcomposeexample.popExitTransition
import com.example.mviwithcomposeexample.presentation.productlist.ProductListRoute


const val productListRoute = "product_list_route"

fun NavController.navigateToProductList(navOptions: NavOptions? = null) {
    this.navigate(productListRoute, navOptions)
}

fun NavGraphBuilder.productListScreen(
    onProductClick: (Int) -> Unit,
    currentTopAppBarAction: TopAppBarActionEnum,
    onAddProductSuccess: () -> Unit,
) {
    composable(
        route = productListRoute,
        enterTransition = {
            enterTransition()
        },
        exitTransition = {
            exitTransition()
        },
        popEnterTransition = {
            popEnterTransition()
        },
        popExitTransition = {
            popExitTransition()
        }
    ) {
        ProductListRoute(
            onProductClick = onProductClick,
            onAddProductSuccess = onAddProductSuccess,
            currentTopAppBarAction = currentTopAppBarAction,
        )
    }
}
