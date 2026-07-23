# PulseReview

> A multi-tenant employee performance review platform for monthly feedback cycles.

---

## Overview

PulseReview is a multi-tenant performance review application built for modern enterprises. It allows companies to run monthly performance feedback cycles where managers evaluate direct reports across structured parameters, employees track their historical performance, and HR personnel monitor real-time completion status across the organization.

---

## Core Features

- **Role-Flexible Feedback:** A single user can act as both a manager (reviewer) and a direct report (reviewee) simultaneously depending on organizational context.
- **Multi-Tenant Company Isolation:** Complete tenant separation ensuring users access data only within their assigned company.
- **Monthly Feedback Cycles:** Standardized monthly evaluation rounds covering 5 fixed performance parameters.
- **Duplicate-Submission Prevention:** Automatic pre-submission checks preventing multiple submissions for the same employee in an active cycle, switching to a read-only view.
- **HR Submission Tracking:** Dedicated HR view displaying company-wide review completion status with live completion badges.
- **Employee Score History:** Historical performance tracking grouped by period, preserving feedback records over time.

---

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose & Material 3
- **Architecture:** MVVM with StateFlow & Coroutines
- **Navigation:** Navigation Compose (Type-safe routes with `@Serializable` targets)
- **Backend & Database:** Supabase (PostgreSQL, Supabase Auth, Row Level Security)
- **Network & Serialization:** `supabase-kt`, Ktor Client, `kotlinx-serialization`

---

## Architecture

PulseReview models organizational structure by decoupling user identities from managerial roles. Reporting relationships are stored as active edges in a dedicated `reporting_relationships` table (`manager_id` → `employee_id` with `active_from`/`active_to` timestamps) rather than fixed `role` or `manager_id` columns on the user record. This design allows users to simultaneously manage reports and report to superiors, while supporting dynamic, matrix, or evolving org structures without schema changes.

---

## Screenshots

![Splash Screen](screenshots/splash.png)  
*Splash screen introducing the PulseReview platform.*

![Login Screen](screenshots/login.png)  
*Sign-in screen supporting Supabase authentication.*

![Manager Dashboard](screenshots/dashboard.png)  
*Priya's dashboard listing her 6 direct reports with options to give feedback or view history.*

![Employee Dashboard](screenshots/employee_dashboard.png)  
*Rohan's dashboard showing Priya as his one report and access to his own feedback history.*

![Give Feedback Form](screenshots/feedback_form.png)  
*The 5-parameter scoring form with 1–5 ratings and optional comments.*

![Feedback History](screenshots/feedback_history.png)  
*The read-only history view displaying past monthly performance scores.*

---

## Assumptions

- **Reporting Relationships:** Reporting relationships can change over time while historical records remain intact via `active_from` and `active_to` dates.
- **Feedback Cycles:** Feedback cycles are monthly and aligned with calendar periods.
- **HR Access Control:** HR access is scoped per company via a dedicated `hr_users` mapping table rather than a static user role attribute.
- **Contextual Roles:** A user can hold manager and employee status simultaneously depending on reporting relationships.
- **Data Security:** Row Level Security (RLS) on PostgreSQL enforces company-level data isolation at the database layer rather than relying solely on client-side logic.

---

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/VaibhavGupta-1/PulseReview.git
   cd PulseReview
   ```

2. **Configure Supabase Credentials:**
   Add your Supabase credentials to `local.properties`:
   ```properties
   SUPABASE_URL=https://your-supabase-project.supabase.co
   SUPABASE_KEY=your-supabase-anon-key
   ```

3. **Build & Run:**
   Open the project in **Android Studio** and run `./gradlew assembleDebug` or launch on a device/emulator.
