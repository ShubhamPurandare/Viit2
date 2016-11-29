package college.root.viit2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import college.root.viit2.Realm.Data;

/**
 * Created by root on 25/11/16.
 */

public class CustomAdapter extends RecyclerView.Adapter<MyHolder> {
    Context context;
    ArrayList<Data> arrayList;


    public CustomAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single , parent , false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Data data = arrayList.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvDesc.setText(data.getDesc());
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
