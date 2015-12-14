package com.cablush.cablushandroidapp.view.drawer;

/**
 * Created by oscar on 13/12/15.
 */
public interface DrawerItem {

    int getId();
    String getTitle();
    int getType();
    boolean isEnabled();
    boolean updateActionBarTitle();

}
