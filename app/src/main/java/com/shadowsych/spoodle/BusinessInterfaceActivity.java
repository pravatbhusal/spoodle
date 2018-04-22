package com.shadowsych.spoodle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.shadowsych.spoodle.assets.BusinessCategory;
import com.shadowsych.spoodle.assets.BusinessCategoryAdapter;
import com.shadowsych.spoodle.assets.BusinessUpdateControl;
import com.shadowsych.spoodle.assets.PayPalControl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessInterfaceActivity extends AppCompatActivity implements BusinessCategoryAdapter.onItemClickListener, BusinessCategoryAdapter.onItemLongClickListener{

    private String businessId;
    private String businessName;
    private String businessImage;
    private String businessEmail;
    private String businessAddress;
    private String businessPhone;
    private String weeklyTotalRevenue;
    private String time;

    private RecyclerView mRecyclerView;
    private BusinessCategoryAdapter mCustomerBusinessCategoryAdapter;
    private ArrayList<BusinessCategory> mBusinessCategoryList;

    private final int IMG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_interface);

        //based on the information of the business, request its information
        businessId = MainActivity.userId;
        businessEmail = MainActivity.userEmail;
        businessName = MainActivity.userName;
        getBusinessInformationRequest();

        //recycler view properties, layout manager, and categories business request
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBusinessCategoryList = new ArrayList<>();
        mCustomerBusinessCategoryAdapter = new BusinessCategoryAdapter(BusinessInterfaceActivity.this, mBusinessCategoryList);
        mRecyclerView.setAdapter(mCustomerBusinessCategoryAdapter);
        getBusinessCategoriesRequest();

        //asynchronous onclick listener when clicking the time text view
        final TextView timeText = findViewById(R.id.timeText);
        timeText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the edit time alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(BusinessInterfaceActivity.this);
                final EditText timeText = new EditText(BusinessInterfaceActivity.this);
                timeText.setText(time);
                alert.setTitle("Edit Time");
                alert.setTitle("Opening to closing time:");
                alert.setView(timeText);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //update the new time
                        BusinessUpdateControl.updateTimeRequest(BusinessInterfaceActivity.this, businessId, timeText.getText().toString());
                        time = timeText.getText().toString();
                        TextView timeText = findViewById(R.id.timeText);
                        timeText.setText(time);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        //asynchronous onclick listener when clicking the address text view
        final TextView addressText = findViewById(R.id.addressText);
        addressText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the edit address alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(BusinessInterfaceActivity.this);
                final EditText addressText = new EditText(BusinessInterfaceActivity.this);
                addressText.setText(businessAddress);
                alert.setTitle("Edit Address");
                alert.setView(addressText);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //update the new address
                        BusinessUpdateControl.updateAddressRequest(BusinessInterfaceActivity.this, businessId, addressText.getText().toString());
                        businessAddress = addressText.getText().toString();
                        TextView addressText = findViewById(R.id.addressText);
                        addressText.setText(businessAddress);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        //asynchronous onclick listener when clicking the business image image view
        final ImageView businessImage = findViewById(R.id.businessImage);
        businessImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the edit business image alert dialog
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent, IMG_REQUEST);
            }
        });

        //asynchronous onclick listener when clicking the email image button
        final ImageButton emailBTN = findViewById(R.id.emailBTN);
        emailBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the edit email alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(BusinessInterfaceActivity.this);
                final EditText emailText = new EditText(BusinessInterfaceActivity.this);
                emailText.setText(businessEmail);
                alert.setTitle("Edit Email");
                alert.setView(emailText);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //update the new email
                        BusinessUpdateControl.updateEmailRequest(BusinessInterfaceActivity.this, businessId, emailText.getText().toString());
                        businessEmail = emailText.getText().toString();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        //asynchronous onclick listener when clicking the phone image button
        final ImageButton phoneBTN = findViewById(R.id.phoneBTN);
        phoneBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the edit phone alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(BusinessInterfaceActivity.this);
                final EditText phoneText = new EditText(BusinessInterfaceActivity.this);
                phoneText.setText(businessPhone);
                alert.setTitle("Edit Phone Number");
                alert.setView(phoneText);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //update the new phone number
                        BusinessUpdateControl.updatePhoneRequest(BusinessInterfaceActivity.this, businessId, phoneText.getText().toString());
                        businessPhone = phoneText.getText().toString();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        //asynchronous onclick listener when clicking the analytics image button
        final ImageButton analyticsBTN = findViewById(R.id.analyticsBTN);
        analyticsBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the analytics intent
                Intent BusinessAnalyticsIntent = new Intent(BusinessInterfaceActivity.this, BusinessAnalyticsActivity.class);
                BusinessAnalyticsIntent.putExtra("weeklyTotalRevenue", weeklyTotalRevenue);
                startActivity(BusinessAnalyticsIntent);
            }
        });

        //asynchronous onclick listener when clicking the buy promotion image button
        final ImageButton buyPromotionBTN = findViewById(R.id.buyPromotionBTN);
        buyPromotionBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the buy promotion PayPal control, also the cart variable below is worthless and just a placeholder
                Map<String, ArrayList<String>> cart = new HashMap<>();
                cart.put("promotion", null);
                new PayPalControl(BusinessInterfaceActivity.this, "placeholder_email", "businessPromotion", cart);
            }
        });

        //asynchronous onclick listener when clicking the add category button
        final Button addCategoryBTN = findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the add category alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(BusinessInterfaceActivity.this);
                final EditText addCategoryText = new EditText(BusinessInterfaceActivity.this);
                alert.setTitle("Add Category");
                alert.setView(addCategoryText);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //add a new category
                        String newCategory = addCategoryText.getText().toString();
                        //check if the category is blank
                        if(newCategory.isEmpty()) {
                            Toast.makeText(BusinessInterfaceActivity.this,
                                    "The inputted category was blank!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(int i = 0; i < mBusinessCategoryList.size(); i++) {
                            //if the category name is already in-use, then don't allow it to be used
                            if(mBusinessCategoryList.get(i).getCategoryText().equalsIgnoreCase(newCategory) || newCategory.isEmpty()) {
                                Toast.makeText(BusinessInterfaceActivity.this,
                                        "The category name is already in-use!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        BusinessUpdateControl.addCategoryRequest(BusinessInterfaceActivity.this, businessId, newCategory);
                        mBusinessCategoryList.add(new BusinessCategory(null, newCategory));
                        mRecyclerView.setAdapter(mCustomerBusinessCategoryAdapter);
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    //requests the business information from the server based on its id, and show its information
    private void getBusinessInformationRequest() {
        //send an HTTPRequest to the server to get business information
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(BusinessInterfaceActivity.this);
        final StringRequest httpBusinessRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/business_data.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //set the proper variables, image, and texts
                            JSONObject jsonResponse = new JSONObject(response);
                            final String businessResult = jsonResponse.getString("result");
                            if(businessResult.equals("success")) {
                                businessName = jsonResponse.getString("businessName");
                                businessImage = MainActivity.serverURL + jsonResponse.getString("businessImage");
                                businessEmail = jsonResponse.getString("businessEmail");
                                businessAddress = jsonResponse.getString("businessAddress");
                                businessPhone = jsonResponse.getString("businessPhone");
                                weeklyTotalRevenue = jsonResponse.getString("weeklyTotalRevenue");
                                time = jsonResponse.getString("time");
                                Picasso.with(BusinessInterfaceActivity.this).load(businessImage)
                                        .fit().centerInside().into((ImageView)findViewById(R.id.businessImage));
                                TextView addressText = findViewById(R.id.addressText);
                                addressText.setText(businessAddress);
                                TextView timeText = findViewById(R.id.timeText);
                                timeText.setText(time);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(BusinessInterfaceActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(BusinessInterfaceActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
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
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(BusinessInterfaceActivity.this);
        final StringRequest httpCategoryRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/categories.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //set the proper variables
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
                                //swipeable delete touch listener using a left swipe
                                SwipeableRecyclerViewTouchListener swipeTouchListener =
                                    new SwipeableRecyclerViewTouchListener(mRecyclerView,
                                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                            @Override
                                            public boolean canSwipeLeft(int position) { return true; }
                                            @Override
                                            public boolean canSwipeRight(int position) { return false; }
                                            //delete the item when swiping left
                                            @Override
                                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                                for (int position : reverseSortedPositions) {
                                                    //remove the category that was swipped
                                                    BusinessUpdateControl.removeCategoryRequest(BusinessInterfaceActivity.this, businessId,
                                                            mBusinessCategoryList.get(position).getCategoryText());
                                                    mBusinessCategoryList.remove(position);
                                                    mCustomerBusinessCategoryAdapter.notifyItemRemoved(position);
                                                }
                                                mCustomerBusinessCategoryAdapter.notifyDataSetChanged();
                                            }
                                            @Override
                                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {}
                                });
                                mRecyclerView.setAdapter(mCustomerBusinessCategoryAdapter);
                                mRecyclerView.addOnItemTouchListener(swipeTouchListener);
                                mCustomerBusinessCategoryAdapter.setOnItemClickListener(BusinessInterfaceActivity.this);
                                mCustomerBusinessCategoryAdapter.setOnItemLongClickListener(BusinessInterfaceActivity.this);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(BusinessInterfaceActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(BusinessInterfaceActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
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

    //disable pressing the back button within this activity
    @Override
    public void onBackPressed() {}

    //receive the clicked business category
    @Override
    public void onItemClick(int position) {
        //create the customer view business's activity intent (page)
        Intent viewBusinessItemsItent = new Intent(BusinessInterfaceActivity.this,
                BusinessEditItemsActivity.class);
        //set the customer view business's id variable, then open the intent
        BusinessCategory clickedItem = mBusinessCategoryList.get(position);
        viewBusinessItemsItent.putExtra("businessId", businessId);
        viewBusinessItemsItent.putExtra("category", clickedItem.getCategoryText());
        startActivity(viewBusinessItemsItent);
    }

    //receive the held business category
    @Override
    public void onItemLongClick(final int position) {
        final BusinessCategory longClickedItem = mBusinessCategoryList.get(position);
        //open the category edit alert dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(BusinessInterfaceActivity.this);
        final EditText editCategoryText = new EditText(BusinessInterfaceActivity.this);
        editCategoryText.setText(longClickedItem.getCategoryText());
        alert.setTitle("Edit Category");
        alert.setView(editCategoryText);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //update the category's name
                String editCategory = editCategoryText.getText().toString();
                //check if the category is blank
                if(editCategory.isEmpty()) {
                    Toast.makeText(BusinessInterfaceActivity.this,
                            "The inputted category was blank!", Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0; i < mBusinessCategoryList.size(); i++) {
                    //if the category name is already in-use and the user did change the name, then don't allow it to be used
                    if(mBusinessCategoryList.get(i).getCategoryText().equalsIgnoreCase(editCategory)
                            && !editCategory.equalsIgnoreCase(longClickedItem.getCategoryText())) {
                        Toast.makeText(BusinessInterfaceActivity.this,
                                "The category name is already in-use!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                BusinessUpdateControl.updateCategoryRequest(BusinessInterfaceActivity.this, businessId,
                        longClickedItem.getCategoryText(), editCategory);
                mBusinessCategoryList.set(position, new BusinessCategory(null, editCategory));
                mRecyclerView.setAdapter(mCustomerBusinessCategoryAdapter);
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //receive when paypal cart payment is complete
        if(requestCode == PayPalControl.PAYPAL_REQUEST_CODE && resultCode == RESULT_OK) {
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            //payment was confirmed! show the verified dialog and send update promotion request
            if(confirmation != null) {
                //send an update promotion request
                PayPalControl.updateBusinessPromotion(BusinessInterfaceActivity.this, businessId);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BusinessInterfaceActivity.this);
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
                Toast.makeText(BusinessInterfaceActivity.this, "Error processing payment...", Toast.LENGTH_LONG).show();
                return;
            }
        }
        //receive when the business selects new image from its gallery
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK) {
            Uri imagePath = data.getData();
            try {
                //set the new image and send a request to update it
                Bitmap businessBitMapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                ((ImageView)findViewById(R.id.businessImage)).setImageBitmap(businessBitMapImage);
                BusinessUpdateControl.updateImageRequest(BusinessInterfaceActivity.this, businessId, businessBitMapImage);
            } catch(IOException error) {
                Toast.makeText(BusinessInterfaceActivity.this,
                        "Error when uploading the image from your gallery!", Toast.LENGTH_LONG).show();
                error.printStackTrace();
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
