package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LensViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LensViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val scans by viewModel.allScans.collectAsStateWithLifecycle()
                val favorites by viewModel.favoriteScans.collectAsStateWithLifecycle()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            scans = scans,
                            onScanClick = { navController.navigate("scan") },
                            onItemClick = { id -> navController.navigate("knowledge/$id") },
                            onCategoryClick = { cat ->
                                // Quick quick scan or category
                                navController.navigate("discover")
                            },
                            onNavigateLibrary = { navController.navigate("library") },
                            onNavigateCompare = { navController.navigate("compare") },
                            onNavigateDiscover = { navController.navigate("discover") },
                            onNavigateProfile = { navController.navigate("profile") }
                        )
                    }
                    composable("scan") {
                        ScanScreen(
                            onBack = { navController.popBackStack() },
                            onAnalysisComplete = { id ->
                                navController.navigate("knowledge/$id") {
                                    popUpTo("home")
                                }
                            },
                            viewModel = viewModel
                        )
                    }
                    composable(
                        route = "knowledge/{scanId}",
                        arguments = listOf(navArgument("scanId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val scanId = backStackEntry.arguments?.getLong("scanId") ?: 0L
                        KnowledgeScreen(
                            scanId = scanId,
                            onBack = { navController.popBackStack() },
                            viewModel = viewModel
                        )
                    }
                    composable("library") {
                        LibraryScreen(
                            scans = scans,
                            onItemClick = { id -> navController.navigate("knowledge/$id") },
                            onBack = { navController.popBackStack() },
                            onNavigateScan = { navController.navigate("scan") },
                            viewModel = viewModel
                        )
                    }
                    composable("compare") {
                        CompareScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("discover") {
                        DiscoverScreen(
                            onBack = { navController.popBackStack() },
                            onSelectDiscovery = { title, whatIsIt, howItWorks, components, facts, similar, simple, advanced ->
                                viewModel.saveQuickScan(title, whatIsIt, howItWorks, components, facts, similar, simple, advanced)
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            totalScansCount = scans.size,
                            favoritesCount = favorites.size,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
