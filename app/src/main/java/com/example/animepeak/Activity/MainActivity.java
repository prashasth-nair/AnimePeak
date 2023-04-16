package com.example.animepeak.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.animepeak.Fragments.FavouriteFragment;
import com.example.animepeak.Fragments.HomeFragment;
import com.example.animepeak.Fragments.SearchFragment;
import com.example.animepeak.Fragments.SettingsFragment;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    SearchFragment searchFragment = new SearchFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    public static ArrayList<Fav_object> fav_list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment, "HOME_FRAGMENT_TAG");
        tr.addToBackStack(null);
        tr.commit();

        fav_list = get_fav_list();
        if (fav_list==null){
            fav_list = new ArrayList<>();
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint({"NonConstantResourceId", "RestrictedApi"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()!=R.id.fav){
                    MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.fav);
                    menuItem.setIcon(R.drawable.baseline_favorite_unselected);
                }
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, homeFragment, "HOME_FRAGMENT_TAG")
                                .commit();
                        return true;
                    case R.id.search:
                        FragmentTransaction tr = getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, searchFragment, "SEARCH_FRAGMENT_TAG");
                        tr.addToBackStack(null);
                        tr.commit();
                        return true;
                    case R.id.fav:
                        item.setIcon(R.drawable.baseline_favorite_24_selected);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, favouriteFragment, "FAV_FRAGMENT_TAG")
                                .commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        return true;

                }

                return false;
            }
        });
    }
    public ArrayList<Fav_object> get_fav_list(){
        // Get the JSON string from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String favListJson = sharedPreferences.getString("favListJson", "");

// Convert the JSON string to an ArrayList
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Fav_object>>() {}.getType();
        ArrayList<Fav_object> favList = gson.fromJson(favListJson, type);
        return favList;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}