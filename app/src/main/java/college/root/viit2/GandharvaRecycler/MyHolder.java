package college.root.viit2.GandharvaRecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import college.root.viit2.EventDetailsActivity;
import college.root.viit2.R;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;

/**
 * Created by root on 26/11/16.
 */

public class MyHolder extends RecyclerView.ViewHolder {


    TextView tvTitle , tvDesc , tvTime , tvPid;
    ImageView imageView;
    Context context;
    String TAG ="Test";
    RealmHelper helper;
    Data data;


    public MyHolder(final Context context, View itemView) {
        super(itemView);

        tvDesc = (TextView) itemView.findViewById(R.id.tvdesc);
        tvTitle = (TextView) itemView.findViewById(R.id.tvtitle);
        imageView = (ImageView)itemView.findViewById(R.id.imageLoad);
        tvTime = (TextView)itemView.findViewById(R.id.tvTime);
        tvPid = (TextView)itemView.findViewById(R.id.pid);
        this.context = context;




        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = new Data();
                data= null;
                data = helper.retriveFromPid(Integer.parseInt(tvPid.getText().toString()));
                Log.d(TAG, "onClick: data reurned something");
                if (data != null){

                    String title = data.getTitle();
                    String desc = data.getDesc();
                    Log.d(TAG, "onClick: Title is "+title);
                    Log.d(TAG, "onClick: Desc is "+desc);
                    Log.d(TAG, "onClick: Pid is "+tvPid.getText().toString());

                    Intent i = new Intent(context , EventDetailsActivity.class);
                    i.putExtra("Title" , title);
                    i.putExtra("Desc" , desc);
                    context.startActivity(i);


                }else{
                    Log.d(TAG, "onClick: data is null");
                }



            }
        });


    }
}
