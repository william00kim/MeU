package org.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                ArtSpaceApp()
            }
        }
    }
}

@Composable
fun ArtSpaceApp() {
    var currentPhotoNumber by remember{ mutableStateOf(3) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color =MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when(currentPhotoNumber){
                1 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).width(350.dp).height(350.dp)
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_1),
                            contentDescription = stringResource(R.string.Photo1),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo1),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber++ },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
                2 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).width(350.dp).height(350.dp)
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_2),
                            contentDescription = stringResource(R.string.Photo2),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo2),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber-- },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber++ },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
                3 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).height(400.dp).width(R.drawable.number_3.dp ),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_3),
                            contentDescription = stringResource(R.string.Photo3),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                ),
                            alignment = Alignment.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo3),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber-- },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber++ },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
                4 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).width(350.dp).height(350.dp)
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_4),
                            contentDescription = stringResource(R.string.Photo4),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo4),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber-- },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber++ },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
                5 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).width(350.dp).height(350.dp)
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_5),
                            contentDescription = stringResource(R.string.Photo5),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo5),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber-- },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber++ },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
                6 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).width(350.dp).height(350.dp)
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_6),
                            contentDescription = stringResource(R.string.Photo6),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo6),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber-- },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber++ },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
                7 -> {
                    Box(
                        modifier = Modifier.border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        ).shadow(
                            elevation = 20.dp
                        ).width(350.dp).height(350.dp)
                    ){
                        Image(
                            painter = painterResource(R.drawable.number_7),
                            contentDescription = stringResource(R.string.Photo7),
                            modifier = Modifier.wrapContentWidth().wrapContentHeight()
                                .border(
                                    width = 40.dp,
                                    color = Color.White
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Box(
                        Modifier.shadow(
                            elevation = 3.dp
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Photo7),
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "증산역에서 (2022)" ,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = { currentPhotoNumber-- },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Previous")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { currentPhotoNumber },
                            modifier = Modifier.width(180.dp)
                        ){
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}