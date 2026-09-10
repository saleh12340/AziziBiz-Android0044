package com.mruraza.ims.Utils.Objects

import android.R.attr.enabled
import android.R.attr.type
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.toUri
import kotlinx.coroutines.sync.Mutex

object UtilsObject {

    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var final_uri = uri
        if(final_uri=="".toUri()) final_uri = "android.resource://com.mruraza.ims/drawable/product_image_not_available".toUri()
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Spinner(
        modifier: Modifier = Modifier,
        options: List<String>,
        onOptionSelected: (String) -> Unit = {},
        initialSelected : String = ""
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(options[0]) }
        if(initialSelected!="") selectedOption = initialSelected
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {}, // Don't allow manual editing
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = modifier
                    .menuAnchor() // Required for positioning
                    .fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize() // Optional styling
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }


    @Composable
    fun leadingIconAndText(
        modifier: Modifier = Modifier,
        icon: ImageVector,
        text: String
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = "icon",
            )
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }

}