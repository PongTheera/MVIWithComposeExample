package com.example.mviwithcomposeexample

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry


private const val TRANSITION_DELAY = 700

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DELAY)
    )


fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DELAY)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DELAY)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DELAY)
    )
