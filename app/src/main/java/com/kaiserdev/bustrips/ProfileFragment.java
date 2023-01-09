package com.kaiserdev.bustrips;

import static com.kaiserdev.bustrips.MainActivity.DESTINATION_KEY;
import static com.kaiserdev.bustrips.MainActivity.DRIVER_DATA_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.DRIVER_DATA_KEY;
import static com.kaiserdev.bustrips.MainActivity.DRIVER_KEY;
import static com.kaiserdev.bustrips.MainActivity.SHARED_PREFS;
import static com.kaiserdev.bustrips.MainActivity.STUDENT_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.FNAME_KEY;
import static com.kaiserdev.bustrips.MainActivity.LNAME_KEY;
import static com.kaiserdev.bustrips.MainActivity.PROFILE_PIC_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_DESTINATION_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_DESTINATION_KEY;
import static com.kaiserdev.bustrips.MainActivity.TEMP_PASSENGER_NAME_KEY;
import static com.kaiserdev.bustrips.MainActivity.VEHICLE_DATA_ID_KEY;
import static com.kaiserdev.bustrips.MainActivity.VEHICLE_DATA_KEY;
import static com.kaiserdev.bustrips.MainActivity.destination_fare_list;
import static com.kaiserdev.bustrips.MainActivity.destination_id_list;
import static com.kaiserdev.bustrips.MainActivity.destination_list;
import static com.kaiserdev.bustrips.MainActivity.driver_id_list;
import static com.kaiserdev.bustrips.MainActivity.driver_list;
import static com.kaiserdev.bustrips.MainActivity.driver_person_id_list;
import static com.kaiserdev.bustrips.MainActivity.driver_school_id_list;
import static com.kaiserdev.bustrips.MainActivity.vehicle_id_list;
import static com.kaiserdev.bustrips.MainActivity.vehicle_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String sharedStudent_id, sharedFname, sharedLname, sharedProfile_pic;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedStudent_id = sharedPreferences.getString(STUDENT_ID_KEY, null);
        sharedFname = sharedPreferences.getString(FNAME_KEY, null);
        sharedLname = sharedPreferences.getString(LNAME_KEY, null);
        sharedProfile_pic = sharedPreferences.getString(PROFILE_PIC_KEY, null);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView user_id = (TextView) view.findViewById(R.id.user_id);

        ImageView picture = (ImageView) view.findViewById(R.id.pic);

        Button logout = (Button) view.findViewById(R.id.logout);
        Button trip_details_button = (Button) view.findViewById(R.id.button_trip_details);
        Button save = (Button) view.findViewById(R.id.save);


        name.setText(sharedFname +" "+ sharedLname);
        user_id.setText(sharedStudent_id);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder altdial = new AlertDialog.Builder(getActivity());
                altdial.setMessage("Are you sure you want to Logout?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(STUDENT_ID_KEY, null);
                                editor.putString(FNAME_KEY, null);
                                editor.putString(LNAME_KEY, null);
                                editor.putString(PROFILE_PIC_KEY, null);
                                editor.putString(DESTINATION_KEY, null);
                                editor.putString(DRIVER_KEY, null);
                                editor.putString(DRIVER_DATA_KEY, null);
                                editor.putString(DRIVER_DATA_ID_KEY, null);
                                editor.putString(VEHICLE_DATA_KEY, null);
                                editor.putString(VEHICLE_DATA_ID_KEY, null);
                                editor.apply();

                                destination_id_list.clear();
                                destination_list.clear();
                                destination_fare_list.clear();

                                driver_list.clear();
                                driver_id_list.clear();
                                driver_school_id_list.clear();
                                driver_person_id_list.clear();

                                vehicle_list.clear();
                                vehicle_id_list.clear();

                                Intent intent = new Intent(getActivity(), Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = altdial.create();
                alert.setTitle("Logout");
                alert.show();
            }
        });
        trip_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Driver");
                builder.setMessage(null);
                builder.setSingleChoiceItems(driver_list.toArray(new CharSequence[0]), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String selectedDriver = driver_list.get(which);
                        String selectedDriver_id = driver_id_list.get(which);

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(DRIVER_DATA_KEY, selectedDriver);
                        editor.putString(DRIVER_DATA_ID_KEY, selectedDriver_id);
                        editor.apply();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.setTitle("Select Vehicle");
                        builder.setMessage(null);
                        builder.setSingleChoiceItems(vehicle_list.toArray(new CharSequence[0]), -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String selectedVehicle = vehicle_list.get(which);
                                String selectedVehicle_id = vehicle_id_list.get(which);

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(VEHICLE_DATA_KEY, selectedVehicle);
                                editor.putString(VEHICLE_DATA_ID_KEY, selectedVehicle_id);
                                editor.apply();

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String name = sharedPreferences.getString(DRIVER_DATA_KEY,null);
                                String vehicle = sharedPreferences.getString(VEHICLE_DATA_KEY,null);

                                Toast.makeText(getContext(), "Driver: "+ name + " Vehicle: "+vehicle, Toast.LENGTH_LONG).show();

                                dialogInterface.dismiss();
                            }
                        }).create().show();
                    }
                }).create().show();

            }
        });

        return  view;
    }

}
