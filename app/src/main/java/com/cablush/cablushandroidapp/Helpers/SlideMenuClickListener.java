package com.cablush.cablushandroidapp.Helpers;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.Usuario;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by jonathan on 04/11/15.
 */
public class SlideMenuClickListener implements ListView.OnItemClickListener {
    private Context context;
    public SlideMenuClickListener(Context ctx) {
        context = ctx;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // display view for selected nav drawer item
        displayView(position-1);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case DialogHelpers.LOGIN:
                DialogHelpers.getInstance().showLoginDialog(context);
                break;
            case DialogHelpers.LOJA:
            case DialogHelpers.PISTA:
            case DialogHelpers.EVENTO:
                if(Usuario.LOGGED_USER != null) {
                    DialogHelpers.getInstance().showOptionsDialog(context, position);
                }else{
                    DialogHelpers.getInstance().showBuscarDialog(context,position);
                }
                break;
            default:
                break;
        }

    }
}
