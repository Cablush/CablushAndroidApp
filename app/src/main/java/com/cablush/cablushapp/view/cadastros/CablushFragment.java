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
     * Callback called on the Picture is loaded.
     * <p>Implement "imageView.setImageBitmap(PictureUtils.getBitmapFromUri(this, pictureFileUri));"
     * to load the picture on a imageView. </p>
     *
     * @param pictureFileUri
     */
    public void onPictureLoaded(Uri pictureFileUri) {
        /* callback - no nothing */
    }

    /**
     *
     */
    public void dispatchTakePictureIntent() {
        pictureFileUri = PictureUtils.getPictureFileUri(getContext());
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
        startActivityForResult(imageCaptureIntent, REQUEST_TAKE_PICTURE);
    }

    /**
     *
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
                    onPictureLoaded(pictureFileUri);
                    break;
                case REQUEST_LOAD_PICTURE:
                    onPictureLoaded(data.getData());
                    break;
            }
        } else {
            Toast.makeText(getContext(), R.string.error_getting_picture, Toast.LENGTH_SHORT).show();
            // TODO delete file!
            //Não resolvi este to do, nao tem como deletar uma foto q deu erro, hehe
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
        if (!PermissionUtils.checkLocationPermission(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PermissionUtils.PERMISSIONS_LOCATION);
            }
        } else {
            onLocationPermissionGranted();
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
                return;
            case PermissionUtils.PERMISSIONS_STORAGE:
                return;
        }
    }
}
