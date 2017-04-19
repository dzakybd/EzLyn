package id.ac.its.ezlyn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.its.ezlyn.R;
import id.ac.its.ezlyn.adapter.ItemClickSupport;
import id.ac.its.ezlyn.adapter.LynRecyclerViewAdapter;
import id.ac.its.ezlyn.model.Lyn;
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

        ItemClickSupport.addTo(rvLynList).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        AlertDialog.Builder pilihan = new AlertDialog.Builder(LynListActivity.this);
                        pilihan.setMessage("Anda ingin menunggu "+lyns.get(position).getPlate()+" ?");
                        pilihan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(LynListActivity.this, Track.class));
                                finish();
                            }
                        });
                        pilihan.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alert = pilihan.create();
                        alert.show();

                    }
                }
        );
    }


    private void initializeData(){
        lyns = new ArrayList<>();
        lyns.add(new Lyn(LynType.S,"L 1234 A", 20, "Full"));
        lyns.add(new Lyn(LynType.WK,"L 1234 A", 20, "Full"));
        lyns.add(new Lyn(LynType.O,"L 1234 A", 20, "Full"));
        lyns.add(new Lyn(LynType.S,"L 1234 A", 20, "Full"));
        lyns.add(new Lyn(LynType.O,"L 1234 A", 20, "Full"));
    }
}
