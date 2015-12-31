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
        View view = null;
        DrawerItem menuItem = this.getItem(position);
        switch (menuItem.getType()) {
            case DrawerMenuHeader.HEADER_TYPE:
                view = getHeaderView(convertView, parent, menuItem);
                break;
            case DrawerMenuSection.SECTION_TYPE:
                view = getSectionView(convertView, parent, menuItem);
                break;
            case DrawerMenuItem.ITEM_TYPE:
                view = getItemView(convertView, parent, menuItem);
                break;
        }
        return view;
    }

    private View getItemView(View convertView, ViewGroup parentView, DrawerItem drawerItem) {
        DrawerMenuItem drawerMenuItem = (DrawerMenuItem) drawerItem;
        MenuItemHolder menuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_item, parentView, false);
            TextView titleView = (TextView) convertView.findViewById(R.id.title);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.icon);

            menuItemHolder = new MenuItemHolder();
            menuItemHolder.titleView = titleView;
            menuItemHolder.iconView = iconView;

            convertView.setTag(menuItemHolder);
        }

        if (menuItemHolder == null) {
            menuItemHolder = (MenuItemHolder) convertView.getTag();
        }

        menuItemHolder.titleView.setText(drawerMenuItem.getTitle());
        menuItemHolder.iconView.setImageResource(drawerMenuItem.getIcon());
        menuItemHolder.iconView.setContentDescription(drawerMenuItem.getTitle());

        return convertView;
    }

    private View getSectionView(View convertView, ViewGroup parentView, DrawerItem drawerItem) {
        DrawerMenuSection drawerMenuSection = (DrawerMenuSection) drawerItem;
        MenuSectionHolder menuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_section, parentView, false);
            TextView titleView = (TextView) convertView.findViewById(R.id.title);

            menuItemHolder = new MenuSectionHolder();
            menuItemHolder.titleView = titleView;
            convertView.setTag(menuItemHolder);
        }

        if (menuItemHolder == null) {
            menuItemHolder = (MenuSectionHolder) convertView.getTag();
        }

        menuItemHolder.titleView.setText(drawerMenuSection.getTitle());

        return convertView;
    }

    private View getHeaderView(View convertView, ViewGroup parentView, DrawerItem drawerItem) {
        DrawerMenuHeader drawerMenuHeader = (DrawerMenuHeader) drawerItem;
        MenuHeaderHolder menuHeaderHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_header, parentView, false);
            TextView nameView = (TextView) convertView.findViewById(R.id.name);
            TextView emailView = (TextView) convertView.findViewById(R.id.email);

            menuHeaderHolder = new MenuHeaderHolder();
            menuHeaderHolder.nameView = nameView;
            menuHeaderHolder.emailView = emailView;
            convertView.setTag(menuHeaderHolder);
        }

        if (menuHeaderHolder == null) {
            menuHeaderHolder = (MenuHeaderHolder) convertView.getTag();
        }

        if (!drawerMenuHeader.isEnabled()) {
            menuHeaderHolder.nameView.setText(drawerMenuHeader.getName());
            menuHeaderHolder.emailView.setText(drawerMenuHeader.getEmail());
            menuHeaderHolder.emailView.setVisibility(View.VISIBLE);
        } else {
            menuHeaderHolder.nameView.setText(R.string.drawer_item_login_register);
            menuHeaderHolder.emailView.setText(drawerMenuHeader.getEmail());
            menuHeaderHolder.emailView.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }

    private static class MenuItemHolder {
        private TextView titleView;
        private ImageView iconView;
    }

    private class MenuSectionHolder {
        private TextView titleView;
    }

    private class MenuHeaderHolder {
        private TextView nameView;
        private TextView emailView;
    }
}