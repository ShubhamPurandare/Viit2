package college.root.viit2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import college.root.viit2.GandharvaRecycler.CustomAdapter;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
public class MainActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    String TAG = "Test";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d(TAG, "run: In background thread");

        SharedPreferences sharedPreferences = getSharedPreferences("ShaPreferences1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        boolean  firstTime=sharedPreferences.getBoolean("first", true);

        firebaseAuth= FirebaseAuth.getInstance();

        FirebaseUser user=firebaseAuth.getCurrentUser();

        Log.d(TAG, "run: checking conditions");

        if(firstTime || user==null) {
            editor.putBoolean("first",false);
            //For commit the changes, Use either editor.commit(); or  editor.apply();.
            editor.commit();
            Intent intent = new Intent(MainActivity.this, signin.class);
            Log.d(TAG, "run: user is null");
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(MainActivity.this, EventsActivity.class);
            Log.d(TAG, "run: user is not null");
            startActivity(intent);
            finish();
        }
        // After 5 seconds redirect to another intent
        //Intent i=new Intent(getBaseContext(),FirstScreen.class);
        //startActivity(i);

        //Remove activity



    }// end of onCreate method







}
