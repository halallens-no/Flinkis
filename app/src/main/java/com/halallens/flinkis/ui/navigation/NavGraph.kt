package com.halallens.flinkis.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.halallens.flinkis.ui.screens.onboarding.ChildSetupScreen
import com.halallens.flinkis.ui.screens.onboarding.LanguageSelectionScreen
import com.halallens.flinkis.ui.screens.onboarding.ThemeSelectionScreen
import com.halallens.flinkis.ui.screens.rewards.RewardsScreen
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.ui.screens.templates.TemplatePickerScreen
import com.halallens.flinkis.ui.screens.templates.QuickBuilderScreen
import com.halallens.flinkis.ui.screens.settings.EditRoutineScreen
import com.halallens.flinkis.ui.screens.settings.ManageChildrenScreen
import com.halallens.flinkis.ui.screens.settings.SettingsScreen
import com.halallens.flinkis.ui.screens.settings.SettingsViewModel
import com.halallens.flinkis.ui.screens.splash.SplashScreen
import com.halallens.flinkis.ui.screens.today.TodayScreen
import com.halallens.flinkis.ui.screens.weekly.WeeklyScreen

/**
 * Navigation routes for MyRoutine app
 */
sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object LanguageSelection : Routes("language_selection")
    object ThemeSelection : Routes("theme_selection")
    object ChildSetup : Routes("child_setup")
    object Today : Routes("today")
    object Weekly : Routes("weekly")
    object Rewards : Routes("rewards")
    object Settings : Routes("settings")
    object EditRoutine : Routes("edit_routine/{routineId}") {
        fun createRoute(routineId: Long = -1L) = "edit_routine/$routineId"
    }
    object ManageChildren : Routes("manage_children")
    object TemplatePicker : Routes("template_picker")
    object QuickBuilder : Routes("quick_builder")
}

/**
 * Bottom navigation items
 */
data class BottomNavItem(
    val route: Routes,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Routes.Today,
        label = "Today",
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle
    ),
    BottomNavItem(
        route = Routes.Weekly,
        label = "Weekly",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    ),
    BottomNavItem(
        route = Routes.Rewards,
        label = "Rewards",
        selectedIcon = Icons.Filled.Stars,
        unselectedIcon = Icons.Outlined.Stars
    ),
    BottomNavItem(
        route = Routes.Settings,
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

/**
 * Main navigation host
 */
@Composable
fun MyRoutineNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // Onboarding flow (no bottom nav)
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigateToLanguageSelection = {
                    navController.navigate(Routes.LanguageSelection.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ThemeSelection.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Routes.Today.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LanguageSelection.route) {
            LanguageSelectionScreen(
                onLanguageSelected = {
                    // Navigate to theme selection after language is set
                    navController.navigate(Routes.ThemeSelection.route) {
                        popUpTo(Routes.LanguageSelection.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ThemeSelection.route) {
            ThemeSelectionScreen(
                onThemeSelected = {
                    navController.navigate(Routes.ChildSetup.route)
                }
            )
        }
        composable(Routes.ChildSetup.route) {
            ChildSetupScreen(
                onSetupComplete = {
                    navController.navigate(Routes.TemplatePicker.route) {
                        popUpTo(Routes.ChildSetup.route) { inclusive = true }
                    }
                }
            )
        }

        // Main app (with bottom nav)
        composable(Routes.Today.route) {
            MainScaffold(navController = navController) {
                TodayScreen()
            }
        }
        composable(Routes.Weekly.route) {
            MainScaffold(navController = navController) {
                WeeklyScreen()
            }
        }
        composable(Routes.Rewards.route) {
            MainScaffold(navController = navController) {
                RewardsScreen()
            }
        }
        composable(Routes.Settings.route) {
            val context = LocalContext.current
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            MainScaffold(navController = navController) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateToEditRoutine = { navController.navigate(Routes.EditRoutine.route) },
                    onNavigateToManageChildren = { navController.navigate(Routes.ManageChildren.route) },
                    onNavigateToTemplatePicker = { navController.navigate(Routes.TemplatePicker.route) },
                    onPrintRoutines = { settingsViewModel.printRoutines(context) }
                )
            }
        }

        // Settings sub-screens (no bottom nav)
        composable(Routes.EditRoutine.route) {
            EditRoutineScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ManageChildren.route) {
            ManageChildrenScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTemplatePicker = {
                    navController.navigate(Routes.TemplatePicker.route)
                }
            )
        }
        composable(Routes.TemplatePicker.route) {
            TemplatePickerScreen(
                onTemplateApplied = {
                    navController.navigate(Routes.Today.route) {
                        popUpTo(Routes.TemplatePicker.route) { inclusive = true }
                    }
                },
                onCreateCustom = {
                    navController.navigate(Routes.QuickBuilder.route)
                }
            )
        }
        composable(Routes.QuickBuilder.route) {
            QuickBuilderScreen(
                onNavigateBack = { navController.popBackStack() },
                onTemplateSaved = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Scaffold with bottom navigation bar
 */
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            MyRoutineBottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}

/**
 * Bottom navigation bar with icon bounce on tab switch
 */
@Composable
fun MyRoutineBottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route.route
            } == true

            // Bounce scale on selection
            val iconScale by animateFloatAsState(
                targetValue = if (isSelected) 1.15f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "nav_icon_${item.label}"
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        }
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}
