package college.root.viit2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.provider.MediaStore.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    RecyclerView recyclerView ;
    CustomAdapter customAdapter;
    RealmChangeListener realmChangeListener ;
    DatabaseReference mDatabase;
    String TAG = "Test";
    int currentPid =0 ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    StorageReference mstorageReference;
    String imageNameRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ALLEvents");
        mstorageReference = FirebaseStorage.getInstance().getReference();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        editor = sharedPreferences.edit();

        String pid = sharedPreferences.getString("CurrentPid" , "0");
        currentPid = Integer.parseInt(pid);
        Log.d(TAG , "Latest pid obtained by shared prefs is :" +currentPid);


        RealmConfiguration configuration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);
        helper.retrive();
        Log.d(TAG , "Helper set");
        customAdapter = new CustomAdapter(helper.refresh() , this);
        recyclerView.setAdapter(customAdapter);
        Log.d(TAG , "Adapter set");
       realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                Log.d(TAG , "Realm changed");
                customAdapter = new CustomAdapter(helper.refresh() , MainActivity.this);
                recyclerView.setAdapter(customAdapter);
                onCreateNotification();
            }
        };

        realm.addChangeListener(realmChangeListener);

        mDatabase.keepSynced(true);
        mDatabase.addChildEventListener(childEventListener);




    }// end of onCreate method


     ChildEventListener childEventListener = new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             Log.d(TAG , "in childevent listener");
             long count = dataSnapshot.getChildrenCount();
             Log.d(TAG , "Childresn count is :" +count);

             Iterator i = dataSnapshot.getChildren().iterator();

             while (i.hasNext()){
                    try {
                        String pid = dataSnapshot.child("Post_id").getValue().toString();
                        Log.d(TAG, "Pid is " + pid);
                        String title = dataSnapshot.child("Title").getValue().toString();
                        String desc = dataSnapshot.child("Desc").getValue().toString();
                        mstorageReference.child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imageNameRealm = downloadImage(uri);



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                         currentPid = Integer.parseInt(pid);
                        Log.d(TAG , "Current latest pid is"+ currentPid);
                        editor.putString("CurrentPid" , ""+currentPid);
                        editor.commit();
                        Log.d(TAG , "Current latest pid in Shared prefrences is"+ currentPid);


                        Data data = new Data();
                        data.setPostid(Integer.parseInt(pid));
                        data.setDesc(desc);
                        data.setTitle(title);
                        data.setImageName(imageNameRealm);

                        RealmHelper helper = new RealmHelper(realm);

                 if (helper.save(data)){

                 }else {
                     Log.d(TAG , "Post with pid "+ pid + " already exists in realm");

                 }
                    }catch (Exception e){
                            Log.d(TAG , "Exception caught is "+e.getMessage());
                    }

                 i.next();
             }// end of while

         }

         @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {

         }

         @Override
         public void onChildRemoved(DataSnapshot dataSnapshot) {

         }

         @Override
         public void onChildMoved(DataSnapshot dataSnapshot, String s) {

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
           Intent intent = new Intent(MainActivity.this, PostActivity.class);
           intent.putExtra("CurrentPid" , currentPid);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void onCreateNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent=taskStackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);
        String abc =  "Check out the new Event added .... ";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("New entry Added!");
        mBuilder.setContentText(abc);
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.drawable.notify);
        mBuilder.setContentIntent(pendingIntent);
        //mBuilder.setWhen(System.currentTimeMillis());
        Notification notification = mBuilder.build();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        nm.notify(1, notification);

    }


    private String downloadImage(Uri uri) {
        String imageName = null;
        try {
            Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver() , uri);
            // /  BitmapDrawable drawable = (BitmapDrawable) imageButton.getDrawable();

            Log.d(TAG , "Bitmap created");
            File sdCardDirectory = Environment.getExternalStorageDirectory();

           imageName =System.currentTimeMillis()+".png";
            File image = new File(sdCardDirectory, imageName);


            Log.d(TAG , "image file created" +image);
            boolean success = false;

            // Encode the file as a PNG image.
            FileOutputStream outStream;
            try {

                outStream = new FileOutputStream(image);
                b.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

                Log.d(TAG , " bitmap stored ");
                outStream.flush();
                outStream.close();
                success = true;


            } catch (FileNotFoundException e) {

                Log.d(TAG , "Error "+e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {

                Log.d(TAG , "Error" +e.getMessage());
                e.printStackTrace();
            }
            if (success) {
                Toast.makeText(getApplicationContext(), "Image saved with success",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Check Phone Storage for saved image",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error during image saving", Toast.LENGTH_LONG).show();
            }
        }catch (IOException e){
            Log.d(TAG , "Error "+e.getMessage());
        }


        return imageName;





    }// end of download image






    public  void retriveImage(String path , String imageName){

        try{

            File file = new File(path , imageName );
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
            //imageLoad.setImageBitmap(b);
            Log.d(TAG , "Image saved success");




        }catch (FileNotFoundException e){
            Log.d(TAG , "Error "+e.getMessage());
        }



    }


}
