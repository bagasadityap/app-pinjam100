package com.bagas.pinjam100.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.bagas.pinjam100.presentation.features.auth.AuthScreen
import com.bagas.pinjam100.presentation.features.auth.ChangePasswordScreen
import com.bagas.pinjam100.presentation.features.auth.ForgotPasswordScreen
import com.bagas.pinjam100.presentation.features.auth.LoginScreen
import com.bagas.pinjam100.presentation.features.auth.OtpScreen
import com.bagas.pinjam100.presentation.features.auth.RegisterScreen
import com.bagas.pinjam100.presentation.features.auth.ResetPasswordScreen
import com.bagas.pinjam100.presentation.features.guest.GuestHomeScreen
import com.bagas.pinjam100.presentation.features.home.HelpScreen
import com.bagas.pinjam100.presentation.features.home.HistoryScreen
import com.bagas.pinjam100.presentation.features.home.HomeScreen
import com.bagas.pinjam100.presentation.features.installment.LoanInstallmentBillListScreen
import com.bagas.pinjam100.presentation.features.loan.ApplicationListScreen
import com.bagas.pinjam100.presentation.features.loan.LoanApplicationDetailScreen
import com.bagas.pinjam100.presentation.features.loan.LoanApplicationScreen
import com.bagas.pinjam100.presentation.features.notification.NotificationScreen
import com.bagas.pinjam100.presentation.features.profile.AboutScreen
import com.bagas.pinjam100.presentation.features.profile.BankAccountDataScreen
import com.bagas.pinjam100.presentation.features.profile.EmploymentDataScreen
import com.bagas.pinjam100.presentation.features.profile.IdentityCardScreen
import com.bagas.pinjam100.presentation.features.profile.IdentityCardVerificationScreen
import com.bagas.pinjam100.presentation.features.profile.PersonalDataScreen
import com.bagas.pinjam100.presentation.features.profile.ProfileScreen
import com.bagas.pinjam100.presentation.features.profile.SelfieScreen
import com.bagas.pinjam100.presentation.features.profile.SummaryScreen
import com.bagas.pinjam100.presentation.features.simulation.SimulationScreen
import com.bagas.pinjam100.presentation.viewmodel.auth.AuthUiState
import com.bagas.pinjam100.presentation.viewmodel.auth.AuthViewModel
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerOnboardingViewModel
import com.bagas.pinjam100.presentation.viewmodel.document.DocumentViewModel
import com.bagas.pinjam100.presentation.viewmodel.installment.LoanInstallmentViewModel
import com.bagas.pinjam100.presentation.viewmodel.limit.LimitViewModel
import com.bagas.pinjam100.presentation.viewmodel.loanapplication.LoanApplicationViewModel
import com.bagas.pinjam100.presentation.viewmodel.wilayah.WilayahViewModel

