package com.mruraza.ims.Utils.Consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.filled.WorkHistory
import com.mruraza.ims.Domain.Model.BottomNavBar

object NavigationDestination {
    const val BILL = "Billing Management"
    const val CUSTOMER = "Customer Management"
    const val SUPPLIERS = "Suppliers Management"
    const val PROFILE = "Profile Management"
    const val STOCK = "Stock Management"
    const val DASHBOARD = "Dashboard"
    const val LANDING = "Landing"
    const val PURCHASEBILL = "Purchase Bill"
    const val PURCHASEHISTORY = "Purchase History"
    const val SELLHISTORY = "Sell History"
}

val navItemsList = listOf(
    BottomNavBar(NavigationDestination.STOCK, Icons.Default.Warehouse),
    BottomNavBar(NavigationDestination.DASHBOARD, Icons.Default.Dashboard),
    BottomNavBar(NavigationDestination.BILL, Icons.Default.Receipt)
)

val navRailItems = listOf(
    BottomNavBar(title = NavigationDestination.STOCK, icon = Icons.Default.Warehouse),
    BottomNavBar(title = NavigationDestination.DASHBOARD, icon = Icons.Default.Dashboard),
    BottomNavBar(title = NavigationDestination.BILL, icon = Icons.Default.Receipt),
    BottomNavBar(title = NavigationDestination.CUSTOMER, icon = Icons.Default.People),
    BottomNavBar(title = NavigationDestination.SUPPLIERS, icon = Icons.Default.Send),
    BottomNavBar(title = NavigationDestination.PROFILE, icon = Icons.Default.Person),
    BottomNavBar(title = NavigationDestination.PURCHASEBILL, icon = Icons.Default.ReceiptLong),
    BottomNavBar(title = NavigationDestination.PURCHASEHISTORY, icon = Icons.Default.History),
    BottomNavBar(title = NavigationDestination.SELLHISTORY, icon = Icons.Default.WorkHistory),
)