package com.kaiserdev.bustrips;

import static com.kaiserdev.bustrips.MainActivity.DESTINATION_KEY;
import static com.kaiserdev.bustrips.MainActivity.SHARED_PREFS;
import static com.kaiserdev.bustrips.MainActivity.STUDENT_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_DESTINATION_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_DESTINATION_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_NAME_KEY;
import static com.kaiserdev.bustrips.MainActivity.destination_id_list;
import static com.kaiserdev.bustrips.MainActivity.destination_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestQueue mRequestQueue;
    public StringRequest mStringRequest;


    String sharedStudent_id, sharedDestination_list;
    SharedPreferences sharedPreferences;
    boolean isOpen = false;
    FloatingActionButton floatingActionButton, fab_qr, fab_manual;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    TextView manual_input, qr_scan;

    public PassengerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerFragment newInstance(String param1, String param2) {
        PassengerFragment fragment = new PassengerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Volley Variables

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger, container, false);

        //Variables
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, destination_list);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedStudent_id = sharedPreferences.getString(STUDENT_ID_KEY, null);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        fab_qr = view.findViewById(R.id.fab_qr);
        fab_manual = view.findViewById(R.id.fab_manual);

        manual_input = view.findViewById(R.id.textView_manual_input);
        qr_scan = view.findViewById(R.id.textView_qr);

        fab_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
                animateFab();
            }
            private void scanCode() {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Volume Up to turn on flash");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureAct.class);
                barLauncher.launch(options);
            }
            final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
                if (result.getContents() != null){
                    //Fetch Data from database
                    String data = result.getContents();
                    String student_id = sharedStudent_id;

                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String id_num = jsonObject.getString("id_num");
                        String stud_name = jsonObject.getString("stud_name");
                        String course = jsonObject.getString("course");

                        final Spinner sp = new Spinner(getContext());
                        sp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                        sp.setAdapter(adp);


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Result");
                        builder.setMessage(id_num + "\n" + stud_name + "\n" + course);

                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                builder.setView(sp);
                                builder.setTitle("Destination");
                                builder.setMessage(null);
                                builder.setSingleChoiceItems(destination_list.toArray(new CharSequence[0]), -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        String selectedDestination = destination_list.get(which);
                                        String selectedDestination_id = destination_id_list.get(which);

                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString(TEMP_PASSENGER_ID_KEY, id_num);
                                        editor.putString(TEMP_PASSENGER_NAME_KEY, stud_name);
                                        editor.putString(TEMP_PASSENGER_DESTINATION_ID_KEY, selectedDestination_id);
                                        editor.putString(TEMP_PASSENGER_DESTINATION_KEY, selectedDestination);
                                        editor.apply();
                                    }
                                });
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //TODO clear temp passenger data

                                        String name = sharedPreferences.getString(TEMP_PASSENGER_NAME_KEY,null);
                                        String destination_id = sharedPreferences.getString(TEMP_PASSENGER_DESTINATION_ID_KEY,null);
                                        String destination = sharedPreferences.getString(TEMP_PASSENGER_DESTINATION_KEY,null);

                                        Toast.makeText(getContext(),name + " is added",Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(),destination_id + " " + destination,Toast.LENGTH_SHORT).show();

                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                            }
                        }).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    qr_fetch(id_num, student_id);
                }
            });
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }

        });

        return view;
    }
    private void animateFab(){

        //Animations
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        if (isOpen){
            floatingActionButton.startAnimation(rotateBackward);

            fab_qr.startAnimation(fabClose);
            fab_manual.startAnimation(fabClose);

            qr_scan.startAnimation(fabClose);
            manual_input.startAnimation(fabClose);

            fab_qr.setClickable(false);
            fab_manual.setClickable(false);
            isOpen = false;
        }
        else {
            floatingActionButton.startAnimation(rotateForward);

            fab_qr.startAnimation(fabOpen);
            fab_manual.startAnimation(fabOpen);

            qr_scan.startAnimation(fabOpen);
            manual_input.startAnimation(fabOpen);

            fab_qr.setClickable(true);
            fab_manual.setClickable(true);
            isOpen = true;
        }
    }


//TODO Clean up

//    private void JSONCatcher() {
//
//        mRequestQueue = Volley.newRequestQueue(getContext());
//        mStringRequest = new StringRequest(Request.Method.POST,
//                getBaseUrl(), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(DESTINATION_KEY, response);
//                editor.apply();
//
//                sharedDestination_list = sharedPreferences.getString(DESTINATION_KEY, null);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jo = jsonArray.getJSONObject(i);
//                        String destination = jo.getString("destination_name");
//                        String id = jo.getString("id");
//                        String fare = jo.getString("fare");
//                        destination_list.add(destination);
//                        destination_id_list.add(id);
//                    }
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setTitle("TEST");
//                    builder.setMessage(response + destination_list + destination_id_list + '\n' + '\n' + sharedDestination_list);
//                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).show();
//                } catch (JSONException e) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setTitle("TEST");
//                    builder.setMessage(e.toString());
//                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        mStringRequest.setShouldCache(false);
//        mRequestQueue.add(mStringRequest);
//    }

    private String getBaseUrl() {
        return "http://"+getResources().getString(R.string.machine_ip_address)+"/bustrips/destination.php";
    }
}

/*
TODO Database catcher (driver's name for dropdown) see change_pass1.php
TODO Destination catcher (destination name for dropdown)
*/