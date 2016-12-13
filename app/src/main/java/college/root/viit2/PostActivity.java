package college.root.viit2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class PostActivity extends AppCompatActivity {

    private EditText mDepartment;
    private EditText mEventName;
    private EditText mEligibility;
    private EditText mFees;
    private EditText mRounds;
    private EditText mRules;
    private EditText mTeamSize;
    private EditText mTimeLimit;
    private EditText mVenue;
    private EditText mTimings;
    private EditText mExtraDetails;
    private EditText mPrizes;
    private Button mSubmitButton;

    private String allDetails ; // used to check if all details have been filled or not

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private int checkBoxIsChecked = 0;
    int currentPid =0;
    private String TAG = "Test";
    ImageButton mpostImage;
    Button btnEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        currentPid = intent.getIntExtra("CurrentPid" , 0);
        Log.d(TAG , "Curent recieved recent pid is "+currentPid);

        // all binding is below

        mDepartment = (EditText) findViewById(R.id.editText_Department);
        mEventName = (EditText) findViewById(R.id.editText_EventName);
        mEligibility = (EditText) findViewById(R.id.editText_Eligibility);
        mFees = (EditText) findViewById(R.id.editText_Fees);
        mRounds = (EditText) findViewById(R.id.editText_Rounds);
        mRules = (EditText) findViewById(R.id.editText_Rules);
        mTeamSize= (EditText) findViewById(R.id.editText_TeamSize);
        mTimeLimit = (EditText) findViewById(R.id.editText_TimeLimit);
        mVenue = (EditText) findViewById(R.id.editText_Venue);
        mTimings = (EditText) findViewById(R.id.editText_Timings);
        mPrizes = (EditText) findViewById(R.id.editText_Prizes);
        mExtraDetails = (EditText) findViewById(R.id.editText_ExtraDetails);

        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mpostImage = (ImageButton)findViewById(R.id.imagePost);


        //References are created

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ALLEvents");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startAdding();
            }
        });





        mpostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });



    } // oncreate method end.


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            mImageUri=data.getData();

            mpostImage.setImageURI(mImageUri);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(50);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(PostActivity.this, EventsActivity.class);
            finish();
            startActivity(intent);

        }
        return true;
    }


    private void startAdding() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to Post ");
        // builder.create();
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // String is set to null is class variables. Data of all fields is concatenated and stored in string below. if value is NOT null
                // post will be made

                allDetails = mDepartment.getText().toString()
                        + mEventName.getText().toString()
                        + mEligibility.getText().toString()
                        + mFees.getText().toString()
                        + mRounds.getText().toString()
                        + mRules.getText().toString()
                        + mTeamSize.getText().toString()
                        + mTimeLimit.getText().toString()
                        + mVenue.getText().toString()
                        + mTimings.getText().toString()
                        + mExtraDetails.getText().toString()
                        + mPrizes.getText().toString();

                Log.d(TAG, " In startAdding ");

                Log.i(TAG, allDetails);

                if ( !TextUtils.isEmpty(allDetails) && mImageUri !=null ) {
                    mProgress.setMessage("Adding Event...");
                    mProgress.show();
                    mProgress.setCanceledOnTouchOutside(false);
                    //  final int randomVar = new Random().nextInt(1000);
                    StorageReference filepath = mStorage.child("Event_Image").child(random());

                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Log.d(TAG ," Image uploaded " );
                            DatabaseReference mPost = mDatabase.push();

                            Log.d(TAG ," In setting title n other fields " );
                            mPost.child("Image").setValue(downloadUrl.toString());

                            // all details are stored as child nodes of the post

                            mPost.child("Department").setValue(mDepartment);
                            mPost.child("Event Name").setValue(mEventName);
                            mPost.child("Eligibility").setValue(mEligibility);
                            mPost.child("Fees").setValue(mFees);
                            mPost.child("Rounds").setValue(mRounds);
                            mPost.child("Rules").setValue(mRules);
                            mPost.child("Team Size").setValue(mTeamSize);
                            mPost.child("Time Limit").setValue(mTimeLimit);
                            mPost.child("Venue").setValue(mVenue);
                            mPost.child("Timings").setValue(mTimings);
                            mPost.child("Extra Details").setValue(mExtraDetails);
                            mPost.child("Prizes").setValue(mPrizes);


                            //    mPost.child("Image").setValue(downloadUrl.toString());
                            mPost.child("Post_id").setValue(currentPid +1);
                            Log.d(TAG , "Pid posted now is "+currentPid+1);
                            mProgress.dismiss();
                            //onCreateNotification();
                            startActivity(new Intent(PostActivity.this, EventsActivity.class));

                            mProgress.dismiss();


                        }
                    });

                } else {
                    Toast.makeText(PostActivity.this, "Enter all the fields ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PostActivity.this, EventsActivity.class));



    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_technical:
                if (checked)
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("GANDHARVA");
                checkBoxIsChecked= 1;
                break;
            case R.id.radio_cultural:
                if (checked)
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("PERCEPTION");
                checkBoxIsChecked= 1;
                break;


        }
    }


}