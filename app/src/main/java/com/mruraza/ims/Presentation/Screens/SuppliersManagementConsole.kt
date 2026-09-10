package com.mruraza.ims.Presentation.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Presentation.ViewModel.SuppliersViewModel
import com.mruraza.ims.Utils.Consts.DummyData
import com.mruraza.ims.Utils.Consts.NavigationDestination
import com.mruraza.ims.Utils.Objects.InputValidator.formatAsDateKeepCursorAtEnd
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDate
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDouble
import com.mruraza.ims.Utils.Objects.InputValidator.onlyPhone
import com.mruraza.ims.Utils.Objects.Resources
import com.mruraza.ims.Utils.Objects.UtilsObject
import com.mruraza.ims.Utils.Screens.LoadingScreen
import com.mruraza.ims.ui.theme.Gray01
import com.mruraza.ims.ui.theme.Green07
import com.mruraza.ims.ui.theme.VeryLightGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliersManagementConsole(
    title: String,
    viewModel: SuppliersViewModel = hiltViewModel(),
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
                },
                actions = {
                    Box() {
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            IconButton(
                                onClick = {
                                    viewModel.updateIsSearchButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "بحث"
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.updateIsMoreVertButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More vertical 3 dots"
                                )
                            }
                            val isMoreVertButtonClicked by viewModel.isMoreVertButtonClicked.collectAsStateWithLifecycle()
                            if (isMoreVertButtonClicked) {
                                DropdownMenu(
                                    modifier = Modifier.padding(end = 16.dp),
                                    expanded = isMoreVertButtonClicked,
                                    onDismissRequest = {
                                        viewModel.updateIsMoreVertButtonClicked()
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Go To الرئيسية") },
                                        onClick = {
                                            navController.navigate(NavigationDestination.DASHBOARD)
                                            viewModel.updateIsMoreVertButtonClicked()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("AI Insights") },
                                        onClick = {
                                            // todo the AI insights
                                            viewModel.updateIsMoreVertButtonClicked()
                                        }
                                    )
                                }
                            }
                        }
                    }

                }
            )
        }
    ) { innerPadding ->
        SupplierScreenLayout(Modifier.padding(innerPadding))
    }
}

@Composable
private fun SupplierScreenLayout(
    modifier: Modifier,
    viewModel: SuppliersViewModel = hiltViewModel()
) {
    Column(modifier = modifier.fillMaxSize()) {
        val searchedText by viewModel.searchedText.collectAsStateWithLifecycle()
        val isSearchClicked by viewModel.isSearchButtonClicked.collectAsStateWithLifecycle()
        var selectedFilter by remember { mutableStateOf("") }
        val suppliersState by viewModel.allSupplier.collectAsStateWithLifecycle()
        when (suppliersState) {
            is Resources.Loading -> {
                LoadingScreen()
            }

            is Resources.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    "خطأ: ${(suppliersState as Resources.Error).throwable.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resources.Success -> {
                var totalSupplier: List<Supplier> =
                    (suppliersState as Resources.Success<List<Supplier>>).data

                if (isSearchClicked) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        value = searchedText,
                        onValueChange = { viewModel.updateSearchedText(it) },
                        label = { Text("بحث") },
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.updateSearchedText("") }
                            ) {
                                Icon(Icons.Default.Clear, "")
                            }
                        }
                    )
                }
                TopFilters(onSelect = { selectedFilter = it })
                Spacer(Modifier.height(16.dp))
                if (selectedFilter == ALL) totalSupplier = totalSupplier
                if (searchedText != "") totalSupplier =
                    totalSupplier.filter { it.name.lowercase().contains(searchedText.lowercase()) }
                SuppliersContent(
                    modifier = Modifier.weight(1f),
                    totalSupplier,
                    onSupplierUpdate = { viewModel.updateSuppliers(it) })
            }
        }
        AddSuppliers(
            onSuppliersAdd = { viewModel.addSupplier(it) }
        )
    }

}

@Composable
private fun AddSuppliers(
    onSuppliersAdd: (Supplier) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        SupplierInfoAdd(
            modifier = Modifier,
            onSaveClick = {
                onSuppliersAdd(it)
                openDialog.value = false
            },
            onDismissRequest = {
                openDialog.value = false
            }
        )
    }
    Box(
        Modifier.padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .background(Green07)
                .clickable {
                    openDialog.value = true
                }

        ) {
            Text("إضافة الموردون", modifier = Modifier.align(Alignment.Center))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupplierInfoAdd(
    modifier: Modifier,
    onSaveClick: (Supplier) -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = {}) {
        Card {
            var name by remember { mutableStateOf("") }
            var addContacts by remember { mutableStateOf(false) }
            var contacts by remember { mutableStateOf("") }
            var addAddress by remember { mutableStateOf(false) }
            var address by remember { mutableStateOf("") }

            Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "إضافة المورد",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "إضافة الاسم") }
                )
                Spacer(Modifier.height(16.dp))
                UtilsObject.leadingIconAndText(
                    modifier = Modifier.clickable { addContacts = !addContacts },
                    icon = if (!addContacts) Icons.Default.Add else Icons.Default.Remove,
                    text = "إضافة Contacts"
                )
                if (addContacts) {
                    OutlinedTextField(
                        value = contacts,
                        onValueChange = { contacts = it.onlyPhone() },
                        label = { Text(text = "إضافة Contacts") }
                    )
                }
                Spacer(Modifier.height(16.dp))
                UtilsObject.leadingIconAndText(
                    modifier = Modifier.clickable { addAddress = !addAddress },
                    icon = if (!addAddress) Icons.Default.Add else Icons.Default.Remove,
                    text = "إضافة العنوان"
                )
                if (addAddress) {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text(text = "إضافة Contacts") }
                    )
                }
                val updated_supplier =
                    Supplier(name = name, address = address, contactInfo = contacts)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onDismissRequest() }) {
                        Text(text = "إلغاء")
                    }

                    Button(
                        enabled = name != "",
                        onClick = {
                            onSaveClick(updated_supplier)
                            onDismissRequest()
                        }) {
                        Text(text = "حفظ")
                    }
                }
            }
        }
    }
}

