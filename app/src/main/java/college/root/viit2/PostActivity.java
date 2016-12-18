package college.root.viit2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import college.root.viit2.IntentServices.FirebaseServiceOffline;
import college.root.viit2.IntentServices.FirebaseServiceOnline;

//import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {

    private EditText mAddDesc, mAddTitle , mRules , mFees , mRounds , mTeamSize , mVenue, mTime,mcontact,
            mExtra , mPrizes , mTimePerRound , mdate;
    private Uri mImageUri = null;
    private Button mSubmitButton;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase , mPost;
    private boolean checkBoxIsChecked = false , deptSelected = false;
    int currentPidGandharva =0;
    int currentPidPerception =0;
    private String TAG = "Test";
    ImageButton mpostImage;
    RadioButton  rbComputer , rbMech , rbCivil , rbEntc;
    int currentPidToPost = 0;
    String deptEvent ="";
    FirebaseUser user ;
    FirebaseAuth firebaseAuth;
    String title_val =" ", desc_val=" " , date=" " , teamSize=" " , venue=" ", time=" " , fees=" ",
            rounds= " ", extra=" ", prizes=" " , timePerRound=" ", rules=" " , contactDetails = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_post);
        checkBoxIsChecked = false;



        Intent intent = getIntent();
        currentPidGandharva = intent.getIntExtra("CurrentPidGandharva" , 0);
        Log.d(TAG , "Curent recieved Gandharva pid is "+currentPidGandharva);
        currentPidPerception = intent.getIntExtra("CurrentPidPerception" , 0);
        Log.d(TAG , "Curent recieved Perception  pid is "+currentPidPerception);

       currentPidToPost = currentPidGandharva+1;
        Log.d(TAG, "onCreate: current pid to post is :"+currentPidToPost);
        mAddTitle = (EditText) findViewById(R.id.editText_title);
        mAddDesc = (EditText) findViewById(R.id.editText_desc);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mpostImage = (ImageButton)findViewById(R.id.imagePost);
        mFees = (EditText)findViewById(R.id.editText_Fees);
        mRounds = (EditText)findViewById(R.id.editText_Rounds);
        mRules = (EditText)findViewById(R.id.editText_Rules);
        mTeamSize = (EditText)findViewById(R.id.editText_TeamSize);
        mTime = (EditText)findViewById(R.id.editText_Timings);
        mVenue = (EditText)findViewById(R.id.editText_Venue);
        mPrizes = (EditText)findViewById(R.id.editText_Prizes);
        mExtra = (EditText)findViewById(R.id.editText_ExtraDetails);
        mTimePerRound = (EditText)findViewById(R.id.editText_TimeLimit);
        mdate = (EditText)findViewById(R.id.edDate);
        mcontact = (EditText)findViewById(R.id.edContactDetails);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("GANDHARVA");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();
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


            Picasso.with(getApplicationContext()).load(mImageUri).resize(500,400).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                        mpostImage.setImageBitmap(bitmap);
                    }catch (Exception e){
                        Log.d(TAG, "onBitmapLoaded: Exception caught in compressing image "+e.getMessage());
                    }



                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });



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
            finish(); startService(new Intent(PostActivity.this , FirebaseServiceOffline.class));
            Log.d(TAG, "onCreate: user is offline ");

            startActivity(intent);

        }
        return true;
    }


    private  boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private void startAdding() {

        if (isNetWorkAvailable()){
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
                    startService(new Intent(PostActivity.this , FirebaseServiceOnline.class));
                    Log.d(TAG, "onCreate: user is online ");

                    title_val = mAddTitle.getText().toString().trim();
                    desc_val = mAddDesc.getText().toString().trim();
                    date = mdate.getText().toString();
                    fees = mFees.getText().toString();
                    time = mTime.getText().toString();
                    rounds = mRounds.getText().toString();
                    rules = mRules.getText().toString();
                    timePerRound = mTimePerRound.getText().toString();
                    extra = mExtra.getText().toString();
                    teamSize = mTeamSize.getText().toString();
                    venue = mVenue.getText().toString();
                    prizes = mPrizes.getText().toString();
                    contactDetails = mcontact.getText().toString();

                    Log.i(TAG, rules);


                    Log.d(TAG, " In startAdding ");
                    if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && deptSelected) {
                        mProgress.setMessage("Adding Event...");
                        mProgress.show();
                        mProgress.setCanceledOnTouchOutside(false);
                        //  final int randomVar = new Random().nextInt(1000);
                        StorageReference filepath = mStorage.child("Event_Image").child(random());
                        mPost = mDatabase.push();

                        if(mImageUri != null){
                            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                    Log.d(TAG ," Image uploaded " );
                                    mPost.child("Image").setValue(downloadUrl.toString());

                                }
                            });


                        }else {
                            Log.d(TAG, "onClick: No image uploaded");
                            mPost.child("Image").setValue("No Image");
                        }

                                Log.d(TAG ," In setting title n other fields " );

                                mPost.child("Title").setValue(title_val);
                                mPost.child("Desc").setValue(desc_val);
                                mPost.child("Date").setValue(date);
                                mPost.child("Dept").setValue(deptEvent);
                                mPost.child("Rounds").setValue(rounds);
                                mPost.child("Prizes").setValue(prizes);
                                mPost.child("Rules").setValue(rules);
                                mPost.child("Venue").setValue(venue);
                                mPost.child("TimePerRound").setValue(timePerRound);
                                mPost.child("Extra").setValue(extra);
                                mPost.child("Fees").setValue(fees);
                                mPost.child("TeamSize").setValue(teamSize);
                                mPost.child("Time").setValue(time);
                                mPost.child("Post_id").setValue((currentPidToPost));
                                mPost.child("ContactDetails").setValue(contactDetails);

                                Log.d(TAG , "Pid posted now is "+currentPidToPost);
                                mProgress.dismiss();
                                //onCreateNotification();
                           startService(new Intent(PostActivity.this , FirebaseServiceOffline.class));
                            Log.d(TAG, "onCreate: user is offline ");

                        Toast.makeText(PostActivity.this , "Post Uploaded successfully ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PostActivity.this, EventsActivity.class));

                                mProgress.dismiss();



                    } else {
                        Toast.makeText(PostActivity.this, "Enter all the fields ", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.show();
        }else {
            Toast.makeText(PostActivity.this , "No internet connection" , Toast.LENGTH_SHORT).show();
        }





    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(PostActivity.this , FirebaseServiceOffline.class));
        Log.d(TAG, "onCreate: user is offline ");

        startActivity(new Intent(PostActivity.this, EventsActivity.class));



    }



    public void DeptRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        rbMech = (RadioButton) findViewById(R.id.radioMech);
        rbEntc = (RadioButton) findViewById(R.id.radioEntc);
        rbCivil = (RadioButton) findViewById(R.id.radioCivil);
        rbComputer = (RadioButton) findViewById(R.id.radioCompter);


        switch (view.getId()) {
            case R.id.radioMech:
                if (checked) {
                    deptSelected = true;
                    deptEvent = "Mechanical DEPARTMENT";

                }
                break;
            case R.id.radioCompter:
                if (checked) {
                    deptSelected = true;
                    deptEvent = "Computer DEPARTMENT";
                }
                break;
            case R.id.radioCivil:
                if (checked) {
                    deptSelected = true;
                    deptEvent = "Civil DEPARTMENT";
                }
                break;
            case R.id.radioEntc:
                if (checked) {
                    deptSelected = true;
                    deptEvent = "E&TC DEPARTMENT";
                }
                break;


        }
    }


}
