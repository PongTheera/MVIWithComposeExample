package com.example.mviwithcomposeexample

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.mviwithcomposeexample.presentation.detail.navigation.ProductDetailScreen
import com.example.mviwithcomposeexample.presentation.detail.navigation.navigateToProductDetail
import com.example.mviwithcomposeexample.presentation.productlist.navigation.productListRoute
import com.example.mviwithcomposeexample.presentation.productlist.navigation.productListScreen

@Composable
fun MainAppHost(
    appState: MainAppState,
    currentTopAppBarAction: TopAppBarActionEnum,
    onAddProductSuccess: () -> Unit,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = productListRoute
    ) {
        productListScreen(
            onProductClick = { productId ->
                navController.navigateToProductDetail(productId = productId)
            },
            onAddProductSuccess = onAddProductSuccess,
            currentTopAppBarAction = currentTopAppBarAction,
        )
        ProductDetailScreen()
    }
}