@Composable
private fun SuppliersContent(
    modifier: Modifier = Modifier,
    totalSupplier: List<Supplier>,
    onSupplierUpdate: (Supplier) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(totalSupplier) { supplier ->
            SupplierContentLayout(
                Modifier,
                supplier,
                onSupplierUpdate = { onSupplierUpdate(it) }
            )
        }
    }
}


@Composable
private fun SupplierContentLayout(
    modifier: Modifier,
    supplier: Supplier,
    onSupplierUpdate: (Supplier) -> Unit
) {
    var callSupplierDetailLayout by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                callSupplierDetailLayout = true
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = supplier.name,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = supplier.contactInfo, style = TextStyle(color = Color.Gray))
                Spacer(Modifier.width(8.dp))
                Text(text = supplier.address, style = TextStyle(color = Color.Gray))
            }
        }
        Column {
            Text(
                text = supplier.due.toString(),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Red)
            )
            Text(
                text = supplier.totalPaid.toString(),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )
        }

    }
    if (callSupplierDetailLayout) {
        SupplierDetailedViewLayout(
            supplier,
            modifier = Modifier,
            onDismissRequest = { callSupplierDetailLayout = false },
            onSavaClick = {
                onSupplierUpdate(it)
                callSupplierDetailLayout = false
            }
        )
    }
    Spacer(Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun TopFilters(onSelect: (String) -> Unit) {
    var selected by remember { mutableStateOf(ALL) }
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        topChipsLayout(
            modifier = if (selected == ALL) Modifier.background(Gray01) else Modifier.background(
                VeryLightGrey
            ),
            ALL, onClick = {
                selected = ALL
                onSelect(ALL)
            }
        )
    }
}

@Composable
private fun topChipsLayout(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupplierDetailedViewLayout(
    supplier: Supplier,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSavaClick: (Supplier) -> Unit
) {
    val id by remember { mutableStateOf(supplier.id) }
    var name by remember { mutableStateOf(supplier.name) }
    var phone by remember { mutableStateOf(supplier.contactInfo) }
    var address by remember { mutableStateOf(supplier.address) }
    var due by remember { mutableStateOf(supplier.due.toString()) }
    var totalPaid by remember { mutableStateOf(supplier.totalPaid.toString()) }
    var dueDate by remember { mutableStateOf(TextFieldValue(supplier.dueDate)) }

    var isNameEditable by remember { mutableStateOf(false) }
    var isPhoneEditable by remember { mutableStateOf(false) }
    var isAddressEditable by remember { mutableStateOf(false) }
    var isDueEditable by remember { mutableStateOf(false) }
    var isTotalPaidEditable by remember { mutableStateOf(false) }
    var isDueDateEditable by remember { mutableStateOf(false) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Card {
            Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الموردون Details",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp).weight(1f)
                    )
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.History, contentDescription = "السجل")
                    }

                }


                // Name field
                EditableFieldRow(
                    label = "الاسم",
                    value = name,
                    onValueChange = { name = it },
                    editable = isNameEditable,
                    onEditClick = { isNameEditable = !isNameEditable }
                )

                // Phone field
                EditableFieldRow(
                    label = "الهاتف",
                    value = phone,
                    onValueChange = { phone = it.onlyPhone() },
                    editable = isPhoneEditable,
                    onEditClick = { isPhoneEditable = !isPhoneEditable },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Address field
                EditableFieldRow(
                    label = "العنوان",
                    value = address,
                    onValueChange = { address = it },
                    editable = isAddressEditable,
                    onEditClick = { isAddressEditable = !isAddressEditable }

                )

                // Due field
                EditableFieldRow(
                    label = "Due",
                    value = due,
                    onValueChange = {
                        due = it.onlyDouble()
                    },
                    editable = isDueEditable,
                    onEditClick = { isDueEditable = !isDueEditable },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Due Date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        value = dueDate,
                        onValueChange = { dueDate = it.formatAsDateKeepCursorAtEnd() },
                        label = { Text("Due التاريخ") },
                        readOnly = !isDueDateEditable,
                        enabled = isDueDateEditable,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    IconButton(onClick = { isDueDateEditable = !isDueDateEditable }) {
                        Icon(Icons.Default.Edit, contentDescription = "تعديل Due التاريخ")
                    }
                }

                // total paid field
                EditableFieldRow(
                    label = "الإجمالي Paid",
                    value = totalPaid,
                    onValueChange = {
                        totalPaid = it.onlyDouble()
                    },
                    editable = isTotalPaidEditable,
                    onEditClick = { isTotalPaidEditable = !isTotalPaidEditable },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismissRequest) {
                        Text("إلغاء")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val supplier = Supplier(
                            id = supplier.id,
                            name = name,
                            contactInfo = phone,
                            address = address,
                            due = due.toDouble(),
                            totalPaid = totalPaid.toDouble(),
                            dueDate = dueDate.text
                        )
                        onSavaClick(supplier)
                    }) {
                        Text("حفظ")
                    }
                }
            }
        }
    }
}


@Composable
private fun EditableFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    editable: Boolean,
    onEditClick: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            readOnly = !editable,
            enabled = editable,
            singleLine = true,
            keyboardOptions = keyboardOptions
        )
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "تعديل $label")
        }
    }
}



private const val ALL = "ALL"