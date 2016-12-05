package college.root.viit2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();


    }

    public void done(View view) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        /*Firebase ref=new Firebase("https://recyclerview-d0f73.firebaseio.com/");
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String uid=user.getUid();
        ref.child(uid).child("register").setValue(true);
       */
        SharedPreferences sharedPreferences1 = getSharedPreferences("reg", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences1.edit();
        editor.putBoolean("register",true);
        editor.commit();

        Intent intent=new Intent(form.this,EventsActivity.class);
        startActivity(intent);
        finish();
    }
}