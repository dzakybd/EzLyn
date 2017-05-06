package id.ac.its.driverezlyn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import id.ac.its.driverezlyn.R;

public class LynStopActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyn_stop);
        ButterKnife.bind(this);
        Button peta = (Button) findViewById(R.id.button3);
        peta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LynStopActivity.this,Track.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
