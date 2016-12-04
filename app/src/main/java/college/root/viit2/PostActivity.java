package college.root.viit2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {

    private EditText mAddDesc, mAddTitle;
    private Uri mImageUri = null;
    private Button mSubmitButton;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private boolean checkBoxIsChecked = false;
    int currentPidGandharva =0;
    int currentPidPerception =0;
    private String TAG = "Test";
    ImageButton mpostImage;
    RadioButton rbPerception , rbGandharva;
    int currentPidToPost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        checkBoxIsChecked = false;

        Intent intent = getIntent();
        currentPidGandharva = intent.getIntExtra("CurrentPidGandharva" , 0);
        Log.d(TAG , "Curent recieved recent pid is "+currentPidGandharva);
        currentPidPerception = intent.getIntExtra("CurrentPidPerception" , 0);

        mAddTitle = (EditText) findViewById(R.id.editText_title);
        mAddDesc = (EditText) findViewById(R.id.editText_desc);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mpostImage = (ImageButton)findViewById(R.id.imagePost);
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
            Picasso.with(getApplicationContext()).load(mImageUri).resize(500,400).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
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

                final String title_val = mAddTitle.getText().toString().trim();
                final String desc_val = mAddDesc.getText().toString().trim();

                Log.d(TAG, " In startAdding ");
                final SimpleDateFormat date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ");
                if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri !=null && checkBoxIsChecked) {
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
                            mPost.child("Title").setValue(title_val);
                            mPost.child("Desc").setValue(desc_val);
                            mPost.child("Time").setValue(date.format(new Date()));
                            //    mPost.child("Image").setValue(downloadUrl.toString());
                            mPost.child("Post_id").setValue((currentPidToPost+1));
                            Log.d(TAG , "Pid posted now is "+currentPidToPost);
                            mProgress.dismiss();
                            //onCreateNotification();
                            Toast.makeText(PostActivity.this , "Post Uploaded successfully ", Toast.LENGTH_SHORT).show();
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


        rbGandharva = (RadioButton) findViewById(R.id.radio_cultural);
        rbPerception = (RadioButton) findViewById(R.id.radio_technical);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_technical:
                if (checked)
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("GANDHARVA");
                currentPidToPost = currentPidGandharva;
                Log.d(TAG, "onRadioButtonClicked: current post id id of Gandharva");
                checkBoxIsChecked= true;
                break;
            case R.id.radio_cultural:
                if (checked)
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("PERCEPTION");
                checkBoxIsChecked= true;
                currentPidToPost = currentPidPerception;
                Log.d(TAG, "onRadioButtonClicked: current post id id of Perception");
                break;


        }
    }



}
