package com.example.navigationdrawerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationdrawerapp.ui.gallery.ProfileFragment;
import com.example.navigationdrawerapp.ui.home.HomeFragment;
import com.example.navigationdrawerapp.ui.kids.Kids_details_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationdrawerapp.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "Profile";
    private static final String TAG_KIDS = "Kids details";
    private static final String TAG_ADVICE = "Advices";
    private static final String TAG_TIPS = "health tips";
    private static final String TAG_APPOINTMENT = "Appointments";
    private static final String TAG_MEDICATION = "Medication";
    private static final String TAG_CHAT = "Chat";
    public static String CURRENT_TAG = TAG_HOME;
    private String[] activityTitles;
   ImageView imageViewProfile;
    String phoneNumber;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mHandler=new Handler();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            setSupportActionBar(binding.appBarMain.toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fab=findViewById(R.id.fab);
    //fab=binding.appBarMain.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer=findViewById(R.id.drawer_layout);
//        drawer = binding.drawerLayout;
        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.nav_view);
       // navigationView= binding.navView;
    View navHeader=navigationView.getHeaderView(0);
    txtName=navHeader.findViewById(R.id.nav_header_title);
    txtWebsite=navHeader.findViewById(R.id.web);
    imageViewProfile=navHeader.findViewById(R.id.imageView);
    activityTitles=getResources().getStringArray(R.array.nav_item_activties_titles);
  loadNavHeader();
  setUpNavigationView();
  if(savedInstanceState==null){
      navItemIndex=0;
      CURRENT_TAG=TAG_HOME;
      loadHomeFragment();
  }
    }
    private void setToolbarTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(activityTitles[navItemIndex]);
    }

    private Fragment getHomeFragment() {
        System.out.println("INdex "+navItemIndex);
        switch (navItemIndex) {
            case 1:
                // photos
                return new ProfileFragment();
            case 2:
                return new Kids_details_fragment();
//            case 3:
//                // notifications fragment
//                return new NotificationsFragment();
//
//            case 4:
//                // settings fragment
//                return new SettingsFragment();
            default:
                return new HomeFragment();
        }
    }
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    @SuppressLint("ResourceType")
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        // This method will trigger on item Click of navigation menu
        navigationView.setNavigationItemSelectedListener(menuItem -> {

            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()) {
                //Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.nav_home:
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    break;
                case R.id.profile:
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_PROFILE;
                    break;
                case R.id.kids:
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_KIDS;
                    break;
                case R.id.advice:
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_ADVICE;
                    break;
                case R.id.tips:
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_TIPS;
                    break;
                case R.id.appointment:
                    navItemIndex = 5;
                    CURRENT_TAG = TAG_APPOINTMENT;
                    break;
                case R.id.medication:
                    navItemIndex = 6;
                    CURRENT_TAG = TAG_MEDICATION;
                    break;
                case R.id.chat:
                    // launch new intent instead of loading fragment
                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
                    drawer.closeDrawers();
                    return true;
                default:
                    navItemIndex = 0;
            }

            //Checking if the item is in checked state or not, if not make it in checked state
            menuItem.setChecked(!menuItem.isChecked());
            menuItem.setChecked(true);

            loadHomeFragment();

            return true;
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @SuppressLint("SetTextI18n")
    private void loadNavHeader() {
        // name
        txtName.setText("Maternally health");
        txtWebsite.setText("");

        // loading header background image
     //   new DownloadImageTask(imgNavHeaderBg,getApplicationContext()).execute(urlNavHeaderBg);
//        Glide.with(getParent()).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        // Loading profile image
        new DownloadImageTask(imageViewProfile,getApplicationContext()).execute(urlProfileImg);
//        Glide.with(getParent()).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /*
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = () -> {
            // update the main content by replacing fragments
            Fragment fragment = getHomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        };

        // If mPendingRunnable is not null, then add to the message queue
        mHandler.post(mPendingRunnable);

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        // flag to load home fragment when user presses back key
        boolean shouldLoadHomeFragOnBackPress = true;
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

}