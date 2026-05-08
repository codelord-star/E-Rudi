package com.jacob.erudi.models

data class LostItem(
    val itemName: String = "",
    val category: String = "",
    val description: String = "",
    val location: String = "",
    val dateLost: String = "",
    val imageUrl: String = "",
    val email: String = ""
)