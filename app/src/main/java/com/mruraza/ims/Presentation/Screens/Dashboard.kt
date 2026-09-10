package com.mruraza.ims.Presentation.Screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Model.BottomNavBar
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asComposeRenderEffect
import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Presentation.ViewModel.DashBoardViewModel
import com.mruraza.ims.Utils.Consts.NavigationDestination
import com.mruraza.ims.Utils.Consts.navItemsList
import com.mruraza.ims.Utils.Consts.navRailItems
import com.mruraza.ims.Utils.Objects.Resources
import com.mruraza.ims.Utils.Screens.LoadingScreen
import com.mruraza.ims.Utils.Screens.glassEffect
import com.mruraza.ims.ui.theme.Gray01
import com.mruraza.ims.ui.theme.VeryLightGrey
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.AutoScrollCondition
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    title: String,
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var selected by remember { mutableStateOf(NavigationDestination.DASHBOARD) }

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                navRailItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        selected = item.title == selected,
                        onClick = {
                            selected = item.title
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate(item.title)
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.title)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar{
                    navItemsList.forEach { item ->
                        NavigationBarItem(
                            selected = item.title == selected,
                            onClick = {
                                selected = item.title
                                navController.navigate (item.title)
                            },
                            label = { Text(text = item.title) },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            })
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            BackHandler(onBack = {
                (context as? Activity)?.finish()
            })
            DashBoardScreen(Modifier.padding(innerPadding))
        }
    }
}


