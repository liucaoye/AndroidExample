package com.example.app.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.contacts.model.ContactItem;

import java.util.List;

/**
 * @author LIUYAN
 * @date 2015/12/1
 * @time 16:31
 */
public class ContactsAdapter extends ArrayAdapter<ContactItem> {

    private int layoutId;

    public ContactsAdapter(Context context, int resource, List<ContactItem> objects) {
        super(context, 0, objects);
        layoutId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setItem(getItem(position));
        return convertView;
    }

    class ViewHolder {
        private TextView nameTv;
        private TextView phoneTv;

        public ViewHolder(View view) {
            this.nameTv = (TextView) view.findViewById(R.id.name);
            this.phoneTv = (TextView) view.findViewById(R.id.phone);
        }

        public void setItem(ContactItem item) {
            nameTv.setText(item.getName());
            phoneTv.setText(item.getPhone());
        }
    }
}
