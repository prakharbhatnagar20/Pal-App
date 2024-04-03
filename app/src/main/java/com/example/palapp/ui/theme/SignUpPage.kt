package com.example.palapp.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.palapp.data.LoginResponse
import com.example.palapp.data.SignInResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@Composable
fun SignUpPage(navController: NavController,){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium ,color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = username, onValueChange = {username = it}, placeholder = { Text(text = "Username") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = {email = it}, placeholder = { Text(text = "Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = {password = it}, placeholder = { Text(text = "Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            LoginButton(navController= navController,username = username, password = password ) {
                    message ->
                responseText = message

            }
            SignupButton(navController= navController,username = username, password = password){
                    message1 ->
                responseText = message1
            }

        }

    }
    if (responseText.isNotEmpty()){
        Snackbar(modifier = Modifier.padding(4.dp), action = {
            Button(onClick = { responseText = "" }) {
                Text("LogIn")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }){
            Column {
                Text(text = "Already Exist")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = responseText)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

suspend fun SignInUser(username: String, password: String): SignInResponse? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url("https://corgi-strong-tetra.ngrok-free.app/api/register/")
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.peekBody(Long.MAX_VALUE).string()
            Log.d("API_RESPONSE", "Response: $responseBody")
            val signInResponse = responseBody?.let {
                Gson().fromJson(it, SignInResponse::class.java)
            }
            signInResponse
        } catch (e: IOException) {
            null
        }
    }
}

@Preview
@Composable
fun SignUpPagePreview(){
   
}