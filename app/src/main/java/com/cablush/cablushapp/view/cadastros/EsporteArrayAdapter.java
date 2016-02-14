package com.cablush.cablushapp.view.cadastros;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Esporte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 14/02/16.
 */
public class EsporteArrayAdapter extends ArrayAdapter<Esporte> {

    private static final String TAG = EsporteArrayAdapter.class.getSimpleName();

    private final Context mContext;
    private final List<Esporte> mEsportes;
    private final List<Esporte> mEsportesAll;

    private final int mLayoutResourceId;

    private MultiAutoCompleteTextView.Tokenizer mTokenizer;

    public EsporteArrayAdapter(Context context, int resource, List<Esporte> esportes) {
        super(context, resource, esportes);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mEsportes = new ArrayList<>(esportes);
        this.mEsportesAll = new ArrayList<>(esportes);
    }

    public void setTokenizer(MultiAutoCompleteTextView.Tokenizer tokenizer) {
        this.mTokenizer = tokenizer;
    }

    /**
     * Get the selected items by the text value using the tokenizer.
     *
     * @return The selected Items.
     */
    public List<Esporte> getSelectedItems(CharSequence constraint) {
        List<Esporte> selected = new ArrayList<>();
        int start = 0;
        while (start <= constraint.length()) {
            int end = mTokenizer.findTokenEnd(constraint, start);
            start = mTokenizer.findTokenStart(constraint, end);
            if (start == end) {
                break;
            }
            for (Esporte esporte : mEsportesAll) {
                String subConstraint = constraint.subSequence(start, end).toString();
                if (esporte.getCategoriaNome().equals(subConstraint)
                        && !selected.contains(esporte)) {
                    selected.add(esporte);
                    break;
                }
            }
            start = end + 1;
        }
        return selected;
    }

    @Override
    public int getCount() {
        return mEsportes.size();
    }

    @Override
    public Esporte getItem(int position) {
        return mEsportes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            Esporte esporte = getItem(position);
            TextView text = (TextView) convertView;
            text.setText(esporte.getCategoriaNome());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Esporte) resultValue).getCategoriaNome();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    List<Esporte> esportesSuggestion = new ArrayList<>();
                    String strConstraint = constraint.toString().toLowerCase();
                    for (Esporte esporte : mEsportesAll) {
                        if (esporte.getCategoria().toLowerCase().startsWith(strConstraint)
                                || esporte.getNome().toLowerCase().startsWith(strConstraint)) {
                            esportesSuggestion.add(esporte);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = esportesSuggestion;
                    filterResults.count = esportesSuggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mEsportes.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mEsportes.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Esporte) {
                            mEsportes.add((Esporte) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mEsportes.addAll(mEsportesAll);
                }
                notifyDataSetChanged();
            }
        };
    }
}
