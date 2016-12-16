package college.root.viit2.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import college.root.viit2.R;
import college.root.viit2.Realm.User;

/**
 * Created by root on 8/12/16.
 */

public class UserAdaper extends RecyclerView.Adapter<UserHolder> {

    int count = 0;
    User user;
    Context context;
    String TAG = "Test";
    ArrayList<User> arraylist = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser user1;

    public UserAdaper(ArrayList<User> arraylist  ,Context context) {
    this.context = context;
    this.arraylist = arraylist;
        Log.d(TAG, "UserAdaper: constructor called....");
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.registereduser , parent , false);
        return new UserHolder(context ,v);


    }

    @Override
    public void onBindViewHolder(final UserHolder holder, int position) {

        firebaseAuth = FirebaseAuth.getInstance();
        user1 = firebaseAuth.getCurrentUser();

        if (user !=null){

            if (user1.getEmail().equals("shubham.purandare@gmail.com")|| user1.getEmail().equals("mrunalj.369@gmail.com")){

                holder.contact.setVisibility(View.VISIBLE);
                holder.cnt.setVisibility(View.VISIBLE);
            }else{
                holder.contact.setVisibility(View.GONE);
                holder.cnt.setVisibility(View.GONE);

            }


        }else{
            Log.d(TAG, "onCreate: user is null ");
        }
        user = arraylist.get(position);
        holder.displayname.setText(user.getName());
        holder.contact.setText(user.getContact());
        holder.branch.setText(user.getBranch());
        holder.year.setText(user.getYear());
        holder.email.setText(user.getEmail());


        try {
            Picasso.with(context).load(user.getPhotoUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    holder.imageView.setImageBitmap(bitmap);
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


    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}
