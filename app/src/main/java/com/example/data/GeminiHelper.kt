package com.example.data

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ScanResultData(
    val title: String,
    val confidence: Int,
    val whatIsIt: String,
    val howItWorks: String,
    val components: List<String>,
    val interestingFacts: List<String>,
    val similarObjects: List<String>,
    val simpleExplanation: String,
    val advancedExplanation: String
)

object GeminiHelper {

    suspend fun analyzeImage(context: Context, bitmap: Bitmap?, objectNameHint: String? = null): ScanResultData = withContext(Dispatchers.IO) {
        // Simulate advanced AI visual identification with confidence and structured knowledge
        kotlinx.coroutines.delay(1200) // Realistic AI scanning delay
        getMockScanResult(objectNameHint ?: "Car Engine")
    }

    private fun getMockScanResult(query: String): ScanResultData {
        val q = query.lowercase()
        return when {
            q.contains("engine") || q.contains("car") -> ScanResultData(
                title = "Internal Combustion Engine",
                confidence = 98,
                whatIsIt = "A heat engine where combustion of fuel occurs with an oxidizer in a combustion chamber.",
                howItWorks = "1. Intake stroke draws fuel-air mixture.\n2. Compression stroke compresses mixture.\n3. Power stroke ignites fuel, driving piston.\n4. Exhaust stroke pushes out burnt gases.",
                components = listOf("Piston", "Cylinder", "Crankshaft", "Spark Plug", "Valve"),
                interestingFacts = listOf("Invented in the 19th century.", "Converts chemical energy into mechanical motion with high torque."),
                similarObjects = listOf("Electric Motor", "Jet Turbine", "Steam Engine"),
                simpleExplanation = "It's like a metallic heart that burns tiny bits of fuel to push metal rods and make wheels spin!",
                advancedExplanation = "Thermodynamic power cycle utilizing chemical enthalpy to mechanical work conversion via reciprocating pistons."
            )
            q.contains("tree") || q.contains("mango") || q.contains("plant") -> ScanResultData(
                title = "Mango Tree (Mangifera indica)",
                confidence = 96,
                whatIsIt = "A large evergreen fruit-bearing tree belonging to the cashew family Anacardiaceae.",
                howItWorks = "1. Roots absorb water and minerals.\n2. Leaves perform photosynthesis using sunlight.\n3. Flowers bloom and are pollinated.\n4. Sweet succulent drupe fruits develop.",
                components = listOf("Roots", "Trunk", "Canopy", "Leaves", "Drupe Fruit"),
                interestingFacts = listOf("Considered the national fruit in several countries.", "Can live for over 300 years and still produce fruit."),
                similarObjects = listOf("Neem Tree", "Apple Tree", "Citrus Tree"),
                simpleExplanation = "It's a giant leafy umbrella that drinks sunlight and water to grow delicious sweet fruits!",
                advancedExplanation = "Perennial dicotyledonous angiosperm engaging in C3 photosynthesis and zoichorous seed dispersal."
            )
            q.contains("laptop") || q.contains("computer") -> ScanResultData(
                title = "Portable Computing Laptop",
                confidence = 99,
                whatIsIt = "A battery-powered personal computer featuring a screen, keyboard, and trackpad in a clamshell form factor.",
                howItWorks = "1. Battery/AC power supplies voltage.\n2. CPU executes instructions from memory.\n3. GPU renders pixels onto the display.\n4. User interacts via keyboard and trackpad.",
                components = listOf("CPU", "RAM", "Display Panel", "Lithium Battery", "Keyboard"),
                interestingFacts = listOf("First commercially successful laptop was released in 1981.", "Contains billions of microscopic transistors on silicon chips."),
                similarObjects = listOf("Tablet PC", "Smartphone", "Desktop Workstation"),
                simpleExplanation = "It's a magical folding notebook made of glass and microchips that lets you talk to people anywhere in the world!",
                advancedExplanation = "Microprocessor-based von Neumann architecture system executing stored instruction programs across semiconductor memory."
            )
            else -> ScanResultData(
                title = query.replaceFirstChar { it.uppercase() },
                confidence = 95,
                whatIsIt = "A fascinating item identified by LensMind AI with rich real-world significance.",
                howItWorks = "1. Interaction starts.\n2. Core mechanism processes input.\n3. Functional output is generated.",
                components = listOf("Primary Core", "Structural Shell", "Interface Unit", "Support Element"),
                interestingFacts = listOf("Integral to everyday human innovation and discovery.", "Exhibits complex functional properties."),
                similarObjects = listOf("Related Object Alpha", "Related Object Beta"),
                simpleExplanation = "It's an amazing object that helps us understand how the world works around us!",
                advancedExplanation = "Exhibits systematic design principles governed by structural engineering and applied utility."
            )
        }
    }
}
