package college.root.viit2.GandharvaRecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import college.root.viit2.EventDetailsActivity;
import college.root.viit2.R;

/**
 * Created by root on 26/11/16.
 */

public class MyHolder extends RecyclerView.ViewHolder {


    TextView tvTitle , tvDesc , tvTime;
    ImageView imageView;
    Button btnDetails;
    Context context;


    public MyHolder(final Context context, View itemView) {
        super(itemView);

        tvDesc = (TextView) itemView.findViewById(R.id.tvdesc);
        tvTitle = (TextView) itemView.findViewById(R.id.tvtitle);
        imageView = (ImageView)itemView.findViewById(R.id.imageLoad);
        btnDetails = (Button)itemView.findViewById(R.id.btnDetails);
        tvTime = (TextView)itemView.findViewById(R.id.tvTime);

        this.context = context;




        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context , EventDetailsActivity.class));


            }
        });


    }
}
