package college.root.viit2.Adapters;

import android.content.Context;
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
import java.util.ArrayList;

import college.root.viit2.R;
import college.root.viit2.Realm.Data;

/**
 * Created by root on 25/11/16.
 */

public class CustomAdapter extends RecyclerView.Adapter<MyHolder> {
    Context context;
    ArrayList<Data> arrayList;
    String TAG = "Test";

    String path = "/storage/emulated/0";




    public CustomAdapter( ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;

        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single , parent , false);
        return new MyHolder(context ,v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {


            Data data = arrayList.get(position);
            holder.tvTitle.setText(data.getTitle());
            holder.tvDesc.setText(data.getDesc());
            holder.tvTime.setText(data.getDate());
            holder.tvPid.setText(data.getPostid()+"");
            holder.tvDept.setText(data.getDeptName());
            holder.tvContact.setText(data.getContactDetails());

        if (data.isRegistered()){
            holder.imgStar.setVisibility(View.VISIBLE);
            holder.tvRegister.setVisibility(View.VISIBLE);
        }else {
            holder.imgStar.setVisibility(View.GONE);
            holder.tvRegister.setVisibility(View.GONE);

        }


            try{


                if(data.getImageName()!=null){

                    Log.d(TAG , "image name not null");
                    Log.d(TAG, "onBindViewHolder: Stored Path is "+path);

                    holder.imageView.setVisibility(View.VISIBLE);
                    File file = new File(path , data.getImageName() );
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
                    //imageLoad.setImageBitmap(b);
                    holder.imageView.setImageBitmap(b);
                    Log.d(TAG , "Image saved success");

                }else {
                    holder.imageView.setVisibility(View.GONE);

                    Log.d(TAG, "image name is null" +data.getImageName());
                }
            }catch (FileNotFoundException e){
                Log.d(TAG , "Error "+e.getMessage());
            }
        }





    @Override
    public int getItemCount() {

        if(arrayList == null){
            return  0;
        }else{
            return  arrayList.size() ;
        }


        //return 0;
    }




}
