package com.kaiserdev.bustrips;

import static com.kaiserdev.bustrips.MainActivity.FNAME_KEY;
import static com.kaiserdev.bustrips.MainActivity.LNAME_KEY;
import static com.kaiserdev.bustrips.MainActivity.PROFILE_PIC_KEY;
import static com.kaiserdev.bustrips.MainActivity.SHARED_PREFS;
import static com.kaiserdev.bustrips.MainActivity.STUDENT_ID_KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {

    //local Variables
    TextView student_id, password;
    View login;

    // Volley Variables
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        //Declare Variables
        student_id = findViewById(R.id.student_id);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);

        student_id.requestFocus();
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressButton progressButton = new ProgressButton(Login.this, login);
                //handles login button animation
                progressButton.buttonActivated();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //initiates login and connects to the php file
                        signIn(student_id.getText().toString(), password.getText().toString());

                    }
                }, 300);

            }
        });
    }
    private void signIn( final String student_id, final String password) {

        // Initializing Request queue
        mRequestQueue = Volley.newRequestQueue(Login.this);
        mStringRequest = new StringRequest(Request.Method.POST,
                getBaseUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String fname = jsonObject.getString("fname");
                    String lname = jsonObject.getString("lname");
                    String student_id = jsonObject.getString("student_id");
                    String profile_pic = jsonObject.getString("profile_pic");

                    if (success.equals("1")) {
                        ProgressButton progressButton = new ProgressButton(Login.this, login);
                        progressButton.buttonFinished();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this,message,Toast.LENGTH_SHORT).show();
                                // Finish
                                finish();

                                sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(STUDENT_ID_KEY, student_id);
                                editor.putString(FNAME_KEY, fname);
                                editor.putString(LNAME_KEY, lname);
                                editor.putString(PROFILE_PIC_KEY, profile_pic);
                                editor.apply();

                                // Start activity dashboard
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 300);

                    }
                    if (success.equals("0")) {
                        Toast.makeText(Login.this,message,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    ProgressButton progressButton = new ProgressButton(Login.this, login);

                    if (e.toString().equals("org.json.JSONException: No value for fname")){
                        Toast.makeText(Login.this,"Student ID Invalid",Toast.LENGTH_SHORT).show();
                        progressButton.buttonError();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressButton.buttonOriginal();
                            }
                        }, 2000);

                    }
//                    Toast.makeText(Login.this, e.toString(),Toast.LENGTH_SHORT).show();

                    if (e.toString().equals("org.json.JSONException: End of input at character 0 of ")){
                        Toast.makeText(Login.this,"Student ID or Password may be wrong",Toast.LENGTH_SHORT).show();
                        progressButton.buttonError();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressButton.buttonOriginal();
                            }
                        }, 2000);
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressButton progressButton = new ProgressButton(Login.this, login);
                Toast.makeText(Login.this,"Connection Error",Toast.LENGTH_SHORT).show();
                progressButton.buttonError();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressButton.buttonOriginal();
                    }
                }, 2000);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("student_id",student_id);
                params.put("password",password);

                return params;
            }
        };

        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }
    private String getBaseUrl (){
        return "http://"+getResources().getString(R.string.machine_ip_address)+"/bustrips/conductor_sign_in.php";
    }
}