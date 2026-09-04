package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScanEntity
import com.example.ui.theme.*

@Composable
fun LibraryScreen(
    scans: List<ScanEntity>,
    onItemClick: (Long) -> Unit,
    onBack: () -> Unit,
    onNavigateScan: () -> Unit,
    viewModel: com.example.viewmodel.LensViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var filterFavorites by remember { mutableStateOf(false) }

    val filteredScans = scans.filter {
        (if (filterFavorites) it.isFavorite else true) &&
        it.title.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 2.dp,
                color = EditorialSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EditorialTextPrimary)
                        }
                        Text(
                            text = "Personal Learning Library",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EditorialTextPrimary
                        )
                        IconButton(onClick = { filterFavorites = !filterFavorites }) {
                            Icon(
                                imageVector = if (filterFavorites) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Filter Favorites",
                                tint = if (filterFavorites) Color(0xFFF59E0B) else EditorialTextPrimary
                            )
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search your discoveries...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EditorialTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(EditorialBackground)
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (filteredScans.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LibraryBooks,
                                contentDescription = null,
                                tint = EditorialTextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "No discoveries found.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EditorialTextSecondary
                            )
                        }
                    }
                }
            } else {
                items(filteredScans) { scan ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                        border = BorderStroke(1.dp, EditorialBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(scan.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(EditorialPrimary.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = EditorialPrimary
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = scan.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EditorialTextPrimary
                                    )
                                    Text(
                                        text = scan.whatIsIt,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = EditorialTextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = { viewModel.toggleFavorite(scan) }) {
                                    Icon(
                                        imageVector = if (scan.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Favorite",
                                        tint = if (scan.isFavorite) Color(0xFFF59E0B) else EditorialTextSecondary
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteScan(scan) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
