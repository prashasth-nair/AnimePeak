// Generated by view binder compiler. Do not edit!
package com.example.animepeak.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.animepeak.R;
import com.google.android.gms.common.SignInButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityProfileBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final CardView bannerCard;

  @NonNull
  public final ImageView bannerImage;

  @NonNull
  public final LinearLayout headerLayout;

  @NonNull
  public final Button logout;

  @NonNull
  public final TextView nameTextView;

  @NonNull
  public final ImageView profileBack;

  @NonNull
  public final CardView profileCard;

  @NonNull
  public final ImageView profileImage;

  @NonNull
  public final TextView profileTitle;

  @NonNull
  public final LinearLayout profileTitleBar;

  @NonNull
  public final SignInButton signInButton;

  private ActivityProfileBinding(@NonNull RelativeLayout rootView, @NonNull CardView bannerCard,
      @NonNull ImageView bannerImage, @NonNull LinearLayout headerLayout, @NonNull Button logout,
      @NonNull TextView nameTextView, @NonNull ImageView profileBack, @NonNull CardView profileCard,
      @NonNull ImageView profileImage, @NonNull TextView profileTitle,
      @NonNull LinearLayout profileTitleBar, @NonNull SignInButton signInButton) {
    this.rootView = rootView;
    this.bannerCard = bannerCard;
    this.bannerImage = bannerImage;
    this.headerLayout = headerLayout;
    this.logout = logout;
    this.nameTextView = nameTextView;
    this.profileBack = profileBack;
    this.profileCard = profileCard;
    this.profileImage = profileImage;
    this.profileTitle = profileTitle;
    this.profileTitleBar = profileTitleBar;
    this.signInButton = signInButton;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.banner_card;
      CardView bannerCard = ViewBindings.findChildViewById(rootView, id);
      if (bannerCard == null) {
        break missingId;
      }

      id = R.id.bannerImage;
      ImageView bannerImage = ViewBindings.findChildViewById(rootView, id);
      if (bannerImage == null) {
        break missingId;
      }

      id = R.id.headerLayout;
      LinearLayout headerLayout = ViewBindings.findChildViewById(rootView, id);
      if (headerLayout == null) {
        break missingId;
      }

      id = R.id.logout;
      Button logout = ViewBindings.findChildViewById(rootView, id);
      if (logout == null) {
        break missingId;
      }

      id = R.id.nameTextView;
      TextView nameTextView = ViewBindings.findChildViewById(rootView, id);
      if (nameTextView == null) {
        break missingId;
      }

      id = R.id.profile_back;
      ImageView profileBack = ViewBindings.findChildViewById(rootView, id);
      if (profileBack == null) {
        break missingId;
      }

      id = R.id.profile_card;
      CardView profileCard = ViewBindings.findChildViewById(rootView, id);
      if (profileCard == null) {
        break missingId;
      }

      id = R.id.profileImage;
      ImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.profile_title;
      TextView profileTitle = ViewBindings.findChildViewById(rootView, id);
      if (profileTitle == null) {
        break missingId;
      }

      id = R.id.profile_title_bar;
      LinearLayout profileTitleBar = ViewBindings.findChildViewById(rootView, id);
      if (profileTitleBar == null) {
        break missingId;
      }

      id = R.id.sign_in_button;
      SignInButton signInButton = ViewBindings.findChildViewById(rootView, id);
      if (signInButton == null) {
        break missingId;
      }

      return new ActivityProfileBinding((RelativeLayout) rootView, bannerCard, bannerImage,
          headerLayout, logout, nameTextView, profileBack, profileCard, profileImage, profileTitle,
          profileTitleBar, signInButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
