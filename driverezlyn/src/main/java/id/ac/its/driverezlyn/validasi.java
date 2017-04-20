package id.ac.its.driverezlyn;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.its.driverezlyn.activity.LynStopActivity;
import id.ac.its.driverezlyn.activity.MainActivity;

public class validasi extends AppCompatActivity {

    // regis_lyn reg_data = new regis_lyn();
    //private ProgressBar prg;
    CountDownTimer cnt;

    public String plat_nomor, kode_lyn, trayek_nama;
    @BindView(R.id.next_valid)
    Button nextValid;
    @BindView(R.id.show_plat)
    TextView showPlat;
    @BindView(R.id.show_kode)
    TextView showKode;
    private TextView plat_out, kode_out, trayek_out;
    private int i = 0;

    Bundle extras;

    String[] trayek = {"Jembatan Merah – Univ Hang Tuah – Keputih", "Joyoboyo – Bratang – Kenjeran", "Kenjeran - Keputih - Terminal - Bratang PP"};
    String[] kode_asli = {"O", "S", "WK"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validasi);
        ButterKnife.bind(this);
        showKode.setText(getIntent().getExtras().getString("lyn_code"));
        showPlat.setText(getIntent().getExtras().getString("plate_code"));
        nextValid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(validasi.this, LynStopActivity.class));
                finish();
            }
        });
        //prg.setBackgroundColor(Color.YELLOW);
        init();
        //setProgress();

        //setContentView(plat_out);
        //setContentView(kode_out);
        //setContentView(trayek_out);
    }

    /*
    private void setProgress(){
        prg.setProgress(i);
        cnt = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                prg.setProgress(i);
            }

            @Override
            public void onFinish() {
                i++;
                prg.setProgress(i);
            }
        };
    }
    */

    public void LanjutDasbor(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void balikIsi(View v) {
        startActivity(new Intent(this, regis_lyn.class));
    }

    public void init() {
        /*
        if(extras != null){
            plat_nomor = extras.getString("plate_code");
            kode_lyn = extras.getString("lyn_code");
        }
        */
        plat_out = (TextView) findViewById(R.id.show_plat);
        kode_out = (TextView) findViewById(R.id.show_kode);
        trayek_out = (TextView) findViewById(R.id.show_trayek);
        //prg = (ProgressBar)findViewById(R.id.Hiasan);

        //   plat_nomor = reg_data.no_plat;
        //   kode_lyn = reg_data.kode;

        //   plat_out.setText(plat_nomor);
        //   kode_out.setText(kode_lyn);

        if (kode_lyn == "O") {
            trayek_nama = trayek[0];
        } else if (kode_lyn == "S") {
            trayek_nama = trayek[1];
        } else if (kode_lyn == "WK") {
            trayek_nama = trayek[2];
        }
        trayek_out.setText(trayek_nama);
    }
}