@Composable
private fun DashBoardScreen(
    modifier: Modifier = Modifier,
    dashBoardViewModel: DashBoardViewModel = hiltViewModel()
) {
    var lastMonthData by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
    var lastYearData by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
    var lastTenYearData by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
    var allData by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
    var allBills by remember { mutableStateOf<List<Bill>>(listOf()) }
    var allCustomers by remember { mutableStateOf<List<Customer>>(listOf()) }
    var allSuppliers by remember { mutableStateOf<List<Supplier>>(listOf()) }
    var allGoods by remember { mutableStateOf<List<Goods>>(listOf()) }
    val pastMonthState by dashBoardViewModel.getBillsInLastMonth.collectAsStateWithLifecycle()
    val pastYearState by dashBoardViewModel.getBillsInLastYear.collectAsStateWithLifecycle()
    val pastTenYearsState by dashBoardViewModel.getBillsInLastTenYears.collectAsStateWithLifecycle()
    val allDataSate by dashBoardViewModel.allBills.collectAsStateWithLifecycle()
    val totalCustomerState by dashBoardViewModel.allCustomers.collectAsStateWithLifecycle()
    val totalSuppliersState by dashBoardViewModel.allSuppliers.collectAsStateWithLifecycle()
    val totalGoodsState by dashBoardViewModel.allGoods.collectAsStateWithLifecycle()

    when (allDataSate) {
        is Resources.Success -> {
            val data = (allDataSate as Resources.Success<List<Bill>>).data
            allBills = data
            allData = calculateDailyTotals(data)
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {
        }
    }

    when (pastMonthState) {
        is Resources.Success -> {
            val data: List<Bill> = (pastMonthState as Resources.Success<List<Bill>>).data
            Log.d("bill", data.toString())
            lastMonthData = calculateDailyTotals(data)
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {}
    }

    when (pastYearState) {
        is Resources.Success -> {
            val data = (pastYearState as Resources.Success<List<Bill>>).data
            lastYearData = calculateMonthlyTotals(data)
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {}
    }

    when (pastTenYearsState) {
        is Resources.Success -> {
            val data = (pastTenYearsState as Resources.Success<List<Bill>>).data
            lastTenYearData = calculateYearlyTotalsFor10Years(data)
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {}
    }

    when (totalCustomerState) {
        is Resources.Success -> {
            allCustomers = (totalCustomerState as Resources.Success<List<Customer>>).data
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {}
    }

    when (totalSuppliersState) {
        is Resources.Success -> {
            allSuppliers = (totalSuppliersState as Resources.Success<List<Supplier>>).data
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {}
    }

    when (totalGoodsState) {
        is Resources.Success -> {
            allGoods = (totalGoodsState as Resources.Success<List<Goods>>).data
        }

        is Resources.Loading -> {
            LoadingScreen()
        }

        is Resources.Error -> {}
    }

    Column(
        modifier.fillMaxWidth().padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            //border = BorderStroke(width = 1.dp, color = Gray05)
        ) {
            var monthSelected by remember { mutableStateOf(true) }
            var yearSelected by remember { mutableStateOf(false) }
            var tenYearSelected by remember { mutableStateOf(false) }
            var moreSelected by remember { mutableStateOf(false) }

            TopChipsForSalesLineGraph(
                onChipClick = {
                    when (it) {
                        ONEMONTH -> {
                            monthSelected = true
                            yearSelected = false
                            tenYearSelected = false
                            moreSelected = false
                        }

                        ONEYEAR -> {
                            monthSelected = false
                            yearSelected = true
                            tenYearSelected = false
                            moreSelected = false
                        }

                        TENYEAR -> {
                            monthSelected = false
                            yearSelected = false
                            tenYearSelected = true
                            moreSelected = false
                        }

                        MORE -> {
                            moreSelected = !moreSelected
                            if (moreSelected) {
                                monthSelected = false
                                yearSelected = false
                                tenYearSelected = false
                            } else {
                                monthSelected = true
                                yearSelected = false
                                tenYearSelected = false
                            }
                        }
                    }
                },
                selected = if (monthSelected) ONEMONTH else if (yearSelected) ONEYEAR else if (tenYearSelected) TENYEAR else MORE
            )

            if (moreSelected && allData.isNotEmpty() && !monthSelected && !yearSelected && !tenYearSelected) {
                Box(modifier = Modifier.heightIn(max = 350.dp, min = 100.dp).fillMaxWidth()) {
                    if (allData.isEmpty()) {
                        Text(
                            text = "No Data"
                        )
                    }
                    if (allData.isNotEmpty()) {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            allData.forEach { data: Pair<String, Double> ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = data.first)
                                    Text(text = data.second.toString())
                                }
                            }
                        }
                    }
                }
            }

            if (monthSelected) {
                if (lastMonthData.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    LineChart(dayData = lastMonthData)
                }
            }
            if (yearSelected) {
                if (lastYearData.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    LineChart(dayData = lastYearData)
                }
            }
            if (tenYearSelected) {
                if (lastTenYearData.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    LineChart(dayData = lastTenYearData)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            //border = BorderStroke(width = 1.dp, color = Gray05)
        ) {
            Text(modifier = Modifier.padding(horizontal = 8.dp), text = "Popular الأصناف")
            var weekSelected by remember { mutableStateOf(true) }
            var monthSelected by remember { mutableStateOf(false) }
            var yearSelected by remember { mutableStateOf(false) }
            var tenYearSelected by remember { mutableStateOf(false) }
            var moreSelected by remember { mutableStateOf(false) }

            var allPopularGoods by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
            var oneWeekPopularGoods by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
            var oneMonthPopularGoods by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
            var oneYearPopularGoods by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }
            var tenYearsPopularGoods by remember { mutableStateOf<List<Pair<String, Double>>>(listOf()) }

            allPopularGoods = TopSoldAnalyzer.topGoodsAllTime(allBills)
            oneWeekPopularGoods = TopSoldAnalyzer.topGoodsPastWeek(allBills)
            oneMonthPopularGoods = TopSoldAnalyzer.topGoodsPastMonth(allBills)
            oneYearPopularGoods = TopSoldAnalyzer.topGoodsPastYear(allBills)
            tenYearsPopularGoods = TopSoldAnalyzer.topGoodsPast10Years(allBills)

            TopChipsForTopProducts(
                onChipClick = {
                    when (it) {
                        ONEWEEEK -> {
                            weekSelected = true
                            monthSelected = false
                            yearSelected = false
                            tenYearSelected = false
                            moreSelected = false
                        }

                        ONEMONTH -> {
                            weekSelected = false
                            monthSelected = true
                            yearSelected = false
                            tenYearSelected = false
                            moreSelected = false
                        }

                        ONEYEAR -> {
                            weekSelected = false
                            monthSelected = false
                            yearSelected = true
                            tenYearSelected = false
                            moreSelected = false
                        }

                        TENYEAR -> {
                            weekSelected = false
                            monthSelected = false
                            yearSelected = false
                            tenYearSelected = true
                            moreSelected = false
                        }

                        MORE -> {
                            moreSelected = !moreSelected
                            if (moreSelected) {
                                weekSelected = false
                                monthSelected = false
                                yearSelected = false
                                tenYearSelected = false
                            } else {
                                weekSelected = true
                                monthSelected = false
                                yearSelected = false
                                tenYearSelected = false
                            }
                        }
                    }
                },
                selected = if (weekSelected) ONEWEEEK else if (monthSelected) ONEMONTH else if (yearSelected) ONEYEAR else if (tenYearSelected) TENYEAR else MORE
            )

            if (moreSelected) {
                if (allPopularGoods.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    topGoodsLayout(allPopularGoods)
                }
            }
            if (weekSelected) {
                if (oneWeekPopularGoods.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    topGoodsLayout(oneWeekPopularGoods)
                }
            }

            if (monthSelected) {
                if (oneMonthPopularGoods.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    topGoodsLayout(oneMonthPopularGoods)
                }
            }

            if (yearSelected) {
                if (oneYearPopularGoods.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    topGoodsLayout(oneYearPopularGoods)
                }
            }

            if (tenYearSelected) {
                if (tenYearsPopularGoods.isEmpty()) {
                    Box(
                        modifier = Modifier.height(100.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Data"
                        )
                    }
                } else {
                    topGoodsLayout(tenYearsPopularGoods)
                }
            }

        }
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            //border = BorderStroke(width = 1.dp, color = Gray05)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("الموردون Due")
                    }
                    var moreSelected by remember { mutableStateOf(false) }
                    var dateSelected by remember { mutableStateOf(true) }
                    topChipsForDueFilter(
                        onChipClick = {
                            when (it) {
                                DATE -> {
                                    dateSelected = true
                                    moreSelected = false
                                }

                                MAXI -> {
                                    dateSelected = false
                                    moreSelected = true
                                }
                            }
                        },
                        selected = if (dateSelected) DATE else MAXI
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    if (moreSelected) {
                        val moreList = allSuppliers.sortedByDescending { it -> it.due }
                        if (moreList.isEmpty()) {
                            Box(
                                modifier = Modifier.heightIn(min = 100.dp).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Data",
                                )
                            }
                        } else {
                            dueListScreenSupplier(modifier = Modifier.fillMaxWidth(), moreList)
                        }
                    } else {
                        val dateList = allSuppliers.filter { it -> it.dueDate != "" }
                            .sortedBy { it -> it.dueDate }
                        if (dateList.isEmpty()) {
                            Box(
                                modifier = Modifier.heightIn(min = 100.dp).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Data",
                                )
                            }
                        } else {
                            dueListScreenSupplier(modifier = Modifier.fillMaxWidth(), dateList)
                        }
                    }
                }
                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("العملاء Due")
                    }
                    var moreSelected by remember { mutableStateOf(false) }
                    var dateSelected by remember { mutableStateOf(true) }
                    topChipsForDueFilter(
                        onChipClick = {
                            when (it) {
                                DATE -> {
                                    dateSelected = true
                                    moreSelected = false
                                }

                                MAXI -> {
                                    dateSelected = false
                                    moreSelected = true
                                }
                            }
                        },
                        selected = if (dateSelected) DATE else MAXI
                    )
                    HorizontalDivider(Modifier.padding(vertical = 4.dp))
                    if (moreSelected) {

                        val moreList = allCustomers.sortedByDescending { it -> it.due }
                        if (moreList.isEmpty()) {
                            Box(
                                modifier = Modifier.heightIn(min = 100.dp).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Data",
                                )
                            }
                        } else {
                            dueListScreenCustomer(modifier = Modifier.fillMaxWidth(), moreList)
                        }
                    } else {
                        val dateList = allCustomers.filter { it -> it.dueDate != "" }
                            .sortedBy { it -> it.dueDate }
                        if (dateList.isEmpty()) {
                            Box(
                                modifier = Modifier.heightIn(min = 100.dp).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Data",
                                )
                            }
                        } else {
                            dueListScreenCustomer(modifier = Modifier.fillMaxWidth(), dateList)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            //border = BorderStroke(width = 1.dp, color = Gray05)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Out Of Stock")
                    }
                    if (allGoods.none { it -> it.quantity <= 0 }) {
                        Box(
                            modifier = Modifier.heightIn(min = 100.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Data",
                            )
                        }
                    } else {
                        stockInsightScreen(
                            modifier = Modifier.fillMaxWidth(),
                            list = allGoods.filter { it -> it.quantity <= 0 }
                        )
                    }
                }
                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("In Stock")
                    }
                    if (allGoods.none { it -> it.quantity > 0 }) {
                        Box(
                            modifier = Modifier.heightIn(min = 100.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Data",
                            )
                        }
                    } else {
                        stockInsightScreen(
                            modifier = Modifier.fillMaxWidth(),
                            list = allGoods.filter { it -> it.quantity > 0 }
                                .sortedByDescending { it -> it.quantity }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun topChipsForDueFilter(
    onChipClick: (String) -> Unit,
    selected: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        topChipsLayout(
            modifier = if (selected == DATE) Modifier.background(Gray01) else Modifier.background(
                VeryLightGrey
            ),
            DATE,
            onClick = {
                onChipClick(DATE)
            }
        )

        topChipsLayout(
            modifier = if (selected == MAXI) Modifier.background(Gray01) else Modifier.background(
                VeryLightGrey
            ),
            MAXI,
            onClick = {
                onChipClick(MAXI)
            }
        )
    }
}

@Composable
private fun topGoodsLayout(goods: List<Pair<String, Double>>) {
    Column(
        modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp).padding(bottom = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        goods.forEach {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = it.first)
                Text(text = it.second.toString())
            }
        }
    }
}

