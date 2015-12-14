package com.cablush.cablushandroidapp.view.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.domain.Localizavel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by jonathan on 16/11/15.
 */
public class CustomInfoWindow <L extends Localizavel> implements GoogleMap.InfoWindowAdapter {

    private L localizavel;
    private Context context;

    public CustomInfoWindow(L localizavel, Context context) {
        this.localizavel = localizavel;
        this.context= context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marker_layout, null);

        TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        TextView txtDescricao = (TextView)view.findViewById(R.id.txtDescricao);
        TextView txtLogradouro = (TextView)view.findViewById(R.id.txtLogradouro);
        TextView txtCidade = (TextView)view.findViewById(R.id.txtCidadeRG);
        TextView txtCEP = (TextView)view.findViewById(R.id.txtCep);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView2);

        txtTitle.setText(localizavel.getNome());
        txtDescricao.setText(localizavel.getDescricao());
        txtCEP.setText(localizavel.getLocal().getCep());
        txtCidade.setText(localizavel.getLocal().getBairro() + "/" + localizavel.getLocal().getEstado());
        txtLogradouro.setText(localizavel.getLocal().getLogradouro());

//        URL url = null;
//        Bitmap bmp = null;
//        try {
//            url = new URL(localizavel.getLogo());
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            imageView.setImageBitmap(bmp);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return view;
    }
}
