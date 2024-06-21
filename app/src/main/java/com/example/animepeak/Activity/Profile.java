package com.example.animepeak.Activity;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;


import static com.example.animepeak.Activity.MainActivity.fav_list;
import static com.example.animepeak.Activity.MainActivity.is_login;
import static com.example.animepeak.Activity.MainActivity.storeArrayToFirebase;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animepeak.R;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class Profile extends AppCompatActivity {
    ImageView back;
    TextView name;
    ImageView profile_dp;
    SignInButton signInButton;
    Button logout;
    public Uri personPhoto;


    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 100;  // Can be any integer unique to the Activity.

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.profile_back);
        name = findViewById(R.id.nameTextView);
        profile_dp = findViewById(R.id.profileImage);
        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.sign_in_button);
        logout = findViewById(R.id.logout);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SignInClient oneTapClient = Identity.getSignInClient(Profile.this);
        String RequestIDToken = getAssetJsonData(this);

//         Configure sign-in to request the user's ID, email address, and basic
//         profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        assert RequestIDToken != null;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(RequestIDToken)
                .build();

//         Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//         Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            Glide.with(this)
                    .load(personPhoto)
                    .into(profile_dp);
            signInButton.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.raw.boy1)
                    .into(profile_dp);
            name.setText("John Doe");
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signInButton.setVisibility(View.VISIBLE);
                        logout.setVisibility(View.GONE);
                        Glide.with(Profile.this)
                                .load(R.raw.boy1)
                                .into(profile_dp);
                        name.setText("John Doe");
                        fav_list.clear();
                    }
                });
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
    public static String getAssetJsonData(Context context) {
        String json = null;
        String value = null;
        try {
            InputStream is = context.getAssets().open("keys.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            value = jsonObject.getString("value");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return value;

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            Toast.makeText(this,"LOG In Successful",Toast.LENGTH_LONG).show();
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
//                String personGivenName = acct.getGivenName();
//                String personFamilyName = acct.getFamilyName();
//                String personEmail = acct.getEmail();
//                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                name.setText(personName);
                Glide.with(this)
                        .load(personPhoto)

                        .into(profile_dp);
                signInButton.setVisibility(View.GONE);
                is_login = true;
//                Toast.makeText(Profile.this, account.getId(), Toast.LENGTH_SHORT).show();
                // Get the Google ID token and authenticate with Firebase
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User is successfully authenticated with Firebase.
                                    // Store the array in the user's Firebase database.
                                    if (fav_list.size() > 0) {
                                        storeArrayToFirebase();
                                    }
//                                    Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();

                                } else {
                                    // Handle authentication failure.
                                    Toast.makeText(Profile.this, "Firebase authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                logout.setVisibility(View.VISIBLE);

            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Error!!", Toast.LENGTH_LONG).show();

        }
    }


}