package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.fav_list;
import static com.example.animepeak.Activity.MainActivity.is_login;
import static com.example.animepeak.Activity.MainActivity.storeArrayToFirebase;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animepeak.Adapters.FavAdapter;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Objects;

public class FavouriteFragment extends Fragment {
    public static RecyclerView fav_recycler;
    @SuppressLint("StaticFieldLeak")
    public static TextView no_fav;
    TextView FavTitle;
    static String Source;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseApp.initializeApp(requireView().getContext());
        mAuth = FirebaseAuth.getInstance();
        fav_recycler = requireView().findViewById(R.id.fav_recycler);
        no_fav = requireView().findViewById(R.id.no_fav);
        FavTitle = requireView().findViewById(R.id.fav_title);



        SharedPreferences sharedpreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Source = sharedpreferences.getString("Source_Name", "GogoAnime");


        fav_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topVisiblePosition = ((GridLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                        .findFirstVisibleItemPosition();
                if (dy > 0) {
                    // The user has scrolled down, so shrink the title text
                    animateTextSizeChange(FavTitle, 20);
                }


                if (topVisiblePosition == 0) {
                    // The user has scrolled to the top, so expand the title text
                    animateTextSizeChange(FavTitle, 34);
                }
            }
        });


        int orientation = getResources().getConfiguration().orientation;


        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            fav_recycler.setLayoutManager(new GridLayoutManager(requireView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            fav_recycler.setLayoutManager(new GridLayoutManager(requireView().getContext(), 4));
        }

    }
    public void removeArrayFromFirebase(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            databaseRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // The array is successfully deleted from Firebase
                            Toast.makeText(getContext(), "Deleted Array", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error if the deletion fails
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }
    public void RetreiveArrayFromFirebase(){
        FirebaseUser user = mAuth.getCurrentUser();
        fav_list.clear();
        if (user != null) {

            String userId = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Fav_object favObject = snapshot.getValue(Fav_object.class);
                            if (favObject != null) {
                                Log.d("Fav", "Fav Title: " + favObject.getTitle());
                                Log.d("Fav", "Fav Source: " + favObject.fav_source);
                                fav_list.add(favObject);
                            } else {
                                Log.d("Fav", "Fav_object is null for snapshot: " + snapshot.toString());
                            }
                        }
                        Log.d("Fav", "fav_list size after retrieval: " + fav_list.size()); // Verify the size of fav_list
                        countSource(getActivity());
                        if (fav_list.size()==0){
                            no_fav.setVisibility(View.VISIBLE);
                        }else {
                            no_fav.setVisibility(View.GONE);
                        }
                        // Perform other operations on fav_list here if needed
                        // ...
                    } else {
                        // Handle the case when the array does not exist in the database
                        countSource(getActivity());
                        if (fav_list.size()==0){
                            no_fav.setVisibility(View.VISIBLE);
                        }else {
                            no_fav.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during the database read operation
                }
            });
        }
    }

    private void animateTextSizeChange(final TextView textView, final int newSize) {

        ValueAnimator animator = ValueAnimator.ofFloat(textView.getTextSize(), convertDpToPixel(newSize, requireActivity()));
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue);
            }
        });
        animator.start();
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static ArrayList<Fav_object> temp_fav_list(){
        // Create a temporary ArrayList to store items with a source of "gogo"
        ArrayList<Fav_object> tempArray = new ArrayList<>();

        // Loop through each item in fav_list

        for (Fav_object item : fav_list) {
            // Get the source string for this item
            String source = item.fav_source;


            // Check if the source for this item is "gogo"
            if (source!=null) {
                if (source.equals(Source)) {
                    // Add this item to the temporary ArrayList
                    tempArray.add(item);
                }
            }
        }
        return tempArray;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));
//            RetreiveArrayFromFirebase();
        } else {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
//            RetreiveArrayFromFirebase();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        fav_list.clear();
        if (acct != null) {
            is_login=true;
            Log.d("Status","Sucess");
            RetreiveArrayFromFirebase();
        }else{
            Log.d("Status","Failed");
            is_login=false;


        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public static void countSource(Activity activity){
        int gogoCount = 0;
        int zoroCount = 0;
        int hanimeCount = 0;

        // Loop through each item in fav_list
        for (Fav_object item : fav_list) {
            // Get the source string for this item
            String source = item.fav_source;

            if (source!=null) {// Check if the source for this item is "gogo"
                //        Fav_object item = temp_list.get(0);

                if (source.equals("GogoAnime")) {
                    // Increment the count for "gogo"
                    gogoCount++;
                } else if (source.equals("Zoro")) {
                    // Increment the count for "Zoro"
                    zoroCount++;
                }
            }
        }

        ArrayList<Fav_object> temp_list = temp_fav_list();

        FavAdapter favAdapter = new FavAdapter(activity, temp_list,Source,gogoCount,zoroCount,hanimeCount);
        fav_recycler.setAdapter(favAdapter);
        favAdapter.notifyDataSetChanged();
    }
}