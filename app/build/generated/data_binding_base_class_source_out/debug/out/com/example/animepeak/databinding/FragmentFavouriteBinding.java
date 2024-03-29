// Generated by view binder compiler. Do not edit!
package com.example.animepeak.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.animepeak.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFavouriteBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView favLoading;

  @NonNull
  public final RecyclerView favRecycler;

  @NonNull
  public final TextView favTitle;

  @NonNull
  public final TextView noFav;

  private FragmentFavouriteBinding(@NonNull RelativeLayout rootView, @NonNull ImageView favLoading,
      @NonNull RecyclerView favRecycler, @NonNull TextView favTitle, @NonNull TextView noFav) {
    this.rootView = rootView;
    this.favLoading = favLoading;
    this.favRecycler = favRecycler;
    this.favTitle = favTitle;
    this.noFav = noFav;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFavouriteBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFavouriteBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_favourite, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFavouriteBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fav_loading;
      ImageView favLoading = ViewBindings.findChildViewById(rootView, id);
      if (favLoading == null) {
        break missingId;
      }

      id = R.id.fav_recycler;
      RecyclerView favRecycler = ViewBindings.findChildViewById(rootView, id);
      if (favRecycler == null) {
        break missingId;
      }

      id = R.id.fav_title;
      TextView favTitle = ViewBindings.findChildViewById(rootView, id);
      if (favTitle == null) {
        break missingId;
      }

      id = R.id.no_fav;
      TextView noFav = ViewBindings.findChildViewById(rootView, id);
      if (noFav == null) {
        break missingId;
      }

      return new FragmentFavouriteBinding((RelativeLayout) rootView, favLoading, favRecycler,
          favTitle, noFav);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
