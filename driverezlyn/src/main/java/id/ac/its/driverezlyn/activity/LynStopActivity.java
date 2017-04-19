package id.ac.its.driverezlyn.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.its.driverezlyn.R;
import id.ac.its.driverezlyn.adapter.LynStopRecyclerViewAdapter;
import id.ac.its.driverezlyn.model.LynStop;

public class LynStopActivity extends AppCompatActivity {

    @BindView(R.id.rv_lyn_stop)
    RecyclerView rvLynStop;

    List<LynStop> lynStops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyn_stop);
        ButterKnife.bind(this);

        initializeData();

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvLynStop.setLayoutManager(llm);
        rvLynStop.setHasFixedSize(true);

        LynStopRecyclerViewAdapter adapter = new LynStopRecyclerViewAdapter(getApplicationContext(), lynStops);
        rvLynStop.setAdapter(adapter);

        Button peta = (Button) findViewById(R.id.button3);
        peta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LynStopActivity.this,Track.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeData(){
        lynStops = new ArrayList<>();
        lynStops.add(new LynStop("Halte Hangtuah", 10));
        lynStops.add(new LynStop("Halte ITATS", 8));
        lynStops.add(new LynStop("Halte Manyar", 12));
    }
}
