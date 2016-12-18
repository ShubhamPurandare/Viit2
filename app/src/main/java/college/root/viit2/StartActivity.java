package college.root.viit2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.relex.circleindicator.CircleIndicator;

public class StartActivity extends AppCompatActivity implements FragmentOne.OnFragmentInteractionListener,Second.OnFragmentInteractionListener,FragmentThree.OnFragmentInteractionListener{
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        viewPager = (ViewPager) findViewById(R.id.view);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter(FragmentManager fm, Context applicationContext) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentOne();
                case 1:
                    return new Second();
                case 2:
                    return new FragmentThree();
                default:
                    return null;
            }


        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
