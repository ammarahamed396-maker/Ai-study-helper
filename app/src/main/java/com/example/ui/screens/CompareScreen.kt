package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun CompareScreen(
    onBack: () -> Unit
) {
    var itemA by remember { mutableStateOf("Lithium-ion Battery 🔋") }
    var itemB by remember { mutableStateOf("Hydrogen Fuel Cell ⚡") }

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
                        text = "AI Compare Mode",
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = BorderStroke(1.dp, EditorialBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "🔄 COMPARATIVE ANALYSIS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EditorialPrimary,
                            letterSpacing = 1.5.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = EditorialPrimary.copy(alpha = 0.1f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = itemA,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary
                                )
                            }
                            Text(
                                text = "VS",
                                modifier = Modifier.padding(horizontal = 12.dp),
                                fontWeight = FontWeight.Bold,
                                color = EditorialTextSecondary
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = EditorialSecondary.copy(alpha = 0.1f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = itemB,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialSecondary
                                )
                            }
                        }

                        Divider(color = EditorialBorder)

                        // Comparison matrix table
                        CompareRow("Primary Purpose", "Stores electrical energy chemically", "Converts chemical energy into electricity continuously")
                        CompareRow("Energy Source", "Internal lithium ions & cathode/anode", "External hydrogen gas and oxygen supply")
                        CompareRow("Refuel / Recharge", "Plugs into electric grid (hours)", "Refuels hydrogen in minutes")
                        CompareRow("Efficiency", "Higher round-trip efficiency (~85%)", "Lower overall efficiency (~55%)")
                        CompareRow("Best Context", "Urban passenger EVs, portable devices", "Heavy transport, long-haul trucking, grid storage")
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = BorderStroke(1.dp, EditorialBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "💡 AI CONTEXTUAL VERDICT",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EditorialAccentGreen,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "Batteries excel for daily short-range commuting and lightweight consumer electronics due to established charging infrastructure. Fuel cells win for massive commercial fleets requiring rapid refueling and high continuous payload capacities.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = EditorialTextPrimary,
                            lineHeight = 22.sp
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

@Composable
fun CompareRow(metric: String, valA: String, valB: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = metric.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = EditorialTextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = valA, style = MaterialTheme.typography.bodySmall, color = EditorialTextPrimary, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = valB, style = MaterialTheme.typography.bodySmall, color = EditorialTextPrimary, modifier = Modifier.weight(1f))
        }
        Divider(color = EditorialBorder.copy(alpha = 0.5f), modifier = Modifier.padding(top = 8.dp))
    }
}
