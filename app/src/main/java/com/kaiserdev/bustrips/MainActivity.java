package com.kaiserdev.bustrips;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaiserdev.bustrips.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String STUDENT_ID_KEY = "student_id_key";
    public static final String FNAME_KEY = "fname_key";
    public static final String LNAME_KEY = "lname_key";
    public static final String PROFILE_PIC_KEY = "profile_pic_key";
    public static final String TEMP_PASSENGER_ID_KEY = "temp_passenger_id_key";
    public static final String TEMP_PASSENGER_NAME_KEY = "temp_passenger_name_key";
    public static final String DESTINATION_KEY = "destination_key";
    public static final String DRIVER_KEY = "driver_key";

    public RequestQueue mRequestQueue;
    public StringRequest mStringRequest;

    public static final List<String> destination_list = new ArrayList<String>();
    public static final List<String> destination_id_list = new ArrayList<String>();
    public static final List<String> destination_fare_list = new ArrayList<String>();

    SharedPreferences sharedPreferences;
    String sharedStudent_id, sharedFname, sharedLname, sharedProfile_pic, sharedDestination_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new PassengerFragment());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedStudent_id = sharedPreferences.getString(STUDENT_ID_KEY, null);
        sharedFname = sharedPreferences.getString(FNAME_KEY, null);
        sharedLname = sharedPreferences.getString(LNAME_KEY, null);
        sharedProfile_pic = sharedPreferences.getString(PROFILE_PIC_KEY, null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Window window = MainActivity.this.getWindow();

            switch (item.getItemId()){
                case R.id.passengers:

                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    // finally change the color
                    window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.dark_blue));
                    replaceFragment(new PassengerFragment());
                    break;
                case R.id.sync:
                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    // finally change the color
                    window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.dark_blue));
                    replaceFragment(new SyncFragment());
                    break;
                case R.id.profile:

                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    // finally change the color
                    window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.white));
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return  true;
        });

//        sharedDestination_list = sharedPreferences.getString(DESTINATION_KEY, null);
        //TODO broken
//        if (sharedDestination_list == null) {
//            JSONCatcher("destination", DESTINATION_KEY);
//        }


    }

    private void Destination_toList() {
        JSONCatcher("destination", DESTINATION_KEY);
        String response = sharedDestination_list = sharedPreferences.getString(DESTINATION_KEY, null);
        if (sharedDestination_list != null && destination_list.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String destination = jo.getString("destination_name");
                    String id = jo.getString("id");
                    String fare = jo.getString("fare");
                    destination_list.add(destination);
                    destination_id_list.add(id);
                    destination_fare_list.add(fare);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("TEST");
                builder.setMessage(response + destination_list + destination_id_list + '\n' + '\n' + sharedDestination_list);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            } catch (JSONException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("TEST");
                builder.setMessage(e.toString());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        }
    }

    private void JSONCatcher(final String url_key, final String sharedPreference_key) {

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        mStringRequest = new StringRequest(Request.Method.POST,
                getBaseUrl(url_key), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(sharedPreference_key, response);
                editor.apply();
                Toast.makeText(MainActivity.this,"here",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);

    }

    private String getBaseUrl(final String keyword) {
        switch (keyword) {
            case "destination":
                return "http://"+getResources().getString(R.string.machine_ip_address)+"/bustrips/destination.php";
            case "driver":
                return "http://"+getResources().getString(R.string.machine_ip_address)+"/bustrips/driver.php";
            default:
                return null;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Destination_toList();
        if (sharedStudent_id == null && sharedLname == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }
}