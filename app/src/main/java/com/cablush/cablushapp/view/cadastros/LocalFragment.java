package com.cablush.cablushapp.view.cadastros;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Local;
import com.cablush.cablushapp.model.geonames.GeonamesLoader;
import com.cablush.cablushapp.model.services.FetchAddressIntentService;
import com.cablush.cablushapp.utils.CountryLocale;
import com.cablush.cablushapp.utils.ViewUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by oscar on 06/02/16.
 */
public class LocalFragment extends CablushFragment implements MapaFragment.SelectLocationListener {

    private static final String LOCAL_BUNDLE_KEY = "LOCAL_BUNDLE_KEY";

    private Local local;

    private GeonamesLoader geonamesLoader;

    private ArrayAdapter<CountryLocale> paisesAdaper;
    private ArrayAdapter<String> estadosAdapter;

    private Spinner paisSpinner;
    private EditText cepEditText;
    private Spinner estadoSpinner;
    private EditText cidadeEditText;
    private EditText bairroEditText;
    private EditText logradouroEditText;
    private EditText numeroEditText;
    private EditText complementoEditText;

    private AddressResultReceiver mResultReceiver;

    public LocalFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment with the necessary data.
     */
    public static LocalFragment newInstance(@NonNull Local local) {
        LocalFragment fragment = new LocalFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCAL_BUNDLE_KEY, local);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        // Initialize necessary data
        if (getArguments() != null) {
            local = (Local) getArguments().getSerializable(LOCAL_BUNDLE_KEY);
        }

        geonamesLoader = new GeonamesLoader();

        paisesAdaper = new ArrayAdapter<>(getActivity(), R.layout.simple_item, CountryLocale.getContriesLocales());
        paisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estadosAdapter.clear();
                final CountryLocale country = paisesAdaper.getItem(position);
                Thread loader = new Thread() {
                    @Override
                    public void run() {
                        List<String> estados = geonamesLoader.getFirstSubdivisions(country.getCountry());
                        estadosAdapter.addAll(estados.toArray(new String[estados.size()]));
                    }
                };
                loader.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        estadosAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_item, new String[0]);
        estadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        initializeView(view);
        setViewValues();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        getViewValues();
    }

    @Override
    public void onLocationSelected(LatLng latLng) {
        Intent intent = FetchAddressIntentService.makeIntent(getActivity(), mResultReceiver, latLng);
        getActivity().startService(intent);
    }

    private void initializeView(View view) {
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewPais));
        paisSpinner = (Spinner) view.findViewById(R.id.spinnerPais);
        if (paisesAdaper != null) {
            paisSpinner.setAdapter(paisesAdaper);
            paisSpinner.setSelection(paisesAdaper.getPosition(CountryLocale.getDefault()));
        }

        cepEditText = (EditText) view.findViewById(R.id.editTextCep);

        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewEstado));
        estadoSpinner = (Spinner) view.findViewById(R.id.spinnerEstado);
        if (estadosAdapter != null) {
            estadoSpinner.setAdapter(estadosAdapter);
        }

        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewCidade));
        cidadeEditText = (EditText) view.findViewById(R.id.editTextCidade);

        bairroEditText = (EditText) view.findViewById(R.id.editTextBairro);
        logradouroEditText = (EditText) view.findViewById(R.id.editTextLogradouro);
        numeroEditText = (EditText) view.findViewById(R.id.editTextNumero);
        complementoEditText = (EditText) view.findViewById(R.id.editTextComplemento);
    }

    private void setViewValues() {
        if (paisesAdaper != null) {
            paisSpinner.setSelection(paisesAdaper.getPosition(CountryLocale.getCountryLocale(local.getPais())));
        }
        cepEditText.setText(local.getCep());
        if (estadosAdapter != null) {
            estadoSpinner.setSelection(estadosAdapter.getPosition(local.getEstado()));
        }
        cidadeEditText.setText(local.getCidade());
        bairroEditText.setText(local.getBairro());
        logradouroEditText.setText(local.getLogradouro());
        numeroEditText.setText(local.getNumero());
        complementoEditText.setText(local.getComplemento());
    }

    private void getViewValues() {
        local.setPais(paisesAdaper.getItem(paisSpinner.getSelectedItemPosition()).getCountry());
        local.setCep(cepEditText.getText().toString());
        local.setEstado((String)estadoSpinner.getSelectedItem());
        local.setCidade(cidadeEditText.getText().toString());
        local.setBairro(bairroEditText.getText().toString());
        local.setLogradouro(logradouroEditText.getText().toString());
        local.setNumero(numeroEditText.getText().toString());
        local.setComplemento(complementoEditText.getText().toString());
    }

    /**
     *
     * @return
     */
    public Local getLocal() {
        if (isAdded() || isDetached()) { // If is added or detached, update the object with the views
            getViewValues();
        }
        return local;
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        public final String TAG = AddressResultReceiver.class.getSimpleName();

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.d(TAG, "onReceiveResult()");
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                local = (Local) resultData.getSerializable(FetchAddressIntentService.RESULT_LOCAL_KEY);
                setViewValues();
                if (local != null) {
                    Toast.makeText(getActivity(), R.string.msg_check_address, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.msg_set_address_manually, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
