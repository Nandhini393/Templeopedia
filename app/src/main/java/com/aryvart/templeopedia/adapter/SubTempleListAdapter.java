package com.aryvart.templeopedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.Interface.TempleList;
import com.aryvart.templeopedia.R;
import com.aryvart.templeopedia.bean.TempleListBean;

import java.util.ArrayList;

/**
 * Created by Android2 on 8/2/2016.
 */
public class SubTempleListAdapter extends BaseAdapter {
    ArrayList<TempleListBean> alBean;
    Context context;
    TempleListBean templeListBean;
    TempleList templeList;

    public SubTempleListAdapter(Context cont, ArrayList<TempleListBean> providerBean, TempleList list) {
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

                convertView = inflater.inflate(R.layout.temple_list_content, parent, false);

                // get layout from mobile.xml
                view.txt_temple_name = (TextView) convertView.findViewById(R.id.txt_temple_name);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Toast.makeText(context, "You Clicked--> " + position, Toast.LENGTH_LONG).show();
                        TempleListBean tB = alBean.get(position);
                        templeList.getTempleName(tB.getTemple_name(),tB.getTemple_id());
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


        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    class ViewHolder {

        public TextView txt_temple_name;

    }
}