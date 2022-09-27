package com.lahoriagency.cikolive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lahoriagency.cikolive.Adapters.CheckboxUsersAdapter;
import com.lahoriagency.cikolive.Adapters.TimelineAdapter;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.QbUsersDbManager;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.ScrollViewWithMaxHeight;
import com.lahoriagency.cikolive.Classes.TimeLine;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.UiUtils;
import com.lahoriagency.cikolive.DataBase.TimeLineDAO;
import com.lahoriagency.cikolive.NewPackage.SharedPrefsHelper;
import com.melnykov.fab.FloatingActionButton;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class SelectUsersActivity extends BaseActivity  {
    private static final String EXTRA_QB_DIALOG = "qb_dialog";
    public static final String EXTRA_QB_USERS = "qb_users";
    public static final String EXTRA_CHAT_NAME = "chat_name";
    private static final int REQUEST_DIALOG_NAME = 135;
    public static final int MINIMUM_CHAT_OCCUPANTS_SIZE = 1;
    public static final int PRIVATE_CHAT_OCCUPANTS_SIZE = 2;
    private static final int USERS_PAGE_SIZE = 100;
    private static final int MIN_SEARCH_QUERY_LENGTH = 3;
    private static final long SEARCH_DELAY = 600;

    private static final long CLICK_DELAY = TimeUnit.SECONDS.toMillis(2);

    private ListView usersListView;

    private SearchView searchView;
    private Menu menu;

    private ScrollViewWithMaxHeight scrollView;
    private ChipGroup chipGroup;
    private TextView tvNotFound;
    private CheckboxUsersAdapter usersAdapter;
    private Set<QBUser> existingUsers = new HashSet<>();
    private long lastClickTime = 0L;
    private QBChatDialog qbChatDialog = null;
    private String chatName = null;
    private int currentPage = 0;
    private Boolean isLoading = false;
    private String lastSearchQuery = "";
    private Boolean hasNextPage = true;
    private ProgressDialog progressDialog;
    ContentLoadingProgressBar progressBar;
    private static final String PREF_NAME = "Ciko";
    SharedPreferences userPreferences;
    private Integer qbUserId;
    private QBUser currentUser;
    private QbUsersDbManager dbManager;
    int diamondCount,diamondID;
    private int collections,qbUserID;
    SharedPrefsHelper sharedPrefsHelper;
    FloatingActionButton fabNew;
    private SavedProfile savedProfile;
    private Diamond diamond;
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    SharedPreferences sharedPref;
    private double liveAmt;
    private int liveDuration;
    private Bundle extras;
    private Date date;
    long diff,minutes;
    public static final int MAX_CONFERENCE_OPPONENTS_ALLOWED = 11;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private TimelineAdapter timelineAdapter;
    private TimeLineDAO timeLineDAO;
    private RecyclerView timeLineRecyclerView;
    private ArrayList<TimeLine> timeLineArrayList;
    private int savedProfileID;


    public static void startForResult(Activity activity, int code, QBChatDialog dialog) {
        Intent intent = new Intent(activity, SelectUsersActivity.class);
        intent.putExtra(EXTRA_QB_DIALOG, dialog);
        activity.startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_users);
        extras= new Bundle();
        timeLineArrayList= new ArrayList<>();
        timeLineDAO= new TimeLineDAO(this);
        date= new Date();
        timeLineRecyclerView = findViewById(R.id.timeLineRecy);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        extras = getIntent().getExtras();
        savedProfile= new SavedProfile();
        gson= new Gson();
        currentUser= new QBUser();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);

        if (getIntent() != null && getIntent().getSerializableExtra(EXTRA_QB_DIALOG) != null) {
            qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_QB_DIALOG);
        }
        initUi();

        if (qbChatDialog != null) {
            updateDialog();
        } else {
            loadUsersFromQB(null);
        }
        if(savedProfile !=null){
            diamond=savedProfile.getSavedPDiamond();
            savedProfileID=savedProfile.getSavedProfID();

        }
        if(qbUser !=null){
            qbUserID=qbUser.getFileId();
        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
            diamondID=diamond.getDiamondWalletID();
            collections=diamond.getDiamondCollections();

        }
        timeLineArrayList=timeLineDAO.getTimeLineByProfID(savedProfileID);
        timelineAdapter = new TimelineAdapter(SelectUsersActivity.this, timeLineArrayList);
        SnapHelper snapHelper = new PagerSnapHelper();
        timeLineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(timeLineRecyclerView);
        timeLineRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        timeLineRecyclerView.setAdapter(timelineAdapter);

    }

    private void initUi() {
        progressBar = findViewById(R.id.progress_select_users);
        usersListView = findViewById(R.id.list_select_users);
        searchView = findViewById(R.id.search);
        scrollView = findViewById(R.id.scroll_view);
        scrollView.setMaxHeight(225);
        chipGroup = findViewById(R.id.chips);
        tvNotFound = findViewById(R.id.tv_no_users_found);
        usersAdapter = new CheckboxUsersAdapter(this, new ArrayList<QBUser>());
        usersListView.setAdapter(usersAdapter);

        searchView.setOnQueryTextListener(new SearchQueryListener());

        boolean editingChat = getIntent().getSerializableExtra(EXTRA_QB_DIALOG) != null;
        if (editingChat) {
            getSupportActionBar().setTitle(getString(R.string.select_users_edit_chat));
        } else {
            getSupportActionBar().setTitle(getString(R.string.select_users_create_chat_title));
            getSupportActionBar().setSubtitle(getString(R.string.select_users_create_chat_subtitle, "0"));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usersAdapter.onItemClicked(position, view, parent);
                menu.getItem(0).setVisible(usersAdapter.getSelectedUsers().size() >= 1);

                String subtitle = "";
                if (usersAdapter.getSelectedUsers().size() != 1) {
                    subtitle = getString(R.string.select_users_create_chat_subtitle, String.valueOf(usersAdapter.getSelectedUsers().size()));
                } else {
                    subtitle = getString(R.string.select_users_create_chat_subtitle_single, "1");
                }
                getSupportActionBar().setSubtitle(subtitle);

                chipGroup.removeAllViews();
                for (QBUser user : usersAdapter.getSelectedUsers()) {
                    Chip chip = new Chip(SelectUsersActivity.this);
                    chip.setText(user.getFullName());
                    chip.setChipIcon(UiUtils.getColorCircleDrawable(user.getId().hashCode()));
                    chip.setCloseIconVisible(false);
                    chip.setCheckable(false);
                    chip.setClickable(false);
                    chipGroup.addView(chip);
                    chipGroup.setVisibility(View.VISIBLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                if (usersAdapter.getSelectedUsers().size() == 0) {
                    chipGroup.setVisibility(View.GONE);
                }
            }
        });

        usersListView.setOnScrollListener(new ScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_activity_select_users, menu);
        if (qbChatDialog != null) {
            menu.getItem(0).setTitle(R.string.menu_done);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((SystemClock.uptimeMillis() - lastClickTime) < CLICK_DELAY) {
            return super.onOptionsItemSelected(item);
        }
        lastClickTime = SystemClock.uptimeMillis();

        switch (item.getItemId()) {
            case R.id.menu_select_people_action_done:
                if (usersAdapter != null) {
                    if (usersAdapter.getSelectedUsers().size() < MINIMUM_CHAT_OCCUPANTS_SIZE) {
                        ToastUtils.shortToast(R.string.select_users_choose_users);
                    } else {
                        if (qbChatDialog == null && usersAdapter.getSelectedUsers().size() >= PRIVATE_CHAT_OCCUPANTS_SIZE) {
                            NewGroupActivity.startForResult(SelectUsersActivity.this, REQUEST_DIALOG_NAME);
                        } else {
                            passResultToCallerActivity();
                        }
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && !TextUtils.isEmpty(data.getSerializableExtra(EXTRA_CHAT_NAME).toString())) {
                chatName = data.getSerializableExtra(EXTRA_CHAT_NAME).toString();
            }
            passResultToCallerActivity();
        }
    }

    private void passResultToCallerActivity() {
        Intent result = new Intent();
        ArrayList<QBUser> selectedUsers = new ArrayList<>(usersAdapter.getSelectedUsers());
        result.putExtra(EXTRA_QB_USERS, selectedUsers);
        if (!TextUtils.isEmpty(chatName)) {
            result.putExtra(EXTRA_CHAT_NAME, chatName);
        }
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void updateDialog() {
        showProgressDialog(R.string.dlg_loading);
        String dialogID = qbChatDialog.getDialogId();
        ChatHelper.getInstance().getDialogById(dialogID, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                SelectUsersActivity.this.qbChatDialog = qbChatDialog;
                loadUsersFromDialog();
            }

            @Override
            public void onError(QBResponseException e) {
                disableProgress();
                showErrorSnackbar(R.string.select_users_get_dialog_error, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog();
                    }
                });
            }
        });
    }

    private void loadUsersFromDialog() {
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> usersFromDialog, Bundle bundle) {
                if (usersFromDialog != null) {
                    existingUsers.addAll(usersFromDialog);
                }
                loadUsersFromQB(null);
            }

            @Override
            public void onError(QBResponseException e) {
                disableProgress();
                showErrorSnackbar(R.string.select_users_get_users_dialog_error, e, null);
            }
        });
    }

    private void loadUsersFromQB(String query) {
        if (!isProgressDialogShowing()) {
            enableProgress();
        }
        currentPage += 1;
        ArrayList<GenericQueryRule> rules = new ArrayList<>();
        rules.add(new GenericQueryRule(ChatAct.ORDER_RULE, ChatAct.ORDER_VALUE_UPDATED_AT));

        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setRules(rules);
        requestBuilder.setPerPage(USERS_PAGE_SIZE);
        requestBuilder.setPage(currentPage);

        if (TextUtils.isEmpty(query)) {
            loadUsersWithoutQuery(requestBuilder);
        } else {
            loadUsersByQuery(query, requestBuilder);
        }
    }

    private void loadUsersWithoutQuery(final QBPagedRequestBuilder requestBuilder) {
        QBUsers.getUsers(requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> usersList, Bundle params) {
                tvNotFound.setVisibility(View.INVISIBLE);
                int totalPagesFromParams = (int) params.get(ChatHelper.TOTAL_PAGES_BUNDLE_PARAM);
                if (currentPage >= totalPagesFromParams) {
                    hasNextPage = false;
                }
                if (qbChatDialog != null) {
                    usersList.removeAll(existingUsers);
                }
                if (currentPage == 1) {
                    usersAdapter.addNewList(usersList);
                } else {
                    usersAdapter.addUsers(usersList);
                }
                disableProgress();
            }

            @Override
            public void onError(QBResponseException e) {
                disableProgress();
                currentPage -= 1;
                showErrorSnackbar(R.string.select_users_get_users_error, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadUsersWithoutQuery(requestBuilder);
                    }
                });
            }
        });
    }

    private void loadUsersByQuery(final String query, final QBPagedRequestBuilder requestBuilder) {
        QBUsers.getUsersByFullName(query, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle params) {
                int totalPagesFromParams = (int) params.get(ChatHelper.TOTAL_PAGES_BUNDLE_PARAM);
                if (currentPage >= totalPagesFromParams) {
                    hasNextPage = false;
                }
                if (qbUsers != null) {
                    tvNotFound.setVisibility(View.INVISIBLE);
                    if (qbChatDialog != null) {
                        qbUsers.removeAll(existingUsers);
                    }
                    if (currentPage == 1) {
                        usersAdapter.addNewList(qbUsers);
                        usersListView.smoothScrollToPosition(0);
                    } else {
                        usersAdapter.addUsers(qbUsers);
                    }
                } else {
                    usersAdapter.clearList();
                    tvNotFound.setVisibility(View.VISIBLE);
                }
                disableProgress();
            }

            @Override
            public void onError(QBResponseException e) {
                disableProgress();
                if (e.getHttpStatusCode() == 404) {
                    usersAdapter.clearList();
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    currentPage -= 1;
                    showErrorSnackbar(R.string.select_users_get_users_error, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadUsersByQuery(query, requestBuilder);
                        }
                    });
                }
            }
        });
    }

    private void enableProgress() {
        progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void disableProgress() {
        hideProgressDialog();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    private class SearchQueryListener implements SearchView.OnQueryTextListener {
        private Timer timer = new Timer();

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            if (newText != null) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentPage = 0;
                                hasNextPage = true;
                                lastSearchQuery = newText;
                                if (newText.length() >= MIN_SEARCH_QUERY_LENGTH) {
                                    loadUsersFromQB(newText);
                                }
                                if (newText.isEmpty()) {
                                    loadUsersFromQB(null);
                                }
                            }
                        });

                    }
                }, SEARCH_DELAY);
            }
            return false;
        }
    }

    private class ScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!isLoading && totalItemCount > 0 && (firstVisibleItem + visibleItemCount * 3) >= totalItemCount && hasNextPage) {
                if (TextUtils.isEmpty(lastSearchQuery)) {
                    loadUsersFromQB(null);
                } else {
                    loadUsersFromQB(lastSearchQuery);
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }
}