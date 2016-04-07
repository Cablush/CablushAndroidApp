package com.cablush.cablushapp.view.drawer;

/**
 * Created by oscar on 13/12/15.
 */
public class DrawerActivityConfiguration {

    private int mainLayout;
    private int drawerLayoutId;
    private int toolbarId;
    private int navigationId;
    private int headerId;
    private int drawerOpenDesc;
    private int drawerCloseDesc;
    private int mainDrawerContent;
    private int optionalDrawerContent;

    public int getMainLayout() {
        return mainLayout;
    }

    public void setMainLayout(int mainLayout) {
        this.mainLayout = mainLayout;
    }

    public int getDrawerLayoutId() {
        return drawerLayoutId;
    }

    public void setDrawerLayoutId(int drawerLayoutId) {
        this.drawerLayoutId = drawerLayoutId;
    }

    public int getToolbarId() {
        return toolbarId;
    }

    public void setToolbarId(int toolbarId) {
        this.toolbarId = toolbarId;
    }

    public int getNavigationId() {
        return navigationId;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public void setNavigationId(int navigationId) {
        this.navigationId = navigationId;
    }

    public int getDrawerOpenDesc() {
        return drawerOpenDesc;
    }

    public void setDrawerOpenDesc(int drawerOpenDesc) {
        this.drawerOpenDesc = drawerOpenDesc;
    }

    public int getDrawerCloseDesc() {
        return drawerCloseDesc;
    }

    public void setDrawerCloseDesc(int drawerCloseDesc) {
        this.drawerCloseDesc = drawerCloseDesc;
    }

    public int getMainDrawerContent() {
        return mainDrawerContent;
    }

    public void setMainDrawerContent(int mainDrawerContent) {
        this.mainDrawerContent = mainDrawerContent;
    }

    public int getOptionalDrawerContent() {
        return optionalDrawerContent;
    }

    public void setOptionalDrawerContent(int optionalDrawerContent) {
        this.optionalDrawerContent = optionalDrawerContent;
    }
}
