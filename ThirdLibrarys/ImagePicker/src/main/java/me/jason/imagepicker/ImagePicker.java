package me.jason.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

import me.jason.imagepicker.internal.entity.Item;

public final class ImagePicker {
    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;

    private ImagePicker(Activity activity) {
        this(activity, null);
    }

    private ImagePicker(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private ImagePicker(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static ImagePicker from(Activity activity) {
        return new ImagePicker(activity);
    }

    public static ImagePicker from(Fragment fragment) {
        return new ImagePicker(fragment);
    }

    public static ArrayList<Item> obtainItemResult(@NonNull Intent data) {
        return data.getParcelableArrayListExtra(IntentHub.EXTRA_RESULT_SELECTED_ITEM);
    }

    public static ArrayList<Uri> obtainUriResult(@NonNull Intent data) {
        return data.getParcelableArrayListExtra(IntentHub.EXTRA_RESULT_SELECTED_URI);
    }

    public static ArrayList<String> obtainPathResult(@NonNull Intent data) {
        return data.getStringArrayListExtra(IntentHub.EXTRA_RESULT_SELECTED_PATH);
    }

    public static int obtainFromType(@NonNull Intent data) {
        return data.getIntExtra(IntentHub.EXTRA_RESULT_FROM, IntentHub.FROM_NONE);
    }

    public SelectionCreator choose(Set<MimeType> mimeTypes) {
        return this.choose(mimeTypes, true);
    }

    public SelectionCreator choose(Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        return new SelectionCreator(this, mimeTypes, mediaTypeExclusive);
    }

    @Nullable
    Activity getActivity() {
        return mContext.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}
