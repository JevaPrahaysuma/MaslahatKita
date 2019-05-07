package com.example.jeva.maslahatkita.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jeva.maslahatkita.Fragment.DataPribadiInvestorFragment;
import com.example.jeva.maslahatkita.Fragment.InvestasiApproveFragment;
import com.example.jeva.maslahatkita.Fragment.LoginFragment;
import com.example.jeva.maslahatkita.Fragment.MarketPlaceFragment;
import com.example.jeva.maslahatkita.Fragment.PengajuanUsahaBaruFragment;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.Fragment.VirtualAccountInvestorFragment;
import com.google.firebase.auth.FirebaseAuth;

public class InvestorActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investor_activity);
        if (getIntent().hasExtra("back")){
            openPengajuanBaruFragment();
        }
        openDataPribadiFragment();

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_data_pribadi:
                fragmentClass = DataPribadiInvestorFragment.class;
                break;
            case R.id.nav_market_place:
                fragmentClass = MarketPlaceFragment.class;
                break;
            case R.id.nav_rekening:
                fragmentClass = VirtualAccountInvestorFragment.class;
                break;
            case R.id.nav_investasi:
                fragmentClass = InvestasiApproveFragment.class;
                break;

            default:
                Intent i = new Intent(InvestorActivity.this, MainActivity.class);
                fragmentClass = LoginFragment.class;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(i);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bu = getIntent().getExtras();
        bu.putString("data1",bu.getString("data1"));
        bu.putString("email",bu.getString("email"));
        fragment.setArguments(bu);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPengajuanBaruFragment() {
        // TODO : ganti fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new PengajuanUsahaBaruFragment()).commit();

    }
    public void openDataPribadiFragment() {
        // TODO : ganti fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new DataPribadiInvestorFragment()).commit();

    }
}
