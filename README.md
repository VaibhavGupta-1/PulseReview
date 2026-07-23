<p align="center">
  <img src="screenshots/logo.png" width="120" alt="PulseReview Logo"/>
</p>

<h1 align="center">PulseReview</h1>

<p align="center">
  <i>A multi-tenant monthly performance feedback platform for managers, employees, and HR</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.20-7F52FF?style=flat&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=flat&logo=android&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Supabase-Postgres_%26_Auth-3FCF8E?style=flat&logo=supabase&logoColor=white" alt="Supabase"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=flat&logo=android&logoColor=white" alt="MVVM"/>
</p>

---

## Overview

PulseReview is a multi-tenant employee performance review platform built for modern organizations. Managers provide structured monthly feedback across 5 fixed parameters for their direct reports, while employees track their historical ratings over time. The same platform supports multiple isolated companies and allows an individual to simultaneously act as a manager to some and a report to another.

---

## Features

- **Role-Flexible Feedback:** Supports contextual relationships where a single user can simultaneously serve as a manager (reviewer) for direct reports and an employee (reviewee) for their superior.
- **Multi-Tenant Company Isolation:** Complete data separation across tenants enforced by PostgreSQL Row Level Security (RLS).
- **Monthly Feedback Cycles:** Standardized monthly evaluation rounds covering 5 fixed rating parameters.
- **Duplicate-Submission Prevention:** Pre-submission checks prevent multiple reviews per cycle, rendering a read-only view of existing scores if already submitted.
- **HR Submission Tracking Dashboard:** Company-wide review completion tracking with live completion status indicators.
- **Employee Score History:** Historical performance tracking grouped by period, preserving records across past review cycles.

---

## Screenshots

<table>
  <tr>
    <td align="center" width="50%">
      <img src="screenshots/splash.jpeg" width="250" alt="Splash Screen"/><br/>
      <b>Splash Screen</b>
    </td>
    <td align="center" width="50%">
      <img src="screenshots/login.jpeg" width="250" alt="Login Screen"/><br/>
      <b>Login Screen</b>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <img src="screenshots/dashboard.jpeg" width="250" alt="Manager Dashboard"/><br/>
      <b>Manager Dashboard — Priya, 6 direct reports</b>
    </td>
    <td align="center" width="50%">
      <img src="screenshots/employee_dashboard.jpeg" width="250" alt="Employee Dashboard"/><br/>
      <b>Employee Dashboard — Rohan, reports to Priya</b>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <img src="screenshots/feedback_form.jpeg" width="250" alt="Give Feedback Form"/><br/>
      <b>Give Feedback Form</b>
    </td>
    <td align="center" width="50%">
      <img src="screenshots/feedback_history.jpeg" width="250" alt="Feedback History"/><br/>
      <b>Feedback History, read-only</b>
    </td>
  </tr>
</table>

---

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose, Material 3
- **Architecture:** MVVM, StateFlow, Coroutines
- **Navigation:** Navigation Compose (Type-safe `@Serializable` routes)
- **Backend:** Supabase (PostgreSQL, Supabase Auth, Row Level Security)
- **Networking & Serialization:** `supabase-kt`, Ktor Client, `kotlinx-serialization`

---

## Architecture & Data Model

Reporting relationships are modeled as dedicated graph edges in a `reporting_relationships` table (`manager_id` → `employee_id` with `active_from`/`active_to` dates) rather than a static `role` or `manager_id` column on the user record. This design decision enables one person to manage a team while reporting to a executive, and allows both flat and deeply hierarchical organizational structures to evolve over time without schema modifications.

---

## Assumptions

- **Relationship History:** Reporting relationships can change over time while historical feedback records remain preserved via `active_from` and `active_to` date bounds.
- **Feedback Cycles:** Feedback cycles are monthly and calendar-aligned.
- **HR Access Control:** HR access is scoped per company via a dedicated `hr_users` table rather than a fixed user role attribute.
- **Contextual Roles:** A user can hold manager and employee status simultaneously depending on reporting relationship context.
- **Data Security:** Row Level Security (RLS) enforces company isolation at the database layer.

---

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/VaibhavGupta-1/PulseReview.git
   cd PulseReview
   ```

2. **Configure Supabase Credentials:**
   Add credentials to `local.properties`:
   ```properties
   SUPABASE_URL=https://your-supabase-project.supabase.co
   SUPABASE_KEY=your-supabase-anon-key
   ```

3. **Open in Android Studio:**
   Open project in Android Studio (Hedgehog or newer).

4. **Build & Run:**
   Run `./gradlew assembleDebug` or launch the application on an emulator or connected device.

---

## Download

📱 [Download APK](https://drive.google.com/file/d/1ctMjAncdgJMxPRwVDvgmeDh73-JXx2Ln/view?usp=drive_link)

