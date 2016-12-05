package college.root.viit2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class signin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static int RC_SIGNIN=0;
    private static String TAG="Test";
    //public static boolean register=false;
    public static boolean register;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
       // Firebase.setAndroidContext(this);

        SharedPreferences sharedPreferences1 = getSharedPreferences("reg", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences1.edit();

        register=sharedPreferences1.getBoolean("register", false);

        firebaseAuth= FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Log.d(TAG,"User logged in ");
                }
                else
                {
                    Log.d(TAG,"User logged out ");
                }

            }
        };

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        findViewById(R.id.view).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGNIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            firebaseAuth= FirebaseAuth.getInstance();
            FirebaseUser user=firebaseAuth.getCurrentUser();

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                Toast.makeText(getApplicationContext(),"Succesfully signed in!Press next ",Toast.LENGTH_SHORT).show();
             //   String email=user.getEmail();
               // Log.d("email",email);
            

            }
            else
                Log.d(TAG, "Google Login Failed");
                //Toast.makeText(getApplicationContext(),"Google login failed ",Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:oncomplete: " + task.isSuccessful());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void Signin()
    {
        Intent intent= Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient
        );
        startActivityForResult(intent,RC_SIGNIN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed.");
        Toast.makeText(getApplication(),"Connection failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Signin();

    }
  public void next(View view){
      firebaseAuth= FirebaseAuth.getInstance();
      final FirebaseUser user=firebaseAuth.getCurrentUser();

  //    Firebase rootRef = new Firebase("https://recyclerview-d0f73.firebaseio.com/");
      Log.d("user : ", String.valueOf(user));
      if(user!=null) {

          if(register) {
              Intent intent1 = new Intent(signin.this, EventsActivity.class);
              startActivity(intent1);
              finish();
          }
          else
          {
              Intent intent1 = new Intent(signin.this, form.class);
              startActivity(intent1);
              finish();
          }
      }
      else
      {
              Toast.makeText(getApplication(),"Please Sign in first !",Toast.LENGTH_SHORT).show();

      }

  }

}
