package college.root.viit2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import college.root.viit2.Realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class form extends AppCompatActivity {

    EditText edName , edGrnumber , edContact , edYear , edBranch;
    String name , year, branch , grnumber , contact;
    college.root.viit2.Realm.UserInfo info;
    Realm realm;
    String TAG = "Test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formpage);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        RealmConfiguration configuration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        edBranch = (EditText)findViewById(R.id.edBranch);
        edName = (EditText)findViewById(R.id.edName);
        edYear = (EditText)findViewById(R.id.edYear);
        edContact = (EditText)findViewById(R.id.edContact);
        edGrnumber = (EditText)findViewById(R.id.edGr);



    }

    public void done(View view) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

                name = edName.getText().toString();
                year = edYear.getText().toString();
                branch = edBranch.getText().toString();
                contact = edContact.getText().toString();
                grnumber = edGrnumber.getText().toString();

                if (name != null && contact!=null && year!=null && branch!=null && grnumber!=null){
                    // save data to realm

                     info = new college.root.viit2.Realm.UserInfo();
                    info.setName(name);
                    info.setBranch(branch);
                    info.setContact(contact);
                    info.setGrnumber(grnumber);
                    info.setYear(year);

                    RealmHelper helper = new RealmHelper(realm);
                    if (helper.saveUserInfo(info)){
                        Toast.makeText(form.this , "Details saved successfully" , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(form.this , "Error in saving details please try again." , Toast.LENGTH_SHORT).show();

                    }



                }else {
                    Toast.makeText(form.this , "Please enter all the fields ", Toast.LENGTH_SHORT).show();
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




        SharedPreferences sharedPreferences1 = getSharedPreferences("reg", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences1.edit();
        editor.putBoolean("register",true);
        editor.commit();

        Intent intent=new Intent(form.this,EventsActivity.class);
        startActivity(intent);
        finish();
    }


}