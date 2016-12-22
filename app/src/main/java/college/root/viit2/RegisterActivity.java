package college.root.viit2;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

                                        //TODO: add functionality of setting reminder in calendar 24 hrs before the event

                                        addEventInCalendar(); // adds reminder in calender 24hrs before the event

                                        //startActivity(new Intent(RegisterActivity.this , EventsActivity.class));

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

    private void addEventInCalendar() {


        int dayOfEvent =  extractDate(data.getDate());

        addEvent(RegisterActivity.this);

    }

    private int extractDate(String date) {

        int i = 0;

        String day = "";

        while(date.charAt(i)  != '/' ) {

            day += date.charAt(i);

            i++;

        }

        return Integer.parseInt(day);

    }

    // Add an event to the calendar of the user.
    public void addEvent(Context context) {
        GregorianCalendar calDate = new GregorianCalendar();

        //this._year, this._month, this._day, this._hour, this._minute);

        try {
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, 36000000);
            values.put(CalendarContract.Events.DTEND, 36000000+7*60*60*1000);
            values.put(CalendarContract.Events.TITLE, data.getTitle());
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                    .getTimeZone().getID());
           // System.out.println(Calendar.getInstance().getTimeZone().getID());
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // Save the eventId into the Task object for possible future delete.
         long eventId = Long.parseLong(uri.getLastPathSegment());
            // Add a 1 day reminders (3 reminders)
            setReminder(cr, eventId, 1440);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // routine to add reminders with the event
    public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            Cursor c = CalendarContract.Reminders.query(cr, eventID,
                    new String[]{CalendarContract.Reminders.MINUTES});

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
