package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Diversity3
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.AppNavigationScreen

data class NavTabItem(
    val screen: AppNavigationScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun BottomNavBar(
    currentScreen: AppNavigationScreen,
    onTabSelected: (AppNavigationScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavTabItem(
            screen = AppNavigationScreen.HOME,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_home_tab"
        ),
        NavTabItem(
            screen = AppNavigationScreen.PRINT_FLOW,
            label = "Print",
            selectedIcon = Icons.Filled.Print,
            unselectedIcon = Icons.Outlined.Print,
            testTag = "nav_print_tab"
        ),
        NavTabItem(
            screen = AppNavigationScreen.BUDDY,
            label = "Buddy",
            selectedIcon = Icons.Filled.Diversity3,
            unselectedIcon = Icons.Outlined.Diversity3,
            testTag = "nav_buddy_tab"
        ),
        NavTabItem(
            screen = AppNavigationScreen.ORDERS,
            label = "Orders",
            selectedIcon = Icons.Filled.ReceiptLong,
            unselectedIcon = Icons.Outlined.ReceiptLong,
            testTag = "nav_orders_tab"
        ),
        NavTabItem(
            screen = AppNavigationScreen.REWARDS,
            label = "Rewards",
            selectedIcon = Icons.Filled.MilitaryTech,
            unselectedIcon = Icons.Outlined.MilitaryTech,
            testTag = "nav_rewards_tab"
        ),
        NavTabItem(
            screen = AppNavigationScreen.PROFILE,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            testTag = "nav_profile_tab"
        )
    )

    Surface(
        color = PrintPilotSurfaceLowest,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PrintPilotBorderLight),
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen ||
                        (item.screen == AppNavigationScreen.PRINT_FLOW && currentScreen == AppNavigationScreen.PRINT_FLOW) ||
                        (item.screen == AppNavigationScreen.ORDERS && currentScreen == AppNavigationScreen.QUEUE_TRACKING)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (isSelected) {
                                Modifier.background(PrintPilotPrimaryContainer.copy(alpha = 0.15f))
                            } else {
                                Modifier
                            }
                        )
                        .clickable { onTabSelected(item.screen) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag(item.testTag)
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
