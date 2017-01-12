package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chris on 1/11/2017.
 */
public class DialogEditText extends Dialog{

    public Activity c;
    public Dialog d;
    public ImageView ok, cancel;
    public EditText input;
    String msg;
    int inputLimit;

    public DialogEditText(Activity a, String msg, int inputLimit) {
        super(a);
        this.c = a;
        this.msg = msg;
        this.inputLimit = inputLimit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_text);
        ((TextView)findViewById(R.id.alertTitle)).setText(msg);
        input = (EditText)findViewById(R.id.input);
        input.setMaxLines(1);
        input.getBackground().setColorFilter(c.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
        ok = (ImageView) findViewById(R.id.alertOkButton);
        cancel = (ImageView) findViewById(R.id.alertCancelButton);
//        ok.setOnClickListener(this);
//        cancel.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.alertOkButton:
//                changed = true;
//                dismiss();
//                break;
//            case R.id.alertCancelButton:
//                dismiss();
//                break;
//            default:
//                break;
//        }
//        dismiss();
//    }


}
