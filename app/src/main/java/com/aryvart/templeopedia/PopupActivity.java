package com.aryvart.templeopedia;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aryvart.templeopedia.genericclasses.GeneralData;

/**
 * Created by android1 on 6/9/16.
 */
public class PopupActivity extends Activity {
    Context context;
    View itemView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        GeneralData.context = this;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        itemView1 = LayoutInflater.from(context)
                .inflate(R.layout.user_current_loc_addr_alert, null);
        final AlertDialog altDialog = builder.create();
        altDialog.setView(itemView1);
        altDialog.show();
        TextView txt_msg = (TextView) itemView1.findViewById(R.id.text_address);
        txt_msg.setText("Please check your Internet Connection!!");
    }
}
