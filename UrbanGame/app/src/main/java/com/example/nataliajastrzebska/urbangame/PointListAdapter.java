package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nataliajastrzebska on 01/11/15.
 */
public class PointListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<PointListItem> mPointItems;

    public PointListAdapter(Context context, ArrayList<PointListItem> pointListItems) {
        mContext = context;
        mPointItems = pointListItems;
    }

    @Override
    public int getCount() {
        return mPointItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mPointItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.point_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.pointItem_name);

        titleView.setText( mPointItems.get(position).mTitle );

        return view;
    }
}