private val bottomBarRoutes = setOf(
    HomeRoute::class,
    TransactionRoute::class,
    LoanApplicationListRoute::class,
    HelpRoute::class,
    ProfileRoute::class
)

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authState: AuthUiState,
) {
    val destinations = remember { TopLevelDestination.entries }

    val currentDestination =
        navController.currentBackStackEntryAsState().value?.destination

    val currentTab = destinations.firstOrNull { destination ->
        currentDestination?.hierarchy?.any {
            it.hasRoute(destination.route::class)
        } == true
    }

    val showBottomBar = currentDestination?.hierarchy?.any { destination ->
        bottomBarRoutes.any { route ->
            destination.hasRoute(route)
        }
    } == true

    if (authState.isRestoringSession) {
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    destinations = destinations,
                    currentTab = currentTab,
                    onDestinationClick = {
                        if (it != currentTab) {
                            navController.navigateToTab(it)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        val startDestination = remember(
            authState.isLoggedIn,
            authState.user?.profileCompleted
        ) {
            when {
                !authState.isLoggedIn -> AuthGraph
                authState.user?.profileCompleted == true -> HomeGraph
                else -> OnboardingGraph
            }
        }

        NavHost(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = if (showBottomBar) {
                    innerPadding.calculateBottomPadding()
                } else {
                    0.dp
                }
            ),
            navController = navController,
            startDestination = startDestination
        ) {
            authGraph(
                navController = navController
            )

            resetPasswordGraph(
                navController = navController
            )
            
            guestGraph()

            onboardingGraph(
                navController = navController,
                authState = authState
            )

            homeGraph(
                navController = navController,
                authState = authState
            )

            transactionGraph()

            loanGraph(
                navController = navController
            )

            helpGraph()

            profileGraph(
                navController = navController
            )

            notificationGraph()
            productGraph()
            simulationGraph()
        }
    }
}

@Composable
private fun AppBottomBar(
    destinations: List<TopLevelDestination>,
    currentTab: TopLevelDestination?,
    onDestinationClick: (TopLevelDestination) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .height(70.dp)
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            destinations.forEach { destination ->
                if (destination == TopLevelDestination.LOAN) {
                    Spacer(
                        modifier = Modifier.size(64.dp)
                    )
                } else {
                    NavigationBarItem(
                        selected = destination == currentTab,
                        onClick = {
                            onDestinationClick(destination)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.12f
                            )
                        ),
                        icon = {
                            Icon(
                                imageVector = destination.iconRes,
                                contentDescription = destination.labelRes
                            )
                        },
                        label = {
                            Text(destination.labelRes)
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-12).dp)
                .size(64.dp)
                .clickable {
                    onDestinationClick(TopLevelDestination.LOAN)
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = "Pinjaman",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(27.dp)
                )
            }
        }

        Text(
            text = "Pinjaman",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(64.dp)
        )
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
) {
    navigation<AuthGraph>(
        startDestination = AuthRoute
    ) {
        composable<AuthRoute> {
            AuthScreen(
                modifier = Modifier,
                navController = navController
            )
        }

        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = { user ->
                    if (user.profileCompleted) {
                        navController.navigate(HomeGraph) {
                            popUpTo(AuthGraph) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(OnboardingGraph) {
                            popUpTo(AuthGraph) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                onForgotPassword = {
                    navController.navigate(ForgotPasswordRoute)
                },
                onRegister = {
                    navController.navigate(RegisterRoute)
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = { phoneNumber ->
                    navController.navigate(
                        OtpRoute(
                            phoneNumber = phoneNumber
                        )
                    )
                },
                onLoginClick = {
                    navController.navigate(LoginRoute)
                }
            )
        }

        composable<OtpRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<OtpRoute>()

            OtpScreen(
                phoneNumber = route.phoneNumber,
                onVerifySuccess = {
                    navController.navigate(OnboardingGraph) {
                        popUpTo<OtpRoute> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<ForgotPasswordRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()

            ForgotPasswordScreen(
                onSubmitClick = { email ->
                    authViewModel.forgotPassword(email)
                },
                onLoginClick = {
                    navController.navigate(LoginRoute) {
                        popUpTo<ForgotPasswordRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<ChangePasswordRoute> {
            ChangePasswordScreen()
        }
    }
}

fun NavGraphBuilder.resetPasswordGraph(
    navController: NavHostController
) {
    composable<ResetPasswordRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://pinjam100.bagasaditya.com/reset-password?token={token}"
            }
        )
    ) { backStackEntry ->

        val route = backStackEntry.toRoute<ResetPasswordRoute>()
        val viewModel: AuthViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        ResetPasswordScreen(
            token = route.token,
            uiState = uiState,
            onResetPasswordClick = { password, confirmPassword ->
                viewModel.resetPassword(
                    token = route.token,
                    password = password,
                    confirmPassword = confirmPassword,
                    onSuccess = {
                        navController.navigate(LoginRoute) {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            },
            onLoginClick = {
                navController.navigate(LoginRoute) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

fun NavGraphBuilder.guestGraph() {
    navigation<GuestGraph>(
        startDestination = GuestHomeRoute
    ) {
        composable<GuestHomeRoute> {
            GuestHomeScreen()
        }
    }
}

fun NavGraphBuilder.onboardingGraph(
    navController: NavHostController,
    authState: AuthUiState
) {
    navigation<OnboardingGraph>(
        startDestination = PersonalDataRoute
    ) {
        composable<PersonalDataRoute> { backStackEntry ->
            val onboardingEntry = remember(backStackEntry) {
                navController.getBackStackEntry<OnboardingGraph>()
            }

            val onboardingViewModel: CustomerOnboardingViewModel =
                hiltViewModel(onboardingEntry)

            val onboardingState by onboardingViewModel.uiState.collectAsState()

            val wilayahViewModel: WilayahViewModel = hiltViewModel()
            val wilayahState by wilayahViewModel.uiState.collectAsState()

            PersonalDataScreen(
                onboardingState = onboardingState,
                wilayahState = wilayahState,
                onNationalIdChanged = onboardingViewModel::updateNationalId,
                onBirthDateChanged = onboardingViewModel::updateBirthDate,
                onPlaceOfBirthChanged = onboardingViewModel::updatePlaceOfBirth,
                onGenderChanged = onboardingViewModel::updateGender,
                onAddressChanged = onboardingViewModel::updateAddress,
                onPostalCodeChanged = onboardingViewModel::updatePostalCode,
                onProvinceSelected = {
                    wilayahViewModel.selectProvince(it)
                    onboardingViewModel.updateProvince(it.name)
                },
                onRegencySelected = {
                    wilayahViewModel.selectRegency(it)
                    onboardingViewModel.updateCity(it.name)
                },
                onDistrictSelected = {
                    wilayahViewModel.selectDistrict(it)
                    onboardingViewModel.updateDistrict(it.name)
                },
                onVillageSelected = {
                    wilayahViewModel.selectVillage(it)
                    onboardingViewModel.updateVillage(it.name)
                },
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(EmploymentDataRoute) }
            )
        }

        composable<EmploymentDataRoute> { backStackEntry ->
            val onboardingEntry = remember(backStackEntry) {
                navController.getBackStackEntry<OnboardingGraph>()
            }

            val onboardingViewModel: CustomerOnboardingViewModel =
                hiltViewModel(onboardingEntry)

            val onboardingState by onboardingViewModel.uiState.collectAsState()

            EmploymentDataScreen(
                onboardingState = onboardingState,
                onEmploymentTypeChanged = onboardingViewModel::updateEmploymentType,
                onCompanyNameChanged = onboardingViewModel::updateCompanyName,
                onPositionChanged = onboardingViewModel::updatePosition,
                onMonthlyIncomeChanged = onboardingViewModel::updateMonthlyIncome,
                onStartDateChanged = onboardingViewModel::updateStartDate,
                onCompanyAddressChanged = onboardingViewModel::updateCompanyAddress,
                onCompanyPhoneChanged = onboardingViewModel::updateCompanyPhone,
                onBack = {
                    navController.popBackStack()
                },
                onNext = {
                    navController.navigate(BankAccountDataRoute)
                }
            )
        }

        composable<BankAccountDataRoute> { backStackEntry ->
            val onboardingEntry = remember(backStackEntry) {
                navController.getBackStackEntry<OnboardingGraph>()
            }

            val onboardingViewModel: CustomerOnboardingViewModel =
                hiltViewModel(onboardingEntry)

            val onboardingState by onboardingViewModel.uiState.collectAsState()

            BankAccountDataScreen(
                onboardingState = onboardingState,
                onNamaBankChanged = onboardingViewModel::updateNamaBank,
                onNoRekeningChanged = onboardingViewModel::updateNoRekening,
                onAccountHolderChanged = onboardingViewModel::updateAccountHolder,
                onBack = {
                    navController.popBackStack()
                },
                onNext = {
                    navController.navigate(IdentityVerificationGraph)
                }
            )
        }

        composable<EmergencyContactRoute> {
        }

        composable<LoanReviewRoute> {
        }

        navigation<IdentityVerificationGraph>(
            startDestination = SelfieRoute
        ) {
            composable<SelfieRoute> { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<OnboardingGraph>()
                }

                val documentViewModel: DocumentViewModel =
                    hiltViewModel(onboardingEntry)

                val customerId = authState.user?.id

                if (customerId != null) {
                    SelfieScreen(
                        customerId = customerId,
                        documentViewModel = documentViewModel,
                        onBack = {
                            navController.popBackStack()
                        },
                        onNext = {
                            navController.navigate(IdentityCardPhotoRoute)
                        }
                    )
                }
            }

            composable<IdentityCardPhotoRoute> { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<OnboardingGraph>()
                }

                val documentViewModel: DocumentViewModel =
                    hiltViewModel(onboardingEntry)

                val customerId = authState.user?.id

                if (customerId != null) {
                    IdentityCardScreen(
                        customerId = customerId,
                        documentViewModel = documentViewModel,
                        onBack = {
                            navController.popBackStack()
                        },
                        onNext = {
                            navController.navigate(IdentityCardVerificationRoute)
                        }
                    )
                }
            }

            composable<IdentityCardVerificationRoute> { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<OnboardingGraph>()
                }

                val documentViewModel: DocumentViewModel =
                    hiltViewModel(onboardingEntry)

                val customerId = authState.user?.id

                if (customerId != null) {
                    IdentityCardVerificationScreen(
                        customerId = customerId,
                        documentViewModel = documentViewModel,
                        onBack = {
                            navController.popBackStack()
                        },
                        onNext = {
                            navController.navigate(SummaryRoute)
                        }
                    )
                }
            }

            composable<SummaryRoute> { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<OnboardingGraph>()
                }

                val onboardingViewModel: CustomerOnboardingViewModel =
                    hiltViewModel(onboardingEntry)

                val documentViewModel: DocumentViewModel =
                    hiltViewModel(onboardingEntry)

                val authViewModel: AuthViewModel = hiltViewModel()

                val customerId = authState.user?.id

                if (customerId != null) {
                    SummaryScreen(
                        customerId = customerId,
                        onboardingViewModel = onboardingViewModel,
                        documentViewModel = documentViewModel,
                        onBack = {
                            navController.popBackStack()
                        },
                        onProfileCompleted = {
                            authViewModel.markProfileCompleted()
                        },
                        onSubmit = {
                            navController.navigate(HomeGraph) {
                                popUpTo<OnboardingGraph> {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    authState: AuthUiState
) {
    navigation<HomeGraph>(
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            val limitViewModel: LimitViewModel = hiltViewModel()
            val limitState by limitViewModel.uiState.collectAsStateWithLifecycle()

            val customerId = authState.user?.id

            val lifecycleOwner = LocalLifecycleOwner.current

            DisposableEffect(lifecycleOwner, customerId) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        customerId?.let {
                            limitViewModel.getCustomerLimit(it)
                        }
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            HomeScreen(
                navController = navController,
                user = authState.user,
                limitState = limitState,
                onRefresh = {
                    customerId?.let {
                        limitViewModel.getCustomerLimit(it)
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.transactionGraph() {
    navigation<TransactionGraph>(
        startDestination = TransactionRoute
    ) {
        composable<TransactionRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.uiState.collectAsStateWithLifecycle()

            val customerId = authState.user?.id

            if (customerId != null) {
                HistoryScreen(
                    customerId = customerId
                )
            }
        }

        composable<TransactionDisbursementDetailRoute> {
        }

        composable<TransactionInstallmentDetailRoute> {
        }
    }
}

fun NavGraphBuilder.loanGraph(
    navController: NavHostController
) {
    navigation<LoanGraph>(
        startDestination = LoanApplicationListRoute
    ) {
        composable<LoanApplicationRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.uiState.collectAsStateWithLifecycle()

            val limitViewModel: LimitViewModel = hiltViewModel()
            val limitState by limitViewModel.uiState.collectAsStateWithLifecycle()

            val customerId = authState.user?.id

            LaunchedEffect(customerId) {
                customerId?.let {
                    limitViewModel.getCustomerLimit(it)
                }
            }

            if (customerId != null) {
                LoanApplicationScreen(
                    customerId = customerId,
                    limitState = limitState,
                    onRefresh = {
                        limitViewModel.getCustomerLimit(customerId)
                    }
                )
            }
        }

        composable<LoanApplicationListRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.uiState.collectAsStateWithLifecycle()

            val loanApplicationViewModel: LoanApplicationViewModel = hiltViewModel()

            val customerId = authState.user?.id

            if (customerId != null) {
                ApplicationListScreen(
                    customerId = customerId,
                    viewModel = loanApplicationViewModel,
                    onApplicationClick = { applicationId ->
                        navController.navigate(
                            LoanApplicationDetailRoute(
                                applicationId
                            )
                        )
                    },
                    onInstallmentClick = {
                        navController.navigate(LoanInstallmentListRoute)
                    },
                    onLoanApplicationClick = {
                        navController.navigate(LoanApplicationRoute)
                    }
                )
            }
        }

        composable<LoanApplicationDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LoanApplicationDetailRoute>()

            val loanApplicationViewModel: LoanApplicationViewModel = hiltViewModel()
            val uiState by loanApplicationViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(route.applicationId) {
                loanApplicationViewModel.getById(route.applicationId)
            }

            val application = uiState.selectedLoanApplication

            if (application != null) {
                LoanApplicationDetailScreen(
                    application = application,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable<LoanInstallmentListRoute> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.uiState.collectAsStateWithLifecycle()

            val loanInstallmentViewModel: LoanInstallmentViewModel = hiltViewModel()

            val customerId = authState.user?.id

            if (customerId != null) {
                LoanInstallmentBillListScreen(
                    customerId = customerId,
                    onBack = {
                        navController.popBackStack()
                    },
                    viewModel = loanInstallmentViewModel
                )
            }
        }

        composable<LoanApplicationStatusRoute> {
        }

        composable<LoanApprovalRoute> {
        }

        composable<DisbursementRoute> {
        }

        composable<DisbursementSuccessRoute> {
        }

        composable<ActiveLoanRoute> {
        }

        composable<InstallmentScheduleRoute> {
        }

        composable<InstallmentPaymentRoute> {
        }

        composable<InstallmentPaymentSuccessRoute> {
        }
    }
}

fun NavGraphBuilder.notificationGraph() {
    composable<NotificationRoute> {
        NotificationScreen()
    }
}

fun NavGraphBuilder.productGraph() {
    navigation<ProductGraph>(
        startDestination = ProductListRoute
    ) {
        composable<ProductListRoute> {
        }

        composable<ProductDetailRoute> {
        }
    }
}

fun NavGraphBuilder.simulationGraph() {
    composable<LoanSimulationRoute> {
        SimulationScreen()
    }
}

fun NavGraphBuilder.helpGraph() {
    navigation<HelpGraph>(
        startDestination = HelpRoute
    ) {
        composable<HelpRoute> {
            HelpScreen()
        }

        composable<AboutRoute> {
            AboutScreen()
        }
    }
}

fun NavGraphBuilder.profileGraph(
    navController: NavHostController
) {
    navigation<ProfileGraph>(
        startDestination = ProfileRoute
    ) {
        composable<ProfileRoute> {
            ProfileScreen(
                onChangePassword = {
                    navController.navigate(ChangePasswordRoute)
                },
                onHelp = {
                    navController.navigate(HelpRoute)
                },
                onAbout = {
                    navController.navigate(AboutRoute)
                },
                onLogout = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun NavHostController.navigateToTab(
    destination: TopLevelDestination
) {
    navigate(destination.route) {
        popUpTo(HomeGraph) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}