package college.root.viit2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import college.root.viit2.Realm.Data;
import college.root.viit2.Realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EventsDetailsAct extends AppCompatActivity {
    final Context context = this;
    private Button button , btnReg , btnComp;
    String TAG ="Test";
    Realm realm;
    Data data;
    String pid;

    TextView tvEventName , tvDesc, tvFee, tvParticipants, tvVenue , tvDate , tvTime , tvContact , tvHead , tvRounds , tvPrize1
            , tvPrize2 , tvExtra;
    Button btnRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        tvContact = (TextView)findViewById(R.id.tv_contact);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvTime = (TextView)findViewById(R.id.tv_time);
        tvHead = (TextView)findViewById(R.id.tv_eventHead);
        tvParticipants = (TextView)findViewById(R.id.tvParticipants);
        tvVenue = (TextView)findViewById(R.id.tv_venue);
        tvFee = (TextView)findViewById(R.id.fee_text);
        tvEventName = (TextView)findViewById(R.id.event_name);
        tvDesc = (TextView)findViewById(R.id.description_text);
        tvRounds = (TextView)findViewById(R.id.levels);
        tvPrize1 = (TextView)findViewById(R.id.prize1);
        tvPrize2 = (TextView)findViewById(R.id.prize2);
        tvExtra = (TextView)findViewById(R.id.details);
        btnReg = (Button)findViewById(R.id.btnRegister);
        btnComp = (Button)findViewById(R.id.btnComp);

         Intent i = getIntent();
        String title = i.getStringExtra("Title");
        String desc = i.getStringExtra("Desc");
        pid = i.getStringExtra("pid");
        RealmConfiguration configuration = new RealmConfiguration.Builder(EventsDetailsAct.this).deleteRealmIfMigrationNeeded().schemaVersion(4).build();
        Realm.setDefaultConfiguration(configuration);
        Log.d(TAG , "Realm set");
        realm = Realm.getDefaultInstance();
         RealmHelper helper = new RealmHelper(realm);
         data = new Data();
        data = helper.retriveFromPid(Integer.parseInt(pid));


        if(tvContact == null )
        Log.i(TAG, "onCreate: is null");
        else
            Log.i(TAG   , "onCreate: not null");


        tvContact.setText(data.getContactDetails());
        tvDate.setText(data.getDate());
        tvTime.setText(data.getTime());
        tvHead.setText(data.getContactDetails());
        tvParticipants.setText(data.getTeamSize());
        tvVenue.setText(data.getFees());
        tvEventName.setText(data.getTitle());
        tvDesc.setText(data.getDesc());
        tvRounds.setText(data.getRounds());
        tvPrize1.setText(data.getPrizes());
        tvPrize2.setText(data.getPrizes());
        tvExtra.setText(data.getExtra());



        button = (Button) findViewById(R.id.rulesbtn);

        // add button listener
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.rules);
                dialog.setTitle("RULES");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                //text.setText("");
                
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogbtn);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsDetailsAct.this , RegisterActivity.class);
                intent.putExtra("pid" , pid);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein , R.anim.slideout);


            }
        });


        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventsDetailsAct.this , UsersActivity.class);
                i.putExtra("pid" , pid);
                startActivity(i);
                overridePendingTransition(R.anim.slidein , R.anim.slideout);


            }
        });

    }
}
