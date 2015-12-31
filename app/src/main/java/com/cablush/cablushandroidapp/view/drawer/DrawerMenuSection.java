package com.cablush.cablushandroidapp.view.drawer;

/**
 * Created by oscar on 13/12/15.
 */
public class DrawerMenuSection implements DrawerItem {

    public static final int SECTION_TYPE = 1;

    private int id;
    private String title;

    private DrawerMenuSection() {
    }

    public static DrawerMenuSection create(int id, String title) {
        DrawerMenuSection section = new DrawerMenuSection();
        section.setId(id);
        section.setTitle(title);
        return section;
    }

    @Override
    public int getType() {
        return SECTION_TYPE;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }
}
