package com.mruraza.ims.Presentation.Screens


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.PurchaseBill
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Presentation.ViewModel.BillingViewModel
import com.mruraza.ims.Presentation.ViewModel.PurchaseBillViewModel
import com.mruraza.ims.Utils.Objects.DateHelper
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDigits
import com.mruraza.ims.Utils.Objects.InputValidator.onlyPhone
import com.mruraza.ims.Utils.Objects.Resources
import com.mruraza.ims.Utils.Objects.UtilsObject
import com.mruraza.ims.ui.theme.Green07
import com.mruraza.ims.ui.theme.VeryLightGrey
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseBilling(
    title: String,
    billViewModel: PurchaseBillViewModel = hiltViewModel(),
    activity: Activity,
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
                        val context = LocalContext.current
                        val selectedSupplier by billViewModel.selectedSupplier.collectAsStateWithLifecycle()
                        val discount by billViewModel.discount.collectAsStateWithLifecycle()
                        val bill by billViewModel.bill.collectAsStateWithLifecycle()
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            IconButton(
                                enabled = selectedSupplier != null && bill.isNotEmpty(),
                                onClick = {
                                    saveComposableAsPdf(
                                        activity = activity,
                                        fileName = "Bill_${System.currentTimeMillis()}",
                                    ) {
                                        billPrintLayout(
                                            supplier = selectedSupplier!!,
                                            bill = bill,
                                            discount = discount,
                                            modifier = Modifier
                                        )
                                    }

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Print,
                                    contentDescription = "Print"
                                )
                            }
                        }
                    }

                }
            )
        }
    ) { innerPadding ->
        BillingScreenLayout(
            Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun BillingScreenLayout(
    modifier: Modifier,
    billViewModel: PurchaseBillViewModel = hiltViewModel()
) {
    Column(modifier = modifier.fillMaxSize()) {
        val context = LocalContext.current
        val goodsState by billViewModel.allGoods.collectAsStateWithLifecycle()
        val supplierState by billViewModel.allSuppliers.collectAsStateWithLifecycle()
        var GoodsList: List<Goods> by remember { mutableStateOf(listOf()) }
        var SuppliersList: List<Supplier> by remember { mutableStateOf(listOf()) }
        var selectedSupplier: Supplier? = null
        val selectedGoodsWithQuantity = remember { mutableStateListOf<Pair<Goods, Int>>() }


        when (supplierState) {
            is Resources.Loading -> {}
            is Resources.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    "Error: ${(supplierState as Resources.Error).throwable.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resources.Success -> {
                SuppliersList = (supplierState as Resources.Success<List<Supplier>>).data
            }
        }
        when (goodsState) {
            is Resources.Loading -> {}
            is Resources.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    "Error: ${(goodsState as Resources.Error).throwable.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resources.Success -> {
                GoodsList = (goodsState as Resources.Success<List<Goods>>).data
            }
        }

        BillBody(
            modifier = Modifier.weight(1f),
            goods = selectedGoodsWithQuantity,
            suppliers = SuppliersList,
            onSupplierSelect = { it: Supplier ->
                selectedSupplier = it
                billViewModel.updateSupplier(it)
            },
            onSupplierAdd = { billViewModel.addSupplier(it) },
            onDelete = { it: Goods ->
                selectedGoodsWithQuantity.removeAll { index ->
                    index.first == it
                }
                billViewModel.updateBill(selectedGoodsWithQuantity)
            },
            onUpdateQuantity = { (id, newQty) ->
                val idx = selectedGoodsWithQuantity.indexOfFirst { it.first.id == id }
                if (idx != -1) {
                    val good = selectedGoodsWithQuantity[idx].first
                    selectedGoodsWithQuantity[idx] =
                        good to newQty  // update ONLY the second of the pair
                }
                billViewModel.updateBill(selectedGoodsWithQuantity)
            }
        )
        val totDiscount by billViewModel.discount.collectAsStateWithLifecycle()
        BillFooter(
            modifier = Modifier,
            bill = selectedGoodsWithQuantity,
            onItemSelect = { it ->
                val index =
                    selectedGoodsWithQuantity.indexOfFirst { idx -> idx.first.id == it.first.id }
                if (index != -1) {
                    val currentPair = selectedGoodsWithQuantity[index]
                    selectedGoodsWithQuantity[index] =
                        currentPair.copy(second = currentPair.second + it.second)
                } else {
                    selectedGoodsWithQuantity.add(it)
                }
                billViewModel.updateBill(selectedGoodsWithQuantity)
            },
            onBillSave = {
                if (selectedSupplier == null) {
                    Toast.makeText(
                        context,
                        "Please select a customer",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (selectedGoodsWithQuantity.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please select some goods",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val bill = PurchaseBill(
                        supplier = selectedSupplier,
                        good = selectedGoodsWithQuantity,
                        discount = totDiscount,
                        date = DateHelper.randomDateWithin1Year() // todo need to change
                    )
                    billViewModel.addPurchaseBill(bill)
                    Toast.makeText(
                        context,
                        "Bill saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                billViewModel.updateBill(selectedGoodsWithQuantity)
            },
            allItems = GoodsList,
            suppliers = SuppliersList,
            onSuppliersSave = { billViewModel.addSupplier(it) },
            onGoodsSave = { billViewModel.addGoods(it) },
            onDiscountChange = { billViewModel.updateDiscount(it) }
        )

    }
}


@Composable
private fun BillHeaderSupplier(
    suppliers: List<Supplier>,
    selectedSupplier: Supplier?,   // <-- state comes from parent
    onSupplierSelect: (Supplier) -> Unit,
    supplierName: String,
    onTextChanged: (String) -> Unit,
    onSupplierAdd: (Supplier) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .background(VeryLightGrey)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text("Supplier")
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SearchableDropdown(
                        items = suppliers,
                        selectedText = supplierName,
                        onTextChanged = { onTextChanged(it) },
                        onItemSelected = { onSupplierSelect(it) }
                    )
                }
                IconButton(onClick = { openDialog.value = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }
            if (selectedSupplier != null && selectedSupplier.name.isNotBlank()) {
                val contactInfo =
                    selectedSupplier.contactInfo.takeIf { it.isNotBlank() } ?: "Not Available"
                val address = selectedSupplier.address.takeIf { it.isNotBlank() } ?: "Not Available"
                Text(text = "contact : $contactInfo")
                Text(text = "address : $address")
            }
        }
    }

    if (openDialog.value) {
        SupplierInfoAdd(
            onSaveClick = {
                openDialog.value = false
                onSupplierAdd(it)
            },
            onDismissRequest = {
                openDialog.value = false
            }
        )
    }

}

@Composable
private fun BillBody(
    goods: List<Pair<Goods, Int>>,
    suppliers: List<Supplier>,
    onSupplierAdd: (Supplier) -> Unit,
    onSupplierSelect: (Supplier) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (Goods) -> Unit,
    onUpdateQuantity: (Pair<Long, Int>) -> Unit,
) {
    var supplierQuery by rememberSaveable { mutableStateOf("") }
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            BillHeaderSupplier(
                suppliers = suppliers,
                onSupplierSelect = {
                    selectedSupplier = it
                    onSupplierSelect(it)
                },
                onSupplierAdd = { onSupplierAdd(it) },
                selectedSupplier = selectedSupplier,
                supplierName = supplierQuery,
                onTextChanged = { supplierQuery = it }
            )
            Spacer(Modifier.height(16.dp))
        }
        items(goods) {
            billingBodyLayout(
                modifier = Modifier,
                good = it,
                onDelete = { onDelete(it) },
                onUpdateQuantity = { it: Pair<Long, Int> ->
                    onUpdateQuantity(it)
                }
            )
        }
    }
}

