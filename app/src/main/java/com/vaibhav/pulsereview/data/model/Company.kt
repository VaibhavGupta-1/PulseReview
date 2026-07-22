package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val id: String = "",
    val name: String = ""
)
