package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class DiscoveryCard(val title: String, val category: String, val description: String)

@Composable
fun DiscoverScreen(
    onBack: () -> Unit,
    onSelectDiscovery: (String, String, String, String, String, String, String, String) -> Unit
) {
    val discoveries = listOf(
        DiscoveryCard("James Webb Space Telescope", "Space", "Infrared space observatory observing the earliest stars and galaxies."),
        DiscoveryCard("Superconducting Magnetic Coil", "Electricity", "Zero-resistance electrical conduction for high magnetic resonance fields."),
        DiscoveryCard("Photosynthesis Cellular Chloroplast", "Biology", "Plant organelle converting solar photon energy into chemical glucose."),
        DiscoveryCard("Internal Combustion Engine", "Machines", "Converts thermal energy from fuel combustion into mechanical torque."),
        DiscoveryCard("Tectonic Plate Subduction Zone", "Earth", "Geological boundary where oceanic lithosphere descends into the mantle."),
        DiscoveryCard("Quantum Qubit Processor", "Technology", "Superposition-based computing processor executing parallel multi-state algorithms.")
    )

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 2.dp,
                color = EditorialSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EditorialTextPrimary)
                    }
                    Text(
                        text = "Discover Mode",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EditorialTextPrimary
                    )
                    Spacer(modifier = Modifier.width(48.dp))
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
            item {
                Text(
                    text = "🔥 TRENDING EDUCATIONAL DISCOVERIES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EditorialPrimary,
                    letterSpacing = 1.5.sp
                )
            }

            items(discoveries) { card ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = BorderStroke(1.dp, EditorialBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectDiscovery(
                                card.title,
                                card.description,
                                "1. Input received\n2. Internal process\n3. Functional output",
                                "Core Module, Control Unit, Frame",
                                "Fascinating breakthrough in ${card.category}.",
                                "Alternative Model",
                                "It's like a magical science machine!",
                                "Advanced thermodynamic and operational paradigm."
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = card.category.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EditorialPrimary,
                                letterSpacing = 1.sp
                            )
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = EditorialPrimary, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = card.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EditorialTextPrimary
                        )
                        Text(
                            text = card.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = EditorialTextSecondary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
