package kzcs.taskmanagment
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import core.data.room.ApiFactory
import features.presentation._navigation.NavigationRoute

class MainActivity : ComponentActivity() {
    private var isReady = false

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen() //before super.onCreate()
        //splashScreen.setKeepOnScreenCondition { !isReady }
        super.onCreate(savedInstanceState)
        ApiFactory.init(this)
       enableEdgeToEdge()

        setContent {
            NavigationRoute()
            isReady = true
        }
    }
}