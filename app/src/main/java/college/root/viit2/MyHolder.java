package college.root.viit2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by root on 26/11/16.
 */

public class MyHolder extends RecyclerView.ViewHolder {


    TextView tvTitle , tvDesc;

    public MyHolder(View itemView) {
        super(itemView);

        tvDesc = (TextView) itemView.findViewById(R.id.tvdesc);
        tvTitle = (TextView) itemView.findViewById(R.id.tvtitle);


    }
}
