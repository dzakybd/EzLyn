package id.ac.its.ezlyn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Ilham Aulia Majid on 18-May-17.
 */

public class TutorialActivity extends AppIntro {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int bgColor =  Color.parseColor("#ffdc5a");
        int blackColor = Color.parseColor("#212121");
        int black2Color = Color.parseColor("#424242");

        addSlide(AppIntroFragment.newInstance("Pilih halte", "Silakan memilih salah satu halte yang paling dekat dengan anda, lalu tekan logo halte tersebut", R.drawable.tutor_sub_1, bgColor, blackColor, blackColor));
        addSlide(AppIntroFragment.newInstance("Konfirmasi menunggu", "Tekan deskripsi helte tersebut untuk konfirmasi kalau anda akan menunggu angkot di halte tersebut", R.drawable.tutor_sub_2, bgColor, blackColor, blackColor));
        addSlide(AppIntroFragment.newInstance("Angkot tersedia", "Sistem akan menampilkan beberapa angkot yang tersedia", R.drawable.tutor_sub_3, bgColor, blackColor, blackColor));
        addSlide(AppIntroFragment.newInstance("Langkah terakhir", "Sistem akan menampilkan deskripsi dari angkot yang sudah anda pilih.\n Lalu tekan Selesai", R.drawable.tutor_sub_4, bgColor, blackColor, blackColor));

        showSkipButton(false);
        setColorDoneText(blackColor);
        setIndicatorColor(blackColor, black2Color);
        setNextArrowColor(blackColor);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }
}
