package ramzi.eljabali.justjog

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
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
import androidx.compose.material3.SnackbarHostState
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ramzi.eljabali.justjog.loactionservice.ForegroundService
import ramzi.eljabali.justjog.model.permissioninformation.LocationPermissionTextProvider
import ramzi.eljabali.justjog.model.permissioninformation.NotificationPermissionTextProvider
import ramzi.eljabali.justjog.ui.design.JustJogTheme
import ramzi.eljabali.justjog.ui.views.JoggingFAB
import ramzi.eljabali.justjog.ui.navigation.JustJogNavigation
import ramzi.eljabali.justjog.ui.views.BottomNavigationView
import ramzi.eljabali.justjog.ui.views.PermissionDialogBox
import ramzi.eljabali.justjog.viewmodel.StatisticsViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val statisticsViewModel: StatisticsViewModel by viewModel()
        val dialogQueue = statisticsViewModel.visiblePermissionDialogQueue

        askForPermission(
            Permissions.list
        ) { permission: String, isGranted: Boolean ->
            statisticsViewModel.onPermissionResult(
                permission,
                isGranted
            )
        }

        setContent {
            val navController = rememberNavController()
            JustJogTheme(true) {
                Scaffold(
                    bottomBar = { BottomNavigationView(navController) },
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
        for (permission in Permissions.list) {
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
                }
            }
        }
    }

    // You are asking for permission
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun askForPermission(
        permissionsList: List<String>,
        onPermissionResult: (String, Boolean) -> Unit
    ) {
        val permissionRequestResult = registerForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissionsMap ->
            permissionsMap.keys.forEach { permission ->
                if (permissionsMap[permission] == true) {
                    Log.d("Log.d", "$permission has been granted")
                    onPermissionResult(permission, true)
                } else {
                    Log.d("Log.d", "$permission has been denied")
                    onPermissionResult(permission, false)
                }
            }
        }
        permissionRequestResult.launch(permissionsList.toTypedArray())
    }
}

//Making an open detailed settings request
private fun Activity.openSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

