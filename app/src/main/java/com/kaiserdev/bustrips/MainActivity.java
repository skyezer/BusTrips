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

    //FOR JSON CATCHER KEYS
    public static final String DESTINATION_KEY = "destination_key";
    public static final String DRIVER_KEY = "driver_key";
    public static final String VEHICLE_KEY = "vehicle_key";

    //FOR TEMPORARY PASSENGER DATA IN THE DATA ENTRY PROCESS (QR CODE, MANUAL INPUT)
    public static final String TEMP_PASSENGER_ID_KEY = "temp_passenger_id_key";
    public static final String TEMP_PASSENGER_NAME_KEY = "temp_passenger_name_key";
    public static final String TEMP_PASSENGER_DESTINATION_KEY = "temp_passenger_destination_key";
    public static final String TEMP_PASSENGER_DESTINATION_ID_KEY = "temp_passenger_destination_id_key";

    //FOR DRIVER AND VEHICLE DATA ENTRY AT THE PROFILE SECTION
    public static final String DRIVER_DATA_KEY = "driver_data_key";
    public static final String DRIVER_DATA_ID_KEY = "driver_data_id_key";
    public static final String VEHICLE_DATA_KEY = "vehicle_data_key";
    public static final String VEHICLE_DATA_ID_KEY = "vehicle_data_id_key";

    public RequestQueue mRequestQueue;
    public StringRequest mStringRequest;

    public static final List<String> destination_list = new ArrayList<String>();
    public static final List<String> destination_id_list = new ArrayList<String>();
    public static final List<String> destination_fare_list = new ArrayList<String>();

    public static final List<String> driver_list = new ArrayList<String>();
    public static final List<String> driver_id_list = new ArrayList<String>();
    public static final List<String> driver_school_id_list = new ArrayList<String>();
    public static final List<String> driver_person_id_list = new ArrayList<String>();

    public static final List<String> vehicle_list = new ArrayList<String>();
    public static final List<String> vehicle_id_list = new ArrayList<String>();


    SharedPreferences sharedPreferences;
    String sharedStudent_id, sharedFname, sharedLname, sharedProfile_pic, sharedDestination_list, sharedDriver_list, sharedVehicle_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedStudent_id = sharedPreferences.getString(STUDENT_ID_KEY, null);
        sharedFname = sharedPreferences.getString(FNAME_KEY, null);
        sharedLname = sharedPreferences.getString(LNAME_KEY, null);
        sharedProfile_pic = sharedPreferences.getString(PROFILE_PIC_KEY, null);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new PassengerFragment());

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

                    // Change always depending on the column name in the database
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

    private void Driver_toList() {
        JSONCatcher("driver", DRIVER_KEY);
        String response = sharedDriver_list = sharedPreferences.getString(DRIVER_KEY, null);
        if (sharedDriver_list != null && driver_list.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);

                    // Change always depending on the column name in the database
                    String driver_name = jo.getString("driver_name");
                    String id = jo.getString("id");
                    String school_id = jo.getString("school_id");
                    String person_id = jo.getString("person_id");

                    driver_list.add(driver_name);
                    driver_id_list.add(id);
                    driver_school_id_list.add(school_id);
                    driver_person_id_list.add(person_id);

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("TEST");
                builder.setMessage(response + driver_list + driver_id_list + driver_school_id_list + driver_person_id_list+ '\n' + '\n' + sharedDriver_list);
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

    private void Vehicle_toList() {
        JSONCatcher("vehicle", VEHICLE_KEY);
        String response = sharedVehicle_list = sharedPreferences.getString(VEHICLE_KEY, null);
        if (sharedVehicle_list != null && vehicle_list.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);

                    // Change always depending on the column name in the database
                    String vehicle_number = jo.getString("vehicle_number");
                    String id = jo.getString("id");
                    String model = jo.getString("model");
                    String plate_no = jo.getString("plate_no");
                    String color = jo.getString("color");

                    vehicle_list.add("Vehicle Number: "+ vehicle_number +" Plate Number: "+ plate_no +" Color: "+ color +" Model: "+ model);
                    vehicle_id_list.add(id);

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("TEST");
                builder.setMessage(response + vehicle_list + vehicle_id_list + '\n' + '\n' + sharedVehicle_list);
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
            case "vehicle":
                return "http://"+getResources().getString(R.string.machine_ip_address)+"/bustrips/vehicle.php";
            default:
                return null;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Destination_toList();
        Driver_toList();
        Vehicle_toList();
        if (sharedStudent_id == null && sharedLname == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }
}