package com.cablush.cablushandroidapp.view.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushandroidapp.R;

/**
 * Created by jonathan on 02/11/15.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    private LayoutInflater inflater;

    public DrawerAdapter(Context context, int textViewResourceId, DrawerItem[] objects) {
        super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        DrawerItem menuItem = this.getItem(position);
        if (menuItem.getType() == DrawerMenuItem.ITEM_TYPE) {
            view = getItemView(convertView, parent, menuItem );
        } else {
            view = getSectionView(convertView, parent, menuItem);
        }
        return view;
    }

    private View getItemView(View convertView, ViewGroup parentView, DrawerItem drawerItem) {

        DrawerMenuItem drawerMenuItem = (DrawerMenuItem) drawerItem;
        NavMenuItemHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate( R.layout.drawer_item, parentView, false);
            TextView titleView = (TextView) convertView.findViewById(R.id.title);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.icon);

            navMenuItemHolder = new NavMenuItemHolder();
            navMenuItemHolder.titleView = titleView;
            navMenuItemHolder.iconView = iconView;

            convertView.setTag(navMenuItemHolder);
        }

        if (navMenuItemHolder == null) {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }

        navMenuItemHolder.titleView.setText(drawerMenuItem.getTitle());
        navMenuItemHolder.iconView.setImageResource(drawerMenuItem.getIcon());

        return convertView;
    }

    private View getSectionView(View convertView, ViewGroup parentView, DrawerItem drawerItem) {

        DrawerMenuSection drawerMenuSection = (DrawerMenuSection) drawerItem;
        NavMenuSectionHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_section, parentView, false);
            TextView titleView = (TextView) convertView.findViewById(R.id.title);

            navMenuItemHolder = new NavMenuSectionHolder();
            navMenuItemHolder.titleView = titleView ;
            convertView.setTag(navMenuItemHolder);
        }

        if (navMenuItemHolder == null) {
            navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
        }

        navMenuItemHolder.titleView.setText(drawerMenuSection.getTitle());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }

    private static class NavMenuItemHolder {
        private TextView titleView;
        private ImageView iconView;
    }

    private class NavMenuSectionHolder {
        private TextView titleView;
    }
}