package com.shadowsych.spoodle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shadowsych.spoodle.assets.BusinessCategoryItem;
import com.shadowsych.spoodle.assets.BusinessCategoryItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerViewBusinessItemsActivity extends AppCompatActivity implements BusinessCategoryItemAdapter.onItemClickListener {

    private String businessId;
    private String category;

    private RecyclerView mRecyclerView;
    private BusinessCategoryItemAdapter mCustomerBusinessCategoryItemsAdapter;
    private ArrayList<BusinessCategoryItem> mBusinessCategoryItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_business_items);

        //set the id and category from the clicked category
        Intent intent = getIntent();
        businessId = intent.getStringExtra("businessId");
        category = intent.getStringExtra("category");

        //recycler view properties, layout manager, and categories items request
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBusinessCategoryItemsList = new ArrayList<>();
        mCustomerBusinessCategoryItemsAdapter = new BusinessCategoryItemAdapter
                (CustomerViewBusinessItemsActivity.this, mBusinessCategoryItemsList);
        mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
        getCategoryItemsRequest();

        //asynchronous onclick listener for the home button
        final ImageButton homeBTN = findViewById(R.id.homeBTN);
        homeBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go back to the previous intent [customer view business activity's intent (page)]
                onBackPressed();
            }
        });
    }

    //requests the category items from the server based on businessId, and shows category items
    private void getCategoryItemsRequest() {
        //send an HTTPRequest to the server to get category items information
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(CustomerViewBusinessItemsActivity.this);
        final StringRequest httpCategoryItemsRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/category_items.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //set the proper variables
                            JSONObject jsonResponse = new JSONObject(response);
                            final String categoryItemsResult = jsonResponse.getString("result");
                            final JSONArray categoryItemsData = jsonResponse.getJSONArray("categoryItems");
                            if(categoryItemsResult.equals("success")) {
                                mBusinessCategoryItemsList.clear();
                                //set the values from the HTTP response, then input them to the recycle view using the adapter
                                for(int i = 0; i < categoryItemsData.length(); i++) {
                                    JSONObject business = categoryItemsData.getJSONObject(i);
                                    String category = business.getString("category");
                                    String categoryItem = business.getString("categoryItem");
                                    double price = business.getDouble("price");
                                    int quantity = Integer.parseInt(business.getString("quantity"));
                                    String externalities = business.getString("externalities");
                                    mBusinessCategoryItemsList.add(new BusinessCategoryItem(category, categoryItem, price, quantity, externalities));
                                }
                                mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
                                mCustomerBusinessCategoryItemsAdapter.setOnItemClickListener(CustomerViewBusinessItemsActivity.this);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(CustomerViewBusinessItemsActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(CustomerViewBusinessItemsActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the category items request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("category", category);
                return params;
            }
            //header values to send to the category items request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpCategoryItemsRequest);
        mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
        mCustomerBusinessCategoryItemsAdapter.setOnItemClickListener(CustomerViewBusinessItemsActivity.this);
    }

    //receive the clicked business category item and open/set a quantity add dialog
    @Override
    public void onItemClick(int position) {
        final BusinessCategoryItem clickedItem = mBusinessCategoryItemsList.get(position);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CustomerViewBusinessItemsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        final TextView titleText = view.findViewById(R.id.titleText);
        titleText.setText(clickedItem.getCategoryItem());
        final NumberPicker quantityPicker = view.findViewById(R.id.quantityPicker);
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(clickedItem.getQuantity());
        final TextView externalitiesText = view.findViewById(R.id.externalitiesText);
        externalitiesText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        externalitiesText.setText(clickedItem.getExternalities());
        final Button addCartBTN = view.findViewById(R.id.addCartBTN);
        final Button cancelCartBTN = view.findViewById(R.id.cancelCartBTN);
        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        //asynchronous onclick listener for the addCart button
        addCartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemKey = clickedItem.getCategory() + " -> " + clickedItem.getCategoryItem();
                ArrayList<String> itemValue = new ArrayList<String>();
                //make ArrayList for the cartItem's value
                itemValue.add(clickedItem.getCategory());
                itemValue.add(clickedItem.getCategoryItem());
                itemValue.add(String.valueOf(clickedItem.getCategoryItemPrice()));
                itemValue.add(String.valueOf(quantityPicker.getValue()));
                CustomerViewBusinessActivity.cartItems.put(itemKey, itemValue);
                dialog.dismiss();
                Toast.makeText(CustomerViewBusinessItemsActivity.this,
                        "Updated " + clickedItem.getCategoryItem() + " x" +
                                Integer.toString(quantityPicker.getValue()) + " to your cart!",
                        Toast.LENGTH_LONG).show();
            }
        });

        //asynchronous onclick listener for the cancelCart button
        cancelCartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
