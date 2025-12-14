package com.katharina.recipesapp.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.katharina.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun LoginScreenDummy(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
) {
    val userName by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()
    val refreshToken by viewModel.refreshToken.collectAsState()
    val accessToken by viewModel.accessToken.collectAsState()

    Column(modifier) {
        LoginScreen(userName = userName, password = password, onLogin = viewModel::onLogin)
        CredentialScreen(userName = userName, password = password, refreshToken = refreshToken, accessToken = accessToken)
        Button(
            onClick = viewModel::refreshTokens,
            modifier = modifier,
        ) {
            Text("Refresh Tokens")
        }
        Button(
            onClick = { viewModel.fetchRecipes() },
            modifier = modifier,
        ) {
            Text("Fetch Recipes")
        }
        Button(
            onClick = { viewModel.fetchRecipe42() },
            modifier = modifier,
        ) {
            Text("Fetch Recipe 42")
        }
    }
}

@Composable
fun LoginScreen(
    userName: String,
    password: String,
    onLogin: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var myUserName by remember(userName) { mutableStateOf(userName) }
    var myPassword by remember(password) { mutableStateOf(password) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = myUserName,
            onValueChange = { myUserName = it },
            label = { Text("Username") },
            modifier = modifier,
        )

        OutlinedTextField(
            value = myPassword,
            onValueChange = { myPassword = it },
            label = { Text("Password") },
            modifier = modifier,
        )

        Button(
            onClick = { onLogin(myUserName, myPassword) },
            modifier = modifier,
        ) {
            Text("Login")
        }
    }
}

@Composable
fun CredentialScreen(
    userName: String,
    password: String,
    refreshToken: String,
    accessToken: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Username: $userName",
            modifier = modifier,
        )
        Text(
            text = "Password: $password",
            modifier = modifier,
        )
        Text(
            text = "Refresh Token: $refreshToken",
            modifier = modifier,
        )
        Text(
            text = "Access Token: $accessToken",
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RecipesAppTheme {
        LoginScreen(
            userName = "Android",
            password = "secret",
            onLogin = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CredentialScreenPreview() {
    RecipesAppTheme {
        CredentialScreen(
            userName = "Android",
            password = "secret",
            refreshToken = "aaa#123",
            accessToken = "bbb#456",
        )
    }
}
