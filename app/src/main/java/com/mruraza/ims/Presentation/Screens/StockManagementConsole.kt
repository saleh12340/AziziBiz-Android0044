package com.mruraza.ims.Presentation.Screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Presentation.ViewModel.StockScreenViewModel
import com.mruraza.ims.R
import com.mruraza.ims.Utils.Consts.DummyData
import com.mruraza.ims.Utils.Objects.Resources
import com.mruraza.ims.Utils.Objects.UtilsObject
import com.mruraza.ims.Utils.Screens.LoadingScreen
import com.mruraza.ims.ui.theme.Gray01
import com.mruraza.ims.ui.theme.Green07
import com.mruraza.ims.ui.theme.VeryLightGrey
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mruraza.ims.Utils.Consts.NavigationDestination
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDigits
import com.mruraza.ims.Utils.Objects.InputValidator.onlyDouble
import com.mruraza.ims.Utils.Objects.InputValidator.onlyPhone
import kotlin.math.cos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(
    modifier: Modifier = Modifier,
    title: String,
    stockViewModel: StockScreenViewModel = hiltViewModel(),
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
                                    stockViewModel.updateIsSearchButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "بحث"
                                )
                            }
                            IconButton(
                                onClick = {
                                    stockViewModel.updateIsMoreVertButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More vertical 3 dots"
                                )
                            }
                            val isMoreVertButtonClicked by stockViewModel.isMoreVertButtonClicked.collectAsStateWithLifecycle()
                            if (isMoreVertButtonClicked) {
                                DropdownMenu(
                                    modifier = Modifier.padding(end = 16.dp),
                                    expanded = isMoreVertButtonClicked,
                                    onDismissRequest = {
                                        stockViewModel.updateIsMoreVertButtonClicked()
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Go To DashBoard") },
                                        onClick = {
                                            navController.navigate(NavigationDestination.DASHBOARD)
                                            stockViewModel.updateIsMoreVertButtonClicked()
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
        StockScreenLayout(Modifier.padding(innerPadding))
    }

}

@Composable
private fun StockScreenLayout(
    modifier: Modifier = Modifier,
    stockViewModel: StockScreenViewModel = hiltViewModel()
) {
    val searchText by stockViewModel.searchedText.collectAsStateWithLifecycle()
    val isSearchButtonClicked by stockViewModel.isSearchButtonClicked.collectAsStateWithLifecycle()
    Column(modifier = modifier.fillMaxSize()) {
        var selected by remember { mutableStateOf(ALL) }
        val goodsState by stockViewModel.allGoods.collectAsStateWithLifecycle()
        val suppliersState by stockViewModel.allSupplier.collectAsStateWithLifecycle()
        var suppliers by remember { mutableStateOf(emptyList<Supplier>()) }

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
                suppliers = (suppliersState as Resources.Success<List<Supplier>>).data
            }
        }
        when (goodsState) {
            is Resources.Success -> {
                var stockData = (goodsState as Resources.Success<List<Goods>>).data
                if (selected == INSTOCK) {
                    stockData = stockData.filter { it.quantity > 0 }
                }
                if (selected == OUTOFSTOCK) {
                    stockData = stockData.filter { it.quantity <= 0 }
                }
                if (selected == ALL) {
                    stockData = stockData
                }

                if (isSearchButtonClicked) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        value = searchText,
                        onValueChange = { stockViewModel.updateSearchedText(it) },
                        label = { Text("بحث") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    stockViewModel.updateSearchedText("")
                                    stockViewModel.updateIsSearchButtonClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear بحث"
                                )
                            }
                        }
                    )
                }
                if (searchText.isNotEmpty()) {
                    stockData =
                        stockData.filter { it.name.lowercase().contains(searchText.lowercase()) }
                }

                TopChips(
                    onSelect = { selected = it },
                    inStock = stockData.filter { it.quantity > 0 }.size,
                    outOfStock = stockData.filter { it.quantity <= 0 }.size,
                    total = stockData.size
                );
                if (!isSearchButtonClicked) Spacer(Modifier.height(16.dp))
                StockContent(
                    Modifier.weight(1f),
                    stockData = stockData.toList(),
                    onSave = {
                        stockViewModel.updateGoods(it)
                    },
                    suppliers = suppliers,
                    onSupplierSave = { stockViewModel.addSupplier(it) }
                )

            }

            is Resources.Loading -> LoadingScreen()
            is Resources.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    "خطأ: ${(goodsState as Resources.Error).throwable.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        AddStock(
            onGoodsAdd = { stockViewModel.addGoods(it) },
            onSupplierAdd = { stockViewModel.addSupplier(it) },
            suppliers = suppliers
        )
    }
}


