package com.example.jeva.maslahatkita.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jeva.maslahatkita.Fragment.InvestorManagementFragment;
import com.example.jeva.maslahatkita.Fragment.LoginFragment;
import com.example.jeva.maslahatkita.Fragment.MitraManagementFragment;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.Fragment.UserManagementFragment;
import com.example.jeva.maslahatkita.Fragment.VerifikasiInvestasiFragment;
import com.example.jeva.maslahatkita.Fragment.VerifikasiInvestorFragment;
import com.example.jeva.maslahatkita.Fragment.VerifikasiMitraFragment;
import com.example.jeva.maslahatkita.Fragment.VerifikasiPengajuanUsahaFragment;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.example.jeva.maslahatkita.model.nonFirebase.InvestorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FirebaseAuth mAuth;
    private InvestorModel investorModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        openUserManagementFragment();
        if (getIntent().hasExtra("userManagement")){
            openUserManagementFragment();
        } else if(getIntent().hasExtra("mitraManagement")){
            openMitraManagementFragment();
        } else if(getIntent().hasExtra("investorManagement")){
            openInvestorManagementFragment();
        }
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


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final HashMap<String, String> listInvestorName = new HashMap<>();
        db.getReference().child("InvestorManagement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    InvestorManagementModel investorManagementModel = ds.getValue(InvestorManagementModel.class);
                    String nama = investorManagementModel.getNama();
                    listInvestorName.put(ds.getKey(), nama);
                }
                investorModel = new InvestorModel(listInvestorName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
            case R.id.nav_v_investor:
                fragmentClass = VerifikasiInvestorFragment.class;
                break;
            case R.id.nav_v_mitra_usaha:
                fragmentClass = VerifikasiMitraFragment.class;
                break;
            case R.id.nav_v_pengajuan_usaha:
                fragmentClass = VerifikasiPengajuanUsahaFragment.class;
                break;
            case R.id.nav_v_investasi:
                fragmentClass = VerifikasiInvestasiFragment.class;
                break;
            case R.id.nav_user_management:
                fragmentClass = UserManagementFragment.class;
                break;
            case R.id.nav_mitra_management:
                fragmentClass = MitraManagementFragment.class;
                break;
            case R.id.nav_investor_management:
                fragmentClass = InvestorManagementFragment.class;
                break;
            default:
                Intent i = new Intent(AdminActivity.this, MainActivity.class);
                fragmentClass = LoginFragment.class;
                //logout
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(i);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if  (fragmentClass == VerifikasiInvestasiFragment.class){
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", investorModel);
            fragment.setArguments(bundle);
        }
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
    public void openUserManagementFragment() {
        // TODO : ganti fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new UserManagementFragment()).commit();
    }
    public void openInvestorManagementFragment() {
        // TODO : ganti fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new InvestorManagementFragment()).commit();
    }
    public void openMitraManagementFragment() {
        // TODO : ganti fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new MitraManagementFragment()).commit();
    }
}
