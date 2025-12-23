package com.katharina.recipesapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val userName by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()

    var myUserName by remember { mutableStateOf(userName) }
    var myPassword by remember { mutableStateOf(password) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = myUserName,
                onValueChange = { myUserName = it },
                label = { Text("Username") },
            )

            OutlinedTextField(
                value = myPassword,
                onValueChange = { myPassword = it },
                label = { Text("Password") },
            )

            Button(
                onClick = { viewModel.updateCredentials(myUserName, myPassword) },
            ) {
                Text("Login")
            }

            Text(
                text = "Refresh Token: ${viewModel.refreshToken.collectAsState().value}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Access Token: ${viewModel.accessToken.collectAsState().value}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )

            Button(
                onClick = { viewModel.updateRecipe(42) },
            ) {
                Text("Update Recipe 42")
            }
        }
        if (viewModel.message.isNotEmpty()) {
            val toast = Toast.makeText(LocalContext.current, viewModel.message, Toast.LENGTH_LONG)
            toast.show()
        }
    }
}

// @Composable
// fun CredentialScreen(
//    userName: String,
//    password: String,
//    refreshToken: String,
//    accessToken: String,
//    modifier: Modifier = Modifier,
// ) {
//    Column(modifier = modifier) {
//        Text(
//            text = "Username: $userName",
//            modifier = modifier,
//        )
//        Text(
//            text = "Password: $password",
//            modifier = modifier,
//        )
//        Text(
//            text = "Refresh Token: $refreshToken",
//            modifier = modifier,
//        )
//        Text(
//            text = "Access Token: $accessToken",
//            modifier = modifier,
//        )
//    }
// }

// @Preview(showBackground = true)
// @Composable
// fun LoginScreenPreview() {
//    RecipesAppTheme {
//        LoginScreen(
//            userName = "Android",
//            password = "secret",
//            onLogin = { _, _ -> },
//        )
//    }
// }
//
// @Preview(showBackground = true)
// @Composable
// fun CredentialScreenPreview() {
//    RecipesAppTheme {
//        CredentialScreen(
//            userName = "Android",
//            password = "secret",
//            refreshToken = "aaa#123",
//            accessToken = "bbb#456",
//        )
//    }
// }
