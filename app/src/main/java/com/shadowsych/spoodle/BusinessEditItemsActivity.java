package com.shadowsych.spoodle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.shadowsych.spoodle.assets.BusinessCategoryItem;
import com.shadowsych.spoodle.assets.BusinessCategoryItemAdapter;
import com.shadowsych.spoodle.assets.BusinessUpdateControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessEditItemsActivity extends AppCompatActivity implements BusinessCategoryItemAdapter.onItemClickListener {

    private String businessId;
    private String category;

    private RecyclerView mRecyclerView;
    private BusinessCategoryItemAdapter mCustomerBusinessCategoryItemsAdapter;
    private ArrayList<BusinessCategoryItem> mBusinessCategoryItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_edit_items);

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
                (BusinessEditItemsActivity.this, mBusinessCategoryItemsList);
        mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
        getCategoryItemsRequest();

        //asynchronous onclick listener when clicking the add category item button
        final Button addCategoryItemBTN = findViewById(R.id.addCategoryItemBTN);
        addCategoryItemBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the add category item dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(BusinessEditItemsActivity.this);
                LinearLayout layout = new LinearLayout(BusinessEditItemsActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                //add the EditTexts with their respective properties, and set the layout views
                final EditText itemText = new EditText(BusinessEditItemsActivity.this);
                final EditText price = new EditText(BusinessEditItemsActivity.this);
                price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                price.setTransformationMethod(new NumericKeyBoardTransformationMethod());
                final EditText quantity = new EditText(BusinessEditItemsActivity.this);
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantity.setTransformationMethod(new NumericKeyBoardTransformationMethod());
                final EditText externalities = new EditText(BusinessEditItemsActivity.this);
                externalities.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                final RadioButton termsRadioButton = new RadioButton(BusinessEditItemsActivity.this);
                termsRadioButton.setText("I agree with the terms of service.");
                //text hints
                itemText.setHint("Item Name");
                itemText.setHintTextColor(Color.LTGRAY);
                price.setHint("Price (in USD)");
                price.setHintTextColor(Color.LTGRAY);
                quantity.setHint("Quantity Available");
                quantity.setHintTextColor(Color.LTGRAY);
                externalities.setHint("Externalities (e.g. expires tomorrow, tear in wrapper, etc.)");
                externalities.setHintTextColor(Color.LTGRAY);
                //add texts
                layout.addView(itemText);
                layout.addView(price);
                layout.addView(quantity);
                layout.addView(externalities);
                layout.addView(termsRadioButton);
                //set the alert itemText
                alert.setTitle("Add Item");
                alert.setView(layout);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //add the category item, use ternary operator on price and quantity so
                        //if the value is blank, it doesn't parse blank
                        String newItem = itemText.getText().toString();
                        Double priceValue = price.getText().toString().isEmpty() ? -1 : Double.parseDouble(price.getText().toString());
                        Integer quantityValue = quantity.getText().toString().isEmpty() ? -1 : Integer.parseInt(quantity.getText().toString());
                        String externalitiesValue = externalities.getText().toString();
                        //check if the user accepted the terms of service
                        if(!termsRadioButton.isChecked()) {
                            Toast.makeText(BusinessEditItemsActivity.this,
                                    "You must agree to the terms of service!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //check if any of the item's values are blank or invalid
                        if(newItem.isEmpty() || priceValue < 0 || quantityValue <= 0) {
                            Toast.makeText(BusinessEditItemsActivity.this,
                                    "One of your fields were blank or invalid!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(int i = 0; i < mBusinessCategoryItemsList.size(); i++) {
                            //if the category item name is already in-use, then don't allow it to be used
                            if(mBusinessCategoryItemsList.get(i).getCategoryItem().equalsIgnoreCase(newItem)) {
                                Toast.makeText(BusinessEditItemsActivity.this,
                                        "The item name is already in-use!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        BusinessUpdateControl.addCategoryItemRequest(BusinessEditItemsActivity.this, businessId,
                                category, newItem, priceValue, quantityValue, externalitiesValue);
                        mBusinessCategoryItemsList.add(new BusinessCategoryItem(category, newItem, priceValue, quantityValue, externalitiesValue));
                        mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
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
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(BusinessEditItemsActivity.this);
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
                                                    //remove the category item that was swipped
                                                    BusinessUpdateControl.removeCategoryItemRequest(BusinessEditItemsActivity.this, businessId,
                                                            mBusinessCategoryItemsList.get(position).getCategory(),
                                                            mBusinessCategoryItemsList.get(position).getCategoryItem());
                                                    mBusinessCategoryItemsList.remove(position);
                                                    mCustomerBusinessCategoryItemsAdapter.notifyItemRemoved(position);
                                                }
                                                mCustomerBusinessCategoryItemsAdapter.notifyDataSetChanged();
                                            }
                                            @Override
                                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {}
                                });
                                mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
                                mRecyclerView.addOnItemTouchListener(swipeTouchListener);
                                mCustomerBusinessCategoryItemsAdapter.setOnItemClickListener(BusinessEditItemsActivity.this);
                                httpRequestQueue.stop();
                            } else {
                                Toast.makeText(BusinessEditItemsActivity.this, "An external error occurred...", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(BusinessEditItemsActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
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
        mCustomerBusinessCategoryItemsAdapter.setOnItemClickListener(BusinessEditItemsActivity.this);
    }

    //receive the clicked business category item and open an edit category item dialog
    @Override
    public void onItemClick(final int position) {
        final BusinessCategoryItem clickedItem = mBusinessCategoryItemsList.get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(BusinessEditItemsActivity.this);
        LinearLayout layout = new LinearLayout(BusinessEditItemsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //add the EditTexts with their respective properties, and set the layout views
        final EditText itemText = new EditText(BusinessEditItemsActivity.this);
        itemText.setText(clickedItem.getCategoryItem());
        final EditText price = new EditText(BusinessEditItemsActivity.this);
        price.setText(String.valueOf(clickedItem.getCategoryItemPrice()));
        price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        price.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        final EditText quantity = new EditText(BusinessEditItemsActivity.this);
        quantity.setText(String.valueOf(clickedItem.getQuantity()));
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantity.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        final EditText externalities = new EditText(BusinessEditItemsActivity.this);
        externalities.setText(clickedItem.getExternalities());
        externalities.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        final RadioButton termsRadioButton = new RadioButton(BusinessEditItemsActivity.this);
        termsRadioButton.setText("I agree with the terms of service.");
        //text hints
        itemText.setHint("Item Name");
        itemText.setHintTextColor(Color.LTGRAY);
        price.setHint("Price (in USD)");
        price.setHintTextColor(Color.LTGRAY);
        quantity.setHint("Quantity Available");
        quantity.setHintTextColor(Color.LTGRAY);
        externalities.setHint("Externalities (e.g. expires tomorrow, tear in wrapper, etc.)");
        externalities.setHintTextColor(Color.LTGRAY);
        //add texts
        layout.addView(itemText);
        layout.addView(price);
        layout.addView(quantity);
        layout.addView(externalities);
        layout.addView(termsRadioButton);
        //set the alert itemText
        alert.setTitle("Edit Item");
        alert.setView(layout);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //edit the category item, use ternary operator on price and quantity so
                //if the value is blank, it doesn't parse blank
                String newItem = itemText.getText().toString();
                Double priceValue = price.getText().toString().isEmpty() ? -1 : Double.parseDouble(price.getText().toString());
                Integer quantityValue = quantity.getText().toString().isEmpty() ? -1 : Integer.parseInt(quantity.getText().toString());
                String externalitiesValue = externalities.getText().toString();
                //check if the user accepted the terms of service
                if(!termsRadioButton.isChecked()) {
                    Toast.makeText(BusinessEditItemsActivity.this,
                            "You must agree to the terms of service!", Toast.LENGTH_LONG).show();
                    return;
                }
                //check if any of the item's values are blank or invalid
                if(newItem.isEmpty() || priceValue < 0 || quantityValue <= 0) {
                    Toast.makeText(BusinessEditItemsActivity.this,
                            "One of your fields were blank or invalid!", Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0; i < mBusinessCategoryItemsList.size(); i++) {
                    //if the category item name is already in-use and the user did change the name, then don't allow it to be used
                    if(mBusinessCategoryItemsList.get(i).getCategoryItem().equalsIgnoreCase(newItem)
                            && !newItem.equalsIgnoreCase(clickedItem.getCategoryItem())) {
                        Toast.makeText(BusinessEditItemsActivity.this,
                                "The item name is already in-use!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                BusinessUpdateControl.updateCategoryItemRequest(BusinessEditItemsActivity.this, businessId,
                        category, clickedItem.getCategoryItem(), newItem, priceValue, quantityValue, externalitiesValue);
                mBusinessCategoryItemsList.set(position, new BusinessCategoryItem(category, newItem, priceValue, quantityValue, externalitiesValue));
                mRecyclerView.setAdapter(mCustomerBusinessCategoryItemsAdapter);
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

    //set any EditText to use this method in order to show a number keyboard
    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}
