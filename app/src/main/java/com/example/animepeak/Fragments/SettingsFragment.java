package com.example.animepeak.Fragments;


import static com.example.animepeak.Fragments.HomeFragment.Home_IDList;
import static com.example.animepeak.Fragments.HomeFragment.Home_TitleUrlList;
import static com.example.animepeak.Fragments.HomeFragment.Home_imageUrlList;
import static com.example.animepeak.Fragments.SearchFragment.Search_IDList;
import static com.example.animepeak.Fragments.SearchFragment.Search_TitleUrlList;
import static com.example.animepeak.Fragments.SearchFragment.Search_imageUrlList;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.animepeak.Functions.UpdateApp;
import com.example.animepeak.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;

import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class SettingsFragment extends Fragment {
    AutoCompleteTextView autoCompleteTextView;
    LinearLayout Update;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoCompleteTextView = getView().findViewById(R.id.autoCompleteTextView);
        Update = getView().findViewById(R.id.update_button);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for permissions when starting for first time

                Dexter.withContext(getActivity())
                        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    new UpdateApp.update_app(getActivity()).execute();
                                } else {
                                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Current_Source = sharedpreferences.getString("Source_Name", "GogoAnime");
        if (Current_Source.equals("GogoAnime")) {

            autoCompleteTextView.setText("GogoAnime");
        } else if (Current_Source.equals("Zoro")) {

            autoCompleteTextView.setText("Zoro");

        } else if (Current_Source.equals("Hanime")) {

            autoCompleteTextView.setText("Hanime");

        }

        SharedPreferences.Editor editor = sharedpreferences.edit();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("GogoAnime")) {
                    editor.putString("Source_Name", "GogoAnime");
                } else if (adapterView.getItemAtPosition(i).toString().equals("Zoro")) {
                    editor.putString("Source_Name", "Zoro");

                } else if (adapterView.getItemAtPosition(i).toString().equals("Hanime")) {
                    editor.putString("Source_Name", "Hanime");
                }

                editor.commit();
                Home_TitleUrlList.clear();
                Home_imageUrlList.clear();
                Home_IDList.clear();

                Search_TitleUrlList.clear();
                Search_imageUrlList.clear();
                Search_IDList.clear();

                getActivity().getSupportFragmentManager().beginTransaction().detach(getActivity().getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT_TAG")).attach(getActivity().getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT_TAG")).commit();
                if (getActivity().getSupportFragmentManager().findFragmentByTag("SEARCH_FRAGMENT_TAG") != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().detach(getActivity().getSupportFragmentManager().findFragmentByTag("SEARCH_FRAGMENT_TAG")).attach(getActivity().getSupportFragmentManager().findFragmentByTag("SEARCH_FRAGMENT_TAG")).commit();
                }
                getActivity().recreate();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] source_lists = getResources().getStringArray(R.array.source_list);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown, source_lists);
        autoCompleteTextView.setAdapter(arrayAdapter);
    }
}