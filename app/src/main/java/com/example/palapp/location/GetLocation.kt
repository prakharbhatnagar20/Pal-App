package com.example.palapp.location

import android.location.Geocoder
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Locale

private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

