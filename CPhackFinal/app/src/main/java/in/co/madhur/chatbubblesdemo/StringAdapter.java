package in.co.madhur.chatbubblesdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by victor.li on 23/10/2016.
 */

public class StringAdapter extends BaseAdapter {

    private List<String> stringList;
    private Context context;

    public StringAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int i) {
        return stringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;
        ViewHolder holder1;

        if (view == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_string, null, false);
            holder1 = new ViewHolder();

            holder1.tv = (TextView) v.findViewById(R.id.tvString);

            v.setTag(holder1);

        } else {

            v = view;
            holder1 = (ViewHolder) v.getTag();

        }

        holder1.tv.setText(stringList.get(i));

        return v;
    }

    private class ViewHolder {
        public TextView tv;
    }
}
