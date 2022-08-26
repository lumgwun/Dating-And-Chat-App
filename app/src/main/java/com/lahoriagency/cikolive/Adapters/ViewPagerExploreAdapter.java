package com.lahoriagency.cikolive.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.retry.dimdim.fragments.ExploreLiveStreamsFragment;
import com.retry.dimdim.fragments.ExploreProfilesFragment;

public class ViewPagerExploreAdapter extends FragmentStateAdapter {


    public ViewPagerExploreAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ExploreProfilesFragment();
        } else if (position == 1) {
            return new ExploreLiveStreamsFragment();
        }
        return new ExploreProfilesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
