package college.root.viit2.PerceptionRecycler;

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

/**
 * Created by root on 6/12/16.
 */

public class HolderP extends RecyclerView.ViewHolder {



    TextView tvTitle , tvDesc , tvTime;
    ImageView imageView;
    Button btnDetails;
    Context context;
    String TAG ="Test";


    public HolderP(final Context context, View itemView) {
        super(itemView);


        tvDesc = (TextView) itemView.findViewById(R.id.tvdescP);
        tvTitle = (TextView) itemView.findViewById(R.id.tvtitleP);
        imageView = (ImageView)itemView.findViewById(R.id.imageP);
        btnDetails = (Button)itemView.findViewById(R.id.btnDetailsP);
        tvTime = (TextView)itemView.findViewById(R.id.tvTimeP);
        this.context = context;




        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Title is "+tvTitle.getText().toString());
                Log.d(TAG, "onClick: Desc is "+tvDesc.getText().toString());
                context.startActivity(new Intent(context , EventDetailsActivity.class));


            }
        });


    }


    }

