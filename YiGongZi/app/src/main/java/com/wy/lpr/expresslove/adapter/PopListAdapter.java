package com.wy.lpr.expresslove.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wy.lpr.expresslove.R;

@SuppressLint({"ViewHolder", "InflateParams"})
public class PopListAdapter extends BaseAdapter {

    private TextView mList_text;
    private List<String> mListStr;
    private Context mContext;
    private LayoutInflater mInflater;


    public PopListAdapter(Context context, List<String> listStr) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListStr = listStr;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListStr.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = mInflater.inflate(R.layout.listview_list, null);
        mList_text = (TextView) convertView.findViewById(R.id.list_text);
        mList_text.setText(mListStr.get(position));
        return convertView;

    }

}
