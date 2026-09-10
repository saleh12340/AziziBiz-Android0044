package com.mruraza.ims

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mruraza.ims.Presentation.Screens.Billing
import com.mruraza.ims.Presentation.Screens.DashboardScreen
import com.mruraza.ims.Presentation.Screens.PurchaseBilling
import com.mruraza.ims.Presentation.Screens.StockScreen
import com.mruraza.ims.Presentation.Screens.SuppliersManagementConsole
import com.mruraza.ims.Presentation.Screens.UserProfilesScreen
import com.mruraza.ims.Presentation.Screens.customerManagementConsole
import com.mruraza.ims.Presentation.Screens.landingScreen
import com.mruraza.ims.Presentation.Screens.purchaseHistory
import com.mruraza.ims.Presentation.Screens.sellHistory
import com.mruraza.ims.Presentation.ViewModel.UserInfoViewModel
import com.mruraza.ims.Utils.Consts.NavigationDestination
import com.mruraza.ims.ui.theme.IMSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IMSTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestination.DASHBOARD
                    ) {
                        composable(NavigationDestination.LANDING) {
                            landingScreen(navController = navController)
                        }
                        composable(NavigationDestination.BILL) {
                            val profileViewModel: UserInfoViewModel = hiltViewModel()
                            profileViewModel.refresh()
                            val userInfo by profileViewModel.userInfo.collectAsStateWithLifecycle()
                            Billing(
                                title = "فواتير المبيعات",
                                ownerName = userInfo.name,
                                ownerAddress = userInfo.address,
                                ownerContact = userInfo.phone,
                                activity = this@MainActivity,
                                navController = navController
                            )
                        }
                        composable(NavigationDestination.PURCHASEBILL) {
                            PurchaseBilling(title = "فاتورة مشتريات", activity = this@MainActivity, navController = navController)
                        }
                        composable(NavigationDestination.CUSTOMER) {
                            customerManagementConsole(title = "حسابات العملاء", navController = navController)
                        }
                        composable(NavigationDestination.SUPPLIERS) {
                            SuppliersManagementConsole(title = "الموردون", navController = navController)
                        }
                        composable(NavigationDestination.PROFILE) {
                            UserProfilesScreen(title = "بيانات البقالة", navController = navController)
                        }
                        composable(NavigationDestination.STOCK) {
                            StockScreen(title = "المخزون والأصناف", navController = navController)
                        }
                        composable(NavigationDestination.DASHBOARD) {
                            DashboardScreen(title = "بقالة العزي للمواد الغذائية", navController = navController)
                        }
                        composable(NavigationDestination.PURCHASEHISTORY) {
                            purchaseHistory(title = "سجل المشتريات", navController = navController)
                        }
                        composable(NavigationDestination.SELLHISTORY) {
                            sellHistory(title = "سجل المبيعات", navController = navController)
                        }
                    }
                }
            }
        }
    }
}
