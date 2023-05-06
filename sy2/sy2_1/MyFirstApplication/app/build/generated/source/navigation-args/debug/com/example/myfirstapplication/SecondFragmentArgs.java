package com.example.myfirstapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class SecondFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private SecondFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private SecondFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static SecondFragmentArgs fromBundle(@NonNull Bundle bundle) {
    SecondFragmentArgs __result = new SecondFragmentArgs();
    bundle.setClassLoader(SecondFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("myArg")) {
      int myArg;
      myArg = bundle.getInt("myArg");
      __result.arguments.put("myArg", myArg);
    } else {
      throw new IllegalArgumentException("Required argument \"myArg\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static SecondFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    SecondFragmentArgs __result = new SecondFragmentArgs();
    if (savedStateHandle.contains("myArg")) {
      int myArg;
      myArg = savedStateHandle.get("myArg");
      __result.arguments.put("myArg", myArg);
    } else {
      throw new IllegalArgumentException("Required argument \"myArg\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  public int getMyArg() {
    return (int) arguments.get("myArg");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("myArg")) {
      int myArg = (int) arguments.get("myArg");
      __result.putInt("myArg", myArg);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("myArg")) {
      int myArg = (int) arguments.get("myArg");
      __result.set("myArg", myArg);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    SecondFragmentArgs that = (SecondFragmentArgs) object;
    if (arguments.containsKey("myArg") != that.arguments.containsKey("myArg")) {
      return false;
    }
    if (getMyArg() != that.getMyArg()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + getMyArg();
    return result;
  }

  @Override
  public String toString() {
    return "SecondFragmentArgs{"
        + "myArg=" + getMyArg()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull SecondFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(int myArg) {
      this.arguments.put("myArg", myArg);
    }

    @NonNull
    public SecondFragmentArgs build() {
      SecondFragmentArgs result = new SecondFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setMyArg(int myArg) {
      this.arguments.put("myArg", myArg);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public int getMyArg() {
      return (int) arguments.get("myArg");
    }
  }
}
