package com.vaibhav.pulsereview.core.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

open class RemoteDataSource(
    private val client: SupabaseClient = SupabaseManager.client
) {
    protected val auth: Auth
        get() = client.auth

    protected val postgrest: Postgrest
        get() = client.postgrest
}
