package id.ac.its.driverezlyn;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import static id.ac.its.driverezlyn.R.layout.activity_main;
import static id.ac.its.driverezlyn.R.layout.activity_regis_lyn;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        textView = (TextView)findViewById(R.id.result_text);
        textView.setVisibility(View.INVISIBLE);
    }

    public void changeFullState(View view){
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked){
            textView.setBackgroundColor(Color.RED);
        }
        else{
            textView.setBackgroundColor(Color.GRAY);
        }
    }

    public void changeToList(View v){
        startActivity(new Intent(MainActivity.this, regis_lyn.class));
    }
}
