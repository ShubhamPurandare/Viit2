package college.root.viit2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import college.root.viit2.EventDetailsActivity;
import college.root.viit2.EventsDetailsAct;
import college.root.viit2.R;
import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by root on 26/11/16.
 */

public class MyHolder extends RecyclerView.ViewHolder {


    TextView tvTitle , tvDesc , tvTime , tvPid , tvDept;
    ImageView imageView , imgStar;
    Context context;
    String TAG ="Test";
    RealmHelper helper;
    Data data;
    Realm realm;
    Button button;
    TextView tvRegister, tvContact;

    public MyHolder(final Context context, View itemView) {
        super(itemView);

        tvDesc = (TextView) itemView.findViewById(R.id.tvdesc);
        tvTitle = (TextView) itemView.findViewById(R.id.tvtitle);
        imageView = (ImageView)itemView.findViewById(R.id.imageLoad);
        tvTime = (TextView)itemView.findViewById(R.id.tvTime);
        tvPid = (TextView)itemView.findViewById(R.id.pid);
        tvDept = (TextView)itemView.findViewById(R.id.tvDept);
        button = (Button)itemView.findViewById(R.id.btnCompetition);
        tvRegister = (TextView)itemView.findViewById(R.id.tvIsRegistered);
        imgStar = (ImageView) itemView.findViewById(R.id.star);
        tvContact = (TextView)itemView.findViewById(R.id.tvContact);
        imgStar.setVisibility(View.GONE);
        tvRegister.setVisibility(View.GONE);

        this.context = context;




        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = new Data();
                data= null;
                RealmConfiguration configuration = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
                Realm.setDefaultConfiguration(configuration);
                Log.d(TAG , "Realm set");
                realm = Realm.getDefaultInstance();
                helper = new RealmHelper(realm);
                data = helper.retriveFromPid(Integer.parseInt(tvPid.getText().toString()));
                Log.d(TAG, "onClick: data reurned something");
                if (data != null){

                    String title = data.getTitle();
                    String desc = data.getDesc();
                    Log.d(TAG, "onClick: Title is "+title);
                    Log.d(TAG, "onClick: Desc is "+desc);
                    Log.d(TAG, "onClick: Pid is "+tvPid.getText().toString());




                    Intent i = new Intent(context , EventsDetailsAct.class);
                    i.putExtra("Title" , title);
                    i.putExtra("Desc" , desc);
                    //.startAnimation(android.R.anim.fade_in ,android.R.anim.fade_out );
                    i.putExtra("pid" , tvPid.getText().toString());
                    context.startActivity(i);


                }else{
                    Log.d(TAG, "onClick: data is null");
                }



            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                data = new Data();
                data= null;
                RealmConfiguration configuration = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
                Realm.setDefaultConfiguration(configuration);
                Log.d(TAG , "Realm set");
                realm = Realm.getDefaultInstance();
                helper = new RealmHelper(realm);
                data = helper.retriveFromPid(Integer.parseInt(tvPid.getText().toString()));
                Log.d(TAG, "onClick: data reurned something");
                if (data != null){

                    String title = data.getTitle();
                    String desc = data.getDesc();
                    Log.d(TAG, "onClick: Title is "+title);
                    Log.d(TAG, "onClick: Desc is "+desc);
                    Log.d(TAG, "onClick: Pid is "+tvPid.getText().toString());

                    Intent i = new Intent(context , EventsDetailsAct.class);
                    i.putExtra("Title" , title);
                    i.putExtra("Desc" , desc);
                    i.putExtra("pid" , tvPid.getText().toString());
                    context.startActivity(i);


                }else{
                    Log.d(TAG, "onClick: data is null");
                }




            }
        });

    }
}
