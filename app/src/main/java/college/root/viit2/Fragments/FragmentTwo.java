package college.root.viit2.Fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import college.root.viit2.EventsActivity;
import college.root.viit2.GandharvaRecycler.CustomAdapter;
import college.root.viit2.MainActivity;
import college.root.viit2.R;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;

import static android.content.Context.NOTIFICATION_SERVICE;


public class FragmentTwo extends Fragment {

    Realm realm;
    RecyclerView recyclerView ;
    CustomAdapter customAdapter;
    RealmChangeListener realmChangeListener ;
    DatabaseReference mDatabase;
    String TAG = "Test";
  //  int currentPid =0 ;
    SharedPreferences sharedPreferences1 ;
    SharedPreferences.Editor editor ;
    StorageReference mstorageReference;
    String imageNameRealm;
    String imageName = null;


    public FragmentTwo() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences1 = getActivity().getSharedPreferences("userInfo" , Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("GANDHARVA");
        mstorageReference = FirebaseStorage.getInstance().getReference();


        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewFragment2);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        editor = sharedPreferences1.edit();

        String pid = sharedPreferences1.getString("CurrentPidGandharva" , "0");
        ((EventsActivity)getActivity()).currentPidGandharva = Integer.parseInt(pid);
        Log.d(TAG , "Latest pid obtained by shared prefs is :" +((EventsActivity)getActivity()).currentPidGandharva);


        RealmConfiguration configuration = new RealmConfiguration.Builder(getContext()).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);
        helper.retrive();
        Log.d(TAG , "Helper set");
        customAdapter = new CustomAdapter(null ,helper.refresh() , getContext());
        recyclerView.setAdapter(customAdapter);
        Log.d(TAG , "Adapter set");
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                Log.d(TAG , "Realm changed");
                customAdapter = new CustomAdapter(null , helper.refresh() , getContext());
                recyclerView.setAdapter(customAdapter);
                onCreateNotification();
            }
        };

        realm.addChangeListener(realmChangeListener);

        mDatabase.keepSynced(true);
        mDatabase.addChildEventListener(childEventListener);



    }// end of onCreate


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG , "in childevent listener");
            long count = dataSnapshot.getChildrenCount();
            Log.d(TAG , "Childresn count is :" +count);

            Iterator i = dataSnapshot.getChildren().iterator();

            //while (i.hasNext()){
                try {
                    String pid = dataSnapshot.child("Post_id").getValue().toString();
                    Log.d(TAG, "Pid is " + pid);
                    String title = dataSnapshot.child("Title").getValue().toString();
                    String desc = dataSnapshot.child("Desc").getValue().toString();
                    String imageUrl = dataSnapshot.child("Image").getValue().toString();
                    String date = dataSnapshot.child("Time").getValue().toString();
                  //  Log.d(TAG, "onChildAdded: image url is "+imageUrl);


                    RealmHelper helper = new RealmHelper(realm);

                    if(!helper.check(Integer.parseInt(pid))){
                        Log.d(TAG, "onChildAdded: Pid "+ pid+ "Already exists") ;
                    }else {
                        Log.d(TAG, "onChildAdded: Pid "+pid+"doesnot exists");

                        ((EventsActivity)getActivity()).currentPidGandharva = Integer.parseInt(pid);
                        Log.d(TAG , "Current latest pid is"+ ((EventsActivity)getActivity()).currentPidGandharva);
                        editor.putString("CurrentPidGandharva" , ""+((EventsActivity)getActivity()).currentPidGandharva);
                        editor.commit();
                        Log.d(TAG , "Current latest pid in Shared prefrences is"+ ((EventsActivity)getActivity()).currentPidGandharva);

                        Data data = new Data();
                        data.setPostid(Integer.parseInt(pid));
                        data.setDesc(desc);
                        data.setTitle(title);
                        data.setDate(date);
                        imageNameRealm  = downloadImage(imageUrl);

                        data.setImageName(imageNameRealm);
                        Log.d(TAG, "onChildAdded: image name is "+imageNameRealm);


                        if (helper.save(data)){

                        }else {
                            Log.d(TAG , "Post with pid "+ pid + " already exists in realm");

                        }


                    }


                }catch (Exception e){
                    Log.d(TAG , "Exception caught is "+e.getMessage());
                }

                i.next();
            }// end of while

        //}

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



    private void onCreateNotification() {
        Intent intent = new Intent(getContext(), EventsActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getContext());
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent=taskStackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);
        String abc =  "Check out the new Event added in Gandharva";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
        mBuilder.setContentTitle("New entry Added!");
        mBuilder.setContentText(abc);
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.drawable.notify);
        mBuilder.setContentIntent(pendingIntent);
        //mBuilder.setWhen(System.currentTimeMillis());
        Notification notification = mBuilder.build();
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, notification);

    }


    private String downloadImage(String url) {

        Log.d(TAG , "In download method..");



        Picasso.with(getContext())
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // loaded bitmap is here (bitmap)

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
                            bitmap.compress(Bitmap.CompressFormat.PNG, 75, outStream);
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
                            Toast.makeText(getContext(), "Image saved with success",
                                    Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(), "Check Phone Storage for saved image",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Error during image saving", Toast.LENGTH_LONG).show();
                        }




                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d(TAG, "onBitmapFailed: " +errorDrawable);


                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });



           /* URL u = new URL(url);
            HttpURLConnection connec = (HttpURLConnection) u.openConnection();
            connec.setDoInput(true);
            connec.connect();
            InputStream input = connec.getInputStream();
            Bitmap b = BitmapFactory.decodeStream(input); */


        //  Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver() ,uri );
        // /  BitmapDrawable drawable = (BitmapDrawable) imageButton.getDrawable();





        return imageName;





    }// end of download image



}
