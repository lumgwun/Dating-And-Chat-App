package com.lahoriagency.cikolive.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.retry.dimdim.fragments.ChatsFragment;
import com.retry.dimdim.fragments.ExploreFragment;
import com.retry.dimdim.fragments.MatchFragment;
import com.retry.dimdim.fragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ExploreFragment();
            case 1:
                return new MatchFragment();
            case 2:
                return new ChatsFragment();
            case 3:
                return new ProfileFragment();
            default:
                new ExploreFragment();
        }
        return new ExploreFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
