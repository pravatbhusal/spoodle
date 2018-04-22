package com.shadowsych.spoodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //asynchronous onclick listener for the signup button
        final Button signupBTN = findViewById(R.id.signupBTN);
        signupBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get the values from nameText, emailText, passwordText
                final EditText nameText = findViewById(R.id.nameText);
                final EditText emailText = findViewById(R.id.emailText);
                final EditText passwordText = findViewById(R.id.passwordText);
                //check if the user accepted the terms of service
                final RadioButton termsRadioButton = findViewById(R.id.termsType);
                if(!termsRadioButton.isChecked()) {
                    Toast.makeText(SignupActivity.this, "You must agree to the terms of service!", Toast.LENGTH_LONG).show();
                    return;
                }
                //get the selected account type from the radio group, and check if a radio button is selected
                final RadioGroup userTypeGroup = findViewById(R.id.userTypeGroup);
                final int selectedUserTypeId = userTypeGroup.getCheckedRadioButtonId();
                String userType = "";
                if(selectedUserTypeId != -1) {
                    RadioButton userTypeRadioButton = findViewById(selectedUserTypeId);
                    userType = userTypeRadioButton.getText().toString().toLowerCase();
                }
                //call the signupRequest() function
                signupRequest(nameText.getText().toString(), emailText.getText().toString(),
                        passwordText.getText().toString(), userType);
            }
        });

        //asynchronous onclick listener for the home button
        final ImageButton homeBTN = findViewById(R.id.homeBTN);
        homeBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go back to the previous intent [main activity's intent (page)]
                onBackPressed();
            }
        });
    }

    /*
    Sends a signup request to the server
    @param name: The name inputted by the user in the name input textbox
    @param email: The email inputted by the user in the email input textbox
    @param password: The password inputted by the user in the password input textbox
    */
    private void signupRequest(final String name, final String email, final String password, final String userType) {
        //if the name, email, password, or accountType input is empty, then notify the user with a Toast message
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || userType.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Oh no, some of the fields are blank!", Toast.LENGTH_LONG).show();
        //if the email string doesn't contain an @ symbol, then it's not a real email
        } else if (!email.contains("@")) {
            Toast.makeText(SignupActivity.this, "Please enter a valid email!", Toast.LENGTH_LONG).show();
        } else {
            //send an HTTPRequest to the server to signup the user
            final RequestQueue httpRequestQueue = Volley.newRequestQueue(SignupActivity.this);
            final StringRequest httpSignupRequest = new StringRequest(Request.Method.POST, (MainActivity.serverURL + "menu/signup.php"),
                    //success, the request responded successfully!
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //set the result, email, and userType variable, then open the appropriate user's interface's activity intent (page)
                                JSONObject jsonResponse = new JSONObject(response);
                                final String signupResult = jsonResponse.getString("result");
                                if(signupResult.equals("success")) {
                                    MainActivity.userId = jsonResponse.getString("userId");
                                    MainActivity.userName = jsonResponse.getString("userName");
                                    MainActivity.userEmail = jsonResponse.getString("userEmail");
                                    MainActivity.userType = jsonResponse.getString("userType");
                                    if(MainActivity.userType.equals("customer")) {
                                        Intent customerInterfaceIntent = new Intent(SignupActivity.this, CustomerInterfaceActivity.class);
                                        startActivity(customerInterfaceIntent);
									} else if(MainActivity.userType.equals("business")) {
                                        Intent BusinessInterfaceIntent = new Intent(SignupActivity.this, BusinessInterfaceActivity.class);
                                        startActivity(BusinessInterfaceIntent);
									}
                                    httpRequestQueue.stop();
                                } else if(signupResult.equals("emailInUse")) {
                                    Toast.makeText(SignupActivity.this, "The email is already in use!", Toast.LENGTH_LONG).show();
									httpRequestQueue.stop();
								} else {
                                    Toast.makeText(SignupActivity.this, "An external error occurred when registering your account...", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(SignupActivity.this, "An error occurred with your internet...", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                            httpRequestQueue.stop();
                        }
                    }
            ) {
                //POST variables to send to the signup request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", name);
                    params.put("userEmail", email);
                    params.put("userPassword", password);
                    params.put("userType", userType);
                    return params;
                }
                //header values to send to the signup request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
			httpRequestQueue.add(httpSignupRequest);
        }
    }
}
