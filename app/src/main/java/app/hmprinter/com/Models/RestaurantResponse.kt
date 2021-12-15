package app.hmprinter.com.Models

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(

    @SerializedName("error") var error: Boolean = true,
    @SerializedName("result") var result: Result? = null,
    @SerializedName("message") var message: String = "Something Went Wrong"

)

data class Location(

    @SerializedName("coordinates") var coordinates: List<Double>,
    @SerializedName("addressLine1") var addressLine1: String,
    @SerializedName("addressLine2") var addressLine2: String,
    @SerializedName("addressLine3") var addressLine3: String,
    @SerializedName("type") var type: String

)

data class Logo(

    @SerializedName("key") var key: String,
    @SerializedName("name") var name: String,
    @SerializedName("mimetype") var mimetype: String

)

data class OperationalHours(

    @SerializedName("start") var start: String,
    @SerializedName("end") var end: String

)

data class PaymentOptions(

    @SerializedName("cardAndCash") var cardAndCash: Boolean,
    @SerializedName("cashOnly") var cashOnly: Boolean,
    @SerializedName("zeroTouchPayment") var zeroTouchPayment: Boolean

)

data class Seating(

    @SerializedName("dinningIn") var dinningIn: Boolean,
    @SerializedName("takeAway") var takeAway: Boolean

)

data class Expense(

    @SerializedName("webDevelopmentFees") var webDevelopmentFees: Int,
    @SerializedName("startupFeeForECommerce") var startupFeeForECommerce: Int,
    @SerializedName("monthlySupportFee") var monthlySupportFee: Int,
    @SerializedName("perTransactionFee") var perTransactionFee: Int,
    @SerializedName("note") var note: String

)

data class BankDetails(

    @SerializedName("accountNo") var accountNo: String,
    @SerializedName("routingNumber") var routingNumber: String,
    @SerializedName("name") var name: String,
    @SerializedName("address") var address: String

)

data class Accounting(

    @SerializedName("declaredSalesTaxRate") var declaredSalesTaxRate: Int,
    @SerializedName("federalTaxIdNumber") var federalTaxIdNumber: String,
    @SerializedName("internallyGeneratedAccountNumber") var internallyGeneratedAccountNumber: String

)

data class Section1(

    @SerializedName("title") var title: String,
    @SerializedName("subTitle") var subTitle: String,
    @SerializedName("content") var content: String

)

data class Section2(

    @SerializedName("title") var title: String,
    @SerializedName("subTitle") var subTitle: String,
    @SerializedName("content") var content: String

)

data class PasswordResetToken(

    @SerializedName("expired") var expired: Boolean,
    @SerializedName("token") var token: String,
    @SerializedName("issuedDate") var issuedDate: Long

)

data class ProfilePicture(

    @SerializedName("key") var key: String,
    @SerializedName("name") var name: String,
    @SerializedName("mimetype") var mimetype: String

)

data class CreatedBy(

    @SerializedName("profilePicture") var profilePicture: ProfilePicture,
    @SerializedName("passwordResetToken") var passwordResetToken: PasswordResetToken,
    @SerializedName("role") var role: String,
    @SerializedName("email") var email: String,
    @SerializedName("phoneNumber") var phoneNumber: String,
    @SerializedName("additionalPhoneNumber") var additionalPhoneNumber: String,
    @SerializedName("dateOfBirth") var dateOfBirth: Long,
    @SerializedName("password") var password: String,
    @SerializedName("address") var address: String,
    @SerializedName("zipCode") var zipCode: String,
    @SerializedName("otp") var otp: String,
    @SerializedName("otpExpiresIn") var otpExpiresIn: String,
    @SerializedName("isVerified") var isVerified: Int,
    @SerializedName("enable") var enable: Int,
    @SerializedName("createdDate") var createdDate: Long,
    @SerializedName("_id") var Id: String,
    @SerializedName("name") var name: String,
    @SerializedName("restaurantManagedBy") var restaurantManagedBy: List<String>,
    @SerializedName("deviceInfo") var deviceInfo: List<String>,
    @SerializedName("__v") var _v: Int,
    @SerializedName("about") var about: String,
    @SerializedName("gender") var gender: String

)

