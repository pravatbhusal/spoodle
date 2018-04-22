package com.shadowsych.spoodle.assets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.shadowsych.spoodle.MainActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayPalControl {

    //paypal settings (NOTE: make sure to change the environment and client_id when in production)
    private final String PAYPAL_CLIENT_ID = "AW0QkRi8B__Prrlvpb12E-upfQBzrrs0FjSWTzFjKBJFBD3qksaU43cUleT4gpOifKDnYLrFld69Divp";
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    private Context mContext;
    private Map<String, ArrayList<String>> mCart;
    private String mSellerEmail;

    public PayPalControl(Context context, String sellerEmail, String checkoutType, Map<String, ArrayList<String>> cart) {
        mSellerEmail = sellerEmail;
        mContext = context;
        mCart = cart;

        //verify if the cart is empty
        if(cart.isEmpty()) {
            Toast.makeText(context, "Your cart is empty!", Toast.LENGTH_LONG).show();
            return;
        }

        //start paypal service
        Intent paypalIntent = new Intent(mContext, PayPalService.class);
        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        mContext.startService(paypalIntent);

        //get the checkout type
        if(checkoutType.equals("customerCartItems")) {
            customerCartItemsCheckout();
        } else if(checkoutType.equals("businessPromotion")) {
            businessPromotionCheckout();
        } else {
            Toast.makeText(context, "Unknown cart checkout type...", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void customerCartItemsCheckout() {
        //for each item in the cartItems, get the total price
        double totalPrice = 0.0;
        for (Map.Entry<String, ArrayList<String>> entry : mCart.entrySet()) {
            ArrayList<String> value = entry.getValue();
            //totalPrice += price * quantity
            totalPrice += Double.parseDouble(value.get(2)) * Integer.parseInt(value.get(3));
        }
        //initiate the paypal payment intent
        PayPalPayment payment = new PayPalPayment(BigDecimal.valueOf(totalPrice), "USD",
                "Cart Checkout", PayPalPayment.PAYMENT_INTENT_SALE);
        //when specified, payments will be sent to this receiver, instead of the account of the provided client_id.
        payment.payeeEmail(mSellerEmail);
        Intent paymentIntent = new Intent(mContext, PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        ((AppCompatActivity)mContext).startActivityForResult(paymentIntent, PAYPAL_REQUEST_CODE);
    }

    private void businessPromotionCheckout() {
        //initiate the paypal payment intent
        PayPalPayment payment = new PayPalPayment(BigDecimal.valueOf(10.00), "USD",
                "1 Month Promotion", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent paymentIntent = new Intent(mContext, PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        ((AppCompatActivity)mContext).startActivityForResult(paymentIntent, PAYPAL_REQUEST_CODE);
    }

    //send an HTTPRequest to update the purchased items quantity
    public static void updatePurchasedQuantityRequest (final Context context, final String businessId, final String data) {
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpUpdateQuantityRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_purchased_quantity.php"),
                //the request responded with no external errors
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        httpRequestQueue.stop();
                    }
                },
                //error, the request responded with a failure...
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the update purchased items quantity request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("data", data);
                return params;
            }
            //header values to send to the update purchased items quantity request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpUpdateQuantityRequest);
    }

    //send an HTTPRequest to update the promotion status
    public static void updateBusinessPromotion (final Context context, final String businessId) {
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpUpdatePromotionRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_promotion.php"),
                //the request responded with no external errors
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        httpRequestQueue.stop();
                    }
                },
                //error, the request responded with a failure...
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the update promotion request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                return params;
            }
            //header values to send to the update promotion request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpUpdatePromotionRequest);
    }

    //send an HTTPRequest to the server with an email about the purchase
    public static void sendPurchasedItemsEmailRequest(final Context context, final String emailTo, final String emailFrom, final String userName, final String data) {
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpEmailRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "global/send_purchased_items_email.php"),
                //the request responded with no external errors
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        httpRequestQueue.stop();
                    }
                },
                //error, the request responded with a failure...
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the email request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailTo", emailTo);
                params.put("emailFrom", emailFrom);
                params.put("userName", userName);
                params.put("data", data);
                return params;
            }
            //header values to send to the email request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpEmailRequest);
    }
}
