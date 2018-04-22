package com.shadowsych.spoodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shadowsych.spoodle.assets.BusinessCard;
import com.shadowsych.spoodle.assets.BusinessCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerInterfaceActivity extends AppCompatActivity implements BusinessCardAdapter.onItemClickListener {

    private RecyclerView mRecyclerView;
    private BusinessCardAdapter mBusinessCardAdapter;
    private ArrayList<BusinessCard> mBusinessCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_interface);

        //recycler view properties, layout manager, and promotion business request
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBusinessCardList = new ArrayList<>();
        mBusinessCardAdapter = new BusinessCardAdapter(CustomerInterfaceActivity.this, mBusinessCardList);
        mRecyclerView.setAdapter(mBusinessCardAdapter);
        getPromotionBusinessRequest();

        //asynchronous onclick listener for the promotions button
        final ImageButton promotionsBTN = findViewById(R.id.promotionsBTN);
        //sets the image button to have a background opacity of 0
        promotionsBTN.setBackgroundResource(0);
        promotionsBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPromotionBusinessRequest();
            }
        });

        //asynchronous onclick listener for the search button
        final ImageButton searchBTN = findViewById(R.id.searchBTN);
        //sets the image button to have a background opacity of 0
        searchBTN.setBackgroundResource(0);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            final EditText searchText = findViewById(R.id.searchText);
            final String text = searchText.getText().toString();
            if(!text.isEmpty()) {
                businessSearchRequest(searchText.getText().toString());
            }
            }
        });
    }

    //requests the businesses that have purchased a promotion, and show them on the recycler view
    private void getPromotionBusinessRequest() {
        //send an HTTPRequest to the server to get the promoted businesses
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(CustomerInterfaceActivity.this);
        final StringRequest httpPromotionBusinessRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/promotions.php"),
            //success, the request responded successfully!
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //set the businessImage, timeText, and titleText variables
                        JSONObject jsonResponse = new JSONObject(response);
                        final String promotionBusinessResult = jsonResponse.getString("result");
                        final JSONArray promotionBusinessData = jsonResponse.getJSONArray("businesses");
                        if(promotionBusinessResult.equals("success")) {
                            mBusinessCardList.clear();
                            //set the values from the HTTP response, then input them to the recycle view using the adapter
                            for(int i = 0; i < promotionBusinessData.length(); i++) {
                                JSONObject business = promotionBusinessData.getJSONObject(i);
                                String businessId = business.getString("id");
                                String businessImage = MainActivity.serverURL + business.getString("image");
                                String timeText = business.getString("time");
                                String titleText = business.getString("title");
                                mBusinessCardList.add(new BusinessCard(businessId, businessImage, timeText, titleText, true));
                            }
                            mRecyclerView.setAdapter(mBusinessCardAdapter);
                            mBusinessCardAdapter.setOnItemClickListener(CustomerInterfaceActivity.this);
                            httpRequestQueue.stop();
                        } else {
                            Toast.makeText(CustomerInterfaceActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(CustomerInterfaceActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    httpRequestQueue.stop();
                }
            });
        httpRequestQueue.add(httpPromotionBusinessRequest);
    }

    /*
    Sends a search request to the server
    @param search: The search inputted by the user in the search input textbox
    */
    private void businessSearchRequest(final String search) {
        //send an HTTPRequest to the server to search for businesses
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(CustomerInterfaceActivity.this);
        final StringRequest httpSearchBusinessRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "interface/search.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //set the businessImage, timeText, and titleText variables
                            JSONObject jsonResponse = new JSONObject(response);
                            final String promotionBusinessResult = jsonResponse.getString("result");
                            final JSONArray promotionBusinessData = jsonResponse.getJSONArray("businesses");
                            if(promotionBusinessResult.equals("success")) {
                                mBusinessCardList.clear();
                                //set the values from the HTTP response, then input them to the recycle view using the adapter
                                for(int i = 0; i < promotionBusinessData.length(); i++) {
                                    JSONObject business = promotionBusinessData.getJSONObject(i);
                                    String businessId = business.getString("id");
                                    String businessImage = MainActivity.serverURL + business.getString("image");
                                    String timeText = business.getString("time");
                                    String titleText = business.getString("title");
                                    mBusinessCardList.add(new BusinessCard(businessId, businessImage, timeText, titleText, false));
                                }
                                mRecyclerView.setAdapter(mBusinessCardAdapter);
                                mBusinessCardAdapter.setOnItemClickListener(CustomerInterfaceActivity.this);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(CustomerInterfaceActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(CustomerInterfaceActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
            ) {
                //POST variables to send to the search request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("search", search);
                    return params;
                }
                //header values to send to the search request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            httpRequestQueue.add(httpSearchBusinessRequest);
    }

    //disable pressing the back button within this activity
    @Override
    public void onBackPressed() {}

    //receive the clicked business card
    @Override
    public void onItemClick(int position) {
        //create the customer view business's activity intent (page)
        Intent customerViewBusinessIntent = new Intent(CustomerInterfaceActivity.this,
                CustomerViewBusinessActivity.class);
        //set the customer view business's id variable and clear any cart items, then open the intent
        BusinessCard clickedItem = mBusinessCardList.get(position);
        customerViewBusinessIntent.putExtra("businessId", clickedItem.getBusinessId());
        CustomerViewBusinessActivity.cartItems.clear();
        startActivity(customerViewBusinessIntent);
    }
}
