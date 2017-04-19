package id.ac.its.ezlyn.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.its.ezlyn.model.Lyn;
import id.ac.its.ezlyn.R;
import id.ac.its.ezlyn.adapter.LynRecyclerViewAdapter;
import id.ac.its.ezlyn.model.LynType;

public class LynListActivity extends AppCompatActivity {

    @BindView(R.id.rv_lyn_list)
    RecyclerView rvLynList;

    List<Lyn> lyns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyn_list);
        ButterKnife.bind(this);

        initializeData();

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvLynList.setLayoutManager(llm);
        rvLynList.setHasFixedSize(true);

        LynRecyclerViewAdapter adapter = new LynRecyclerViewAdapter(getApplicationContext(), lyns);
        rvLynList.setAdapter(adapter);
    }

    private void initializeData(){
        lyns = new ArrayList<>();
        lyns.add(new Lyn(LynType.S,"L 1234 A", "20 min", "Full"));
        lyns.add(new Lyn(LynType.WK,"L 1234 A", "20 min", "Full"));
        lyns.add(new Lyn(LynType.O,"L 1234 A", "20 min", "Full"));
        lyns.add(new Lyn(LynType.S,"L 1234 A", "20 min", "Full"));
        lyns.add(new Lyn(LynType.O,"L 1234 A", "20 min", "Full"));
    }
}
