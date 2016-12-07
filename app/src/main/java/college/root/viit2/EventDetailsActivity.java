package college.root.viit2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity {

    TextView tvTitle , tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent i = getIntent();
        String title = i.getStringExtra("Title");
        String desc = i.getStringExtra("Desc");

        tvTitle = (TextView)findViewById(R.id.textViewTitle);
        tvDesc = (TextView)findViewById(R.id.textView3);

        tvTitle.setText(title);
        tvDesc.setText(desc);
    }
}
