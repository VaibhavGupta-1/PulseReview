package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
    val id: String = "",
    val name: String = "",
    val description: String = ""
)
