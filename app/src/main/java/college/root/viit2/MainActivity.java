package college.root.viit2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import college.root.viit2.EventsActivity;
import college.root.viit2.SignInActivity;

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
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
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