@Composable
private fun BillFooter(
    modifier: Modifier = Modifier,
    bill: List<Pair<Goods, Int>>,
    onItemSelect: (Pair<Goods, Int>) -> Unit,
    onBillSave: () -> Unit,
    allItems: List<Goods>,
    onSuppliersSave: (Supplier) -> Unit,
    onGoodsSave: (Goods) -> Unit,
    suppliers: List<Supplier>,
    onDiscountChange: (Int) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val showDiscount = remember { mutableStateOf(false) }
    var discountAmount by remember { mutableIntStateOf(0) }
    val totalPrice by remember(bill) {
        derivedStateOf {
            bill.sumOf { it.first.price * it.second }
        }
    }
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        showDiscount.value = !showDiscount.value
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(modifier = Modifier.padding(end = 16.dp)) {
                    Text(text = "Discount : $discountAmount")
                    if (showDiscount.value) {
                        OutlinedTextField(
                            modifier = Modifier.width(200.dp),
                            value = discountAmount.toString(),
                            onValueChange = {
                                discountAmount = it.onlyDigits().toInt()
                                onDiscountChange(discountAmount)
                            },
                            label = { Text("Enter Discount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Box(
                    modifier = Modifier.clickable {
                        onBillSave()
                    }
                        .background(Green07)
                        .weight(1f)
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(
                            Icons.Default.Save,
                            ""
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Save",
                            style = TextStyle(fontSize = 18.sp), fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(VeryLightGrey)
                        .weight(1f)
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val afterDisc = totalPrice - discountAmount
                    Text(
                        text = afterDisc.toString(),
                        style = TextStyle(fontSize = 18.sp), fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier.clickable {
                        openDialog.value = true
                    }
                        .background(Green07)
                        .weight(1f)
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, "")

                        Text(
                            text = "Add",
                            style = TextStyle(fontSize = 18.sp), fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (openDialog.value) {
                    addGoodsLayout(
                        modifier = Modifier,
                        onDismissRequest = { openDialog.value = false },
                        items = allItems,
                        onItemSelected = {
                            openDialog.value = false
                            onItemSelect(it)
                        },
                        suppliersList = suppliers,
                        onSupplierSave = { onSuppliersSave(it) },
                        onGoodsSave = { onGoodsSave(it) }
                    )
                }
            }
        }
    }

}

@Composable
private fun SearchableDropdown(
    items: List<Supplier>,
    selectedText: String,
    onTextChanged: (String) -> Unit,
    onItemSelected: (Supplier) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredItems = items.filter {
        it.name.contains(selectedText, ignoreCase = true)
    }

    Column {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {
                onTextChanged(it)
                expanded = it.isNotEmpty()
                if (it.isEmpty()) onItemSelected(Supplier(name = ""))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search...") },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            }
        )

        DropdownMenu(
            expanded = expanded && filteredItems.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 240.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                filteredItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            onTextChanged(item.name) // keep in parent
                            expanded = false
                            onItemSelected(item)
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun billingBodyLayout(
    modifier: Modifier = Modifier,
    good: Pair<Goods, Int>,
    onDelete: (Goods) -> Unit,
    onUpdateQuantity: (Pair<Long, Int>) -> Unit
) {
    var totalQuantity = good.second
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = UtilsObject.uriToBitmap(LocalContext.current, good.first.image.toUri())!!
                .asImageBitmap(),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(48.dp).aspectRatio(1f).padding(end = 8.dp),
            contentDescription = "Good Image"
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = good.first.name,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = good.first.price.toString(),
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (totalQuantity > 0) {
                                totalQuantity--
                                onUpdateQuantity(Pair(good.first.id, totalQuantity))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = ""
                        )
                    }
                    Text(
                        text = totalQuantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = {
                            totalQuantity++
                            onUpdateQuantity(Pair(good.first.id, totalQuantity))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = ""
                        )
                    }
                }
                Text(
                    text = "Rs ${good.first.price * totalQuantity}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
            }
            IconButton(
                onClick = {
                    onDelete(good.first)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    ""
                )
            }
        }

    }
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(Modifier.height(8.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun addGoodsLayout(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    items: List<Goods>,
    onItemSelected: (Pair<Goods, Int>) -> Unit,
    suppliersList: List<Supplier>,
    onSupplierSave: (Supplier) -> Unit,
    onGoodsSave: (Goods) -> Unit
) {
    var openDialogToAddGoods = remember { mutableStateOf(false) }
    var selectedGoods by remember { mutableStateOf<Goods?>(null) }
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        var quantity by remember { mutableIntStateOf(1) }
        Card {
            Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Add Goods",
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        SearchableDropdownWithImage(
                            items,
                            onItemSelected = {
                                selectedGoods = it
                            }
                        )
                    }
                    IconButton(
                        onClick = {
                            openDialogToAddGoods.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            ""
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (quantity > 0)
                                quantity -= 1
                        }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                ""
                            )
                        }
                        Text(
                            text = quantity.toString(),
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = {
                            quantity += 1
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                ""
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        enabled = selectedGoods != null && quantity >= 1,
                        onClick = {
                            onItemSelected(Pair(selectedGoods!!, quantity))
                        }
                    ) {
                        Text("Save")
                    }
                }

                if (openDialogToAddGoods.value) {
                    addGoods(
                        onDismissRequest = { openDialogToAddGoods.value = false },
                        onSavaClick = {
                            onGoodsSave(it)
                            openDialogToAddGoods.value = false
                        },
                        onSuppliersSave = { onSupplierSave(it) },
                        suppliers = suppliersList
                    )
                }
            }
        }

    }
}

@Composable
private fun SearchableDropdownWithImage(
    items: List<Goods>,
    onItemSelected: (Goods) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredItems = items.filter {
        it.name.contains(query, ignoreCase = true)
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                expanded = query.isNotEmpty()
                if (query.isEmpty()) onItemSelected(
                    Goods(
                        id = 1,
                        name = "",
                        price = 0.0,
                        cp = 0.0
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search...") },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            }
        )

        DropdownMenu(
            expanded = expanded && filteredItems.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(horizontal = 16.dp)// set dropdown max height
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 240.dp)
                    .verticalScroll(rememberScrollState()) // makes it scrollable
            ) {
                filteredItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            query = item.name
                            expanded = false
                            onItemSelected(item)
                        },
                        leadingIcon = {
                            Icon(
                                bitmap = UtilsObject.uriToBitmap(
                                    LocalContext.current,
                                    item.image.toUri()
                                )!!.asImageBitmap(),
                                contentDescription = "Good Image",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun addGoods(
    onDismissRequest: () -> Unit,
    onSavaClick: (Goods) -> Unit,
    onSuppliersSave: (Supplier) -> Unit,
    suppliers: List<Supplier>
) {
    addStockLayout(
        onDismissRequest = { onDismissRequest() },
        onSavaClick = { it: Goods ->
            onSavaClick(it)
        },
        onSupplierSave = { it: Supplier ->
            onSuppliersSave(it)
        },
        suppliers = suppliers
    )
}

@Composable
private fun billPrintLayout(
    supplier: Supplier,
    bill: List<Pair<Goods, Int>>,
    discount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
        //.verticalScroll(rememberScrollState())
    ) {
        supplierDetailsForPrint(
            name = supplier.name,
            phone = supplier.contactInfo,
            address = supplier.address
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        billItemLayoutForPrint(bill)
        // total
        Spacer(modifier = Modifier.height(16.dp))
        val totalAmount = bill.sumOf { it.first.price * it.second }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Total: ₹$totalAmount",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Discount: -₹$discount",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Total: ₹${totalAmount - discount}",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun supplierDetailsForPrint(
    name: String,
    phone: String,
    address: String
) {
    Column(
        modifier = Modifier
    ) {
        Row {
            Text(
                text = "Name: ",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
            )
            Text(text = name, style = TextStyle(fontSize = 12.sp))
        }
        if (phone != "") {
            Row {
                Text(
                    text = "Phone: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                )
                Text(text = phone, style = TextStyle(fontSize = 12.sp))
            }
        }
        if (address != "") {
            Row {
                Text(
                    text = "Address: ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                )
                Text(text = address, style = TextStyle(fontSize = 12.sp))
            }
        }
    }
}

@Composable
private fun billItemLayoutForPrint(items: List<Pair<Goods, Int>>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, (good, qty) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.CenterStart) {
                    Text("${index + 1}. ${good.name}", fontSize = 14.sp)
                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("x$qty", fontSize = 14.sp)
                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                    Text("₹${good.price * qty}", fontSize = 14.sp)
                }

            }
        }
    }
}


//fun saveComposableAsPdf(
//    activity: Activity,
//    fileName: String,
//    content: @Composable () -> Unit
//) {
//    val root = FrameLayout(activity)
//    activity.addContentView(
//        root,
//        ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//    )
//
//    val composeView = ComposeView(activity).apply {
//        layoutParams = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.MATCH_PARENT,
//            FrameLayout.LayoutParams.WRAP_CONTENT
//        )
//        setContent { content() }
//    }
//    root.addView(composeView)
//
//    composeView.doOnLayout {
//        Log.d(
//            "ment0z",
//            "doOnLayout called ✅ width=${composeView.width} height=${composeView.height}"
//        )
//
//        composeView.post {
//            if (composeView.width == 0 || composeView.height == 0) {
//                Toast.makeText(activity, "Layout failed (0 size)", Toast.LENGTH_SHORT).show()
//                root.removeView(composeView)
//                return@post
//            }
//
//            val bitmap = composeView.drawToBitmap()
//
//            val document = PdfDocument()
//            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
//            val page = document.startPage(pageInfo)
//            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
//            document.finishPage(page)
//
//            val filePath = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                "$fileName.pdf"
//            )
//            FileOutputStream(filePath).use {
//                document.writeTo(it)
//            }
//            document.close()
//
//            Toast.makeText(activity, "Bill saved at Downloads", Toast.LENGTH_SHORT).show()
//
//            // cleanup
//            root.removeView(composeView)
//        }
//    }
//}

private fun saveComposableAsPdf(
    activity: Activity,
    fileName: String,
    content: @Composable () -> Unit
) {
    val root = FrameLayout(activity)
    activity.addContentView(
        root,
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    )

    val composeView = ComposeView(activity).apply {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        setContent { content() }
    }
    root.addView(composeView)

    composeView.doOnLayout {
        composeView.post {
            if (composeView.width == 0) {
                Toast.makeText(activity, "Layout failed (0 size)", Toast.LENGTH_SHORT).show()
                root.removeView(composeView)
                return@post
            }

            // 🟢 Step 1: Force ComposeView to measure with a finite but large max height
            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                composeView.width,
                View.MeasureSpec.EXACTLY
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(20_0000, View.MeasureSpec.AT_MOST)
            // allow up to 20,000px

            composeView.measure(widthSpec, heightSpec)
            composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

            val totalHeight = composeView.measuredHeight
            if (totalHeight == 0) {
                Toast.makeText(activity, "Measured height is 0", Toast.LENGTH_SHORT).show()
                root.removeView(composeView)
                return@post
            }

            // 🟢 Step 2: Render into bitmap
            val fullBitmap = Bitmap.createBitmap(
                composeView.measuredWidth,
                totalHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(fullBitmap)
            composeView.draw(canvas)

            // 🟢 Step 3: Split into multiple A4 pages
            val document = PdfDocument()
            val pageWidth = 595  // A4 width in points
            val pageHeight = 842 // A4 height in points

            val scale = pageWidth.toFloat() / fullBitmap.width
            val scaledHeight = (fullBitmap.height * scale).toInt()

            val scaledBitmap =
                Bitmap.createScaledBitmap(fullBitmap, pageWidth, scaledHeight, true)

            var yOffset = 0
            var pageNum = 1
            while (yOffset < scaledHeight) {
                val bottom = minOf(yOffset + pageHeight, scaledHeight)
                val pageBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    yOffset,
                    pageWidth,
                    bottom - yOffset
                )

                val pageInfo =
                    PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
                val page = document.startPage(pageInfo)
                page.canvas.drawBitmap(pageBitmap, 0f, 0f, null)
                document.finishPage(page)

                yOffset += pageHeight
                pageNum++
            }

            // 🟢 Step 4: Save PDF
            val filePath = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$fileName.pdf"
            )
            FileOutputStream(filePath).use {
                document.writeTo(it)
            }
            document.close()

            Toast.makeText(activity, "Bill saved in Downloads", Toast.LENGTH_SHORT).show()

            // cleanup
            root.removeView(composeView)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupplierInfoAdd(onSaveClick: (Supplier) -> Unit, onDismissRequest: () -> Unit) {
    BasicAlertDialog(onDismissRequest = {}) {
        Card {
            var name by remember { mutableStateOf("") }
            var addContacts by remember { mutableStateOf(false) }
            var contacts by remember { mutableStateOf("") }
            var addAddress by remember { mutableStateOf(false) }
            var address by remember { mutableStateOf("") }

            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Add Supplier",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Add Name") }
                )
                Spacer(Modifier.height(16.dp))
                UtilsObject.leadingIconAndText(
                    modifier = Modifier.clickable { addContacts = !addContacts },
                    icon = if (!addContacts) Icons.Default.Add else Icons.Default.Remove,
                    text = "Add Contacts"
                )
                if (addContacts) {
                    OutlinedTextField(
                        value = contacts,
                        onValueChange = { contacts = it.onlyPhone() },
                        label = { Text(text = "Add Contacts") }
                    )
                }
                Spacer(Modifier.height(16.dp))
                UtilsObject.leadingIconAndText(
                    modifier = Modifier.clickable { addAddress = !addAddress },
                    icon = if (!addAddress) Icons.Default.Add else Icons.Default.Remove,
                    text = "Add Address"
                )
                if (addAddress) {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text(text = "Add Contacts") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }

                    Button(
                        enabled = name != "",
                        onClick = {
                            val updated_supplier =
                                Supplier(name = name, address = address, contactInfo = contacts)
                            onSaveClick(updated_supplier)
                            onDismissRequest()
                        }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}