package com.mruraza.ims.Presentation.Screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Presentation.ViewModel.SellHistoryViewModel
import com.mruraza.ims.Utils.Objects.Resources
import com.mruraza.ims.Utils.Screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sellHistory(
    title: String,
    navController: NavController,
    sellHistoryViewModel: SellHistoryViewModel = hiltViewModel(),
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
        val allBillsState by sellHistoryViewModel.allBills.collectAsStateWithLifecycle()
        var isDetailsOpen by remember { mutableStateOf(false) }
        var selectedBill by remember { mutableStateOf<Bill?>(null) }
        when (allBillsState) {
            is Resources.Loading -> {
                LoadingScreen()
            }

            is Resources.Success -> {
                var allBills: List<Bill> = (allBillsState as Resources.Success).data
                allBills = getCleanedBill(allBills)
                allBills = allBills.sortedByDescending { it-> it.date }
                if (isDetailsOpen && selectedBill != null) {
                    BillDetailedLayout(
                        modifier = Modifier.fillMaxWidth().padding(innerPadding),
                        bill = selectedBill!!,
                        onClose = {
                            isDetailsOpen = false
                        }
                    )
                } else {
                    historyLayoutSell(
                        modifier = Modifier.padding(innerPadding),
                        list = allBills,
                        onItemSelect = { it ->
                            isDetailsOpen = true
                            selectedBill = it
                        }
                    )
                }
            }

            is Resources.Error -> {}
        }
    }
}

@Composable
private fun historyLayoutSell(
    list: List<Bill>,
    modifier: Modifier,
    onItemSelect: (Bill) -> Unit
) {
    LazyColumn(modifier.fillMaxSize()) {
        items(list.size) { index ->
            val item = list[index]
            var total = item.good.sumOf { it ->
                it.first.cp * it.second
            }
            total -= item.discount
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clickable {
                        onItemSelect(item)
                    }, horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column( modifier = Modifier.padding(horizontal = 16.dp).weight(1f)){
                    Text(
                        text = item.customer.name,
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = item.date,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "ر.ي $total",
                    style = TextStyle(fontSize = 14.sp)
                )
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BillDetailedLayout(
    modifier: Modifier = Modifier,
    bill: Bill,
    onClose: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "${bill.customer.name} Bill", style = TextStyle(fontSize = 18.sp))
            IconButton(
                onClick = {
                    onClose()
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(text = "التاريخ : ${bill.date}", style = TextStyle(fontSize = 14.sp))
        Text(text = "Bill No : ${bill.id}", style = TextStyle(fontSize = 14.sp))

        Spacer(Modifier.height(24.dp))
        var total = 0.0
        bill.good.forEach {
            total += it.first.cp * it.second
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = it.first.name, style = TextStyle(fontSize = 12.sp))
                Text(text = "ر.ي ${it.first.cp * it.second}", style = TextStyle(fontSize = 12.sp))
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
        }
        Spacer(Modifier.height(16.dp))
        Text(text = "الإجمالي : ر.ي $total", style = TextStyle(fontSize = 14.sp))
        Text(text = "الخصم : ر.ي ${bill.discount}", style = TextStyle(fontSize = 14.sp))
        Text(text = "Net الإجمالي : ر.ي ${total - bill.discount}", style = TextStyle(fontSize = 14.sp))
    }
}

private fun getCleanedBill(bill: List<Bill>): List<Bill> {
    val cleanedList = mutableListOf<Bill>()
    bill.forEach { it ->
        val allGoods = mutableListOf<Pair<Goods, Int>>()
        it.good.forEach {
            val goodsMap = it.first as Map<String, Any>
            val quantityMap = it.second as? Double ?: 0.0
            val good = Goods(
                id = (goodsMap["id"] as Double).toLong(),
                name = goodsMap["name"] as String,
                image = goodsMap["image"] as String,
                price = goodsMap["price"] as Double,
                cp = goodsMap["cp"] as Double,
                description = goodsMap["description"] as String,
                quantity = (goodsMap["quantity"] as Double).toInt(),
                supplier = goodsMap["supplier"] as String
            )
            allGoods.add(Pair(good, quantityMap.toInt()))
        }
        cleanedList.add(
            Bill(
                id = it.id,
                customer = it.customer,
                good = allGoods.toList(),
                discount = it.discount,
                date = it.date
            )
        )

    }
    return cleanedList
}