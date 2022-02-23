package com.example.permission

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.permission.ui.theme.PermissionTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    val myviewmodel by viewModels<viewmodel>()
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PermissionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val cameraPermissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.RECORD_AUDIO
                    )


                    )
                    val lifecycle = LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifecycle, effect ={
                        val obsrv = LifecycleEventObserver { _,event ->
                            if(event == Lifecycle.Event.ON_RESUME){
                                cameraPermissionState.launchMultiplePermissionRequest()
                            }


                        }
                        lifecycle.lifecycle.addObserver(obsrv)
                        onDispose {
                            lifecycle.lifecycle.removeObserver(obsrv)
                        }
                    } )
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                        cameraPermissionState.permissions.forEach { perm ->
                            when(perm.permission){
                                Manifest.permission.CAMERA ->{
                                    when{
                                        perm.hasPermission ->{
                                            Text(text = "CAmera Permission ${perm.hasPermission}")}
                                        perm.shouldShowRationale->{
                                            Text(text = "Rational Permission ${perm.hasPermission}")
                                        }
                                        !perm.hasPermission && !perm.shouldShowRationale ->{
                                            Text(text = "CAmera Permission Declined")
                                        }
                                    }
                                }
                                Manifest.permission.RECORD_AUDIO ->{
                                    when{
                                        perm.hasPermission ->{
                                            Text(text = "Record Audio Permission ${perm.hasPermission}")
                                        }
                                        perm.shouldShowRationale->{
                                            Text(text = "Show Rational Permission ")
                                        }
                                        !perm.hasPermission && !perm.shouldShowRationale ->{
                                            Text(text = "Permanently Declined")
                                        }
                                    }
                                }
                                Manifest.permission.ACCESS_COARSE_LOCATION ->{
                                    when{
                                        perm.hasPermission ->{
                                            Text(text = "Acces Location Permission ${perm.hasPermission}")
                                        }
                                        perm.shouldShowRationale->{
                                            Text(text = "CAmera Permission ${perm.shouldShowRationale}")
                                        }
                                        !perm.hasPermission && !perm.shouldShowRationale ->{
                                            Text(text = "Permanently declined Permission ")
                                        }
                                    }
                                }
                            }

                        }
                    }
                    val mypermissions = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = { per ->
                            per.forEach { s, b ->
                                Log.i("MainActivity", "My permisiions $s --> $b")
                            }
                            myviewmodel.setstate(
                                per[Manifest.permission.CAMERA] == true
                            )
                            Log.i("MainActivity","Logged value ${myviewmodel.mystate}")
                        })
//                    if (myviewmodel.mystate){
//                        SimpleCameraPreview()
//                    }else{
//                        imagepick()
//                    }

//                    LaunchedEffect(key1 = true) {
//                        mypermissions.launch(
//                            arrayOf(
//                                Manifest.permission.CAMERA,
//                                Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.RECORD_AUDIO
//                            )
//                        )
//                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PermissionTheme {
        Greeting("Android")
    }
}