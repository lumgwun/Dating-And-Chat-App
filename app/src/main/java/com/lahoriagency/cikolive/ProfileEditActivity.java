package com.lahoriagency.cikolive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.BaseAsyncTask22;
import com.lahoriagency.cikolive.Classes.Config;
import com.lahoriagency.cikolive.Classes.GetFilePathFromUri;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PhotoUploadReply;
import com.lahoriagency.cikolive.Classes.PhotosRecyclerViewAdapter;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.ProfileDeletePhotoRequest;
import com.lahoriagency.cikolive.Classes.ProfilePhotoData;
import com.lahoriagency.cikolive.Classes.ProfileUpdateRequest;
import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.SimpleItemTouchHelperCallback;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Interfaces.OnStartDragListener;
import com.lahoriagency.cikolive.Interfaces.PhotoUploadService;

import com.google.gson.Gson;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressWarnings("deprecation")
public class ProfileEditActivity extends AppCompatActivity implements OnStartDragListener {
    public static final int PICK_PHOTO_FOR_AVATAR = 652;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 99;

    private EditText descriptionEditText;

    private PreferencesManager preferencesManager;
    private MyPreferences myPreferences;
    private static final String PREF_NAME = "Ciko";
    private PhotosRecyclerViewAdapter photosAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private PhotoUploadService photoUploadService;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    private UserProfileInfo userProfileInfo;
    public static final String userId = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile_edit);
        preferencesManager = AppE.getPreferencesManager();
        myPreferences = AppE.getPreferences();
        setActionBarSettings();
        preferencesManager = AppE.getPreferencesManager();
        myPreferences = AppE.getPreferences();
        savedProfile= new SavedProfile();
        userProfileInfo= new UserProfileInfo();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        myPreferences= new MyPreferences();
        preferencesManager=new PreferencesManager(this);
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        json2 = sharedPref.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);

        descriptionEditText = findViewById(R.id.profile_edit_description_edit_text);
        descriptionEditText.setText(myPreferences.getDescription());

        photosAdapter = new PhotosRecyclerViewAdapter(this, this);
        photosAdapter.setHasStableIds(true);
        RecyclerView photosRecyclerView = findViewById(R.id.edit_proile_photos_recyclerview);
        photosRecyclerView.setHasFixedSize(true);
        photosRecyclerView.setAdapter(photosAdapter);

        final int spanCount = 3;
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        photosRecyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(photosAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(photosRecyclerView);
    }

    private void setActionBarSettings() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#555555' align='right'>Edit profile</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.primary_purple), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveProfile();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveProfile();
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            GetFilePathFromUri getFilePathFromUri = new GetFilePathFromUri(getApplicationContext());
            String path = getFilePathFromUri.getPath(data.getData());
            uploadPhoto(path, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    photosAdapter.pickImage();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void saveProfile() {
        myPreferences.setDescription(descriptionEditText.getText().toString());
        preferencesManager.savePreferences();

        Gson gson = new Gson();
        final QBUser currentUser = SharedPrefsHelper.getInstance().getQbUser();
        currentUser.setOldPassword(currentUser.getPassword());
        QBUserCustomData customData = new QBUserCustomData();
        List<ProfilePhotoData> photoDataList = photosAdapter.getProfilePhotos();
        customData.getProfilePhotoData().addAll(photoDataList);
        currentUser.setCustomData(gson.toJson(customData));
        QBUsers.updateUser(currentUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                SharedPrefsHelper.getInstance().saveQbUser(currentUser);
            }

            @Override
            public void onError(QBResponseException errors) {
                System.out.print("error update user");
            }
        });
        StringBuilder builder = new StringBuilder();
        String delimeter = "";
        for (int i = 0; i < photoDataList.size(); i++) {
            if (photoDataList.get(i).getBlobId() != null) {
                builder.append(delimeter);
                delimeter = ";";
                builder.append(photoDataList.get(i).getLink());
            }
        }
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(myPreferences.getUserId(), myPreferences.getDescription(),
                builder.toString());
        BaseAsyncTask22<ProfileUpdateRequest> saveSettingsTask = new BaseAsyncTask22<>(ServerMethodsConsts.USERPROFILE, profileUpdateRequest);
        saveSettingsTask.setHttpMethod("POST");
        saveSettingsTask.execute();
    }

    private void uploadPhoto(final String filePath, int position) {
        if (photoUploadService == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            photoUploadService = new Retrofit.Builder().baseUrl(Config.getServer() + ServerMethodsConsts.PHOTO + "/")
                    .client(builder.build())
                    .build()
                    .create(PhotoUploadService.class);
        }
        File file = new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), myPreferences.getUserId().toString());
        RequestBody imagePosition = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(position));
        RequestBody qbSessionToken = RequestBody.create(MediaType.parse("text/plain"), QBSessionManager.getInstance().getToken());
        Call<ResponseBody> call = photoUploadService.upload(body, userId, imagePosition, qbSessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    PhotoUploadReply photoUploadReply = AppE.getGson().fromJson(response.body().string(), PhotoUploadReply.class);
                    if (photoUploadReply.isStatusOkay()) {
                        updateUserCustomData(new ProfilePhotoData(photoUploadReply.getPhotoLink(), photoUploadReply.getBlobId()), filePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void deletePhoto(final ProfilePhotoData photoData) {
        ProfileDeletePhotoRequest deletePhotoModel = new ProfileDeletePhotoRequest(myPreferences.getUserId(),
                QBSessionManager.getInstance().getToken(), photoData.getLink(), photoData.getBlobId());
        BaseAsyncTask22<ProfileDeletePhotoRequest> deletePhotoTask = new BaseAsyncTask22<>(ServerMethodsConsts.PHOTO + "/", deletePhotoModel);
        deletePhotoTask.setHttpMethod("DELETE");
        deletePhotoTask.execute();

        final QBUser currentUser = SharedPrefsHelper.getInstance().getQbUser();
        if (currentUser.getCustomData() != null) {
            Gson gson = new Gson();
            QBUserCustomData customData = gson.fromJson(currentUser.getCustomData(), QBUserCustomData.class);
            if (customData != null) {
                customData.getProfilePhotoData().remove(photoData);
                currentUser.setCustomData(gson.toJson(customData));
                currentUser.setOldPassword(currentUser.getPassword());
                QBUsers.updateUser(currentUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        SharedPrefsHelper.getInstance().saveQbUser(currentUser);
                        photosAdapter.remove(photoData);
                    }

                    @Override
                    public void onError(QBResponseException errors) {
                        System.out.print("error update user");
                    }
                });
            }
        }
    }

    private void updateUserCustomData(final ProfilePhotoData photoData, final String filePath) {
        Gson gson = new Gson();
        final QBUser currentUser = SharedPrefsHelper.getInstance().getQbUser();
        currentUser.setOldPassword(currentUser.getPassword());
        QBUserCustomData customData = gson.fromJson(currentUser.getCustomData(), QBUserCustomData.class);
        if (customData == null)
            customData = new QBUserCustomData();
        customData.getProfilePhotoData().add(photoData);
        currentUser.setCustomData(gson.toJson(customData));
        QBUsers.updateUser(currentUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                SharedPrefsHelper.getInstance().saveQbUser(currentUser);
                photosAdapter.update(photoData, filePath);
            }

            @Override
            public void onError(QBResponseException errors) {
                System.out.print("error update user");
            }
        });

    }
}