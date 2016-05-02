package mx.spin.mobile;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import mx.spin.mobile.fragments.AnalizeFragment;
import mx.spin.mobile.fragments.ConceptsFragment;
import mx.spin.mobile.fragments.PoolsFragment;
import mx.spin.mobile.fragments.ProfileFragment;
import mx.spin.mobile.fragments.WhereBuyFragment;

public class DrawerActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private final static String TAG = DrawerActivity.class.getSimpleName();
    public static DrawerLayout drawerLayout;
    private LinearLayout containerMyProfile;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private final int HOME = 0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        containerMyProfile = (LinearLayout) navigationView.findViewById(R.id.linearDrawerMyProfile);

        containerMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                changeFragment(new ProfileFragment(), getSupportFragmentManager());
            }
        });

        navigationView.inflateHeaderView(R.layout.drawer_header);
        navigationView.setNavigationItemSelectedListener(this);
    //    navigationView.setItemTextColor(getColorStateList(R.color.white));

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt("showFragment", 0) == 2) {
            changeFragment(new WhereBuyFragment(), getSupportFragmentManager());
        } else {
            updateFragment(HOME);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opendrawer, R.string.closedrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        drawerLayout.closeDrawers();
        updateFragment(getMenuPosition(item));
        return true;
    }

    private int getMenuPosition(MenuItem item){
       int position = 0;
        switch (item.getItemId()) {
            case R.id.drawerPools:
                position =  0;
                break;
            case R.id.drawerAnalize:
                position =  1;
                break;
            case R.id.drawerWhereBuy:
                position =  2;
                break;
            case R.id.drawerConcepts:
                position = 3;
                break;
        }
        return position;
    }

    protected void updateFragment(int postition){
        Log.d(TAG, "updateFragment:  " + postition);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (postition){
            case 0:
                fragment = new PoolsFragment();
                break;
            case 1:
                fragment = new AnalizeFragment();
                break;
            case 2:
                fragment = new WhereBuyFragment();
                break;
            case 3:
                fragment = new ConceptsFragment();
                break;
        }

        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public static void changeFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addNewPool(View v) {
        startActivity(new Intent(DrawerActivity.this, AddPoolActivity.class));
    }

    public void redirectHome(View v){
        updateFragment(HOME);
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
    }
}
