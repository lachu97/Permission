package com.example.permission

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun imagepick() {
    var imageuri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageuri = it
        })
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pick Your Images ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(15.dp))
        imageuri?.let {
            if(Build.VERSION.SDK_INT < 28){
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            }else {
                val source = ImageDecoder.createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .height(500.dp)
                    )
            }
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Button(onClick = {
            launcher.launch("image/")
        }) {
            Text(text = "Pick Image", fontSize = 25.sp)
        }
    }
}