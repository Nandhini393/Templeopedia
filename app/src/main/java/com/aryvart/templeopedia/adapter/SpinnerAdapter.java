package com.aryvart.templeopedia.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aryvart.templeopedia.R;
import com.aryvart.templeopedia.bean.TempleListBean;


import java.util.ArrayList;

/**
 * To load the Spinner values using this Adapter
 * Created by Rajaji on 14-04-2016.
 */
public class SpinnerAdapter extends ArrayAdapter<TempleListBean> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    // private SpinnerBean[] values;

    ArrayList<TempleListBean> alSpinBean;

    public SpinnerAdapter(Context context, int textViewResourceId,
                          ArrayList<TempleListBean> alSpin) {
        super(context, textViewResourceId, alSpin);
        this.context = context;
        this.alSpinBean = alSpin;
    }

    public int getCount() {
        return alSpinBean.size();
    }

    public TempleListBean getItem(int position) {
        return alSpinBean.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv;

    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        try {
            Holder holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.spin_item, null);
            holder.tv = (TextView) rowView.findViewById(R.id.spin_text);
if(position==0){
    holder.tv.setHint("select country");
}
            if(position==1){
                holder.tv.setHint("select state");
            }
            if(position==2){
                holder.tv.setHint("select city");
            }
            if(position==3){
                holder.tv.setHint("select festival");
            }
            if(position==4){
                holder.tv.setHint("select diety");
            }

            TempleListBean sB = alSpinBean.get(position);
            holder.tv.setTextColor(Color.BLACK);
            Log.i("BB", "sB.get_spinid().toString() : " + sB.getTemple_name().toString());
            holder.tv.setText(sB.getTemple_name().toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowView;
    }

}