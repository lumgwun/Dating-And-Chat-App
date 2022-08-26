package com.lahoriagency.cikolive;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Fragments.ContentFragment;
import com.quickblox.users.model.QBUser;

public class ContentAct extends AppCompatActivity {
    private static final String TAG = ContentAct.class.getSimpleName();
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_content);
        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ContentFragment();
        fragmentTransaction.replace(R.id.con_frag, fragment);
        fragmentTransaction.commit();
    }
}