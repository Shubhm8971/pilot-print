package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CreateBuddyRequestDialog
import com.example.ui.components.OtpInputDialog
import com.example.ui.components.PrivateCallDialog
import com.example.ui.components.TopNavBar
import com.example.ui.screens.BuddyScreen
import com.example.ui.screens.ConfigureEditScreen
import com.example.ui.screens.HeroLandingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.PrintWizardScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QueueTrackingScreen
import com.example.ui.screens.ReviewPayScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppNavigationScreen
import com.example.viewmodel.PrintPilotViewModel
import com.example.viewmodel.PrintWizardStep
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PrintPilotApp()
            }
        }
    }
}

@Composable
fun PrintPilotApp(
    viewModel: PrintPilotViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Toast / Snackbar feedback
    LaunchedEffect(uiState.userToastMessage) {
        uiState.userToastMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
            }
            viewModel.clearToast()
        }
    }

    // Handle Hardware Back Button
    BackHandler(enabled = uiState.currentScreen != AppNavigationScreen.HOME) {
        when (uiState.currentScreen) {
            AppNavigationScreen.PRINT_FLOW -> {
                when (uiState.printStep) {
                    PrintWizardStep.STEP_1_2_SHOP_UPLOAD -> viewModel.navigateTo(AppNavigationScreen.HOME)
                    PrintWizardStep.STEP_3_CONFIGURE_EDIT -> viewModel.setPrintStep(PrintWizardStep.STEP_1_2_SHOP_UPLOAD)
                    PrintWizardStep.STEP_4_REVIEW_PAY -> viewModel.setPrintStep(PrintWizardStep.STEP_3_CONFIGURE_EDIT)
                    PrintWizardStep.STEP_5_SUCCESS -> viewModel.navigateTo(AppNavigationScreen.HOME)
                }
            }
            AppNavigationScreen.QUEUE_TRACKING -> viewModel.navigateTo(AppNavigationScreen.ORDERS)
            AppNavigationScreen.HERO_LANDING -> viewModel.navigateTo(AppNavigationScreen.HOME)
            else -> viewModel.navigateTo(AppNavigationScreen.HOME)
        }
    }

    val isChildScreen = uiState.currentScreen == AppNavigationScreen.QUEUE_TRACKING ||
            uiState.currentScreen == AppNavigationScreen.HERO_LANDING ||
            (uiState.currentScreen == AppNavigationScreen.PRINT_FLOW && uiState.printStep != PrintWizardStep.STEP_1_2_SHOP_UPLOAD)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            TopNavBar(
                title = when (uiState.currentScreen) {
                    AppNavigationScreen.PRINT_FLOW -> "Print Order"
                    AppNavigationScreen.QUEUE_TRACKING -> "Live Queue"
                    AppNavigationScreen.HERO_LANDING -> "Print Pilot"
                    AppNavigationScreen.BUDDY -> "Buddy Network"
                    AppNavigationScreen.ORDERS -> "Orders & Queue"
                    AppNavigationScreen.REWARDS -> "Campus Coins"
                    AppNavigationScreen.PROFILE -> "Student Profile"
                    else -> "Print Pilot"
                },
                coinsBalance = uiState.profile.coinsBalance,
                showBackButton = isChildScreen,
                onBackClick = {
                    when (uiState.currentScreen) {
                        AppNavigationScreen.PRINT_FLOW -> {
                            when (uiState.printStep) {
                                PrintWizardStep.STEP_1_2_SHOP_UPLOAD -> viewModel.navigateTo(AppNavigationScreen.HOME)
                                PrintWizardStep.STEP_3_CONFIGURE_EDIT -> viewModel.setPrintStep(PrintWizardStep.STEP_1_2_SHOP_UPLOAD)
                                PrintWizardStep.STEP_4_REVIEW_PAY -> viewModel.setPrintStep(PrintWizardStep.STEP_3_CONFIGURE_EDIT)
                                PrintWizardStep.STEP_5_SUCCESS -> viewModel.navigateTo(AppNavigationScreen.HOME)
                            }
                        }
                        AppNavigationScreen.QUEUE_TRACKING -> viewModel.navigateTo(AppNavigationScreen.ORDERS)
                        AppNavigationScreen.HERO_LANDING -> viewModel.navigateTo(AppNavigationScreen.HOME)
                        else -> viewModel.navigateTo(AppNavigationScreen.HOME)
                    }
                },
                onCoinsClick = { viewModel.navigateTo(AppNavigationScreen.REWARDS) },
                onProfileClick = { viewModel.navigateTo(AppNavigationScreen.PROFILE) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = uiState.currentScreen,
                onTabSelected = { screen ->
                    if (screen == AppNavigationScreen.PRINT_FLOW) {
                        viewModel.startNewPrintJob()
                    } else {
                        viewModel.navigateTo(screen)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    AppNavigationScreen.HOME -> {
                        HomeScreen(
                            uiState = uiState,
                            onStartNewPrint = { shop ->
                                viewModel.startNewPrintJob(shop)
                            },
                            onViewOrderTracking = { order ->
                                viewModel.viewOrderTracking(order)
                            },
                            onFilterChanged = { filter ->
                                viewModel.setShopFilter(filter)
                            },
                            onNavigateTo = { screen ->
                                viewModel.navigateTo(screen)
                            }
                        )
                    }

                    AppNavigationScreen.PRINT_FLOW -> {
                        when (uiState.printStep) {
                            PrintWizardStep.STEP_1_2_SHOP_UPLOAD -> {
                                PrintWizardScreen(
                                    uiState = uiState,
                                    onSelectShop = { shop -> viewModel.selectShop(shop) },
                                    onAddDocument = { name, pages -> viewModel.addDocument(name, pages) },
                                    onRemoveDocument = { id -> viewModel.removeDocument(id) },
                                    onContinueToEdit = { viewModel.setPrintStep(PrintWizardStep.STEP_3_CONFIGURE_EDIT) }
                                )
                            }
                            PrintWizardStep.STEP_3_CONFIGURE_EDIT -> {
                                ConfigureEditScreen(
                                    uiState = uiState,
                                    onColorChanged = { viewModel.updateColorOutput(it) },
                                    onSidesChanged = { viewModel.updateSides(it) },
                                    onBindingChanged = { viewModel.updateBinding(it) },
                                    onDeletePage = {
                                        Toast.makeText(context, "Page deleted from print layout", Toast.LENGTH_SHORT).show()
                                    },
                                    onProceedToReview = { viewModel.setPrintStep(PrintWizardStep.STEP_4_REVIEW_PAY) }
                                )
                            }
                            PrintWizardStep.STEP_4_REVIEW_PAY -> {
                                ReviewPayScreen(
                                    uiState = uiState,
                                    onPriorityToggled = { viewModel.updatePriorityQueue(it) },
                                    onCoinsToggled = { viewModel.toggleCoinsDiscount(it) },
                                    onConfirmAndPay = {
                                        val placedOrder = viewModel.confirmOrderAndPay()
                                        viewModel.viewOrderTracking(placedOrder)
                                    }
                                )
                            }
                            PrintWizardStep.STEP_5_SUCCESS -> {
                                QueueTrackingScreen(
                                    order = uiState.selectedOrderForTracking,
                                    onAdvanceStep = { ordId -> viewModel.advanceOrderStep(ordId) },
                                    onRequestBuddy = {
                                        viewModel.navigateTo(AppNavigationScreen.BUDDY)
                                        viewModel.setCreateBuddyDialogVisible(true)
                                    },
                                    onBackToHome = { viewModel.navigateTo(AppNavigationScreen.HOME) }
                                )
                            }
                        }
                    }

                    AppNavigationScreen.QUEUE_TRACKING -> {
                        QueueTrackingScreen(
                            order = uiState.selectedOrderForTracking,
                            onAdvanceStep = { ordId -> viewModel.advanceOrderStep(ordId) },
                            onRequestBuddy = {
                                viewModel.navigateTo(AppNavigationScreen.BUDDY)
                                viewModel.setCreateBuddyDialogVisible(true)
                            },
                            onBackToHome = { viewModel.navigateTo(AppNavigationScreen.HOME) }
                        )
                    }

                    AppNavigationScreen.BUDDY -> {
                        BuddyScreen(
                            uiState = uiState,
                            onAcceptRequest = { request -> viewModel.acceptBuddyRequest(request) },
                            onOpenOtpVerify = { request -> viewModel.openOtpVerification(request) },
                            onCreateRequestClick = { viewModel.setCreateBuddyDialogVisible(true) }
                        )
                    }

                    AppNavigationScreen.ORDERS -> {
                        OrdersScreen(
                            uiState = uiState,
                            onOrderClick = { order -> viewModel.viewOrderTracking(order) },
                            onStartNewOrder = { viewModel.startNewPrintJob() }
                        )
                    }

                    AppNavigationScreen.REWARDS -> {
                        RewardsScreen(
                            uiState = uiState,
                            onRedeemReward = { reward -> viewModel.redeemReward(reward) }
                        )
                    }

                    AppNavigationScreen.PROFILE -> {
                        ProfileScreen(
                            profile = uiState.profile,
                            onViewHeroLanding = { viewModel.navigateTo(AppNavigationScreen.HERO_LANDING) }
                        )
                    }

                    AppNavigationScreen.HERO_LANDING -> {
                        HeroLandingScreen(
                            onGetStarted = { viewModel.startNewPrintJob() }
                        )
                    }
                }
            }

            // Interactive Modal Dialogs
            if (uiState.isPrivateCallActive) {
                PrivateCallDialog(
                    buddyName = uiState.callingBuddyName,
                    onDismiss = { viewModel.closePrivateCall() },
                    onOpenOtpVerification = {
                        val buddyReq = uiState.buddyRequests.firstOrNull { it.requesterName.contains(uiState.callingBuddyName, ignoreCase = true) }
                            ?: uiState.buddyRequests.firstOrNull()
                        if (buddyReq != null) {
                            viewModel.openOtpVerification(buddyReq)
                        }
                    }
                )
            }

            if (uiState.showOtpVerificationDialog) {
                OtpInputDialog(
                    request = uiState.selectedBuddyRequestForOtp,
                    isVerifiedSuccess = uiState.isOtpVerifiedSuccess,
                    onVerify = { enteredOtp -> viewModel.verifyPickupOtp(enteredOtp) },
                    onDismiss = { viewModel.closeOtpVerification() },
                    onReturnToOrders = {
                        viewModel.closeOtpVerification()
                        viewModel.navigateTo(AppNavigationScreen.ORDERS)
                    }
                )
            }

            if (uiState.showCreateBuddyDialog) {
                CreateBuddyRequestDialog(
                    shops = uiState.shops,
                    coinsBalance = uiState.profile.coinsBalance,
                    onDismiss = { viewModel.setCreateBuddyDialogVisible(false) },
                    onSubmit = { shop, coins, pages, note ->
                        viewModel.createBuddyRequest(shop, coins, pages, note)
                    }
                )
            }
        }
    }
}

