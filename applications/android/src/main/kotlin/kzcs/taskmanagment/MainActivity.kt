package kzcs.taskmanagment

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.kzcse.hilsadetector.feature._core.presentation.AppTheme
import core.data.room.ApiFactory
import features.presentation._navigation.NavigationRoute

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiFactory.init(this)
//        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavigationRoute()
            }
        }
    }
}