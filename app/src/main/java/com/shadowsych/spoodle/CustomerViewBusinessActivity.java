package com.shadowsych.spoodle;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.shadowsych.spoodle.assets.BusinessCategory;
import com.shadowsych.spoodle.assets.BusinessCategoryAdapter;
import com.shadowsych.spoodle.assets.PayPalControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerViewBusinessActivity extends AppCompatActivity implements OnMapReadyCallback, BusinessCategoryAdapter.onItemClickListener {

    private String businessId;
    private String businessName;
    private String businessEmail;
    private String businessAddress;
    private String businessPhone;

    private RecyclerView mRecyclerView;
    private BusinessCategoryAdapter mCustomerBusinessCategoryAdapter;
    private ArrayList<BusinessCategory> mBusinessCategoryList;

    public static Map<String, ArrayList<String>> cartItems = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_business);

        //set the id from the clicked business card, then request for its information
        Intent intent = getIntent();
        businessId = intent.getStringExtra("businessId");
        getBusinessInformationRequest();

        //recycler view properties, layout manager, and categories business request
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBusinessCategoryList = new ArrayList<>();
        mCustomerBusinessCategoryAdapter = new BusinessCategoryAdapter(CustomerViewBusinessActivity.this, mBusinessCategoryList);
        mRecyclerView.setAdapter(mCustomerBusinessCategoryAdapter);
        getBusinessCategoriesRequest();

        //asynchronous onclick listener for the checkout button
        final ImageButton checkoutBTN = findViewById(R.id.checkoutBTN);
        checkoutBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the checkout menu dialog, and add items to the recycle view
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerViewBusinessActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_checkout_items, null);
                //recycler view properties and layout manager, also we're re-using the BusinessCategoryAdapter
                final RecyclerView checkoutRecyclerView = view.findViewById(R.id.recycler_view);
                final Button checkoutBTN = view.findViewById(R.id.checkoutBTN);
                final Button cancelCheckoutBTN = view.findViewById(R.id.cancelCheckoutBTN);
                checkoutRecyclerView.setHasFixedSize(true);
                checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                final ArrayList<BusinessCategory> checkoutList = new ArrayList<BusinessCategory>();
                //for each item in the cartItems, add them to the checkoutList
                for (Map.Entry<String, ArrayList<String>> entry : cartItems.entrySet()) {
                    String key = entry.getKey();
                    ArrayList<String> value = entry.getValue();
                    //itemPrice = price * quantity
                    double itemPrice = Double.parseDouble(value.get(2)) * Integer.parseInt(value.get(3));
                    checkoutList.add(new BusinessCategory(key, value.get(1) + " x" + value.get(3) + " for $" + itemPrice));
                }
                final BusinessCategoryAdapter checkoutAdapter = new BusinessCategoryAdapter(view.getContext(), checkoutList);
                SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(checkoutRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) { return true; }
                            @Override
                            public boolean canSwipeRight(int position) { return false; }
                            //delete the item when swiping left
                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    cartItems.remove(checkoutList.get(position).getCheckoutKey());
                                    checkoutList.remove(position);
                                    checkoutAdapter.notifyItemRemoved(position);
                                }
                                checkoutAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {}
                });
                checkoutRecyclerView.addOnItemTouchListener(swipeTouchListener);
                checkoutRecyclerView.setAdapter(checkoutAdapter);
                mBuilder.setView(view);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //asynchronous onclick listener for the checkout button
                checkoutBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PayPalControl(CustomerViewBusinessActivity.this, businessEmail, "customerCartItems", cartItems);
                        dialog.dismiss();
                    }
                });

                //asynchronous onclick listener for the cancelCheckout button
                cancelCheckoutBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //asynchronous onclick listener for the email button
        final ImageButton emailBTN = findViewById(R.id.emailBTN);
        emailBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //create a new email intent and set values
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + businessEmail));
                startActivity(emailIntent);
            }
        });

        //asynchronous onclick listener for the phone button
        final ImageButton phoneBTN = findViewById(R.id.phoneBTN);
        phoneBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", businessPhone, null));
                startActivity(phoneIntent);
            }
        });

        //asynchronous onclick listener for the home button
        final ImageButton homeBTN = findViewById(R.id.homeBTN);
        homeBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go back to the previous intent [customer view business activity's intent (page)]
                onBackPressed();
            }
        });
    }

    //requests the business information from the server based on its id, and show its information
    private void getBusinessInformationRequest() {
        //send an HTTPRequest to the server to get business information
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(CustomerViewBusinessActivity.this);
        final StringRequest httpBusinessRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/business_data.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //set the proper variables
                            JSONObject jsonResponse = new JSONObject(response);
                            final String businessResult = jsonResponse.getString("result");
                            if(businessResult.equals("success")) {
                                businessName = jsonResponse.getString("businessName");
                                businessEmail = jsonResponse.getString("businessEmail");
                                businessAddress = jsonResponse.getString("businessAddress");
                                businessPhone = jsonResponse.getString("businessPhone");
                                TextView addressText = findViewById(R.id.addressText);
                                addressText.setText(businessAddress);
                                //get the SupportMapFragment and request notification when the map is ready to be used.
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(CustomerViewBusinessActivity.this);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(CustomerViewBusinessActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
                                httpRequestQueue.stop();
                            }
                        } catch (JSONException error) {
                            error.printStackTrace();
                            httpRequestQueue.stop();
                        }
                    }
                },
                //error, the request responded with a failure...
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerViewBusinessActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the business request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                return params;
            }
            //header values to send to the business request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpBusinessRequest);
    }

    //requests the business categories from the server based on its id, and show its information
    private void getBusinessCategoriesRequest() {
        //send an HTTPRequest to the server to get category information
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(CustomerViewBusinessActivity.this);
        final StringRequest httpCategoryRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/categories.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //set the proper variables and texts
                            JSONObject jsonResponse = new JSONObject(response);
                            final String categoryResult = jsonResponse.getString("result");
                            final JSONArray categoryData = jsonResponse.getJSONArray("categories");
                            if(categoryResult.equals("success")) {
                                mBusinessCategoryList.clear();
                                //set the values from the HTTP response, then input them to the recycle view using the adapter
                                for(int i = 0; i < categoryData.length(); i++) {
                                    JSONObject business = categoryData.getJSONObject(i);
                                    String category = business.getString("category");
                                    mBusinessCategoryList.add(new BusinessCategory(null, category));
                                }
                                mRecyclerView.setAdapter(mCustomerBusinessCategoryAdapter);
                                mCustomerBusinessCategoryAdapter.setOnItemClickListener(CustomerViewBusinessActivity.this);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(CustomerViewBusinessActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
                                httpRequestQueue.stop();
                            }
                        } catch (JSONException error) {
                            error.printStackTrace();
                            httpRequestQueue.stop();
                        }
                    }
                },
                //error, the request responded with a failure...
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerViewBusinessActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the category request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                return params;
            }
            //header values to send to the category request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpCategoryRequest);
    }

    //set the google map to the location of the business
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //add a marker to the business's location and move the camera to the location with a zoom
        LatLng location = getLocationFromAddress(businessAddress);
        googleMap.addMarker(new MarkerOptions().position(location).title(businessName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
    }

    /*
    gets approximate latitude and longitude from given address
    @param strAddress: example is "Street Number, Street, Suburb, State, Postcode"
    */
    public LatLng getLocationFromAddress(String strAddress)
    {
        Geocoder coder = new Geocoder(this);
        LatLng geoLocation = new LatLng(0.0, 0.0);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            geoLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return geoLocation;
    }

    //receive the clicked business category
    @Override
    public void onItemClick(int position) {
        //create the customer view business's activity intent (page)
        Intent customerViewBusinessItemsItent = new Intent(CustomerViewBusinessActivity.this,
                CustomerViewBusinessItemsActivity.class);
        //set the customer view business's id variable, then open the intent
        BusinessCategory clickedItem = mBusinessCategoryList.get(position);
        customerViewBusinessItemsItent.putExtra("businessId", businessId);
        customerViewBusinessItemsItent.putExtra("category", clickedItem.getCategoryText());
        startActivity(customerViewBusinessItemsItent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //receive when paypal cart payment is complete
        if(requestCode == PayPalControl.PAYPAL_REQUEST_CODE && resultCode == RESULT_OK) {
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            //payment was confirmed! show the verified dialog and send requests to the seller with the information
            if(confirmation != null) {
                //send an email request to the seller with the payment information
                PayPalControl.sendPurchasedItemsEmailRequest(CustomerViewBusinessActivity.this, businessEmail, MainActivity.userEmail,
                        MainActivity.userName, new Gson().toJson(cartItems.values()));
                //update the quantity of items through a request with the payment information
                PayPalControl.updatePurchasedQuantityRequest(CustomerViewBusinessActivity.this, businessId, new Gson().toJson(cartItems.values()));
                cartItems.clear();
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerViewBusinessActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_verified_checkout, null);
                final Button okBTN = view.findViewById(R.id.okBTN);
                mBuilder.setView(view);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //asynchronous onclick listener for the ok button
                okBTN.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(CustomerViewBusinessActivity.this, "Error processing payment...", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        //stop any service that we destroy, particularly the paypal service
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
