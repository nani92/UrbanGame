package com.example.nataliajastrzebska.urbangame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nataliajastrzebska on 11/11/15.
 */
public class TaskListAdapter extends ArrayAdapter<String> {

    String[] values;
    Context context;

    public TaskListAdapter(Context context, String[] values){
        super(context, -1, values);
        this.values = values;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_list_element, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tv_taskElement_taskName);
        textView.setText(values[position]);
        return rowView;
    }
}
