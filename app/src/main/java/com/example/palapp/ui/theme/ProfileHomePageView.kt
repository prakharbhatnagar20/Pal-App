package com.example.palapp.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.palapp.data.LoginResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

@Composable
fun ProfileHomePageView(navController: NavController){
    var username by remember { mutableStateOf("") }
    var bio by remember{mutableStateOf("")}
    var location by remember {
        mutableStateOf("")
    }
    var locationValid by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Create Profile",style = MaterialTheme.typography.headlineMedium ,color = MaterialTheme.colorScheme.primary)
        TextField(value = username, onValueChange = {username=it}, placeholder = {Text(text="Enter your name")} )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = bio, onValueChange = {bio= it}, placeholder = { Text(text = "Enter your bio")})
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = location, onValueChange = {location= it}, placeholder = { Text(text = "Enter your location")})
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = locationValid, onValueChange = {locationValid= it}, placeholder = { Text(text = "Enter location validity")})
        Spacer(modifier = Modifier.height(16.dp))
        CreateProfileButton(name = username,bio=bio, location = location, locationValid=locationValid )
    }


}
private fun validateTextField(textValue: String): Boolean {
    if (textValue.isBlank()) {
        // Text field is empty, show error
        // Handle error state accordingly
        // For example, you can display a snackbar or set a boolean flag to true
        return false
        // Here I'm just setting a boolean flag for simplicity
        // You can use a more appropriate way to handle the error state in your app

    } else {
        // Text field is not empty, proceed with the submission
        return true
        // Proceed with form submission
    }
}


@Composable
fun CreateProfileButton(name: String, bio:String, location:String, locationValid: String){
    val context = LocalContext.current

    Button(onClick = {
        if(validateTextField(name)&& validateTextField(bio)&& validateTextField(location)&& validateTextField(locationValid.toString())) {
            GlobalScope.launch {
                CreateUserProfile(name = name, bio = bio, location = location, locationValid = locationValid, context = context)

            }
        }
    }) {
        Text(text = "Create")
    }
}

suspend fun CreateUserProfile(name: String, bio:String, location:String, locationValid: String, context: Context){
    val client = OkHttpClient()
    val locationvalid = locationValid.toInt()
    val acstkn = getData(context, "access_token")
    val usenme = getData(context, "user_nme")
    val pkno = getData(context, "pk_id")
    val mediaType = MediaType.parse("application/json")
    val body = RequestBody.create(mediaType, "{\"username\":\"${usenme}\",\"profile\": {\"name\": \"${name}\",\"user\": \"${pkno}\",\"bio\": \"${bio}\",\"location\": \"${location}\",\"location_validity\": ${locationvalid}}}")
    val request = Request.Builder()
        .url("https://corgi-strong-tetra.ngrok-free.app/api/profile/update/")
        .put(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer ${acstkn}")
        .build()
    val response = client.newCall(request).execute()


}

