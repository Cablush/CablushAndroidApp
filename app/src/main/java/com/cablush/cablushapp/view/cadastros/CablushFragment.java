package com.cablush.cablushapp.view.cadastros;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.utils.PermissionUtils;
import com.cablush.cablushapp.utils.PictureUtils;

/**
 * Created by oscar on 07/02/16.
 */
public abstract class CablushFragment extends Fragment {

    public final String TAG = getClass().getSimpleName();

    static final int REQUEST_TAKE_PICTURE = 1;
    static final int REQUEST_LOAD_PICTURE = 2;

    private Uri pictureFileUri;

    /**
     * Get the picture path;
     *
     * @return The path of picture if this was loaded by camera or gallery, false otherwise.
     */
    public String getPictureFilePath() {
        if (pictureFileUri != null) {
            return PictureUtils.getPath(getContext(), pictureFileUri);
        }
        return null;
    }

    /**
     * Callback called on the Picture is loaded.
     * <p>Implement "imageView.setImageBitmap(PictureUtils.getBitmapFromUri(this, pictureFileUri));"
     * to load the picture on a imageView. </p>
     *
     * @param pictureFileUri The uri of the loaded picture.
     */
    public void onPictureLoaded(Uri pictureFileUri) {
        /* callback - no nothing */
    }

    /**
     * Start the Android Camera Activity for result.
     */
    public void dispatchTakePictureIntent() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Create file to store the picture.
            pictureFileUri = PictureUtils.getPictureFileUri(getContext());
            // Star the camera app, sending the file uri.
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
            startActivityForResult(imageCaptureIntent, REQUEST_TAKE_PICTURE);
        } else {
            Toast.makeText(getContext(), R.string.error_no_camera_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Start the Android Gallery Activity for result.
     */
    public void dispatchLoadPictureIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent,
                getString(R.string.txt_select_picture)), REQUEST_LOAD_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    PictureUtils.galleryAddPicture(getContext(), pictureFileUri);
                    onPictureLoaded(pictureFileUri);
                    break;
                case REQUEST_LOAD_PICTURE:
                    pictureFileUri = data.getData();
                    onPictureLoaded(pictureFileUri);
                    break;
            }
        } else {
            Toast.makeText(getContext(), R.string.error_getting_picture, Toast.LENGTH_SHORT).show();
            if (pictureFileUri != null) {
                PictureUtils.deleteFile(getContext(), pictureFileUri);
                pictureFileUri = null;
            }
        }
    }

    /**
     * Callback called if Location Permissions are granted.
     * <p>To be implemented by concrete classes that need Location Permissions.</p>
     */
    public void onLocationPermissionGranted() {
        /* callback - no nothing */
    }

    /**
     * Check if the Location Permissions are granted, requesting the permissions to the user if necessary.
     * And, call 'locationPermissionGranted()' on permissions granted.
     */
    public void checkLocationPermission() {
        if (!PermissionUtils.checkLocationPermission(getContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PermissionUtils.PERMISSIONS_LOCATION);
            }
        } else {
            onLocationPermissionGranted();
        }
    }

    /**
     * Callback called if Storage Permission are granted.
     * <p>To be implemented by concrete class that need Storage Permissions.</p>
     */
    public void onStoragePermissionGranted() {
        /* callback - do nothing */
    }

    /**
     * Check if the Storage Permissions are granted, requering the permissions to the user if necessary.
     * And, call '
     */
    public boolean checkStoragePermission() {
        if (!PermissionUtils.checkStoragePermission(getContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PermissionUtils.PERMISSIONS_STORAGE);
            }
            return false;
        } else {
            onStoragePermissionGranted();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult()");
        switch (requestCode) {
            case PermissionUtils.PERMISSIONS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionGranted();
                } else {
                    // TODO - Permission denied! Disable the functionality that depends on this permission. (?)
                    //http://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
                    Toast.makeText(getActivity(), R.string.ask_permissions_location, Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionUtils.PERMISSIONS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStoragePermissionGranted();
                } else {
                    // TODO - Permission denied! Disable the functionality that depends on this permission. (?)
                    //http://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
                    Toast.makeText(getActivity(), R.string.ask_permissions_storage, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
