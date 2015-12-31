package com.cablush.cablushandroidapp.view.drawer;

import com.cablush.cablushandroidapp.model.domain.Usuario;

/**
 * Created by oscar on 13/12/15.
 */
public class DrawerMenuHeader implements DrawerItem {

    public static final int HEADER_TYPE = 0;

    private int id;

    private DrawerMenuHeader() {
    }

    public static DrawerMenuHeader create(int id) {
        DrawerMenuHeader header = new DrawerMenuHeader();
        header.setId(id);
        return header;
    }

    @Override
    public int getType() {
        return HEADER_TYPE;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return Usuario.LOGGED_USER == null;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Usuario.LOGGED_USER != null ? Usuario.LOGGED_USER.getNome() : null;
    }

    public String getEmail() {
        return Usuario.LOGGED_USER != null ? Usuario.LOGGED_USER.getEmail() : null;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }
}
