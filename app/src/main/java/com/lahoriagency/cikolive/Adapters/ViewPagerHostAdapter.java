package com.lahoriagency.cikolive.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lahoriagency.cikolive.Fragments.HostChatsFragment;
import com.lahoriagency.cikolive.Fragments.HostDashboardFragment;
import com.lahoriagency.cikolive.Fragments.HostProfileFragment;


public class ViewPagerHostAdapter extends FragmentStateAdapter {


    public ViewPagerHostAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HostDashboardFragment();
            case 1:
                return new HostChatsFragment();
            case 2:
                return new HostProfileFragment();
            default:
                new HostDashboardFragment();
        }
        return new HostDashboardFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
