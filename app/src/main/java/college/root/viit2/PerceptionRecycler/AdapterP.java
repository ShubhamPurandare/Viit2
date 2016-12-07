package college.root.viit2.PerceptionRecycler;

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
import java.util.ArrayList;
import java.util.Calendar;

import college.root.viit2.EventDetailsActivity;
import college.root.viit2.GandharvaRecycler.MyHolder;
import college.root.viit2.R;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.PerceptionData;

/**
 * Created by root on 6/12/16.
 */

public class AdapterP extends RecyclerView.Adapter<HolderP> {

    Context context;
    ArrayList<PerceptionData> arrayListP;
    String TAG = "Test";

    String path = "/storage/emulated/0";

    public AdapterP(ArrayList<PerceptionData> arrayListP, Context context) {
        this.arrayListP = arrayListP;
        this.context = context;
    }

    @Override
    public HolderP onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singlepageperception, parent, false);
        return new HolderP(context, v);
    }

    @Override
    public void onBindViewHolder(HolderP holder, int position) {
        PerceptionData data = arrayListP.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvDesc.setText(data.getDesc());
        Calendar c = Calendar.getInstance();
        holder.tvTime.setText(data.getDate());

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                context.startActivity(new Intent(context, EventDetailsActivity.class));

            }
        });


        try {


            if (data.getImageName() != null) {

                Log.d(TAG, "image name not null");
                Log.d(TAG, "onBindViewHolder: Stored Path is " + path);


                File file = new File(path, data.getImageName());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
                //imageLoad.setImageBitmap(b);
                holder.imageView.setImageBitmap(b);
                Log.d(TAG, "Image saved success");

            } else {
                holder.imageView.setVisibility(View.GONE);

                Log.d(TAG, "image name is null" + data.getImageName());
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (arrayListP == null) {
            return 0;

        } else {
            return arrayListP.size();
        }
    }
}
