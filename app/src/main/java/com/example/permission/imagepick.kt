package com.example.permission

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun imagepick() {
    var imageuri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var myimagelist: List<Uri> = listOf()
    var newlist = remember {
        mutableStateOf(emptyList<Bitmap>())
    }
    //comments

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageuri = it
        })
    val getmultiple = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {
            myimagelist = it
            it.forEach {
                Log.i("MainActivity", "Value = ${it}")
            }
            newlist.value = myimagelist.mapIndexed { _, uri ->
                        calculatebitmap(context,uri)}
            newlist.value.forEach {
                Log.i("mainActivity","Values as BItmao =${it}")
            }

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
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
            bitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(), contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .height(450.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Text(text = "Pick Your Images ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(5.dp))
//
        Button(
            onClick = {
                launcher.launch("image/")
            },
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(text = "Pick Image", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.padding(5.dp))
        LazyRow(
        modifier = Modifier
                .padding(10.dp).fillMaxWidth()){
            itemsIndexed(newlist.value){ _,image ->
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Button(onClick = {
            getmultiple.launch("image/")

        }) {
            Text(text = "Pick Multiple Image", fontSize = 22.sp)
        }
  
    }
}
fun calculatebitmap(ctx:Context,it:Uri): Bitmap {
    val soutc = ImageDecoder.createSource(ctx.contentResolver, it)
    return ImageDecoder.decodeBitmap(soutc)
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun getbitmaps(context: Context, list: List<Uri>) {
    val mybitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val newlist = list.map {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(context.contentResolver, it)
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
        ) {
            items(newlist) { image ->
                mybitmap.value = image
                mybitmap.value?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
//                    mybitmap.value = ImageDecoder.decodeBitmap(
//                        ImageDecoder.createSource(
//                            context.contentResolver,
//                            it
//                        )
//

            }
        }

    }
}