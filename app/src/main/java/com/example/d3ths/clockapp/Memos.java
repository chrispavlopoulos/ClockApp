package com.example.d3ths.clockapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Chris on 8/31/2016.
 */
public class Memos extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_memos, container, false);

        TextView returnFromMemos = (TextView)view.findViewById(R.id.returnFromMemos);
        returnFromMemos.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override

                    public void onClick(View v){
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

                        ft.replace(R.id.container, getActivity().getSupportFragmentManager().findFragmentByTag("homeContent"));// Start the animated transition.
                        ft.commit();
                    }
                }
        );

        return view;

    }

    public static Memos getInstance() {
        Memos f = new Memos();

        return f;
    }

}
