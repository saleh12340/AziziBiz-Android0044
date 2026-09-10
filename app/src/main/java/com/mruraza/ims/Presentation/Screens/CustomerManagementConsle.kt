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
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.TextRange
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
import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Presentation.ViewModel.CustomerManagementViewModel
import com.mruraza.ims.Utils.Consts.DummyData
import com.mruraza.ims.Utils.Consts.NavigationDestination
import com.mruraza.ims.Utils.Objects.InputValidator.formatAsDateKeepCursorAtEnd
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDate
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDouble
import com.mruraza.ims.Utils.Objects.InputValidator.onlyPhone
import com.mruraza.ims.Utils.Objects.Resources
import com.mruraza.ims.Utils.Screens.LoadingScreen
import com.mruraza.ims.ui.theme.Gray01
import com.mruraza.ims.ui.theme.Green07
import com.mruraza.ims.ui.theme.VeryLightGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customerManagementConsole(
    title: String,
    customerManagementViewModel: CustomerManagementViewModel = hiltViewModel(),
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
                                    customerManagementViewModel.updateIsSearchButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "بحث"
                                )
                            }
                            IconButton(
                                onClick = {
                                    customerManagementViewModel.updateIsMoreVertButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More vertical 3 dots"
                                )
                            }
                            val isMoreVertButtonClicked by customerManagementViewModel.isMoreVertButtonClicked.collectAsStateWithLifecycle()
                            if (isMoreVertButtonClicked) {
                                DropdownMenu(
                                    modifier = Modifier.padding(end = 16.dp),
                                    expanded = isMoreVertButtonClicked,
                                    onDismissRequest = {
                                        customerManagementViewModel.updateIsMoreVertButtonClicked()
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Go To الرئيسية") },
                                        onClick = {
                                            navController.navigate(NavigationDestination.DASHBOARD)
                                            customerManagementViewModel.updateIsMoreVertButtonClicked()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("AI Insights") },
                                        onClick = {
                                            // todo it
                                            customerManagementViewModel.updateIsMoreVertButtonClicked()
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
        CustomerScreenLayout(Modifier.padding(innerPadding))
    }
}

@Composable
private fun CustomerScreenLayout(
    modifier: Modifier,
    viewModel: CustomerManagementViewModel = viewModel()
) {
    Column(modifier = modifier.fillMaxSize()) {
        val searchedText by viewModel.searchedText.collectAsStateWithLifecycle()
        val isSearchClicked by viewModel.isSearchButtonClicked.collectAsStateWithLifecycle()
        var selectedFilter by remember { mutableStateOf("") }
        val customerState by viewModel.allCustomers.collectAsStateWithLifecycle()
        when (customerState) {
            is Resources.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    "خطأ: ${(customerState as Resources.Error).throwable.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resources.Loading -> {
                LoadingScreen()
            }

            is Resources.Success -> {
                var totalCustomers = (customerState as Resources.Success<List<Customer>>).data
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
                if (selectedFilter == ALL) totalCustomers = totalCustomers
                if (searchedText != "") totalCustomers =
                    totalCustomers.filter { it.name.lowercase().contains(searchedText.lowercase()) }
                CustomerContent(modifier = Modifier.weight(1f), totalCustomers, onCustomerUpdate = {
                    viewModel.updateCustomer(it)
                })
            }
        }
        AddCustomer(
            onCustomersAdd = { viewModel.addCustomer(it) }
        )
    }

}

@Composable
private fun AddCustomer(
    onCustomersAdd: (Customer) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        addCustomerLayout(
            modifier = Modifier,
            onDismissRequest = { openDialog.value = false },
            onSavaClick = {
                onCustomersAdd(it)
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
            Text("إضافة العميل", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addCustomerLayout(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    onSavaClick: (Customer) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dueAmount by remember { mutableStateOf("") }
    val errors = mutableMapOf<String, String>()
    if (name.isEmpty()) errors["name"] = "الاسم is required"
    if (contactInfo.isNotEmpty() && contactInfo.length != 10) errors["contactInfo"] =
        "Invalid Contact Info"

    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Card {
            Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم") },
                    isError = errors.containsKey("name"),
                    supportingText = {
                        if (errors.containsKey("name")) {
                            Text(errors["name"]!!)
                        }
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = contactInfo,
                    onValueChange = { contactInfo = it.onlyPhone() },
                    label = { Text("Contact Info") },
                    isError = errors.containsKey("contactInfo"),
                    supportingText = {
                        if (errors.containsKey("contactInfo")) {
                            Text(errors["contactInfo"]!!)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("العنوان") }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = dueAmount,
                    onValueChange = { dueAmount = it.onlyDouble() },
                    label = { Text("Due المبلغ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onDismissRequest() }
                    ) {
                        Text("إلغاء")
                    }
                    Button(
                        enabled = errors.isEmpty(),
                        onClick = {
                            val customer = Customer(
                                name = name,
                                phone = contactInfo,
                                address = address,
                                due = if(dueAmount!="")dueAmount.toDouble() else 0.0
                            )
                            onDismissRequest()
                            onSavaClick(customer)
                        }
                    ) {
                        Text("حفظ")
                    }
                }
            }
        }
    }

}

@Composable
private fun CustomerContent(
    modifier: Modifier = Modifier,
    totalCustomers: List<Customer>,
    onCustomerUpdate: (Customer) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(totalCustomers) { customer ->
            customerContentLayout(
                Modifier,
                customer,
                onCustomerUpdate = { onCustomerUpdate(it) }
            )
        }
    }
}


@Composable
private fun customerContentLayout(
    modifier: Modifier,
    customer: Customer,
    onCustomerUpdate: (Customer) -> Unit
) {
    var callCustomerDetailLayout by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                callCustomerDetailLayout = true
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = customer.name,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = customer.phone, style = TextStyle(color = Color.Gray))
                Spacer(Modifier.width(8.dp))
                Text(text = customer.address, style = TextStyle(color = Color.Gray))
            }
        }
        Text(
            text = customer.due.toString(),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
    }
    if (callCustomerDetailLayout) {
        customerDetailedViewLayout(
            customer,
            modifier = Modifier,
            onDismissRequest = { callCustomerDetailLayout = false },
            onSavaClick = {
                onCustomerUpdate(it)
                callCustomerDetailLayout = false
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
private fun customerDetailedViewLayout(
    customer: Customer,
    modifier: Modifier = Modifier, // changed to default Modifier
    onDismissRequest: () -> Unit,
    onSavaClick: (Customer) -> Unit
) {
    var name by remember { mutableStateOf(customer.name) }
    var phone by remember { mutableStateOf(customer.phone) }
    var address by remember { mutableStateOf(customer.address) }
    var due by remember { mutableStateOf(customer.due.toString()) }
    var dueDate by remember { mutableStateOf(TextFieldValue(customer.dueDate)) }

    var isNameEditable by remember { mutableStateOf(false) }
    var isPhoneEditable by remember { mutableStateOf(false) }
    var isAddressEditable by remember { mutableStateOf(false) }
    var isDueEditable by remember { mutableStateOf(false) }
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
                        text = "العميل Details",
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

                // Due date
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

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismissRequest) {
                        Text("إلغاء")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        enabled = name != "",
                        onClick = {
                            val updatedCustomer = Customer(
                                id = customer.id,
                                name = name,
                                phone = phone,
                                address = address,
                                due = due.toDouble(),
                                dueDate = dueDate.text
                            )
                            onDismissRequest()
                            onSavaClick(updatedCustomer)
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