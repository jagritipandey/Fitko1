package com.sugoilabs.fitko;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements RecyclerViewNavigationAdapter.ClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private RecyclerView navigationRecyclerView;
    private RecyclerViewNavigationAdapter navigationRecyclerViewAdapter;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        navigationRecyclerView = (RecyclerView) layout.findViewById(R.id.navigationRecyclerView);
        navigationRecyclerViewAdapter = new RecyclerViewNavigationAdapter(getActivity(), getData());
        navigationRecyclerViewAdapter.setClickListener(this);
        navigationRecyclerView.setAdapter(navigationRecyclerViewAdapter);
        navigationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }


    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.d("Drawer", "Opened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.d("Drawer", "Closed");
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static List<RecyclerViewNavigationItem> getData() {
        List<RecyclerViewNavigationItem> items = new ArrayList<>();
        //Icons from icons8.com
        int[] icons = {R.drawable.schedule, R.drawable.ic_health, R.drawable.profile, R.drawable.goal, R.drawable.diet, R.drawable.paymentstatus};
        String[] title = {"Home", "Health Data", "Profile", "Goal", "Diet", "Payment Status"};
        for (int i = 0; i < icons.length && i < title.length; i++) {
            RecyclerViewNavigationItem item = new RecyclerViewNavigationItem();
            item.iconid = icons[i];
            item.title = title[i];
            items.add(item);
        }

        return items;
    }

    @Override
    public void itemClicked(View view, int position) {
        if (position == 1) {
            startActivity(new Intent(getActivity(), HealthProfile.class));
        }
        if (position == 2) {
            startActivity(new Intent(getActivity(), Profile.class));
        }
        if (position == 3) {
            startActivity(new Intent(getActivity(), Goals.class));
        }
        if (position == 4) {
            startActivity(new Intent(getActivity(), Diet.class));
        }
        if (position == 5) {
            startActivity(new Intent(getActivity(), PaymentStatus.class));
        }
    }
}