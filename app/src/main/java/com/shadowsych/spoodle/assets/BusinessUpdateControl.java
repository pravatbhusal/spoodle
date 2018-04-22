package com.shadowsych.spoodle.assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shadowsych.spoodle.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public final class BusinessUpdateControl {
    public static void updateTimeRequest(final Context context, final String businessId, final String time) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_time.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("time", time);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void updateAddressRequest(final Context context, final String businessId, final String address) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_address.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("address", address);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void updateEmailRequest(final Context context, final String businessId, final String email) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_email.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("email", email);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void updatePhoneRequest(final Context context, final String businessId, final String phone) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_phone.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("phone", phone);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void updateCategoryRequest(final Context context, final String businessId, final String oldCategory, final String newCategory) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_category.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("oldCategory", oldCategory);
                params.put("newCategory", newCategory);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void updateCategoryItemRequest(final Context context, final String businessId, final String category, final String oldCategoryItem, final String newCategoryItem, final Double price, final int quantity, final String externalities) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_category_item.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("category", category);
                params.put("oldCategoryItem", oldCategoryItem);
                params.put("newCategoryItem", newCategoryItem);
                params.put("price", String.valueOf(price));
                params.put("quantity", String.valueOf(quantity));
                params.put("externalities", externalities);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void updateImageRequest(final Context context, final String businessId, final Bitmap businessBitMapImage) {
        //encode the bitmap to a string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        businessBitMapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        final String businessImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/update_image.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("businessImage", businessImage);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void addCategoryRequest(final Context context, final String businessId, final String category) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/add_category.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("category", category);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void addCategoryItemRequest(final Context context, final String businessId, final String category, final String categoryItem, final Double price, final int quantity, final String externalities) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/add_category_item.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("category", category);
                params.put("categoryItem", categoryItem);
                params.put("price", String.valueOf(price));
                params.put("quantity", String.valueOf(quantity));
                params.put("externalities", externalities);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void removeCategoryRequest(final Context context, final String businessId, final String category) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/remove_category.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("category", category);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }

    public static void removeCategoryItemRequest(final Context context, final String businessId, final String category, final String categoryItem) {
        //send an HTTPRequest to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(context);
        final StringRequest httpRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "update/remove_category_item.php"),
                //success, the request responded successfully!
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
            //POST variables to send to the  request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("businessId", businessId);
                params.put("category", category);
                params.put("categoryItem", categoryItem);
                return params;
            }
            //header values to send to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpRequest);
    }
}
