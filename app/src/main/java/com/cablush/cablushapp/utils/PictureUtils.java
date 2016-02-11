package com.cablush.cablushapp.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.cablush.cablushapp.BuildConfig;
import com.cablush.cablushapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.Locale;
import java.util.Random;

/**
 * Created by oscar on 07/02/16.
 */
public class PictureUtils {

    public static void loadRemoteImage(Context context, String imageURL,
                                       final ImageView view, final boolean hideOnFail) {
        if (imageURL != null) {
            if (BuildConfig.DEBUG) {
                imageURL = imageURL.replace("localhost", "10.0.2.2");
            }
            RequestCreator rc = Picasso.with(context).load(imageURL);
            if (!hideOnFail) {
                rc.placeholder(R.drawable.missing);
                rc.error(R.drawable.missing);
            }
            rc.fit().into(view, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    if (hideOnFail) {
                        view.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            if (hideOnFail) {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri pictureUri) {
        String picturePath = getPicturePath(context, pictureUri);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        int scale = 1;
        while (options.outHeight / scale / 2 >= 200 && options.outWidth / scale / 2 >= 200) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    private static String getPicturePath(Context context, Uri pictureUri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        CursorLoader cursorLoader = new CursorLoader(context, pictureUri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Create a file Uri for saving a picture.
     *
     * @param context
     */
    public static Uri getPictureFileUri(Context context) {
        // Check to see if external SDCard is mounted or not.
        if (isExternalStorageWritable()) {
            // Create a picture file name randomly
            final File pictureFile = getStorageDir(
                    String.format(Locale.ROOT, "Cablush_%9d.jpg", getRadomInt()));

            // Always notify the MediaScanners after storing the picture, so that it is immediately
            // available to the user.
            notifyMediaScanners(context, pictureFile);

            // Return Uri from picture file.
            return Uri.fromFile(pictureFile);
        } else {
            // Return null if no SDCard is mounted.
            return null;
        }
    }

    /**
     * Get the External Directory to store the Picture.
     *
     * @param pictureName
     */
    private static File getStorageDir(String pictureName) {
        // Check to see if external SDCard is mounted or not.
        if (isExternalStorageWritable()) {
            // Create a path where we will place our Picture in the user's public DCIM  directory.
            // Note that you should be careful about what you place here, since the user often
            // manages these files.
            final File path = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "Cablush");
            final File file = new File(path, pictureName);
            // Make sure the directory exists.
            path.mkdirs();
            return file;
        } else {
            return null;
        }
    }

    /**
     * Checks if external storage is available for read and write.
     *
     * @return true, if the external storage is writable, false, otherwise.
     */
    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Notifies the MediaScanners after Downloading the Picture, so it is immediately available to
     * the user.
     */
    private static void notifyMediaScanners(Context context, File file) {
        // Tell the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(context,
                new String[]{file.toString()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private static int getRadomInt() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(999999999 - 100000000) + 100000000;
    }

    /**
     * Make PictureUtils a utility class by preventing instantiation.
     */
    private PictureUtils() {
        throw new AssertionError();
    }
}
