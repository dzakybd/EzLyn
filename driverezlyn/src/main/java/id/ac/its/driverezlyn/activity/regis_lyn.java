package id.ac.its.driverezlyn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import id.ac.its.driverezlyn.R;

public class regis_lyn extends AppCompatActivity {
    public EditText in_plat;
    public Spinner in_kode;
    public String no_plat, kode;
    private Button btn;

    String[] kode_lyn = {"S","WK","O"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_lyn);

        Spinner mySpinner = (Spinner)findViewById(R.id.kodeAngkot);

        ArrayAdapter<String> myAdapt = new ArrayAdapter<String> (this ,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.list_kode));
        myAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapt);

        in_plat = (EditText) findViewById(R.id.plat_nomor);
        in_kode = (Spinner) findViewById(R.id.kodeAngkot);
        btn = (Button)findViewById(R.id.next_regis);
    }

    public void goRegis(View v){
        no_plat = in_plat.getText().toString().trim();
        kode = in_kode.getSelectedItem().toString();

        Intent i = new Intent(getApplicationContext(), validasi.class);
        i.putExtra("lyn_code", kode);
        i.putExtra("plate_code", no_plat);
        startActivity(i);
        finish();
    }
}
