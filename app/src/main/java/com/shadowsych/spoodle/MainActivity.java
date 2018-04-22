package com.shadowsych.spoodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public final static String serverURL = "http://spoodle.000webhostapp.com/";
    public static String userId;
    public static String userName;
    public static String userEmail;
    public static String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //asynchronous onclick listener for the login button
        final Button loginBTN = findViewById(R.id.loginBTN);
        loginBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //call the loginRequest() function with the values from emailText and passwordText
                final EditText emailText = findViewById(R.id.emailText);
                final EditText passwordText = findViewById(R.id.passwordText);
                loginRequest(emailText.getText().toString(), passwordText.getText().toString());
            }
        });

        //asynchronous onclick listener for the signup button
        final Button signupBTN = findViewById(R.id.signupBTN);
        signupBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open the signup's activity intent (page)
                Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    /*
    Sends a login request to the server
    @param email: The email inputted by the user in the email input textbox
    @param password: The password inputted by the user in the password input textbox
    */
    private void loginRequest(final String email, final String password) {
        //if the email or password input is empty, then notify the user with a Toast message
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Oh no, some of the fields are blank!", Toast.LENGTH_LONG).show();
        } else {
            //send an HTTPRequest to the server to login the user
            final RequestQueue httpRequestQueue = Volley.newRequestQueue(MainActivity.this);
            final StringRequest httpLoginRequest = new StringRequest(Request.Method.POST, (serverURL + "menu/login.php"),
                    //success, the request responded successfully!
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //set the email and userType variable, then open the appropriate user's interface's activity intent (page)
                                JSONObject jsonResponse = new JSONObject(response);
                                final String loginResult = jsonResponse.getString("result");
                                if(loginResult.equals("success")) {
                                    userId = jsonResponse.getString("userId");
                                    userName = jsonResponse.getString("userName");
                                    userEmail = jsonResponse.getString("userEmail");
                                    userType = jsonResponse.getString("userType");
                                    if (userType.equals("customer")) {
                                        Intent customerInterfaceIntent = new Intent(MainActivity.this, CustomerInterfaceActivity.class);
                                        startActivity(customerInterfaceIntent);
									} else if (userType.equals("business")) {
                                        Intent BusinessInterfaceIntent = new Intent(MainActivity.this, BusinessInterfaceActivity.class);
                                        startActivity(BusinessInterfaceIntent);
									}
                                    httpRequestQueue.stop();
                                } else if(loginResult.equals("invalidLogin")){
                                    Toast.makeText(MainActivity.this, "Your login details were incorrect!", Toast.LENGTH_LONG).show();
									httpRequestQueue.stop();
								} else {
                                    Toast.makeText(MainActivity.this, "An external error occurred when logging your account...", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                            httpRequestQueue.stop();
                        }
                    }
            ){
                //POST variables to send to the login request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userEmail", email);
                    params.put("userPassword", password);
                    return params;
                }
                //header values to send to the login request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
			httpRequestQueue.add(httpLoginRequest);
        }
    }
}