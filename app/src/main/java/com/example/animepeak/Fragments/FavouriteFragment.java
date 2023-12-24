package com.example.animepeak.Fragments;

import static com.example.animepeak.Activity.MainActivity.fav_list;
import static com.example.animepeak.Activity.MainActivity.is_login;
import static com.example.animepeak.Fragments.HomeFragment.home_loading;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animepeak.Adapters.FavAdapter;
import com.example.animepeak.Functions.Fav_object;
import com.example.animepeak.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    public ImageView fav_loading;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseApp.initializeApp(requireView().getContext());
        mAuth = FirebaseAuth.getInstance();
        fav_recycler = requireView().findViewById(R.id.fav_recycler);
        no_fav = requireView().findViewById(R.id.no_fav);
        FavTitle = requireView().findViewById(R.id.fav_title);
        fav_loading = requireView().findViewById(R.id.fav_loading);

        fav_loading.setVisibility(View.VISIBLE);

        Glide.with(this)
                .asGif()
                .load(R.raw.loading_animation)
                .into(home_loading);

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
                                fav_list.add(favObject);
                            } else {
                                Log.d("Fav", "Fav_object is null for snapshot: " + snapshot.toString());
                            }
                        }
                        fav_loading.setVisibility(View.GONE);
                        FavAdapter favAdapter = new FavAdapter(getActivity(),fav_list);
                        fav_recycler.setAdapter(favAdapter);
//                        favAdapter.notifyDataSetChanged();


                        if (fav_list.size()==0){
                            no_fav.setVisibility(View.VISIBLE);
                        }else {
                            no_fav.setVisibility(View.GONE);
                        }
                        // Perform other operations on fav_list here if needed
                        // ...
                    } else {
                        // Handle the case when the array does not exist in the database
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

        return new ArrayList<>(fav_list);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));

        } else {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
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
            RetreiveArrayFromFirebase();
        }else{
            Log.d("Status","Failed");
            is_login=false;


        }
    }
}