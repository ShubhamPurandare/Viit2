package college.root.viit2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView mDepartment;
    private TextView mEventName;
    private TextView mEligibility;
    private TextView mFees;
    private TextView mRounds;
    private TextView mRules;
    private TextView mTeamSize;
    private TextView mTimeLimit;
    private TextView mVenue;
    private TextView mTimings;
    private TextView mExtraDetails;
    private TextView mPrizes;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        
        initializeViews();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ALLEvents");
    }

    private void initializeViews() {

        mDepartment = (TextView) findViewById(R.id.tv_department);
        mEventName = (TextView) findViewById(R.id.tv_EventName);
        mEligibility = (TextView) findViewById(R.id.tv_Eligibility);
        mFees = (TextView) findViewById(R.id.tv_Fees);
        mRounds = (TextView) findViewById(R.id.tv_Rounds);
        mRules = (TextView) findViewById(R.id.tv_Rules);
        mTeamSize = (TextView) findViewById(R.id.tv_TeamSize);
        mTimeLimit = (TextView) findViewById(R.id.tv_TimeLimit);
        mVenue = (TextView) findViewById(R.id.tv_Venue);
        mTimings = (TextView) findViewById(R.id.tv_Timings);
        mExtraDetails = (TextView) findViewById(R.id.tv_ExtraDetails);
        mPrizes = (TextView) findViewById(R.id.tv_Prizes);

    }

}
