package college.root.viit2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import college.root.viit2.Realm.RealmHelper;
import college.root.viit2.Realm.UserInfo;
import io.realm.Realm;

public class EventDetailsActivity extends AppCompatActivity {

    TextView tvTitle , tvDesc;
    Button btnReg , btnComp;
    AlertDialog.Builder dialog;
    RealmHelper helper;
    Realm realm;
    TextView tvName , tvBranch , tvYear , tvGr , tvcontact;
    String TAG = "Test";
    UserInfo info;
    DatabaseReference mdatabase ,mregister;
    AlertDialog alertDialog;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Intent i = getIntent();
        String title = i.getStringExtra("Title");
        final String desc = i.getStringExtra("Desc");
        final String pid = i.getStringExtra("pid");
        tvTitle = (TextView)findViewById(R.id.textViewTitle);
        tvDesc = (TextView)findViewById(R.id.textView3);
        btnReg = (Button) findViewById(R.id.btnRegister);
        btnComp = (Button)findViewById(R.id.btnComp);

        tvTitle.setText(title);
        tvDesc.setText(desc);




        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailsActivity.this , RegisterActivity.class);
                intent.putExtra("pid" , pid);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein , R.anim.slideout);


            }
        });


        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventDetailsActivity.this , UsersActivity.class);
                i.putExtra("pid" , pid);
                startActivity(i);
                overridePendingTransition(R.anim.slidein , R.anim.slideout);


            }
        });

    }
}
