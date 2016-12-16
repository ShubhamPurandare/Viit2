package college.root.viit2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import college.root.viit2.IntentServices.FirebaseServiceOffline;
import college.root.viit2.IntentServices.FirebaseServiceOnline;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import college.root.viit2.Realm.UserInfo;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RegisterActivity extends AppCompatActivity {
    TextView tvName , tvBranch , tvYear , tvGr , tvcontact , tvEmail;
    Button btnReg;
    String TAG = "Test";
    UserInfo info;
    RealmHelper helper;
    Realm realm;
    DatabaseReference mdatabase,mregister, mregisteruid;
    String pid = null;
    ProgressDialog mprogress;
    ImageView profilePic;
    Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvName = (TextView)findViewById(R.id.tvPutName);
        tvBranch = (TextView)findViewById(R.id.tvPutBranch);
        tvYear = (TextView)findViewById(R.id.tvPutYear);
        tvcontact = (TextView)findViewById(R.id.tvPutContact);
        profilePic = (ImageView)findViewById(R.id.profilePic);
        btnReg = (Button)findViewById(R.id.btnRegister);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("RegisteredEvents");

        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");

        RealmConfiguration configuration = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
        helper = new RealmHelper(realm);

        data = new Data();
        data = helper.retriveFromPid(Integer.parseInt(pid));




        info = new UserInfo();
        info = helper.getUserInfo();

        if (info!=null){
            tvName.setText(info.getName());
            tvBranch.setText(info.getBranch());
            tvcontact.setText(info.getContact());
            tvYear.setText(info.getYear());

            try {
                Picasso.with(RegisterActivity.this).load(info.getPhotoUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        profilePic.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            } catch(Exception e) {
                Log.d(TAG, "onBindViewHolder: Error "+e.getMessage());
            }


          }else{
            Log.d(TAG, "onClick: info is null");
        }


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetWorkAvailable()){
                    Toast.makeText(RegisterActivity.this , "No Internet Connection ..." , Toast.LENGTH_SHORT).show();
                    startService(new Intent(RegisterActivity.this , FirebaseServiceOffline.class));
                    Log.d(TAG, "onClick: Disconnected from firebase ...");

                }else{
                    LayoutInflater inflater = getLayoutInflater();
                    View view1 = inflater.inflate(R.layout.fragment_dialog , null);

                    DialogFragment dialogFragment = new DialogFragment(view1);
                    dialogFragment.show(getSupportFragmentManager() , " its working ");




                    startService(new Intent(RegisterActivity.this , FirebaseServiceOnline.class));
                    Log.d(TAG, "onClick: connected to firebase ...");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Confirm Registration ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int i) {

                            mprogress = new ProgressDialog(RegisterActivity.this);
                            mprogress.setTitle("Please wait while we submit the details..");
                            mprogress.show();

                            mregister = mdatabase.child(pid);
                            mregister.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(info.getUid().toString())){
                                        Toast.makeText(RegisterActivity.this, "You have already registered for this event !", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(RegisterActivity.this , EventsActivity.class));
                                        mprogress.dismiss();
                                    }else{
                                        mregisteruid = mregister.child(info.getUid().toString());
                                        mregisteruid.child("Display name").setValue(info.getName());
                                        mregisteruid.child("Contact").setValue(info.getContact());
                                        mregisteruid.child("Branch").setValue(info.getBranch());
                                        mregisteruid.child("Year").setValue(info.getYear());
                                        mregisteruid.child("Email").setValue(info.getEmail());
                                        mregisteruid.child("PhotoUrl").setValue(info.getPhotoUrl());
                                       realm.beginTransaction();
                                        data.setRegistered(true);
                                        realm.commitTransaction();
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                realm.copyToRealmOrUpdate(data);
                                               }
                                        });
                                        Log.d(TAG, "onDataChange: user is registered in realm!");
                                        Log.d(TAG, "onClick: Child created in post "+pid + " with uid "+info.getUid());
                                        mprogress.dismiss();


                                        Toast.makeText(getApplicationContext() , "Successfully registered for the event !", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(RegisterActivity.this , EventsActivity.class));

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });
                    builder.show();
                    startService(new Intent(RegisterActivity.this , FirebaseServiceOffline.class));
                    Log.d(TAG, "onClick: Firebase disconnected ..");

                }




            }
        });


    }

    private  boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