@Composable
private fun TopChips(
    onSelect: (String) -> Unit,
    inStock: Int,
    outOfStock: Int,
    total: Int
) {
    var selected by remember { mutableStateOf(ALL) }
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        topChipsLayout(
            modifier = if (selected == ALL) Modifier.background(Gray01) else Modifier.background(
                VeryLightGrey
            ),
            text = ALL + " (${total})",
            onClick = {
                selected = ALL
                onSelect(ALL)
            })

        Spacer(Modifier.width(16.dp))

        topChipsLayout(
            modifier = if (selected == INSTOCK) Modifier.background(Gray01) else Modifier.background(
                VeryLightGrey
            ), text = INSTOCK + " (${inStock})", onClick = {
                onSelect(INSTOCK)
                selected = INSTOCK
            })

        Spacer(Modifier.width(8.dp))

        topChipsLayout(
            modifier = if (selected == OUTOFSTOCK) Modifier.background(Gray01) else Modifier.background(
                VeryLightGrey
            ), text = OUTOFSTOCK + " (${outOfStock})", onClick = {
                onSelect(OUTOFSTOCK)
                selected = OUTOFSTOCK
            })
    }
}


@Composable
private fun StockContent(
    modifier: Modifier = Modifier,
    stockData: List<Goods>,
    onSave: (Goods) -> Unit,
    suppliers: List<Supplier>,
    onSupplierSave: (Supplier) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(stockData) { list ->
            contentLayout(
                Modifier,
                goods = list,
                suppliers = suppliers,
                onSave = { onSave(it) },
                onSupplierSave = { onSupplierSave(it) }
            )
        }
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

@Composable
private fun contentLayout(
    modifier: Modifier = Modifier,
    goods: Goods,
    onSave: (Goods) -> Unit,
    suppliers: List<Supplier>,
    onSupplierSave: (Supplier) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                isDialogOpen = true
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Log.d("mentoz", "contentLayout: ${goods.image}")
        Image(
            bitmap = UtilsObject.uriToBitmap(LocalContext.current, goods.image.trim().toUri())!!
                .asImageBitmap(),
            contentDescription = "Product Image",
            modifier = Modifier.size(48.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )

        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Text(text = goods.name)
            Spacer(Modifier.height(2.dp))
            Text(
                text = "ر.ي ${goods.price}",
                style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
            )
        }

        Text(
            text = "${goods.quantity}",
            style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
        )
    }
    if (isDialogOpen) {
        stockManage(
            good = goods,
            onSave = { onSave(it) },
            suppliers = suppliers,
            onDialogCancel = { isDialogOpen = false },
            onSaveSupplier = { onSupplierSave(it) }
        )
    }
    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
    Spacer(Modifier.height(8.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun stockManage(
    good: Goods,
    onSave: (Goods) -> Unit = {},
    onDialogCancel: () -> Unit,
    suppliers: List<Supplier> = emptyList(),
    onSaveSupplier: (Supplier) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Card {
            var goodName by remember { mutableStateOf(good.name) }
            var goodPrice by remember { mutableDoubleStateOf(good.price) }
            var costPrice by remember { mutableStateOf(good.cp) }
            var goodQuantity by remember { mutableIntStateOf(good.quantity) }
            var goodSupplier by remember { mutableStateOf(good.supplier) }
            var goodDescription by remember { mutableStateOf(good.description) }

            var goodNameEditable by remember { mutableStateOf(false) }
            var goodPriceEditable by remember { mutableStateOf(false) }
            var costPriceEditable by remember { mutableStateOf(false) }
            var goodQuantityEditable by remember { mutableStateOf(false) }
            var goodSupplierEditable by remember { mutableStateOf(false) }
            var goodDescriptionEditable by remember { mutableStateOf(false) }
            var isSupplerAddDialogOpen by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = goodName,
                    onValueChange = { goodName = it },
                    label = { Text("الاسم") },
                    trailingIcon = {
                        IconButton(onClick = { goodNameEditable = !goodNameEditable }) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل الاسم")
                        }
                    },
                    readOnly = !goodNameEditable,
                    enabled = goodNameEditable
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = costPrice.toString(),
                    onValueChange = { costPrice = it.onlyDouble().toDouble() },
                    label = { Text("Cost السعر") },
                    trailingIcon = {
                        IconButton(onClick = { costPriceEditable = !costPriceEditable }) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل السعر")
                        }
                    },
                    readOnly = !costPriceEditable,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = costPriceEditable
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = goodPrice.toString(),
                    onValueChange = { goodPrice = it.onlyDouble().toDouble() },
                    label = { Text("السعر") },
                    trailingIcon = {
                        IconButton(onClick = { goodPriceEditable = !goodPriceEditable }) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل السعر")
                        }
                    },
                    readOnly = !goodPriceEditable,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = goodPriceEditable
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = goodQuantity.toString(),
                    onValueChange = { goodQuantity = it.onlyDigits().toInt() },
                    label = { Text("الكمية") },
                    trailingIcon = {
                        IconButton(onClick = { goodQuantityEditable = !goodQuantityEditable }) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل الكمية")
                        }
                    },
                    readOnly = !goodQuantityEditable,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = goodQuantityEditable
                )

                Spacer(Modifier.height(16.dp))
                Text(text = "المورد")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var data = suppliers.map { it -> it.name }
                    val temp = good.supplier
                    if (goodSupplier != "") data = listOf(temp) + data

                    if (!data.isEmpty()) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (goodSupplierEditable) {
                                UtilsObject.Spinner(
                                    modifier = Modifier,
                                    data,
                                    { goodSupplier = it },
                                    initialSelected = goodSupplier
                                )
                            } else {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = goodSupplier,
                                    onValueChange = { goodSupplier = it },
                                    placeholder = { Text(goodSupplier) },
                                    readOnly = true,
                                    enabled = false
                                )
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                goodSupplierEditable = !goodSupplierEditable
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "تعديل المورد"
                            )
                        }

                        IconButton(
                            onClick = {
                                isSupplerAddDialogOpen = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "إضافة المورد"
                            )
                        }
                    }

                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = goodDescription,
                    onValueChange = { goodDescription = it },
                    label = { Text("الوصف") },
                    trailingIcon = {
                        IconButton(onClick = {
                            goodDescriptionEditable = !goodDescriptionEditable
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل الوصف")
                        }
                    },
                    readOnly = !goodDescriptionEditable,
                    enabled = goodDescriptionEditable
                )
                if (isSupplerAddDialogOpen) {
                    SupplierInfoAdd(
                        onSaveClick = {
                            onSaveSupplier(it)
                        },
                        onDismissRequest = { isSupplerAddDialogOpen = false })
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        onDialogCancel()
                    }) {
                        Text(text = "إلغاء")
                    }

                    Button(
                        enabled = goodName != "",
                        onClick = {
                            val finalStock = Goods(
                                id = good.id,
                                goodName,
                                good.image,
                                goodPrice,
                                costPrice,
                                goodDescription,
                                goodQuantity,
                                goodSupplier
                            )
                            onSave(finalStock)
                            onDialogCancel()
                        }) {
                        Text(text = "حفظ")
                    }
                }
            }
        }
    }
}


