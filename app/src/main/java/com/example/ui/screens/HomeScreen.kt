package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScanEntity
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    scans: List<ScanEntity>,
    onScanClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    onCategoryClick: (String) -> Unit,
    onNavigateLibrary: () -> Unit,
    onNavigateCompare: () -> Unit,
    onNavigateDiscover: () -> Unit,
    onNavigateProfile: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = EditorialSurface,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = EditorialPrimary)
                    }
                    IconButton(onClick = onScanClick) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(EditorialPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Scan", tint = Color.White)
                        }
                    }
                    IconButton(onClick = onNavigateLibrary) {
                        Icon(Icons.Default.LibraryBooks, contentDescription = "Library", tint = EditorialTextSecondary)
                    }
                    IconButton(onClick = onNavigateCompare) {
                        Icon(Icons.Default.CompareArrows, contentDescription = "Compare", tint = EditorialTextSecondary)
                    }
                    IconButton(onClick = onNavigateProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = EditorialTextSecondary)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(EditorialBackground)
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "VISUAL INTELLIGENCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = EditorialTextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "LensMind AI",
                            style = MaterialTheme.typography.headlineLarge,
                            color = EditorialTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onNavigateProfile,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EditorialBorder.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = EditorialTextPrimary)
                    }
                }
            }

            // Big Camera CTA Card
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clickable { onScanClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(EditorialAccentGreen)
                                )
                                Text(
                                    text = "Point • Scan • Understand",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "Scan Anything",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Instant AI breakdown, components & interactive quiz",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Categories / Discover Mode
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trending Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EditorialTextPrimary
                    )
                    TextButton(onClick = onNavigateDiscover) {
                        Text("View All", color = EditorialPrimary)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val categories = listOf("Space", "Electricity", "Biology", "Machines", "Earth", "Technology")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories) { category ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = EditorialSurface,
                            border = BorderStroke(1.dp, EditorialBorder),
                            modifier = Modifier.clickable { onCategoryClick(category) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = EditorialPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = EditorialTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Recent Discoveries
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Discoveries",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EditorialTextPrimary
                    )
                    TextButton(onClick = onNavigateLibrary) {
                        Text("History", color = EditorialPrimary)
                    }
                }
            }

            if (scans.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = EditorialTextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "No scans yet. Tap the camera to begin!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EditorialTextSecondary
                            )
                        }
                    }
                }
            } else {
                items(scans.take(5)) { scan ->
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
                            if (scan.isFavorite) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Favorite",
                                    tint = Color(0xFFF59E0B)
                                )
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
