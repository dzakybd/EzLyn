package id.ac.its.ezlyn.model;

import android.graphics.Color;
import android.util.Log;

import id.ac.its.ezlyn.R;

/**
 * Created by Ilham Aulia Majid on 19-Apr-17.
 */

public class LynType {

    public static final LynType O = new LynType("O", R.color.textLynO, R.color.backgroundLynO, "Keputih", "JMP", 5500);
    public static final LynType WK = new LynType("WK", R.color.textLynWk, R.color.backgroundLynWk, "Keputih", "Oso", 6000);
    public static final LynType S = new LynType("S", R.color.textLynS, R.color.backgroundLynS, "Kenjeran", "Bratang", 5000);

    private String code;
    private int textColor;
    private int backgroundColor;
    private String startPoint;
    private String endPoint;
    private int fee;

    public LynType(String code, int textColor, int backgroundColor, String startPoint, String endPoint, int fee) {
        this.code = code;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.fee = fee;
    }

    public String getCode() {
        return code;
    }

    public int getTextColor() {
        Log.d("text", String.valueOf(textColor));
        return textColor;
    }

    public int getBackgroundColor() {
        Log.d("text", String.valueOf(backgroundColor));
        return backgroundColor;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getFee() {
        return String.format("IDR %s", fee);
    }
}
