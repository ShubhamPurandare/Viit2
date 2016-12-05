package college.root.viit2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import college.root.viit2.Fragments.FragmentOne;
import college.root.viit2.Fragments.FragmentTwo;


public class EventsActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    String TAG = "Test";
    public  int currentPidGandharva =0 ;
    public  int currentPidPerception =0 ;
    SharedPreferences sharedPreferences , sharedPreferences1;
    String pid = "";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);





            Log.d(TAG, "run: In background thread");

            SharedPreferences sharedPreferences = getSharedPreferences("ShaPreferences1", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            boolean  firstTime=sharedPreferences.getBoolean("first", true);

            firebaseAuth= FirebaseAuth.getInstance();

            FirebaseUser user=firebaseAuth.getCurrentUser();

            Log.d(TAG, "run: checking conditions");

            if(firstTime || user==null) {
                editor.putBoolean("first",false);
                //For commit the changes, Use either editor.commit(); or  editor.apply();.
                editor.commit();
                Intent intent = new Intent(EventsActivity.this, signin.class);
                Log.d(TAG, "run: user is null");
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(EventsActivity.this, EventsActivity.class);
                Log.d(TAG, "run: user is not null");
                startActivity(intent);
                finish();
            }
            // After 5 seconds redirect to another intent
            //Intent i=new Intent(getBaseContext(),FirstScreen.class);
            //startActivity(i);

            //Remove activity













        sharedPreferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("userInfoPerception" , MODE_PRIVATE);
        Log.d(TAG, "onCreate:  in onCreate");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        setupViewPager(mViewPager);





        tabLayout.setupWithViewPager(mViewPager);



    } // end of onCreate method .



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTwo() , "Gandharva");
        adapter.addFragment(new FragmentOne() , "Perception");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            pid = sharedPreferences.getString("CurrentPidGandharva" ,"0");
            currentPidGandharva = Integer.parseInt(pid);
            pid = sharedPreferences.getString("CurrentPidPerception" ,"0");
            currentPidPerception = Integer.parseInt(pid);
            Log.d(TAG, "onOptionsItemSelected:  Pid recieved of gandharva from shared pref is "+currentPidGandharva);
            Log.d(TAG, "onOptionsItemSelected: Pid recieved of perception from shared pref is"+currentPidPerception);


            Intent intent = new Intent(EventsActivity.this, PostActivity.class);
            intent.putExtra("CurrentPidGandharva" , currentPidGandharva);
            intent.putExtra("CurrentPidPerception" , currentPidPerception);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    return "Gandharva";
                case 1:
                    return "Perception";

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



    



























































}
