package com.example.palapp.ui.theme

import android.widget.Toast
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
import com.example.palapp.data.LoginResponse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import com.example.palapp.data.ProfileCreatedResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.util.Log

// Function to save data to SharedPreferences
fun saveData(context: Context, key: String, value: String) {
    val sharedPref = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putString(key, value)
        apply()
    }
}

// Function to retrieve data from SharedPreferences
fun getData(context: Context, key: String): String? {
    val sharedPref = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
    return sharedPref.getString(key, null)
}



@Composable
fun LogInPage(navController: NavController){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium ,color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = username, onValueChange = {username = it}, placeholder = { Text(text = "Username")})
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = {password = it}, placeholder = { Text(text = "Password")})
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            LoginButton(navController= navController,username, password) { message ->
                responseText = message
            }
            SignupButton(navController= navController,username, password){
                    message ->
                responseText = message

            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    if (responseText.isNotEmpty()){
        Snackbar(modifier = Modifier.padding(4.dp), action = {
            Button(onClick = { responseText = "" }) {
                Text("Retry")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }){
            Column {
                Text(text = "Try Again")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = responseText)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun SignupButton(navController: NavController,username: String, password: String,showMessage: (String) -> Unit){
    Button(onClick = {             // Start the login process
        GlobalScope.launch {
            val signInResponse = SignInUser(username, password)
            // Check if loginResponse is not null and show the message
            signInResponse?.let { showMessage(it.toString()) }
        }}) {
        Text(text = "Sign Up")
        
    }
}
@Composable
fun LoginButton(navController: NavController, username: String, password: String, showMessage: (String) -> Unit) {
    val context = LocalContext.current

    Button(
        onClick = {
            // Start the login process
            GlobalScope.launch {
                val loginResponse = loginUser(username, password)
                saveData(context, "access_token", loginResponse?.access.toString())

                saveData(context, "pk_id", loginResponse?.pk.toString())
                saveData(context, "user_name", loginResponse?.username.toString())
                val acstkn = getData(context, "access_token")
                Log.d("acccessss tokeeen", "${acstkn}")
                val pkno = getData(context, "pk_id")
                Log.d("pk tokeeen", "${pkno}")
                val usenme = getData(context, "user_nme")
                Log.d("use_n", "${usenme}")

                // Check if loginResponse is not null and show the message
                val profileCreatedResponse = loginResponse?.let { isProfileCreated(it.access) }

                if(loginResponse?.pk==null){
                    showMessage("Invalid Username or Password")
                }else {
                    if (profileCreatedResponse?.message.toString()=="No") {

                        withContext(Dispatchers.Main) {
                            // Navigate to the create profile page

                            navController.navigate("home_page_view")
                        }
                    }else{withContext(Dispatchers.Main){
                        navController.navigate("create_profile")
                    }

                    }
                }
            }
        }
    ) {
        Text(text = "Log In")
    }
}
suspend fun isProfileCreated(accessToken: String): ProfileCreatedResponse?{
    return withContext(Dispatchers.IO){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://corgi-strong-tetra.ngrok-free.app/api/haveprofile/")
            .header("Authorization", "Bearer $accessToken")
            .build()
        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val responseBody = response.peekBody(Long.MAX_VALUE).string()
            val yourResponseModel = Gson().fromJson(responseBody, ProfileCreatedResponse::class.java)
            yourResponseModel
        } catch (e: IOException) {
            null
        }
    }

}

@Composable
fun showMessage( message:String){
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


suspend fun loginUser(username: String, password: String): LoginResponse? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url("https://corgi-strong-tetra.ngrok-free.app/api/login/")
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.peekBody(Long.MAX_VALUE).string()
            val loginResponse = responseBody?.let {
                Gson().fromJson(it, LoginResponse::class.java)
            }
            loginResponse
        } catch (e: IOException) {
            null
        }
    }
}

@Preview
@Composable
fun LogInPagePreview(){
    PalAppTheme {

    }
}