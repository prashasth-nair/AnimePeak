// Generated by view binder compiler. Do not edit!
package com.example.animepeak.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.animepeak.R;
import com.google.android.exoplayer2.ui.PlayerView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityVideoPlayerBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView loading;

  @NonNull
  public final ImageView tempFastForward;

  @NonNull
  public final ImageView tempRewind;

  @NonNull
  public final PlayerView videoView;

  private ActivityVideoPlayerBinding(@NonNull RelativeLayout rootView, @NonNull ImageView loading,
      @NonNull ImageView tempFastForward, @NonNull ImageView tempRewind,
      @NonNull PlayerView videoView) {
    this.rootView = rootView;
    this.loading = loading;
    this.tempFastForward = tempFastForward;
    this.tempRewind = tempRewind;
    this.videoView = videoView;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityVideoPlayerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityVideoPlayerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_video_player, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityVideoPlayerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.loading;
      ImageView loading = ViewBindings.findChildViewById(rootView, id);
      if (loading == null) {
        break missingId;
      }

      id = R.id.temp_fast_forward;
      ImageView tempFastForward = ViewBindings.findChildViewById(rootView, id);
      if (tempFastForward == null) {
        break missingId;
      }

      id = R.id.temp_rewind;
      ImageView tempRewind = ViewBindings.findChildViewById(rootView, id);
      if (tempRewind == null) {
        break missingId;
      }

      id = R.id.videoView;
      PlayerView videoView = ViewBindings.findChildViewById(rootView, id);
      if (videoView == null) {
        break missingId;
      }

      return new ActivityVideoPlayerBinding((RelativeLayout) rootView, loading, tempFastForward,
          tempRewind, videoView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
