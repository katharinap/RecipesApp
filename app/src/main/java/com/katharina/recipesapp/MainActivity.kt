package com.katharina.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.katharina.recipesapp.data.credentials.CredentialsDataStore
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun MyScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStore = remember(context) { CredentialsDataStore(context) }

    val userName = dataStore.userNameFlow().collectAsState(initial = "")
    val password = dataStore.passwordFlow().collectAsState(initial = "")
    val refreshToken = dataStore.refreshTokenFlow().collectAsState(initial = "")

    Column(modifier) {
        LoginScreen(userName = userName.value, password = password.value, onLogin = { userName, password ->
            coroutineScope.launch {
                dataStore.updateUserName(userName)
                dataStore.updatePassword(password)
            }
        })
        CredentialScreen(userName = userName.value, password = password.value, refreshToken = refreshToken.value)
    }
}

@Composable
fun LoginScreen(
    userName: String,
    password: String,
    onLogin: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var myUserName by remember { mutableStateOf("") }
    if(myUserName.isEmpty()) myUserName = userName

    var myPassword by remember { mutableStateOf("") }
    if(myPassword.isEmpty()) myPassword = password

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
        )
    }
}
