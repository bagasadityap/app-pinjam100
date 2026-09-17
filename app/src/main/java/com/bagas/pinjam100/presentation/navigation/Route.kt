package com.bagas.pinjam100.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object AuthGraph : AppRoute

@Serializable
data object AuthRoute : AppRoute

@Serializable
data object LoginRoute : AppRoute

@Serializable
data object RegisterRoute : AppRoute

@Serializable
data class OtpRoute(
    val phoneNumber: String
) : AppRoute

@Serializable
data object ForgotPasswordRoute : AppRoute

@Serializable
data object ResetPasswordGraph : AppRoute

@Serializable
data class ResetPasswordRoute (
    val token: String
) : AppRoute

@Serializable
data object ChangePasswordRoute : AppRoute

@Serializable
data object OnboardingGraph : AppRoute

@Serializable
data object GuestGraph : AppRoute

@Serializable
data object GuestHomeRoute : AppRoute

@Serializable
data object HomeGraph : AppRoute

@Serializable
data object HomeRoute : AppRoute

@Serializable
data object LoanGraph : AppRoute

@Serializable
data object LoanApplicationRoute : AppRoute

@Serializable
data object LoanApplicationListRoute : AppRoute

@Serializable
data object LoanApplicationStatusRoute : AppRoute

@Serializable
data class LoanApplicationDetailRoute(
    val applicationId: String
) : AppRoute

@Serializable
data object LoanInstallmentListRoute : AppRoute

@Serializable
data object LoanApprovalRoute : AppRoute

@Serializable
data object DisbursementRoute : AppRoute

@Serializable
data object DisbursementSuccessRoute : AppRoute

@Serializable
data object ActiveLoanRoute : AppRoute

@Serializable
data object InstallmentScheduleRoute : AppRoute

@Serializable
data object InstallmentPaymentRoute : AppRoute

@Serializable
data object InstallmentPaymentSuccessRoute : AppRoute

@Serializable
data object LoanApplicationGraph : AppRoute

@Serializable
data object PersonalDataRoute : AppRoute

@Serializable
data object EmploymentDataRoute : AppRoute

@Serializable
data object BankAccountDataRoute : AppRoute

@Serializable
data object EmergencyContactRoute : AppRoute

@Serializable
data object LoanReviewRoute : AppRoute

@Serializable
data object IdentityVerificationGraph : AppRoute

@Serializable
data object SelfieRoute : AppRoute

@Serializable
data object IdentityCardPhotoRoute : AppRoute

@Serializable
data object IdentityCardVerificationRoute : AppRoute

@Serializable
data object SummaryRoute : AppRoute

@Serializable
data object TransactionGraph : AppRoute

@Serializable
data object TransactionRoute : AppRoute

@Serializable
data object TransactionDisbursementDetailRoute : AppRoute

@Serializable
data object TransactionInstallmentDetailRoute : AppRoute

@Serializable
data object NotificationRoute : AppRoute

@Serializable
data object ProfileGraph : AppRoute

@Serializable
data object ProfileRoute : AppRoute

@Serializable
data object HelpGraph : AppRoute

@Serializable
data object HelpRoute : AppRoute

@Serializable
data object AboutRoute : AppRoute

@Serializable
data object ProductGraph : AppRoute

@Serializable
data object ProductListRoute : AppRoute

@Serializable
data object ProductDetailRoute : AppRoute

@Serializable
data object LoanSimulationRoute : AppRoute