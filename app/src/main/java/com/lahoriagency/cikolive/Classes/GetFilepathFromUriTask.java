package com.lahoriagency.cikolive.Classes;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.fragment.app.FragmentManager;

import com.lahoriagency.cikolive.Fragments.ProgressDialogFragment;
import com.lahoriagency.cikolive.Interfaces.OnImagePickedListener;
import com.lahoriagency.cikolive.Interfaces.SchemeType;
import com.lahoriagency.cikolive.NewPackage.CoreApp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URLConnection;
import java.net.URLDecoder;


public class GetFilepathFromUriTask extends BaseAsyncTask<Intent, Void, File> {
    private static final int BUFFER_SIZE_2_MB = 2048;

    private final WeakReference<FragmentManager> fmWeakReference;
    private final OnImagePickedListener listener;
    private final int requestCode;

    public GetFilepathFromUriTask(FragmentManager fm, OnImagePickedListener listener, int requestCode) {
        this.fmWeakReference = new WeakReference<>(fm);
        this.listener = listener;
        this.requestCode = requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgress();
    }

    @Override
    public File performInBackground(Intent... params) throws Exception {
        //Uri fileUri = params[0].getData();


        Intent data = params[0];

        String imageFilePath = null;
        Uri uri = data.getData();
        String uriScheme = uri.getScheme();

        boolean isFromGoogleApp = uri.toString().startsWith(SchemeType.SCHEME_CONTENT_GOOGLE);
        boolean isKitKatAndUpper = true;

        if (SchemeType.SCHEME_FILE.equalsIgnoreCase(uriScheme)) {
            imageFilePath = uri.getPath();
        } else {
            imageFilePath = ImageUtils.saveUriToFile(uri);
        }

        if (TextUtils.isEmpty(imageFilePath)) {
            throw new IOException("Can't find a filepath for URI " + uri.toString());
        }


        return new File(imageFilePath);

    }

    private File getFile(Uri uri) throws Exception {
        String fileExtension = getFileExtension(uri);
        if (TextUtils.isEmpty(fileExtension)) {
            throw new Exception("Didn't get file extension");
        }

        String decodedFilePath = URLDecoder.decode(uri.toString(), "UTF-8");
        String fileName = decodedFilePath.substring(decodedFilePath.lastIndexOf("/") + 1);
        if (!fileName.contains(fileExtension)) {
            fileName = fileName + "." + fileExtension;
        }

        File resultFile = getFileFromCache(fileName);

        if (resultFile == null) {
            resultFile = createAndWriteFileToCache(fileName, uri);
        }

        return resultFile;
    }

    private String getFileExtension(Uri uri) throws Exception {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());

        boolean isUriSchemeContent = uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT);
        if (TextUtils.isEmpty(fileExtension) && isUriSchemeContent) {
            ContentResolver contentResolver = AppChat.getInstance().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }

        boolean isUriSchemeFile = uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_FILE);
        if (TextUtils.isEmpty(fileExtension) && isUriSchemeFile) {
            String path = uri.getPath();
            String sourceFileType = URLConnection.guessContentTypeFromStream(
                    new BufferedInputStream(new FileInputStream(new File(path))));
            fileExtension = sourceFileType.substring(sourceFileType.lastIndexOf("/") + 1);
        }

        return fileExtension;
    }

    private File getFileFromCache(String fileName) {
        File foundFile = null;

        File dir = new File(AppChat.getInstance().getCacheDir().getAbsolutePath());

        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.getName().equals(fileName)) {
                    foundFile = file;
                    break;
                }
            }
        }

        return foundFile;
    }

    private File createAndWriteFileToCache(String fileName, Uri uri) throws Exception {
        File resultFile = new File(AppChat.getInstance().getCacheDir(), fileName);

        ParcelFileDescriptor parcelFileDescriptor = AppChat.getInstance().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        InputStream inputStream = new FileInputStream(fileDescriptor);
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resultFile));

        try {
            byte[] buf = new byte[BUFFER_SIZE_2_MB];
            int length;

            while ((length = bis.read(buf)) > 0) {
                bos.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new Exception("Error create and write file in a cache");
        } finally {
            parcelFileDescriptor.close();
            bis.close();
            bos.close();
        }

        return resultFile;
    }

    @Override
    public void onResult(File file) {
        hideProgress();
        Log.w(GetFilepathFromUriTask.class.getSimpleName(), "onResult listener = " + listener);
        if (listener != null) {
            listener.onImagePicked(requestCode, file);
        }
    }

    @Override
    public void onException(Exception e) {
        hideProgress();
        Log.w(GetFilepathFromUriTask.class.getSimpleName(), "onException listener = " + listener);
        if (listener != null) {
            listener.onImagePickError(requestCode, e);
        }
    }

    private void showProgress() {
        FragmentManager fm = fmWeakReference.get();
        if (fm != null) {
            ProgressDialogFragment.show(fm);
        }
    }

    private void hideProgress() {
        FragmentManager fm = fmWeakReference.get();
        if (fm != null) {
            ProgressDialogFragment.hide(fm);
        }
    }

}
