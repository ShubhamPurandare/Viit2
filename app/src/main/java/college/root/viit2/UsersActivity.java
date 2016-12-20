package college.root.viit2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import college.root.viit2.Adapters.UserAdaper;
import college.root.viit2.IntentServices.FirebaseServiceOffline;
import college.root.viit2.IntentServices.FirebaseServiceOnline;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import college.root.viit2.Realm.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class UsersActivity extends AppCompatActivity {



    DatabaseReference mdatabase;

    TextView tvUserCount , tvTitle ;
    String pid = "";
    String TAG = "Test";
    RecyclerView recyclerView;
    ArrayList<User> arrayList ;
    UserAdaper adaper;
    ProgressDialog mprogess;
    Realm realm;
    Data data;
    RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Intent i = getIntent();
         pid = i.getStringExtra("pid");
        Log.d(TAG , "Pid obtained is "+pid );

        if (!isNetWorkAvailable()){
            Toast.makeText(UsersActivity.this , "No Internet Connection .... Try again later"  , Toast.LENGTH_SHORT).show();

        }else {
            startService(new Intent(UsersActivity.this , FirebaseServiceOnline.class));
            Log.d(TAG, "onCreate: internet connection exists hence connected to firebase...");
        }


        RealmConfiguration configuration = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
        helper = new RealmHelper(realm);

        data = new Data();

        if( pid == null ){

            data = helper.retriveFromPid(0);

        }else {

            data = helper.retriveFromPid(Integer.parseInt(pid));

        }



        recyclerView = (RecyclerView) findViewById(R.id.recyclerUser);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(UsersActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        Log.d(TAG, "onCreate: Recycler view and adapter set ");

        adaper = new UserAdaper( arrayList, UsersActivity.this );
        adaper.notifyDataSetChanged();
        recyclerView.setAdapter(adaper);

        tvUserCount = (TextView)findViewById(R.id.tvUserCount);
        tvUserCount.setText("0");
        tvTitle = (TextView)findViewById(R.id.tvTitleUser);
        tvTitle.setText(data.getTitle());



        mdatabase = FirebaseDatabase.getInstance().getReference().child("RegisteredEvents").child(pid);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mprogess = new ProgressDialog(UsersActivity.this);
                mprogess.setTitle("Please wait while we load the competition ..");
                mprogess.show();
                tvUserCount.setText(dataSnapshot.getChildrenCount()+"");
                arrayList.clear();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){

                    User user = new User();
                    Log.d(TAG, "onDataChange: ");
                    DataSnapshot item = iterator.next();
                    String name = item.child("Display name").getValue().toString();
                    String contact = item.child("Contact").getValue().toString();
                    String year = item.child("Year").getValue().toString();
                    String branch = item.child("Branch").getValue().toString();
                    String email = item.child("Email").getValue().toString();
                    String photoUrl = item.child("PhotoUrl").getValue().toString();
                    Log.d(TAG, "onDataChange: display name is "+name);
                    Log.d(TAG, "onDataChange: contact is "+contact);
                    Log.d(TAG, "onDataChange: branch is "+branch);
                    Log.d(TAG, "onDataChange: year is "+year);
                    user.setName(name);
                    user.setYear(year);
                    user.setBranch(branch);
                    user.setContact(contact);
                    user.setEmail(email);
                    user.setPhotoUrl(photoUrl);

                    arrayList.add(user);
                }
                adaper = new UserAdaper( arrayList, UsersActivity.this );
                recyclerView.setAdapter(adaper);
                adaper.notifyDataSetChanged();
                mdatabase.removeEventListener(this);
                startService(new Intent(UsersActivity.this , FirebaseServiceOffline.class));
                Log.d(TAG, "onDataChange: done displaying hence user disconnected .....");
                mprogess.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private  boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}

