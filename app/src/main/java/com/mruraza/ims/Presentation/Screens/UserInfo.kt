package com.mruraza.ims.Presentation.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mruraza.ims.Presentation.ViewModel.UserInfoViewModel
import com.mruraza.ims.Utils.Objects.InputValidator.onlyPhone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesScreen(
    title: String,
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        UserInfoScreen(Modifier.padding(innerPadding))
    }
}
@Composable
fun UserInfoScreen(modifier: Modifier = Modifier, viewModel: UserInfoViewModel = hiltViewModel()) {
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EditableRow(
            label = "Name",
            value = userInfo.name,
            isEditing = isEditing["name"] == true,
            onValueChange = { viewModel.updateUserInfo(userInfo.copy(name = it)) },
            onEditClick = { viewModel.toggleEditing("name") }
        )

        EditableRow(
            label = "Address",
            value = userInfo.address,
            isEditing = isEditing["address"] == true,
            onValueChange = { viewModel.updateUserInfo(userInfo.copy(address = it)) },
            onEditClick = { viewModel.toggleEditing("address") }
        )

        EditableRow(
            label = "Phone",
            value = userInfo.phone,
            isEditing = isEditing["phone"] == true,
            onValueChange = { viewModel.updateUserInfo(userInfo.copy(phone = it.onlyPhone())) },
            onEditClick = { viewModel.toggleEditing("phone") }
        )
    }
}

@Composable
fun EditableRow(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    onEditClick: () -> Unit
) {
    var currentIcon by remember { mutableStateOf(Icons.Default.Edit) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.width(70.dp).padding(top = 14.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = { if (isEditing) onValueChange(it) },
            enabled = isEditing,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            onEditClick()
            currentIcon = if (currentIcon == Icons.Default.Edit) {
                Icons.Default.Check
            } else {
                Icons.Default.Edit
            }
        }) {
            Icon(currentIcon, contentDescription = "Edit")
        }
    }
}