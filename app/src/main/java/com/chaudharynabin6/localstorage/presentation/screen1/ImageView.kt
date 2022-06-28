package com.chaudharynabin6.localstorage.presentation.screen1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaudharynabin6.localstorage.R

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ImageViewTest0(

) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(R.drawable.image_1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillHeight

                )
                Image(
                    painter = painterResource(R.drawable.image_0),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((200.dp))
                    , contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}


@Composable
fun ImageViewTest1(
    context : Context
) {
//    https://getridbug.com/android/how-to-convert-a-drawable-to-a-bitmap/

    val option = BitmapFactory.Options()
    option.inPreferredConfig = Bitmap.Config.ARGB_8888
    val image1Bitmap by remember {
       mutableStateOf( BitmapFactory.decodeResource(context.resources,R.drawable.image_1,option))
    }

    val image2Bitmap by remember {
        mutableStateOf( BitmapFactory.decodeResource(context.resources,R.drawable.image_0,option))
    }

    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                   bitmap = image1Bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillHeight

                )
                Image(
                    bitmap =image2Bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((200.dp))
                    , contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}