package com.aryvart.templeopedia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aryvart.templeopedia.Interface.TempleList;
import com.aryvart.templeopedia.R;
import com.aryvart.templeopedia.TempleSearchDetails;
import com.aryvart.templeopedia.bean.TempleListBean;

import java.util.ArrayList;

/**
 * Created by Android2 on 8/2/2016.
 */
public class TempleSearchDetailAdapter extends BaseAdapter {
    ArrayList<TempleListBean> alBean;
    Context context;
    TempleListBean templeListBean;
    TempleList templeList;

    public TempleSearchDetailAdapter(Context cont, ArrayList<TempleListBean> providerBean, TempleList list) {
        this.context = cont;
        this.alBean = providerBean;
        templeList = list;

    }

    @Override
    public int getCount() {
        return alBean.size();
    }

    @Override
    public TempleListBean getItem(int position) {
        return alBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewHolder view;
            if (convertView == null) {
                view = new ViewHolder();

                convertView = inflater.inflate(R.layout.search_result_detail_content, parent, false);

                // get layout from mobile.xml
                view.txt_temple_name = (TextView) convertView.findViewById(R.id.txt_temple_name);
                view.txt_temple_address = (TextView) convertView.findViewById(R.id.txt_temple_address);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, "You Clicked--> " + position, Toast.LENGTH_LONG).show();
                        TempleListBean tB = alBean.get(position);
                        Log.i("AD",tB.getTemple_name());
                        Log.i("AD",tB.getTemple_id());
                        templeList.getTempleName(tB.getTemple_name(),tB.getTemple_id(),tB.getTemple_address());
                    }
                });

                convertView.setTag(view);

            } else {
                view = (ViewHolder) convertView.getTag();

            }
            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.color.orangeLight);

            } else {
                convertView.setBackgroundResource(R.color.white);
            }
            templeListBean = alBean.get(position);
            view.txt_temple_name.setText(templeListBean.getTemple_name());
            view.txt_temple_address.setText(templeListBean.getTemple_address());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    class ViewHolder {

        public TextView txt_temple_name;
        public TextView txt_temple_address;

    }
}