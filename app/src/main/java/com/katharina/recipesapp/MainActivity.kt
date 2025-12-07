package com.katharina.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
) {
    val userName by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()
    val refreshToken by viewModel.refreshToken.collectAsState()
    val accessToken = viewModel.accessToken

    Column(modifier) {
        LoginScreen(userName = userName, password = password, onLogin = viewModel::onLogin)
        CredentialScreen(userName = userName, password = password, refreshToken = refreshToken, accessToken = accessToken)
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