@Composable
private fun TopChipsForSalesLineGraph(
    onChipClick: (String) -> Unit,
    selected: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topChipsLayout(
                modifier = if (selected == ONEMONTH) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                ONEMONTH,
                onClick = {
                    onChipClick(ONEMONTH)
                }
            )

            topChipsLayout(
                modifier = if (selected == ONEYEAR) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                ONEYEAR,
                onClick = {
                    onChipClick(ONEYEAR)
                }
            )

            topChipsLayout(
                modifier = if (selected == TENYEAR) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                TENYEAR,
                onClick = {
                    onChipClick(TENYEAR)
                }
            )
        }
        IconButton(
            onClick = {
                onChipClick(MORE)
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = if (selected != MORE) Icons.Outlined.Info else Icons.Filled.Info,
                contentDescription = "More"
            )
        }
    }

}

@Composable
private fun TopChipsForTopProducts(
    onChipClick: (String) -> Unit,
    selected: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topChipsLayout(
                modifier = if (selected == ONEWEEEK) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                ONEWEEEK,
                onClick = {
                    onChipClick(ONEWEEEK)
                }
            )

            topChipsLayout(
                modifier = if (selected == ONEMONTH) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                ONEMONTH,
                onClick = {
                    onChipClick(ONEMONTH)
                }
            )

            topChipsLayout(
                modifier = if (selected == ONEYEAR) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                ONEYEAR,
                onClick = {
                    onChipClick(ONEYEAR)
                }
            )

            topChipsLayout(
                modifier = if (selected == TENYEAR) Modifier.background(Gray01) else Modifier.background(
                    VeryLightGrey
                ),
                TENYEAR,
                onClick = {
                    onChipClick(TENYEAR)
                }
            )
        }
        IconButton(
            onClick = {
                onChipClick(MORE)
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = if (selected != MORE) Icons.Outlined.Info else Icons.Filled.Info,
                contentDescription = "More"
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
fun LineChart(
    dayData: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    val scrollState = rememberVicoScrollState(
        scrollEnabled = true,
        initialScroll = Scroll.Absolute.End,
        autoScroll = Scroll.Absolute.End,
        autoScrollCondition = AutoScrollCondition.OnModelGrowth
    )

    LaunchedEffect(dayData) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    x = dayData.indices.map { it.toFloat() },
                    y = dayData.map { it.second.toFloat() }
                )
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = { _, x, _ ->
                    val index = x.toInt()
                    dayData.getOrNull(index)?.first ?: ""
                }
            )
        ),
        modelProducer = modelProducer,
        scrollState = scrollState,
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(vertical = 16.dp, horizontal = 4.dp)
    )
}

