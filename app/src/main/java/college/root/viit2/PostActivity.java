package college.root.viit2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class PostActivity extends AppCompatActivity {

    private EditText mAddDesc, mAddTitle;
    private Uri mImageUri = null;
    private Button mSubmitButton;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private int checkBoxIsChecked = 0;
    int currentPid =0;
    private String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
         currentPid = intent.getIntExtra("CurrentPid" , 0);
        Log.d(TAG , "Curent recieved recent pid is "+currentPid);

        mAddTitle = (EditText) findViewById(R.id.editText_title);
        mAddDesc = (EditText) findViewById(R.id.editText_desc);
        mSubmitButton = (Button) findViewById(R.id.submit_button);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ALLEvents");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startAdding();
            }
        });

    } // oncreate method end.

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(PostActivity.this, MainActivity.class);
            finish();
            startActivity(intent);

        }
        return true;
    }


    private void startAdding() {

        final String title_val = mAddTitle.getText().toString().trim();
        final String desc_val = mAddDesc.getText().toString().trim();

        Log.d(TAG, " In startAdding ");

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) ) {
            mProgress.setMessage("Adding Event...");
            mProgress.show();
            mProgress.setCanceledOnTouchOutside(false);
          //  final int randomVar = new Random().nextInt(1000);
            DatabaseReference mPost = mDatabase.push();

            mPost.child("Title").setValue(title_val);
            mPost.child("Desc").setValue(desc_val);
            //    mPost.child("Image").setValue(downloadUrl.toString());
            mPost.child("Post_id").setValue(currentPid +1);
            Log.d(TAG , "Pid posted now is "+currentPid+1);
            mProgress.dismiss();
            //onCreateNotification();
            startActivity(new Intent(PostActivity.this, MainActivity.class));


        } else {
            Toast.makeText(PostActivity.this, "Enter all the fields ", Toast.LENGTH_SHORT).show();
        }

    }

}
