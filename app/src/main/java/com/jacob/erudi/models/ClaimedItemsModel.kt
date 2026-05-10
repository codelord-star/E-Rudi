package com.jacob.erudi.models

data class ClaimedItem(
    val id: String = "",
    val itemName: String = "",
    val category: String = "",
    val description: String = "",
    val locationFound: String = "",
    val dateFound: String = "",
    val imageUrl: String = "",
    val originalOwnerEmail: String = "",
    val claimerEmail: String = ""
)