data class Images(

    @SerializedName("title") var title: String,
    @SerializedName("tagLine") var tagLine: String,
    @SerializedName("_id") var Id: String,
    @SerializedName("key") var key: String,
    @SerializedName("name") var name: String,
    @SerializedName("mimetype") var mimetype: String

)

data class OperationalSlots(

    @SerializedName("weekDays") var weekDays: List<String>,
    @SerializedName("_id") var Id: String,
    @SerializedName("start") var start: String,
    @SerializedName("end") var end: String,
    @SerializedName("services") var services: String

)

data class ContactInfo(

    @SerializedName("restaurantEmail") var restaurantEmail: String,
    @SerializedName("businessContactName") var businessContactName: String,
    @SerializedName("contactEmail") var contactEmail: String,
    @SerializedName("addressOfRestaurantStorefront") var addressOfRestaurantStorefront: String,
    @SerializedName("billingPhoneNumber") var billingPhoneNumber: String,
    @SerializedName("restaurantPhoneNumber") var restaurantPhoneNumber: String

)

data class Cuisines(

    @SerializedName("enable") var enable: Int,
    @SerializedName("createdDate") var createdDate: Long,
    @SerializedName("_id") var Id: String,
    @SerializedName("name") var name: String,
    @SerializedName("createdBy") var createdBy: CreatedBy,
    @SerializedName("__v") var _v: Int

)

data class TemplateId(

    @SerializedName("enable") var enable: Int,
    @SerializedName("createdDate") var createdDate: Long,
    @SerializedName("_id") var Id: String,
    @SerializedName("name") var name: String,
    @SerializedName("createdBy") var createdBy: CreatedBy,
    @SerializedName("__v") var _v: Int,
    @SerializedName("images") var images: List<Images>

)

data class Result(

    @SerializedName("location") var location: Location,
    @SerializedName("logo") var logo: Logo,
    @SerializedName("operationalHours") var operationalHours: OperationalHours,
    @SerializedName("paymentOptions") var paymentOptions: PaymentOptions,
    @SerializedName("seating") var seating: Seating,
    @SerializedName("expense") var expense: Expense,
    @SerializedName("bankDetails") var bankDetails: BankDetails,
    @SerializedName("contactInfo") var contactInfo: ContactInfo,
    @SerializedName("accounting") var accounting: Accounting,
    @SerializedName("section1") var section1: Section1,
    @SerializedName("section2") var section2: Section2,
    @SerializedName("tags") var tags: List<String>,
    @SerializedName("openingStatus") var openingStatus: Int,
    @SerializedName("services") var services: List<String>,
    @SerializedName("cuisines") var cuisines: List<Cuisines>,
    @SerializedName("templateId") var templateId: TemplateId,
    @SerializedName("createdBy") var createdBy: CreatedBy,
    @SerializedName("enable") var enable: Int,
    @SerializedName("approved") var approved: Int,
    @SerializedName("createdDate") var createdDate: Long,
    @SerializedName("_id") var Id: String,
    @SerializedName("name") var name: String,
    @SerializedName("images") var images: List<Images>,
    @SerializedName("domainName") var domainName: String,
    @SerializedName("servesAlcohol") var servesAlcohol: Boolean,
    @SerializedName("operationalSlots") var operationalSlots: List<OperationalSlots>,
    @SerializedName("__v") var _v: Int,
    @SerializedName("takeoutActiveMenu") var takeoutActiveMenu: String,
    @SerializedName("storeCode") var storeCode: String,
    @SerializedName("activeMenu") var activeMenu: String,
    @SerializedName("appId") var appId: String

)