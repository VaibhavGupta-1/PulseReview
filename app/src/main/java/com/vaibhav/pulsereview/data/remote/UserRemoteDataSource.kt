package com.vaibhav.pulsereview.data.remote

import com.vaibhav.pulsereview.core.network.SupabaseManager
import com.vaibhav.pulsereview.data.model.HrUser
import com.vaibhav.pulsereview.data.model.ReportingRelationship
import com.vaibhav.pulsereview.data.model.ReportingRelationshipWithNames
import com.vaibhav.pulsereview.data.model.User
import io.github.jan.supabase.postgrest.from

class UserRemoteDataSource {

    private val client = SupabaseManager.client

    /** Fetch the logged-in user's row from the users table. */
    suspend fun fetchUserById(userId: String): User {
        return client.from("users").select {
            filter { eq("id", userId) }
        }.decodeSingle<User>()
    }

    /**
     * Fetch v_reporting_relationships_with_names where manager_id matches the given
     * userId — returns the manager's list of direct reports with names.
     */
    suspend fun fetchDirectReports(managerId: String): List<ReportingRelationshipWithNames> {
        return client.from("v_reporting_relationships_with_names").select {
            filter { eq("manager_id", managerId) }
        }.decodeList<ReportingRelationshipWithNames>()
    }

    /**
     * Fetch reporting_relationships where employee_id matches the given
     * userId — returns who this person reports to.
     */
    suspend fun fetchManagersFor(employeeId: String): List<ReportingRelationship> {
        return client.from("reporting_relationships").select {
            filter { eq("employee_id", employeeId) }
        }.decodeList<ReportingRelationship>()
    }

    /**
     * Check whether a user is an HR user within a given company.
     * Returns the HrUser row if it exists, null otherwise.
     */
    suspend fun fetchHrUser(userId: String, companyId: String): HrUser? {
        return client.from("hr_users").select {
            filter {
                eq("user_id", userId)
                eq("company_id", companyId)
            }
        }.decodeList<HrUser>().firstOrNull()
    }
}
