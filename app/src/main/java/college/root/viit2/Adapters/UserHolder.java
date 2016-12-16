package college.root.viit2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import college.root.viit2.R;

/**
 * Created by root on 8/12/16.
 */

public class UserHolder extends RecyclerView.ViewHolder {

    Context context ;
    ImageView imageView;
    TextView displayname,branch,year,contact ,cnt ,email;


    public UserHolder(Context context , View itemView) {
        super(itemView);
        this.context = context;
        imageView=(ImageView)itemView.findViewById(R.id.Profile);
        displayname=(TextView)itemView.findViewById(R.id.tvDisplayName);
        branch=(TextView) itemView.findViewById(R.id.tvbranchname);
        year=(TextView)itemView.findViewById(R.id.yeartv);
        contact=(TextView)itemView.findViewById(R.id.cnt);
        cnt=(TextView)itemView.findViewById(R.id.contact);
        email=(TextView)itemView.findViewById(R.id.tvEmail);

    }
}
