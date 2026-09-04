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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScanEntity
import com.example.ui.theme.*

@Composable
fun KnowledgeScreen(
    scanId: Long,
    onBack: () -> Unit,
    viewModel: com.example.viewmodel.LensViewModel
) {
    var scan by remember { mutableStateOf<ScanEntity?>(null) }
    var teachMode10 by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: Quiz, 2: Chat
    var chatInput by remember { mutableStateOf("") }
    var chatMessages by remember { mutableStateOf(listOf("AI: Hello! Ask me anything about ${scan?.title ?: "this object"}.")) }
    var quizAnswered by remember { mutableStateOf(false) }
    var quizCorrect by remember { mutableStateOf(false) }

    LaunchedEffect(scanId) {
        scan = viewModel.getScanById(scanId)
        if (scan != null) {
            chatMessages = listOf("AI: I am ready to answer your questions about ${scan!!.title}!")
        }
    }

    val currentScan = scan ?: return Box(modifier = Modifier.fillMaxSize().background(EditorialBackground), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = EditorialPrimary)
    }

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
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EditorialTextPrimary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "VISUAL IDENTIFICATION",
                            style = MaterialTheme.typography.labelSmall,
                            color = EditorialTextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = currentScan.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EditorialTextPrimary
                        )
                    }
                    IconButton(onClick = { viewModel.toggleFavorite(currentScan) }) {
                        Icon(
                            imageVector = if (currentScan.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (currentScan.isFavorite) Color(0xFFF59E0B) else EditorialTextPrimary
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = EditorialSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { selectedTab = 0 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == 0) EditorialPrimary else EditorialBorder.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Learn", color = if (selectedTab == 0) Color.White else EditorialTextPrimary)
                    }
                    Button(
                        onClick = { selectedTab = 1 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == 1) EditorialPrimary else EditorialBorder.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Test Me", color = if (selectedTab == 1) Color.White else EditorialTextPrimary)
                    }
                    Button(
                        onClick = { selectedTab = 2 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == 2) EditorialPrimary else EditorialBorder.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ask AI", color = if (selectedTab == 2) Color.White else EditorialTextPrimary)
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Confidence Header Card
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(EditorialAccentGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
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
                                    text = "${currentScan.confidence}% Confidence",
                                    color = EditorialAccentGreen,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Teach Me Like I'm 10 Toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(EditorialPrimary.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .clickable { teachMode10 = !teachMode10 }
                            ) {
                                Icon(Icons.Default.ChildCare, contentDescription = null, tint = EditorialPrimary, modifier = Modifier.size(18.dp))
                                Text(
                                    text = if (teachMode10) "Kid Mode: ON" else "Kid Mode: OFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary
                                )
                            }
                        }

                        Text(
                            text = if (teachMode10) currentScan.simpleExplanation else currentScan.whatIsIt,
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            color = EditorialTextPrimary,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    // TAB 0: LEARN / KNOWLEDGE
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
                                    text = "⚙️ HOW IT WORKS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary,
                                    letterSpacing = 1.5.sp
                                )
                                Text(
                                    text = currentScan.howItWorks,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = EditorialTextPrimary,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    // Components
                    item {
                        Text(
                            text = "🧩 Explore Components",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EditorialTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val parts = currentScan.components.split(",")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(parts) { part ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = EditorialSurface,
                                    border = BorderStroke(1.dp, EditorialBorder),
                                    modifier = Modifier.width(140.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Settings, contentDescription = null, tint = EditorialPrimary)
                                        Text(
                                            text = part.trim(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = EditorialTextPrimary
                                        )
                                        Text(
                                            text = "Tap to explore role",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = EditorialTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Interesting Facts
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
                                    text = "🤯 DID YOU KNOW?",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary,
                                    letterSpacing = 1.5.sp
                                )
                                val facts = currentScan.interestingFacts.split("|")
                                for (fact in facts) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("•", color = EditorialPrimary, fontWeight = FontWeight.Bold)
                                        Text(fact.trim(), style = MaterialTheme.typography.bodyMedium, color = EditorialTextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    // Advanced Mode
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
                                    text = "🔬 ADVANCED MODE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary,
                                    letterSpacing = 1.5.sp
                                )
                                Text(
                                    text = currentScan.advancedExplanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = EditorialTextPrimary,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // TAB 1: QUIZ / TEST ME
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "QUICK QUIZ 1/1",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EditorialPrimary
                                    )
                                    Icon(Icons.Default.Quiz, contentDescription = null, tint = EditorialPrimary)
                                }

                                Text(
                                    text = "What is the primary function of ${currentScan.title}?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialTextPrimary
                                )

                                val options = listOf(
                                    "A. Converts energy into functional motion or utility",
                                    "B. Generates random electronic noise",
                                    "C. Acts solely as an ornamental display",
                                    "D. Consumes energy without any output"
                                )

                                options.forEachIndexed { index, option ->
                                    val isSelected = quizAnswered && index == 0
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (quizAnswered) {
                                            if (index == 0) EditorialAccentGreen.copy(alpha = 0.2f) else EditorialSurface
                                        } else EditorialSurface,
                                        border = BorderStroke(1.dp, if (quizAnswered && index == 0) EditorialAccentGreen else EditorialBorder),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                quizAnswered = true
                                                quizCorrect = (index == 0)
                                            }
                                    ) {
                                        Text(
                                            text = option,
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = EditorialTextPrimary
                                        )
                                    }
                                }

                                if (quizAnswered) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (quizCorrect) EditorialAccentGreen.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (quizCorrect) "🎉 Correct! This component is designed precisely for energy conversion and output." else "❌ Incorrect. Try again or review the How It Works section!",
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (quizCorrect) EditorialAccentGreen else Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // TAB 2: ASK AI CHAT
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
                                    text = "💬 ASK AI ABOUT THIS OBJECT",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EditorialPrimary
                                )

                                for (msg in chatMessages) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (msg.startsWith("You:")) EditorialPrimary.copy(alpha = 0.1f) else EditorialBorder.copy(alpha = 0.2f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = msg,
                                            modifier = Modifier.padding(12.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = EditorialTextPrimary
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = chatInput,
                                        onValueChange = { chatInput = it },
                                        placeholder = { Text("Ask why it moves, what happens if it stops...") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    Button(
                                        onClick = {
                                            if (chatInput.isNotBlank()) {
                                                val q = chatInput
                                                chatMessages = chatMessages + "You: $q"
                                                chatInput = ""
                                                val answer = "AI: Based on ${currentScan.title}, this happens due to continuous energy flow and structural engineering design."
                                                chatMessages = chatMessages + answer
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = EditorialPrimary)
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                                    }
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