fun calculateDailyTotals(bills: List<Bill>): List<Pair<String, Double>> {
    return bills.groupBy { it.date }
        .map { (date, billsOnDate) ->
            date to billsOnDate.sumOf { bill: Bill ->
                bill.good.sumOf { pair ->
                    val goodsMap = pair.first as Map<String, Any>
                    val qty = pair.second as? Double ?: 0.0
                    val price = goodsMap["price"] as? Double ?: 0.0
                    price * qty
                }
            }
        }
}

fun calculateMonthlyTotals(bills: List<Bill>): List<Pair<String, Double>> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
    val now = LocalDate.now()

    // Generate all past 12 months (including current month)
    val months = (0..11).map { i ->
        now.minusMonths(i.toLong()).format(formatter)
    }.reversed()

    // Group bills by month
    val monthlyMap = bills.groupBy { bill ->
        LocalDate.parse(bill.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .format(formatter)
    }.mapValues { (_, billsInMonth) ->
        billsInMonth.sumOf { bill ->
            bill.good.sumOf { pair ->
                val goodsMap = pair.first as Map<String, Any>
                val qty = pair.second as? Double ?: 0.0
                val price = goodsMap["price"] as? Double ?: 0.0
                price * qty
            }
        }
    }

    // Fill in missing months with 0
    return months.map { month ->
        month to (monthlyMap[month] ?: 0.0)
    }
}


