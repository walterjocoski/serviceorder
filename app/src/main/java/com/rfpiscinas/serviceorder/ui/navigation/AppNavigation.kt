package com.rfpiscinas.serviceorder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.ui.screens.employee.AddServicesScreen
import com.rfpiscinas.serviceorder.ui.screens.employee.CreateOrderInitialScreen
import com.rfpiscinas.serviceorder.ui.screens.employee.EmployeeHomeScreen
import com.rfpiscinas.serviceorder.ui.screens.employee.OrderDetailScreen
import com.rfpiscinas.serviceorder.ui.screens.login.LoginScreen
import com.rfpiscinas.serviceorder.ui.screens.manager.ManagerHomeScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object EmployeeHome : Screen("employee_home")
    object CreateOrder : Screen("create_order")
    object AddServices : Screen("add_services/{orderId}") {
        fun createRoute(orderId: Long) = "add_services/$orderId"
    }
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: Long) = "order_detail/$orderId"
    }
    object ManagerHome : Screen("manager_home")
    object ManagerClients : Screen("manager_clients")
    object ManagerEmployees : Screen("manager_employees")
    object ManagerServices : Screen("manager_services")
    object ManagerProducts : Screen("manager_products")
    object ManagerOrders : Screen("manager_orders")
    object ManagerReports : Screen("manager_reports")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    // Usuário logado compartilhado entre telas (simples com remember no nível da activity)
    var currentUser: User? = null

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { user ->
                    currentUser = user
                    val destination = when (user.role) {
                        com.rfpiscinas.serviceorder.data.model.UserRole.EMPLOYEE ->
                            Screen.EmployeeHome.route
                        com.rfpiscinas.serviceorder.data.model.UserRole.MANAGER,
                        com.rfpiscinas.serviceorder.data.model.UserRole.ADMIN ->
                            Screen.ManagerHome.route
                        else -> Screen.Login.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EmployeeHome.route) {
            currentUser?.let { user ->
                EmployeeHomeScreen(
                    currentUser = user,
                    onCreateOrder = {
                        navController.navigate(Screen.CreateOrder.route)
                    },
                    onOrderClick = { orderId ->
                        navController.navigate(Screen.OrderDetail.createRoute(orderId))
                    },
                    onAddServices = { orderId ->
                        navController.navigate(Screen.AddServices.createRoute(orderId))
                    }
                )
            }
        }

        composable(Screen.CreateOrder.route) {
            currentUser?.let { user ->
                CreateOrderInitialScreen(
                    currentUser = user,
                    onNavigateBack = { navController.popBackStack() },
                    onOrderCreated = { orderId ->
                        // Vai para tela de lançar serviços após criar a OS
                        navController.navigate(Screen.AddServices.createRoute(orderId)) {
                            popUpTo(Screen.CreateOrder.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.AddServices.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")?.toLongOrNull() ?: 0L
            currentUser?.let { user ->
                AddServicesScreen(
                    orderId = orderId,
                    clientId = 0L, // não usado mais — buscado pelo orderId
                    startDateTime = "",
                    onNavigateBack = { navController.popBackStack() },
                    onOrderFinished = {
                        navController.navigate(Screen.EmployeeHome.route) {
                            popUpTo(Screen.EmployeeHome.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.OrderDetail.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")?.toLongOrNull() ?: 0L
            OrderDetailScreen(
                orderId = orderId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManagerHome.route) {
            ManagerHomeScreen(
                onNavigateToClients = { navController.navigate(Screen.ManagerClients.route) },
                onNavigateToEmployees = { navController.navigate(Screen.ManagerEmployees.route) },
                onNavigateToServices = { navController.navigate(Screen.ManagerServices.route) },
                onNavigateToProducts = { navController.navigate(Screen.ManagerProducts.route) },
                onNavigateToOrders = { navController.navigate(Screen.ManagerOrders.route) },
                onNavigateToReports = { navController.navigate(Screen.ManagerReports.route) }
            )
        }

        composable(Screen.ManagerClients.route) {
            com.rfpiscinas.serviceorder.ui.screens.manager.ClientManagementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManagerEmployees.route) {
            com.rfpiscinas.serviceorder.ui.screens.manager.EmployeeManagementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManagerServices.route) {
            com.rfpiscinas.serviceorder.ui.screens.manager.ServiceManagementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManagerProducts.route) {
            com.rfpiscinas.serviceorder.ui.screens.manager.ProductManagementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManagerOrders.route) {
            com.rfpiscinas.serviceorder.ui.screens.manager.AllOrdersScreen(
                onNavigateBack = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                }
            )
        }

        composable(Screen.ManagerReports.route) {
            com.rfpiscinas.serviceorder.ui.screens.manager.ReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
