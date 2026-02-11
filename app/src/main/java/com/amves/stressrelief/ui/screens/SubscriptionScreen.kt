package com.amves.stressrelief.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amves.stressrelief.R

/**
 * SubscriptionScreen is a basic paywall placeholder for premium features.
 */
@Composable
fun SubscriptionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.premium_subscription),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.unlock_premium),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "• AI-powered stress predictions\n• Advanced analytics\n• Personalized recommendations\n• Ad-free experience",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                Text(
                    text = "$9.99 / month",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Button(
            onClick = { /* TODO: Implement subscription logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.subscribe))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = { /* TODO: Restore purchases */ }) {
            Text("Restore Purchases")
        }
    }
}
