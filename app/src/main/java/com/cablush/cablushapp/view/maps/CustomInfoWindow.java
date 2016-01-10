package com.cablush.cablushapp.view.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.lang.ref.WeakReference;

/**
 * Created by jonathan on 16/11/15.
 */
public class CustomInfoWindow <L extends Localizavel> implements GoogleMap.InfoWindowAdapter {

    private L localizavel;
    private WeakReference<Context> context;

    public CustomInfoWindow(L localizavel, Context context) {
        this.localizavel = localizavel;
        this.context = new WeakReference<>(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater)context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.marker_layout, null);

        TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        TextView txtDescricao = (TextView)view.findViewById(R.id.descricaoTextView);
        TextView txtLogradouro = (TextView)view.findViewById(R.id.enderecoTextView);
        TextView txtCidade = (TextView)view.findViewById(R.id.cidadeEstadoTextView);
        TextView txtCEP = (TextView)view.findViewById(R.id.txtCep);

        txtTitle.setText(localizavel.getNome());
        txtDescricao.setText(localizavel.getDescricao());
        txtLogradouro.setText(localizavel.getLocal().getEndereco());
        txtCidade.setText(localizavel.getLocal().getCidadeEstado());
        txtCEP.setText(localizavel.getLocal().getCep());

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
