package college.root.viit2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import college.root.viit2.Fragments.FragmentTwo;


public class EventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDatabase;
    String TAG = "Test";
    public  int currentPidGandharva =0 ;
    public  int currentPidPerception =0 ;
    SharedPreferences sharedPreferences , sharedPreferences1;
    String pid = "";
    FirebaseUser user;
    DrawerLayout drawer;
    FloatingActionButton fabAdd;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.welcomepage , null);

        DialogFragment dialogFragment = new DialogFragment(view1);
        dialogFragment.show(getSupportFragmentManager() , " its working ");

        sharedPreferences = getSharedPreferences("userInfoGandharva" , Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("userInfoPerception" , Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate:  in onCreate");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);

        if (user.getEmail().equals("shubham.purandare@gmail.com") || user.getEmail().equals("mrunalj.369@gmail.com") || user.getEmail().equals("born.coders.rockon@gmail.com"))
        {
            fabAdd.setVisibility(View.VISIBLE);
        }else {
            fabAdd.setVisibility(View.GONE);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(EventsActivity.this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);



      fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user !=null) {

                    if (user.getEmail().equals("shubham.purandare@gmail.com") || user.getEmail().equals("mrunalj.369@gmail.com") || user.getEmail().equals("born.coders.rockon@gmail.com")) {
                        pid = sharedPreferences.getString("CurrentPidGandharva", "0");
                        currentPidGandharva = Integer.parseInt(pid);
                        pid = sharedPreferences1.getString("CurrentPidPerception", "0");
                        currentPidPerception = Integer.parseInt(pid);
                        Log.d(TAG, "onOptionsItemSelected:  Pid recieved of gandharva from shared pref is " + currentPidGandharva);
                        Log.d(TAG, "onOptionsItemSelected: Pid recieved of perception from shared pref is" + currentPidPerception);


                        Intent intent = new Intent(EventsActivity.this, PostActivity.class);
                        intent.putExtra("CurrentPidGandharva", currentPidGandharva);
                        intent.putExtra("CurrentPidPerception", currentPidPerception);
                        startActivity(intent);
                       } else {
                        Toast.makeText(getApplicationContext(), "Permission denied ..", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Log.d(TAG, "onOptionsItemSelected: user returned a null value");
                }





            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("GANDHARVA");
                        Log.d(TAG, "onTabSelected: mdatabase pointing to "+mDatabase.getKey());
                        onStart();
                        break;
                    case 1:
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("PERCEPTION");
                        Log.d(TAG, "onTabSelected: mdatabase pointing to "+mDatabase.getKey());
                        onStart();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


      //  tabLayout.setupWithViewPager(mViewPager);



    } // end of onCreate method .



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTwo() , "Perception");
        adapter.addFragment(new FragmentTwo() , "Gandharva");
        viewPager.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.Logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EventsActivity.this);
            builder.setTitle("Are you sure you want to logout ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseAuth.signOut();
                    Log.d(TAG, "onOptionsItemSelected: signed out");
                    finish();
                    startActivity(new Intent(EventsActivity.this , SignInActivity.class));



                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                }
            });
            builder.show();





        }




            //noinspection SimplifiableIfStatement

    





        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {

        }else if( id == R.id.aboutUs) {

            Intent intent = new Intent(EventsActivity.this , AboutUsActivity.class);
            startActivity(intent);

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }




        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Perception";
                case 1:
                    return "Gandharva";

            }
            return null;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
