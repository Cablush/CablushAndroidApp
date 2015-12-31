package com.cablush.cablushandroidapp.view.drawer;

import android.content.Context;

/**
 * Created by oscar on 13/12/15.
 */
public class DrawerMenuItem implements DrawerItem {

    public static final int ITEM_TYPE = 2;

    private int id;
    private String title;
    private int icon;
    private boolean updateActionBarTitle;

    private DrawerMenuItem() {
    }

    public static DrawerMenuItem create(int id, String title, String icon, boolean updateActionBarTitle, Context context) {
        DrawerMenuItem item = new DrawerMenuItem();
        item.setId(id);
        item.setTitle(title);
        item.setIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
        item.setUpdateActionBarTitle(updateActionBarTitle);
        return item;
    }

    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean updateActionBarTitle() {
        return this.updateActionBarTitle;
    }

    public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
        this.updateActionBarTitle = updateActionBarTitle;
    }
}
