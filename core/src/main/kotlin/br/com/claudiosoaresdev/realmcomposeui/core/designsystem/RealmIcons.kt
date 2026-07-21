package br.com.claudiosoaresdev.realmcomposeui.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Registry de ícones. O servidor manda um símbolo (`crown`, `dragon_egg`…);
 * símbolo desconhecido cai em placeholder, nunca em exceção.
 */
fun sduiIcon(symbol: String?): ImageVector = when (symbol) {
    "menu" -> Icons.Filled.Menu
    "notifications" -> Icons.Filled.Notifications
    "home" -> Icons.Filled.Home
    "map" -> Icons.Filled.Place
    "description" -> Icons.AutoMirrored.Filled.List
    "menu_book" -> Icons.AutoMirrored.Filled.List
    "person" -> Icons.Filled.Person
    "profile" -> Icons.Filled.AccountCircle
    "history" -> Icons.Filled.DateRange
    "crown" -> Icons.Filled.Star
    "tower" -> Icons.Filled.LocationOn
    "castle" -> Icons.Filled.LocationOn
    "dragon_egg" -> Icons.Filled.Favorite
    "handshake" -> Icons.Filled.ThumbUp
    "arrow_forward" -> Icons.AutoMirrored.Filled.ArrowForward
    else -> Icons.Filled.Star
}