@Composable
private fun AddStock(
    onGoodsAdd: (good: Goods) -> Unit,
    onSupplierAdd: (supplier: Supplier) -> Unit,
    suppliers: List<Supplier>
) {
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        addStockLayout(
            modifier = Modifier,
            onDismissRequest = { openDialog.value = false },
            onSavaClick = {
                openDialog.value = false
                onGoodsAdd(it)
            },
            onSupplierSave = { onSupplierAdd(it) },
            suppliers = suppliers
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
            Text("إضافة Stock", modifier = Modifier.align(Alignment.Center))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun addStockLayout(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onSavaClick: (good: Goods) -> Unit = {},
    onSupplierSave: (supplier: Supplier) -> Unit,
    suppliers: List<Supplier>
) {
    var name by remember { mutableStateOf("") }
    var image by remember { mutableStateOf<Uri?>(null) }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var supplierInfo by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                image = it
                try {
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
    )



    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Card {
            Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
                var showLongPressAddSupplierButtonToast by remember { mutableStateOf(false) }
                // to check the error on the outlined text field
                val errors = mutableMapOf<String, String>()
                if (name.isEmpty()) errors["name"] = "الاسم is required"
                if (quantity.isEmpty()) errors["quantity"] = "الكمية is required"
                if (price.isEmpty()) errors["price"] = "السعر is required"
                if (costPrice.isEmpty()) errors["cp"] = "Cost السعر Can't Be Empty"
                var isSupplerAddDialogOpen by remember { mutableStateOf(false) }

                Text(
                    text = "إضافة Stock",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم") },
                    isError = errors.containsKey("name"),
                    supportingText = { Text(errors["name"] ?: "") }
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.onlyDigits() },
                    label = { Text("الكمية") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("quantity"),
                    supportingText = { Text(errors["quantity"] ?: "") }
                )

                OutlinedTextField(
                    value = costPrice,
                    onValueChange = { costPrice = it.onlyDouble() },
                    label = { Text("Cost السعر") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("cp"),
                    supportingText = { Text(errors["cp"] ?: "") }
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.onlyDouble() },
                    label = { Text("السعر") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("price"),
                    supportingText = { Text(errors["price"] ?: "") }
                )


                Text(text = "المورد")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val data = suppliers.map { it -> it.name }
                    if (!data.isEmpty()) {
                        Box(modifier = Modifier.weight(1f)) {
                            UtilsObject.Spinner(
                                modifier = Modifier,
                                data,
                                { supplierInfo = it }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showLongPressAddSupplierButtonToast = true
                                }
                            )
                        }
                    ) {
                        IconButton(
                            onClick = {
                                isSupplerAddDialogOpen = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "إضافة المورد"
                            )
                        }
                    }

                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("الوصف") }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Upload Image",
                )
                IconButton(
                    onClick = { galleryLauncher.launch(arrayOf("image/*")) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.upload_24px),
                        "upload icon"
                    )
                }
                if (image != null) {
                    val imageBitmap = UtilsObject.uriToBitmap(LocalContext.current, image!!)
                    if (imageBitmap != null) {
                        Image(
                            modifier = Modifier.size(48.dp),
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Product Image",
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        onDismissRequest()
                    }) {
                        Text(text = "إلغاء")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        enabled = errors.isEmpty(),
                        onClick = {
                            val goods = Goods(
                                name = name,
                                image = image?.toString()
                                    ?: "android.resource://com.mruraza.ims/drawable/product_image_not_available",
                                price = if (price != "") price.toDouble() else 0.0,
                                cp = if (costPrice != "") costPrice.toDouble() else 0.0,
                                description = description,
                                quantity = if (quantity != "") quantity.toInt() else 0,
                                supplier = supplierInfo
                            )
                            onSavaClick(goods)
                        }) {
                        Text(text = "إضافة")
                    }
                }


                if (isSupplerAddDialogOpen) {
                    SupplierInfoAdd(
                        onSaveClick = {
                            onSupplierSave(it)
                            supplierInfo = it.name
                        },
                        onDismissRequest = {
                            isSupplerAddDialogOpen = false
                        })
                }
                if (showLongPressAddSupplierButtonToast) {
                    val context = LocalContext.current
                    Toast.makeText(context, "إضافة الموردون", Toast.LENGTH_SHORT).show()
                }
            }
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
                            val updated_supplier =
                                Supplier(name = name, address = address, contactInfo = contacts)
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


private const val ALL = "All"
private const val INSTOCK = "In Stock"
private const val OUTOFSTOCK = "Out Of Stock"
