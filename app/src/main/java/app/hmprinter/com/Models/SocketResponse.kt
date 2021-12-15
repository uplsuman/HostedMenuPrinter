package app.hmprinter.com.Models
import com.google.gson.annotations.SerializedName


data class Items (

    @SerializedName("name") val name : String,
    @SerializedName("paymentType") val paymentType : String,
    @SerializedName("images") val images : List<Images>,
    @SerializedName("quantity") val quantity : Int,
    @SerializedName("itemPrice") val itemPrice : Double,
    @SerializedName("additionalPrice") val additionalPrice : Double,
    @SerializedName("modifierGroupName") val modifierGroupName : List<ModifierGroupName>
)

data class SocketResponse(

    @SerializedName("message") val message: String,
    @SerializedName("order") val order: Order? = null,
)

data class ModifierGroupName (

    @SerializedName("name") val name : String,
    @SerializedName("modifierItem") val modifierItem : List<ModifierItem>
)

data class ModifierItem (

    @SerializedName("name") val name : String,
    @SerializedName("optionalFee") val optionalFee : Double
)
data class Order (

    @SerializedName("restaurantName") val restaurantName : String,
    @SerializedName("orderId") val orderId : Int,
    @SerializedName("items") val items : List<Items>,
    @SerializedName("restaurantId") val restaurantId : String,
    @SerializedName("paymentType") val paymentType : String,
    @SerializedName("paymentStatus") val paymentStatus : String,
    @SerializedName("totalPrice") val totalPrice : Double,
    @SerializedName("createdDate") val createdDate : Long,
    @SerializedName("customerName") val customerName : String,
    @SerializedName("phoneNumber") val phoneNumber : String,
    @SerializedName("taxPrice") val taxPrice : Double
)