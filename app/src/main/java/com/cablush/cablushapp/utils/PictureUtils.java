package com.cablush.cablushapp.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

    public static final String TAG = PictureUtils.class.getSimpleName();

    private static final String PICTURE_DIR = "Cablush";
    private static final String PICTURE_FILE_NAME =  "Cablush_%9d.jpg";

    /**
     * Load a remote image by its url into a ImageView.
     * <p>
     * Use a handler to load a image after the view was started, because a bug in Picasso
     * to fit the image in a view without predefined width and height.
     * </p>
     *
     * @param context
     * @param imageURL
     * @param view
     * @param hideOnFail
     */
    public static void loadRemoteImage(final Context context, final String imageURL,
                                       final ImageView view, final boolean hideOnFail) {
        if (imageURL != null) {
            // Preload the image to picasso cache.
            Picasso.with(context).load(imageURL).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Image preloaded in cache!");
                }

                @Override
                public void onError() {
                    Log.d(TAG, "Error preloading the image!");
                }
            });

            // Handler to load the image after the view was visible,
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RequestCreator rc = Picasso.with(context).load(imageURL);
                    rc.placeholder(R.drawable.missing);
                    if (!hideOnFail) {
                        rc.error(R.drawable.missing);
                    }
                    rc.fit().centerInside().into(view, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image loaded!");
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "Error loading image!");
                            if (hideOnFail) {
                                view.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }, 1000); // 1 sec (!?)
        } else {
            if (hideOnFail) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Load a image from path
     *
     * @param context
     * @param imagePath
     * @param view
     * @param hideOnFail
     */
    public static void loadImage(Context context, String imagePath,
                                 final ImageView view, final boolean hideOnFail) {
        if (fileExist(imagePath)) {
            int width = view.getWidth();
            int height = view.getHeight();
            if ((width == 0 || height == 0) && view.getDrawable() != null) {
                width = view.getDrawable().getMinimumWidth();
                height = view.getDrawable().getMinimumHeight();
            }
            view.setImageBitmap(getBitmapFromPath(context, imagePath, width, height));
        } else {
            loadRemoteImage(context, imagePath, view, hideOnFail);
        }
    }

    /**
     * Check if a file exists in the local filesystem.
     *
     * @param filePath
     * @return
     */
    public static boolean fileExist(String filePath) {
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
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
                    String.format(Locale.ROOT, PICTURE_FILE_NAME, getRadomInt()));

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
                    Environment.DIRECTORY_PICTURES), PICTURE_DIR);
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
     * Notifies the MediaScanners after changes on a file, so it is immediately available to
     * the user and other apps.
     * <p>If the file is null, notify the change on the external storage directory.</p>
     *
     * @param context
     * @param file
     */
    private static void notifyMediaScanners(Context context, File file) {
        // Tell the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(context,
                new String[]{file != null
                        ? file.toString()
                        : Environment.getExternalStorageDirectory().toString()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    /**
     * Generate a random int number between 100000000 and 999999999.
     *
     * @return
     */
    private static int getRadomInt() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(999999999 - 100000000) + 100000000;
    }

    /**
     * Add the Picture to a Gallery.
     *
     * @param context
     * @param pictureUri
     */
    public static void galleryAddPicture(Context context, Uri pictureUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(pictureUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * Delete a file by its uri, notifying the MediaScanner if necessary.
     *
     * @param context
     * @param fileUri
     */
    public static void deleteFile(Context context, Uri fileUri) {
        if (fileUri != null) {
            File file = new File(fileUri.getPath());
            if (file.exists() && file.delete()) {
                notifyMediaScanners(context, null);
            }
        }
    }

    /**
     * Get the Scaled Bitmap.
     *
     * @param context
     * @param pictureUri
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap getBitmapFromUri(Context context, Uri pictureUri,
                                          int targetWidth, int targetHeight) {
        // Get picture path
        String picturePath = getPath(context, pictureUri);

        return getBitmapFromPath(context, picturePath, targetWidth, targetHeight);
    }

    /**
     * Get the Scaled Bitmap.
     *
     * @param context
     * @param picturePath
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap getBitmapFromPath(Context context, String picturePath,
                                          int targetWidth, int targetHeight) {
        try {
            // Get the dimensions of the bitmap
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);

            // Determine how much to scale down the image
            int scaleFactor = 1;
            if (targetWidth > 0 && targetHeight > 0) {
                scaleFactor = Math.min(options.outWidth / targetWidth, options.outHeight / targetHeight);
            }

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;

            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception ex) {
            Log.e(TAG, "Error getting Bitmap from file.", ex);
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access Framework Documents,
     * as well as the _data field for the MediaStore and other file-based ContentProviders.
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        // Check if the version of current device is greater than API 19 (KitKat).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) { // ExternalStorageProvider
                final String[] split = DocumentsContract.getDocumentId(uri).split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String[] split = DocumentsContract.getDocumentId(uri).split(":");
                final Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                return getDataColumn(context, contentUri, "_id = ?", new String[] {split[1]});
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris,
     * and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {MediaStore.MediaColumns.DATA};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Make PictureUtils a utility class by preventing instantiation.
     */
    private PictureUtils() {
        throw new AssertionError();
    }
}
