package college.root.viit2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.List;

import college.root.viit2.IntentServices.FirebaseServiceOffline;
import college.root.viit2.Realm.RealmHelper;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FormActivity extends Activity  {

    EditText  edContact , edYear , edBranch;
    String name =null, year = null, branch = null , grnumber = null, contact = null;
    college.root.viit2.Realm.UserInfo info;
    RealmHelper helper;
    Realm realm;
    String TAG = "Test";
    String id;
    FirebaseAuth firebaseAuth;
    FirebaseUser user ;
    Spinner spBranch;
    boolean contactSaved = false;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rzhilevDu2SSisDocUmlcvoa6";
    private static final String TWITTER_SECRET = "RR5VcZEPmRNOyzIqO7xCsGHnROclG0Uz2tDWfd50h4v17nm9gE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formpage);

        firebaseAuth = FirebaseAuth.getInstance();
        info = new college.root.viit2.Realm.UserInfo();
        final List<String> categories = new ArrayList<String>();
        categories.add("Computer");
        categories.add("E and TC");
        categories.add("Mechanical");
        categories.add("Civil");
        categories.add("IT");

         user = firebaseAuth.getCurrentUser();
        name = user.getDisplayName();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.authButton);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                startService(new Intent(FormActivity.this , FirebaseServiceOffline.class));
                Log.d(TAG, "success: user is offine till he clicks submit button");
                // TODO: associate the session userID with your user model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
                info.setContact(phoneNumber);
                contactSaved = true;
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
                contactSaved = false;
               }
        });




        id = user.getUid();
        RealmConfiguration configuration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
         helper = new RealmHelper(realm);





    }

    public void done(View view) {

        /*Firebase ref=new Firebase("https://recyclerview-d0f73.firebaseio.com/");
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String uid=user.getUid();
        ref.child(uid).child("register").setValue(true);
       */
        // add the following to realm

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save the details ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

              //  name = edName.getText().toString();
              //  branch = edBranch.getText().toString();
             //   contact = edContact.getText().toString();
             //   grnumber = edGrnumber.getText().toString();

                if (   year!=null  ){
                    // save data to realm
            if (!contactSaved){
                Toast.makeText(FormActivity.this , "Contact not saved" , Toast.LENGTH_SHORT).show();
            }

                    info.setName(name);
                    info.setBranch(branch);
                   // info.setContact(contact);
                   // info.setGrnumber(grnumber);
                    info.setYear(year);
                    info.setUid(id);
                    info.setEmail(user.getEmail());
                    info.setPhotoUrl(user.getPhotoUrl().toString());

                    if (helper.saveUserInfo(info)){
                        Toast.makeText(FormActivity.this , "Details saved successfully" , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(FormActivity.this , "Error in saving details please try again." , Toast.LENGTH_SHORT).show();

                    }
                    SharedPreferences sharedPreferences1 = getSharedPreferences("reg", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences1.edit();
                    editor.putBoolean("register",true);
                    editor.commit();

                    Intent intent=new Intent(FormActivity.this,EventsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidein , R.anim.slideout);
                    finish();



                }else {
                    Toast.makeText(FormActivity.this , "Please enter all the fields ", Toast.LENGTH_SHORT).show();
                }

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

    public void onDeptClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {

            case R.id.rbComputer:
                if (checked){
                    branch = "Computer ";
                    Log.d(TAG, "onDeptClicked: Comp dept selected");
                    break;
                }
            case R.id.rbCivil:
                if (checked){
                    branch = "Civil ";

                    Log.d(TAG, "onDeptClicked: Civil dept selected");
                    break;
                }
            case R.id.rbENTC:
                if (checked){
                    branch = "E and TC ";

                    Log.d(TAG, "ENTC: Comp dept selected");
                    break;
                }
            case R.id.rbMech:
                if (checked){
                    branch = "Mechanical ";
                    Log.d(TAG, "onDeptClicked: Mech dept selected");
                    break;
                }

        }
    }




    public void onYearClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {

            case R.id.rbFe:
                if (checked){
                    year = "First Year";
                    Log.d(TAG, "onYearClicked: FE selected");
                    break;

                }
            case R.id.rbSE:
                if (checked){
                    year = "Second year";
                    Log.d(TAG, "onYearClicked: SE selected");
                    break;
                }
            case R.id.rbTE:
                if (checked){
                    year = "Third year";
                    Log.d(TAG, "onYearClicked: TE selected");
                    break;
                }
            case R.id.rbBE:
                if (checked){
                    year = "Final year";
                    Log.d(TAG, "onYearClicked: BE selected");
                    break;
                }

        }
    }



}