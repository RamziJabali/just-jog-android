package ramzi.eljabali.justjog

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ramzi.eljabali.justjog.loactionservice.ForegroundService
import ramzi.eljabali.justjog.util.permissions
import ramzi.eljabali.justjog.ui.design.JustJogTheme
import ramzi.eljabali.justjog.ui.views.JoggingFAB
import ramzi.eljabali.justjog.ui.navigation.JustJogNavigation
import ramzi.eljabali.justjog.ui.views.BottomNavigationView
import ramzi.eljabali.justjog.viewmodel.StatisticsViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val statisticsViewModel: StatisticsViewModel by viewModel()
        setContent {
            val navController = rememberNavController()
            JustJogTheme(true) {
                Scaffold(
                    bottomBar = { BottomNavigationView(navController) },
                    floatingActionButton = {
                        JoggingFAB {
                            Intent(applicationContext, ForegroundService::class.java).also {
                                it.action = ForegroundService.Actions.START.name
                                startService(it)
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.EndOverlay,
                ) { contentPadding ->
                    Log.d("Scaffold", "Content Padding $contentPadding")
                    JustJogNavigation(navController, statisticsViewModel)
                }
            }
        }
    }

    // ~~~ PERMISSION Handler ~~~
    //Best way to check which permission status you are on
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermissionStatus() {
        for (permission in permissions.list) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("Log.d", "$permission Permission Already Granted")
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    Log.d("Log.d", "Showing Request Rationale for $permission")
                }

                else -> {
                    Log.d("Log.d", "Asking user to grant permission for $permission")
                    askForPermission(permission)
                }
            }
        }
    }

    // You are asking for permission
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun askForPermission(permission: String): Boolean {
        var isGrantedPermission = false
        val permissionRequestResult = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Log.d", "$permission has been granted")
            } else {
                Log.d("Log.d", "$permission has been denied")
            }
            isGrantedPermission = isGranted
        }
        permissionRequestResult.launch(permission)
        return isGrantedPermission
    }

    //Making an open detailed settings request
    private fun Activity.penSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).also(::startActivity)
    }
}
