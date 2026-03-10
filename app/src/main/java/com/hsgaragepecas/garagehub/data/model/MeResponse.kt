package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeResponse(
    @SerialName("subscription_status")
    val subscriptionStatus: String? = null,
    @SerialName("plan_expires_at")
    val planExpiresAt: String? = null,
    @SerialName("trial_started_at")
    val trialStartedAt: String? = null,
    @SerialName("trial_days")
    val trialDays: Int? = 0
)
