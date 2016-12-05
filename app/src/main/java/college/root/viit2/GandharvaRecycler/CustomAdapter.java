package college.root.viit2.GandharvaRecycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import college.root.viit2.EventDetailsActivity;
import college.root.viit2.GandharvaRecycler.MyHolder;
import college.root.viit2.R;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.PerceptionData;

/**
 * Created by root on 25/11/16.
 */

public class CustomAdapter extends RecyclerView.Adapter<MyHolder> {
    Context context;
    ArrayList<Data> arrayList;
    ArrayList<PerceptionData> arrayListP;
    String TAG = "Test";

    String path = "/storage/emulated/0";




    public CustomAdapter(ArrayList<PerceptionData> arrayListP, ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.arrayListP = arrayListP;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single , parent , false);
        return new MyHolder(context ,v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        if (arrayListP==null){ // adapter called for Gandharva data .


            Data data = arrayList.get(position);
            holder.tvTitle.setText(data.getTitle());
            holder.tvDesc.setText(data.getDesc());
            Calendar c = Calendar.getInstance();
            holder.tvTime.setText(data.getDate());

            holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    context.startActivity(new Intent(context , EventDetailsActivity.class));

                }
            });


            try{


                if(data.getImageName()!=null){

                    Log.d(TAG , "image name not null");
                    Log.d(TAG, "onBindViewHolder: Stored Path is "+path);


                    File file = new File(path , data.getImageName() );
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
                    //imageLoad.setImageBitmap(b);
                    holder.imageView.setImageBitmap(b);
                    Log.d(TAG , "Image saved success");

                }else {

                    Log.d(TAG, "image name is null" +data.getImageName());
                }
            }catch (FileNotFoundException e){
                Log.d(TAG , "Error "+e.getMessage());
            }






        }else{

            PerceptionData pdata = arrayListP.get(position);
            holder.tvTitle.setText(pdata.getTitle());
            holder.tvDesc.setText(pdata.getDesc());
            Calendar c = Calendar.getInstance();
            holder.tvTime.setText(pdata.getDate());
            holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    context.startActivity(new Intent(context , EventDetailsActivity.class));

                }
            });


            try{


                if(pdata.getImageName()!=null){

                    Log.d(TAG , "image name not null");
                    Log.d(TAG, "onBindViewHolder: Stored Path is "+path);


                    File file = new File(path , pdata.getImageName() );
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
                    //imageLoad.setImageBitmap(b);
                    holder.imageView.setImageBitmap(b);
                    Log.d(TAG , "Image saved success");

                }else {

                    Log.d(TAG, "image name is null" +pdata.getImageName());
                }
            }catch (FileNotFoundException e){
                Log.d(TAG , "Error "+e.getMessage());
            }




        }











    }

    @Override
    public int getItemCount() {

        if(arrayList == null){
            return  0 ;
        }else{
            return arrayList.size();
        }


        //return 0;
    }




}
