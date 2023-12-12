package com.example.mviwithcomposeexample.presentation.detail.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.EnterTransition
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mviwithcomposeexample.enterTransition
import com.example.mviwithcomposeexample.exitTransition
import com.example.mviwithcomposeexample.popEnterTransition
import com.example.mviwithcomposeexample.popExitTransition
import com.example.mviwithcomposeexample.presentation.detail.ProductDetailRoute
import java.net.URLDecoder
import java.net.URLEncoder


const val productDetailRoute = "product_detail_route"

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()


@VisibleForTesting
internal const val productIdArg = "productId"

internal class ProductDetailArgs(val productId: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(
                savedStateHandle[productIdArg]
            ), URL_CHARACTER_ENCODING
        )
    )
}

fun NavController.navigateToProductDetail(productId: Int) {
    val encodedId = URLEncoder.encode("$productId", URL_CHARACTER_ENCODING)
    this.navigate("$productDetailRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.ProductDetailScreen() {
    composable(
        route = "$productDetailRoute/{$productIdArg}",
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
        },
        arguments = listOf(
            navArgument(productIdArg) {
                type = NavType.StringType
            }
        ),
        content = {
            ProductDetailRoute()
        }
    )
}