package com.example.animepeak.Activity;

import static com.example.animepeak.Fragments.FavouriteFragment.countSource;
import static com.example.animepeak.Fragments.HomeFragment.Home_IDList;
import static com.example.animepeak.Fragments.HomeFragment.Home_TitleUrlList;
import static com.example.animepeak.Fragments.HomeFragment.Home_imageUrlList;
import static com.example.animepeak.Fragments.HomeFragment.gogoanime_popular;

import static com.example.animepeak.Fragments.HomeFragment.zoro_popular;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.animepeak.Fragments.FavouriteFragment;
import com.example.animepeak.Fragments.HomeFragment;
import com.example.animepeak.Fragments.SearchFragment;
import com.example.animepeak.Fragments.SettingsFragment;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.Functions.UpdateApp;
import com.example.animepeak.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    SearchFragment searchFragment = new SearchFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    public static boolean is_auto_update = false;
    public static boolean is_home = false;
    public static ArrayList<Fav_object> fav_list ;
    private static FirebaseAuth mAuth;
    public static boolean is_login =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment, "HOME_FRAGMENT_TAG");
        tr.addToBackStack(null);
        tr.commit();


        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            is_login=true;
            Log.d("Status","Sucess");
        }else{
            Log.d("Status","Failed");
            is_login=false;
        }

        fav_list = new ArrayList<Fav_object>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (fav_list==null){
            fav_list = new ArrayList<>();
        }
        is_auto_update = sharedPreferences.getBoolean("is_auto_update",false);
        if (is_auto_update){
            is_home = true;
            new UpdateApp.update_app(this).execute();

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
                        Home_TitleUrlList.clear();
                        Home_imageUrlList.clear();
                        Home_IDList.clear();
                        if (gogoanime_popular != null  ) {
                            
//                            gogoanime_popular.cancel(true);
                            gogoanime_popular = null;
                        }
                        if (zoro_popular != null ) {
                            zoro_popular.cancel(true);
                            zoro_popular = null;
                        }

                        return true;
                    case R.id.fav:
                        item.setIcon(R.drawable.baseline_favorite_24_selected);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, favouriteFragment, "FAV_FRAGMENT_TAG")
                                .commit();
                        Home_TitleUrlList.clear();
                        Home_imageUrlList.clear();
                        Home_IDList.clear();
                        if (gogoanime_popular != null  ) {
                           
//                            gogoanime_popular.cancel(true);
                            gogoanime_popular = null;
                        }
                        if (zoro_popular != null ) {
                            zoro_popular.cancel(true);
                            zoro_popular = null;
                        }

                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        Home_TitleUrlList.clear();
                        Home_imageUrlList.clear();
                        Home_IDList.clear();
                        if (gogoanime_popular != null  ) {
                            
//                            gogoanime_popular.cancel(true);
                            gogoanime_popular = null;
                        }
                        if (zoro_popular != null ) {
                            zoro_popular.cancel(true);
                            zoro_popular = null;
                        }

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
        return gson.fromJson(favListJson, type);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static void storeArrayToFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            databaseRef.setValue(fav_list)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(MainActivity.this, "Array stored in Firebase", Toast.LENGTH_SHORT).show();
                                Log.d("Here","SUCCESS");
                            } else {
//                                Toast.makeText(MainActivity.this, "Failed to store array in Firebase", Toast.LENGTH_SHORT).show();
                                Log.d("Here","Failed");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error",e.toString());
                        }
                    });
        }
    }
}