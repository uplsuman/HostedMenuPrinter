package app.hmprinter.com.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import app.hmprinter.com.Models.ContactInfo;
import app.hmprinter.com.Models.RestaurantResponse;
import app.hmprinter.com.Models.SocketResponse;
import app.hmprinter.com.R;


public class PrintingHelper {
    @SuppressLint("StaticFieldLeak")
    static Context context;
    @SuppressLint("StaticFieldLeak")
    static NotificationHelper notificationHelper;
    EscPosPrinter printer;
    String orderItemString = "";


    @SuppressLint("StaticFieldLeak")
    private static PrintingHelper printerConstantSingleTon = null;


    // static method to create instance of Singleton class
    public static PrintingHelper getInstance(Context ctx) {
        if (printerConstantSingleTon == null) {
            context = ctx;
            notificationHelper = new NotificationHelper(context);
            printerConstantSingleTon = new PrintingHelper();
        }

        return printerConstantSingleTon;
    }

    public void testPrinter(DeviceConnection printerConnection) throws EscPosConnectionException {
        printer = new EscPosPrinter(printerConnection, 203, 76f, 48);
        String testPrinter = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.hm_logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                "[L]\n" +
                "[L]\n" +
                "[C]    -----------------------------------------\n" +
                "[L]\n" +
                "[C]<u><font size='big'>    " + "Print Test" + "</font></u>\n" +
                "[L]\n" +
                "[C]    -----------------------------------------\n " +
                "[L]\n" +
                "[L]\n";

        try {
            printer
                    .printFormattedText(testPrinter);
        } catch (EscPosConnectionException | EscPosParserException | EscPosEncodingException | EscPosBarcodeException e) {
            e.printStackTrace();
        }
    }

    public void printQrReceipt(SocketResponse socketResponse, RestaurantResponse restaurantData, int printCopies, DeviceConnection printerConnection) {

        try {
            printer = new EscPosPrinter(printerConnection, 203, 76f, 48);

            System.out.println("data---" + socketResponse);
            System.out.println("restaurantData" + Objects.requireNonNull(restaurantData.getResult()).getDomainName());

            String orderId = String.valueOf(Objects.requireNonNull(socketResponse.getOrder()).getOrderId());
            //send notification
            notificationHelper.sendNotification(orderId, "New incoming order!");


            ContactInfo contactInfo = restaurantData.getResult().getContactInfo();
            String restaurantAddress = contactInfo.getAddressOfRestaurantStorefront();
            String domainName = restaurantData.getResult().getDomainName();
            String restaurantName = Objects.requireNonNull(socketResponse.getOrder()).getRestaurantName();
            String paymentStatus = Objects.requireNonNull(socketResponse.getOrder()).getPaymentStatus();
            String totalPrice = String.valueOf(Objects.requireNonNull(socketResponse.getOrder()).getTotalPrice());
            String taxPrice = String.valueOf(Objects.requireNonNull(socketResponse.getOrder()).getTaxPrice());
            String customerName = Objects.requireNonNull(socketResponse.getOrder()).getCustomerName();
            String phoneNumber = Objects.requireNonNull(socketResponse.getOrder()).getPhoneNumber();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedCurrentDate = df.format(c);

            JSONArray items = (JSONArray) Objects.requireNonNull(socketResponse.getOrder()).getItems();

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String itemName = item.getString("name");
                String itemPrice = item.getString("itemPrice");
                String itemQuantity = item.getString("quantity");
                // String additionalPrice = item.getString("additionalPrice");
                String modifierItem = "";
                if (item.has("modifierGroupName")) {
                    JSONArray modifierGroupNameArray = item.getJSONArray("modifierGroupName");

                    for (int j = 0; j < modifierGroupNameArray.length(); j++) {
                        JSONObject modifierGroupNameObject = modifierGroupNameArray.getJSONObject(j);
                        String modifierGroupName = modifierGroupNameObject.getString("name");
                        modifierItem = modifierItem + "[L]  + ModifierGroup Name : " + modifierGroupName + "\n";
                        JSONArray modifierGroupItemArray = modifierGroupNameObject.getJSONArray("modifierItem");
                        for (int k = 0; k < modifierGroupItemArray.length(); k++) {
                            JSONObject modifierGroupItem = modifierGroupItemArray.getJSONObject(k);
                            String modifierItemName = modifierGroupItem.getString("name");
                            String modifierItemOptionalFee = modifierGroupItem.getString("optionalFee");
                            modifierItem = modifierItem + "[L]  + " + modifierItemName + " : [R]" + modifierItemOptionalFee + "\n";

                        }


                    }
                }

                Float totalItemPrice = Float.parseFloat(itemQuantity) * Float.parseFloat(itemPrice);
                orderItemString = orderItemString + "[L]<b>" + (i + 1) + " " + itemName + "</b>[R]" + itemQuantity + " x " + itemPrice + ": " + totalItemPrice + "\n" +
//                        "[L]  + Additional Price : [R]" + additionalPrice + "\n" +
                        modifierItem +
                        "[L]\n";
            }
            String p1 = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.hm_logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                    "[L]\n" +
                    "[L]\n" +
                    "[C]<u><font size='big'>" + restaurantName + "</font></u>\n" +
                    "[L]\n" +
                    "[L]Restaurant Address: " + restaurantAddress + "\n" +
                    "[L]Customer Name: " + customerName + "|" + phoneNumber + "\n" +
                    "[C]<u><font size='tall'>" + orderId + "</font></u>\n" +
                    "[C]-----------------------------------------------\n" +
                    "[L]\n" +
                    "[L]      Ordered Date: " + formattedCurrentDate + "\n" +
                    "[L]\n" +
                    orderItemString +
                    "[C]================================================\n" +
                    "[L]\n" +
                    "[L]<font size='small'> TAX: [R]" + taxPrice + "</font>\n" +
                    "[L]\n" +
                    "[L]<font size='small'> TOTAL PRICE:[R]" + totalPrice + "</font>\n" +
                    "[L]\n" +
                    "[C]<u><font size='tall'>" + "https://" + domainName + ".bigshowtrucks.com/t" + "</font></u>" +
                    "[L]\n" +
                    "[L]\n" +
                    "[L]\n" +
                    "[L]\n" +
                    "[L]\n";
            String p2 = p1;

            for (int i = 1; i < printCopies; i++) {
                p2 = p2 + p1;
            }
            System.out.println("Printing......");
            printer
                    .printFormattedText(p2);


        } catch (JSONException | EscPosConnectionException | EscPosParserException | EscPosEncodingException | EscPosBarcodeException e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally block executed");
            orderItemString = "";
        }


    }


    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}