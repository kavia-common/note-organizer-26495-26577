package com.oceanpro.notes.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * Formatting utilities.
 */
object Formatters {
    /**
     * PUBLIC_INTERFACE
     * Format timestamp to human readable string.
     */
    fun formatDate(time: Long): String {
        val sdf = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }
}
