package com.tanishq.legaldeepilinking;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DeepLinking extends AppCompatActivity {

    EditText email_otp,contact_otp;
    GifImageView loader,loader1;
    TextView link,otp_heading,date,time,submit,heading_otp,heading_otp_hindi,gst,gst_hindi=null;
    String type,serviceName,myDate,myTime,email,contact;
    Intent intent;
    LinearLayout linearLayout,success,failed;
    RelativeLayout email_layout,contact_layout;
    private static final String PRODUCTS_DEEP_LINK = "/o/";
    private static final String DOCUMENT_LINK = "/d/";

    public final String url = "your api here";
    public final String url_1  = "your api here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);
        email_otp = findViewById(R.id.email_otp);
        contact_otp = findViewById(R.id.contact_otp);
        loader = findViewById(R.id.loader);
        intent = getIntent();
        Uri deeplink = intent.getData();
        // Parse the deeplink and take the adequate action
        if (deeplink != null) {
            parseDeepLink(deeplink);
        }
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,  "mycheck::: ");
                InputMethodManager imm = (InputMethodManager)DeepLinking.this.getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = DeepLinking.this.getCurrentFocus();
                if (focusedView !=null) {
                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }

                contact = contact_otp.getText().toString();
                email = email_otp.getText().toString();
               if (type.contains("MO")){
                   if (contact.isEmpty()){
                       Log.d(TAG, "qwerty:dlfsdbl");
                       contact_otp.setError("Enter the OTP");
                   }else {
                       loader1 = findViewById(R.id.loader1);
                       loader1.setVisibility(View.VISIBLE);
                       Log.d(TAG, "onClick: " + contact);
                       submit.setVisibility(View.GONE);
                       submitOrder(deeplink.getLastPathSegment());
                   }
               }else if (type.equals("EO")) {
                   if (email.isEmpty()) {
                       Log.d(TAG, "iamineo: ");
                       email_otp.setError("Enter the OTP");
                   } else {
                       loader1 = findViewById(R.id.loader1);
                       loader1.setVisibility(View.VISIBLE);
                       submit.setVisibility(View.GONE);
                       submitOrder(deeplink.getLastPathSegment());
                   }
               }else if (type.equals("EOM")){
                   Log.d(TAG, "onClick: mY CONDTION" );
                   if (email.isEmpty() && contact.isEmpty()){
                       email_otp.setError("Enter the OTP");
                       contact_otp.setError("Enter the OTP");
                       Log.d(TAG,  "jlkfbskfk::: ");

                   }else if (!email.isEmpty()){
                       loader1 = findViewById(R.id.loader1);
                       loader1.setVisibility(View.VISIBLE);
                       submit.setVisibility(View.GONE);

                       submitOrder(deeplink.getLastPathSegment());

                   }else{
                       loader1 = findViewById(R.id.loader1);
                       loader1.setVisibility(View.VISIBLE);
                       submit.setVisibility(View.GONE);
                       submitOrder(deeplink.getLastPathSegment());
                   }

               }else {
                   if (email.isEmpty() && contact.isEmpty()) {
                       email_otp.setError("Enter the OTP");
                       contact_otp.setError("Enter the OTP");
                   } else if (email.isEmpty()) {
                       email_otp.setError("Enter the OTP");

                   } else if (contact.isEmpty()) {
                       contact_otp.setError("Enter the OTP");
                   } else {
                       loader1 = findViewById(R.id.loader1);
                       loader1.setVisibility(View.VISIBLE);
                       submit.setVisibility(View.GONE);
                       submitOrder(deeplink.getLastPathSegment());
                   }
               }

            }
        });




    }
    private void parseDeepLink(Uri deeplink) {
        // The path of the deep link, e.g. '/products/123?coupon=save90'
        String path = deeplink.getPath();

        if (path.startsWith(PRODUCTS_DEEP_LINK)) {
            // Handles a product deep link
            intent = new Intent(this, DeepLinking.class);
            intent.putExtra("id", deeplink.getLastPathSegment()); // 123
            placeOrder(deeplink.getLastPathSegment());


        }else if (path.startsWith(DOCUMENT_LINK)){
            intent = new Intent(this, DocumentUpload.class);
            intent.putExtra("my",deeplink.getLastPathSegment());

        }
        else{
            // Fall back to the main activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }


    }
    public void placeOrder (String path)
    {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shortCode", path);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("gott", "" + response);
                        System.out.println(response);


                        try
                        {
                            if(response.getString("code").equalsIgnoreCase("200") && response.getString("status").equalsIgnoreCase("success")) {
                                Toast.makeText(getApplicationContext(), "one_success_call", Toast.LENGTH_SHORT).show();
                                type = response.getString("type");
                                serviceName = response.getString("serviceName");

                                Log.d("my", "onResponse:" + type + serviceName);
                                if (type.contains("MO")) {
                                    Toast.makeText(getApplicationContext(), "typeCheck " + type, Toast.LENGTH_SHORT).show();
                                    loader = findViewById(R.id.loader);
                                    loader.setVisibility(View.GONE);
                                    linearLayout = findViewById(R.id.linear);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    contact_layout = findViewById(R.id.contact_layout);
                                    contact_layout.setVisibility(View.VISIBLE);
                                    check();

                                } else if (type.contains("EOM")) {
                                    Toast.makeText(getApplicationContext(), "typeCheck " + type, Toast.LENGTH_SHORT).show();
                                    loader = findViewById(R.id.loader);
                                    loader.setVisibility(View.GONE);
                                    linearLayout = findViewById(R.id.linear);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    heading_otp = findViewById(R.id.heading_otp);
                                    heading_otp.setVisibility(View.VISIBLE);
                                    heading_otp_hindi = findViewById(R.id.heading_otp_hindi);
                                    heading_otp_hindi.setVisibility(View.VISIBLE);
                                    contact_layout = findViewById(R.id.contact_layout);
                                    contact_layout.setVisibility(View.VISIBLE);
                                    email_layout = findViewById(R.id.email_layout);
                                    email_layout.setVisibility(View.VISIBLE);
                                    check();

                                } else if (type.contains("EO")) {
                                    Toast.makeText(getApplicationContext(), "typeCheck " + type, Toast.LENGTH_SHORT).show();
                                    loader = findViewById(R.id.loader);
                                    loader.setVisibility(View.GONE);
                                    linearLayout = findViewById(R.id.linear);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    email_layout = findViewById(R.id.email_layout);
                                    email_layout.setVisibility(View.VISIBLE);
                                    check();

                                }else if (type.contains("EAM")) {
                                    Toast.makeText(getApplicationContext(), "typeCheck " + type, Toast.LENGTH_SHORT).show();
                                    loader = findViewById(R.id.loader);
                                    loader.setVisibility(View.GONE);
                                    linearLayout = findViewById(R.id.linear);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    contact_layout = findViewById(R.id.contact_layout);
                                    contact_layout.setVisibility(View.VISIBLE);
                                    email_layout = findViewById(R.id.email_layout);
                                    email_layout.setVisibility(View.VISIBLE);
                                    check();
                                }
                            }
                            else if(response.getString("code").equalsIgnoreCase("201") && response.getString("status").equalsIgnoreCase("complete"))
                            {
                                myDate = response.getString("date");
                                myTime = response.getString("time");
                                success = findViewById(R.id.sucess);
                                success.setVisibility(View.VISIBLE);
                                otp_heading =findViewById(R.id.otp);
                                otp_heading.setText("OTP Already Submitted");
                                date = findViewById(R.id.date);
                                date.setText("Date: " + myDate);
                                time = findViewById(R.id.time);
                                time.setText("Time: " + myTime);
                            }else if(response.getString("code").equalsIgnoreCase("201") && response.getString("status").equalsIgnoreCase("expire"))
                            {
                                failed = findViewById(R.id.failed);
                                failed.setVisibility(View.VISIBLE);
                            }else if(response.getString("code").equalsIgnoreCase("201") && response.getString("status").equalsIgnoreCase("fail"))
                            {

                                failed = findViewById(R.id.failed);
                                failed.setVisibility(View.VISIBLE);
                                link = findViewById(R.id.link);
                                link.setText("Invalid Link");


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                   }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //callBack.error(error);
                Log.d("TAG", "onErrorResponse: Error " + error);
                Toast.makeText(getApplicationContext(), "Something Went Wrong ", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AuthKey", "your key");
                return params;
            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }
    public void check(){
        if (serviceName.equals("TR")){
            gst = findViewById(R.id.serviceName);
            gst_hindi = findViewById(R.id.serviceName_hindi);
            gst.setText(R.string.tr);
            gst_hindi.setText(R.string.tr_hindi);
        } if (serviceName.equals("GSTR")){
            gst = findViewById(R.id.serviceName);
            gst_hindi = findViewById(R.id.serviceName_hindi);
            gst.setText(R.string.gst);
            gst_hindi.setText(R.string.gst_hindi);

        }
        if (serviceName.equals("ITRF")){
            gst = findViewById(R.id.serviceName);
            gst_hindi = findViewById(R.id.serviceName_hindi);
            gst.setText(R.string.itr);
            gst_hindi.setText(R.string.itr_hindi);

        } if (serviceName.equals("GSTRF")){
            gst = findViewById(R.id.serviceName);
            gst_hindi = findViewById(R.id.serviceName_hindi);
            gst.setText(R.string.gstrf);
            gst_hindi.setText(R.string.gstrf_hindi);

        }

    }
    public void submitOrder (String path) {


        contact = contact_otp.getText().toString();
        email = email_otp.getText().toString();


        JSONObject jsonObject_1 = new JSONObject();
        try {
                jsonObject_1.put("shortCode", path);
                jsonObject_1.put("type", type);
                jsonObject_1.put("emailOTP", email);
                jsonObject_1.put("contactOTP",contact);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest_1 = new JsonObjectRequest(Request.Method.POST, url_1, jsonObject_1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response_new) {
                Log.d("mymy", "" + response_new);
                System.out.println(response_new);
                try {
                    if (response_new.getString("code").equalsIgnoreCase("200") && response_new.getString("status").equalsIgnoreCase("success")) {
                        myDate = response_new.getString("date");
                        myTime = response_new.getString("time");
                        linearLayout = findViewById(R.id.linear);
                        linearLayout.setVisibility(View.GONE);
                        success = findViewById(R.id.sucess);
                        success.setVisibility(View.VISIBLE);
                        otp_heading =findViewById(R.id.otp);
                        otp_heading.setText("OTP Submitted");
                        date = findViewById(R.id.date);
                        date.setText("Date: " + myDate);
                        time = findViewById(R.id.time);
                        time.setText("Time: " + myTime);

                    }else if(response_new.getString("code").equalsIgnoreCase("201") && response_new.getString("status").equalsIgnoreCase("fail"))
                    {
                        Toast.makeText(getApplicationContext(), "one_fail_call", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: Error " + error);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AuthKey", "your key");
                return params;
            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest_1);




    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }



}