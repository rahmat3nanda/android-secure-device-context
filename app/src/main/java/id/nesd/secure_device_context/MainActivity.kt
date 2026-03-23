package id.nesd.secure_device_context.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.nesd.secure_device_context.SecureDeviceContext
import id.nesd.secure_device_context.enums.RiskLevel
import id.nesd.secure_device_context.enums.SecureContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val secureDeviceContext = SecureDeviceContext(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SecureContextTestScreen(secureDeviceContext)
                }
            }
        }
    }
}

@Composable
fun SecureContextTestScreen(secureDeviceContext: SecureDeviceContext) {
    val statusMap = remember { secureDeviceContext.status }
    val riskLevel = remember { secureDeviceContext.riskLevel }
    val contexts = remember { secureDeviceContext.contexts }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Secure Device Context Test",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Overall Risk Level", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = riskLevel.name,
                        color = when (riskLevel) {
                            RiskLevel.SECURE -> Color(0xFF4CAF50)
                            RiskLevel.WARNING -> Color(0xFFFF9800)
                            RiskLevel.CRITICAL -> Color(0xFFF44336)
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Detected Contexts", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (contexts.isEmpty()) {
                        Text(text = "None (Device is secure)")
                    } else {
                        contexts.forEach { context ->
                            Text(text = "• ${context.name}")
                        }
                    }
                }
            }
        }

        item {
            Text(text = "Raw Status Map", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(statusMap.entries.toList()) { entry ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = entry.key, fontWeight = FontWeight.Medium)
                Text(text = entry.value.toString())
            }
        }
    }
}
