package com.quranku.quranku_app.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.ui.util.History
import com.quranku.quranku_app.ui.util.Menu_book

private val navBarItems = arrayOf(
    Icons.Outlined.Home,
    History,
    Menu_book,
    Icons.Outlined.Person,
)

private val navBarNames = arrayOf(
    "Home",
    "History",
    "Qur'an",
    "Profile"
)

private val COLOR_NORMAL = Color(0xFF767981)
private val COLOR_SELECTED = Color(0xFF2A6989)

private val ICON_SIZE = 24.dp


@Composable
fun AnimatableIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    iconSize: Dp = ICON_SIZE,
    scale: Float = 1.5f,
    color: Color = COLOR_NORMAL,
    onClick: () -> Unit
) {
    // Animation params
    val animatedScale: Float by animateFloatAsState(
        targetValue = scale,
        // Here the animation spec serves no purpose but to demonstrate in slow speed.
        animationSpec = TweenSpec(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        )
    )

    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        )
    )

    // Animation for the size of the IconButton
    val animatedIconButtonSize: Dp by animateDpAsState(
        targetValue = iconSize.times(scale),
        animationSpec = TweenSpec(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        )
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(animatedIconButtonSize)
            .padding(0.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "dummy",
            tint = animatedColor,
            modifier = modifier.scale(animatedScale)
        )

    }
}

@Composable
fun BottomNavBar2(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier,
    iconSize: Dp = ICON_SIZE,
    selectedIconScale: Float = 1.5f
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tentukan posisi item yang dipilih berdasarkan rute saat ini
    val selectedIndex = when (currentRoute) {
        "home" -> 0
        "history" -> 1
        "quran" -> 2
        "profile" -> 3
        else -> 0 // Default ke "home" jika rute tidak dikenal
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(iconSize.times(selectedIconScale + 0.5f))
            .background(color = Color.White) // Disesuaikan dengan tampilan
    ) {
        for ((index, icon) in navBarItems.withIndex()) {
            val route = when (index) {
                0 -> "home"
                1 -> "history"
                2 -> "quran"
                3 -> "profile"
                else -> "home"
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f) // Proporsi area klik dibagi rata
                    .fillMaxWidth()
                    .clickable(
                        indication = null, // Menghilangkan warna klik
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Navigasi hanya jika rute saat ini berbeda
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo("home") { // Atur popUpTo ke "home"
                                    inclusive = false // Jangan hapus "home" dari stack
                                    saveState = true // Simpan state saat berpindah
                                }
                                restoreState = true // Pulihkan state saat kembali ke "home"
                                launchSingleTop = true // Hindari instance ganda
                            }
                        }
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Ikon juga dapat diklik
                    AnimatableIcon(
                        imageVector = icon,
                        scale = if (selectedIndex == index) 1.3f else 1.0f,
                        color = if (selectedIndex == index) COLOR_SELECTED else COLOR_NORMAL,
                        iconSize = ICON_SIZE,
                        onClick = {
                            // Navigasi hanya jika rute saat ini berbeda
                            if (route != currentRoute) {
                                navController.navigate(route) {
                                    popUpTo("home") {
                                        inclusive = false
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                    Text(
                        text = navBarNames[index],
                        fontSize = 12.sp,
                        color = if (selectedIndex == index) COLOR_SELECTED else COLOR_NORMAL
                    )
                }
            }
        }
    }
}





@Preview(group = "Icon")
@Composable
fun PreviewIcon() {
    Surface {

        var selected by remember {
            mutableStateOf(false)
        }

        AnimatableIcon(
            imageVector = Icons.Outlined.FavoriteBorder,
            scale = if (selected) 1.5f else 1f,
            color = if (selected) COLOR_SELECTED else COLOR_NORMAL,
        ) {
            selected = !selected
        }
    }
}

@Preview(group = "Main", name = "Bottom bar - animated")
@Composable
fun PreviewBottomNavBar2() {
    Surface {
        BottomNavBar2()
    }
}