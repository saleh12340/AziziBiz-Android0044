package com.mruraza.ims.Utils.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle

@Composable
fun LoadingScreen(){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}