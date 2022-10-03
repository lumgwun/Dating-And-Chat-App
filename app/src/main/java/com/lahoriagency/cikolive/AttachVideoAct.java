package com.lahoriagency.cikolive;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.io.File;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class AttachVideoAct extends BaseActivity {
    private static final String EXTRA_FILE_NAME = "video_file_name";
    private static final String EXTRA_FILE_URL = "video_file_URL";

    private CircularRevealRelativeLayout rootLayout;
    private VideoView videoView;
    private ProgressBar progressBar;
    private MediaController mediaController;
    private File file = null;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;

    public static void start(Context context, String attachmentName, String url) {
        Intent intent = new Intent(context, AttachVideoAct.class);
        intent.putExtra(EXTRA_FILE_URL, url);
        intent.putExtra(EXTRA_FILE_NAME, attachmentName);
        context.startActivity(intent);
    }
    public static void start(Context context) {
        Intent intent = new Intent(context, AttachVideoAct.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.act_attach_video);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        initUI();
        loadVideo();
    }
    private void initUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.toolbar_video_player_background));
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_FILE_NAME));
            getSupportActionBar().setElevation(0);
        }
        rootLayout = findViewById(R.id.layout_root);
        videoView = findViewById(R.id.vv_full_view);
        progressBar = findViewById(R.id.progress_show_video);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaController.show(2000);
            }
        });
    }

    private void loadVideo() {
        progressBar.setVisibility(View.VISIBLE);
        String filename = getIntent().getStringExtra(EXTRA_FILE_NAME);
        File file = new File(getApplication().getFilesDir(), filename);

        if (file != null) {
            mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoPath(file.getPath());
            videoView.start();
        }

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mediaController.show(2000);
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressBar.setVisibility(View.GONE);
                mediaController.hide();
                Toast.makeText(AttachVideoAct.this, R.string.error_load_video, Toast.LENGTH_LONG).show();
                /*showErrorSnackbar(R.string.error_load_video, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadVideo();
                    }
                });*/
                loadVideo();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_video_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_player_save:
                saveFileToGallery();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveFileToGallery() {
        if (file != null) {
            try {
                String url = getIntent().getStringExtra(EXTRA_FILE_URL);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getName());
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.allowScanningByMediaScanner();
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                if (manager != null) {
                    manager.enqueue(request);
                }
            } catch (SecurityException e) {
                if (e.getMessage() != null) {
                    Log.d("Security Exception", e.getMessage());
                }
            }
        }
    }
}