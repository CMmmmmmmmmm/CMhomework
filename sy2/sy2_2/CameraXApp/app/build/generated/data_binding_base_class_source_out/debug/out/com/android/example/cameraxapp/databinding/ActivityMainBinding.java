// Generated by view binder compiler. Do not edit!
package com.android.example.cameraxapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.android.example.cameraxapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button imageCaptureButton;

  @NonNull
  public final Guideline verticalCenterline;

  @NonNull
  public final Button videoCaptureButton;

  @NonNull
  public final PreviewView viewFinder;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button imageCaptureButton, @NonNull Guideline verticalCenterline,
      @NonNull Button videoCaptureButton, @NonNull PreviewView viewFinder) {
    this.rootView = rootView;
    this.imageCaptureButton = imageCaptureButton;
    this.verticalCenterline = verticalCenterline;
    this.videoCaptureButton = videoCaptureButton;
    this.viewFinder = viewFinder;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.image_capture_button;
      Button imageCaptureButton = ViewBindings.findChildViewById(rootView, id);
      if (imageCaptureButton == null) {
        break missingId;
      }

      id = R.id.vertical_centerline;
      Guideline verticalCenterline = ViewBindings.findChildViewById(rootView, id);
      if (verticalCenterline == null) {
        break missingId;
      }

      id = R.id.video_capture_button;
      Button videoCaptureButton = ViewBindings.findChildViewById(rootView, id);
      if (videoCaptureButton == null) {
        break missingId;
      }

      id = R.id.viewFinder;
      PreviewView viewFinder = ViewBindings.findChildViewById(rootView, id);
      if (viewFinder == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, imageCaptureButton,
          verticalCenterline, videoCaptureButton, viewFinder);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