fun calculateYearlyTotalsFor10Years(bills: List<Bill>): List<Pair<String, Double>> {
    val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    val today = LocalDate.now()
    val startYear = today.minusYears(10).year

    // Group bills by year
    val grouped = bills.groupBy {
        val date = LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        date.format(yearFormatter)
    }

    val results = mutableListOf<Pair<String, Double>>()

    for (year in startYear..today.year) {
        val key = year.toString()

        val total = grouped[key]?.sumOf { bill ->
            bill.good.sumOf { pair ->
                val goodsMap = pair.first as Map<String, Any>
                val qty = pair.second as? Double ?: 0.0
                val price = goodsMap["price"] as? Double ?: 0.0
                price * qty
            }
        } ?: 0.0

        results.add(key to total)
    }

    return results
}


object TopSoldAnalyzer {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Helper to filter bills within a timeframe
    private fun filterBills(bills: List<Bill>, from: LocalDate): List<Bill> {
        return bills.filter {
            val date = LocalDate.parse(it.date, dateFormatter)
            !date.isBefore(from)
        }
    }

    // Helper to find top goods
    private fun findTopGoods(bills: List<Bill>, topN: Int = 5): List<Pair<String, Double>> {
        val goodsCount = mutableMapOf<String, Double>()
        val idToGoods = mutableMapOf<String, String>()
        for (bill in bills) {
            for (pair in bill.good) {
                val goodsMap = pair.first as Map<String, Any>
                val qty = pair.second as? Double ?: 0.0
                val id = goodsMap["id"].toString()
                idToGoods[id] = goodsMap["name"].toString()
                //goodsCount[id] = goodsCount.getOrDefault(id, 0.0) + qty
                goodsCount[id] = goodsCount.getOrDefault(id, 0.0) + 1
            }
        }

        return goodsCount.entries
            .sortedByDescending { it.value }
            .take(topN)
            .map { idToGoods[it.key]!! to it.value }
    }

    fun topGoodsPastWeek(bills: List<Bill>): List<Pair<String, Double>> {
        val from = LocalDate.now().minusWeeks(1)
        val filtered = filterBills(bills, from)
        return findTopGoods(filtered)
    }

    fun topGoodsPastMonth(bills: List<Bill>): List<Pair<String, Double>> {
        val from = LocalDate.now().minusMonths(1)
        val filtered = filterBills(bills, from)
        return findTopGoods(filtered)
    }

    fun topGoodsPastYear(bills: List<Bill>): List<Pair<String, Double>> {
        val from = LocalDate.now().minusYears(1)
        val filtered = filterBills(bills, from)
        return findTopGoods(filtered)
    }

    fun topGoodsPast10Years(bills: List<Bill>): List<Pair<String, Double>> {
        val from = LocalDate.now().minusYears(10)
        val filtered = filterBills(bills, from)
        return findTopGoods(filtered)
    }

    fun topGoodsAllTime(bills: List<Bill>): List<Pair<String, Double>> {
        return findTopGoods(bills, topN = bills.size)
    }
}


@Composable
private fun dueListScreenCustomer(modifier: Modifier = Modifier, list: List<Customer>) {
    Column(
        modifier = modifier.fillMaxWidth().heightIn(max = 250.dp)
            .verticalScroll(rememberScrollState())
    ) {
        list.forEach { customer ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = customer.name, modifier = Modifier.weight(1f))
                Text(text = customer.due.toString())
            }
        }
    }
}

@Composable
private fun dueListScreenSupplier(
    modifier: Modifier = Modifier,
    list: List<com.mruraza.ims.Domain.Model.Supplier>
) {
    Column(
        modifier = modifier.fillMaxWidth().heightIn(max = 250.dp)
            .verticalScroll(rememberScrollState())
    ) {
        list.forEach { supplier ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = supplier.name, modifier = Modifier.weight(1f))
                Text(text = supplier.due.toString())
            }
        }
    }
}

@Composable
private fun stockInsightScreen(
    modifier: Modifier = Modifier,
    list: List<Goods>
) {
    Column(
        modifier = modifier.fillMaxWidth().heightIn(max = 250.dp)
            .verticalScroll(rememberScrollState())
    ) {
        list.forEach { good ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = good.name, modifier = Modifier.weight(1f))
                Text(text = good.quantity.toString())
            }
        }
    }
}

private const val ONEMONTH = "Month"
private const val ONEYEAR = "Year"
private const val TENYEAR = "10 Year"
private const val ONEWEEEK = "Week"
private const val MORE = "More"
private const val DATE = "التاريخ"
private const val MAXI = "Max"