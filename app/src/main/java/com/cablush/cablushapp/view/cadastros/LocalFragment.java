package com.cablush.cablushapp.view.cadastros;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Local;
import com.cablush.cablushapp.model.services.FetchAddressIntentService;
import com.cablush.cablushapp.utils.ViewUtils;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by oscar on 06/02/16.
 */
public class LocalFragment extends CablushFragment implements MapaFragment.SelectLocationListener {

    private static final String LOCAL_BUNDLE_KEY = "LOCAL_BUNDLE_KEY";

    private Local local;

    private ArrayAdapter<String> estadosAdapter;

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
     *
     * @param local
     * @return
     */
    public static LocalFragment newInstance(Local local) {
        LocalFragment fragment = new LocalFragment();
        if (local != null) {
            Bundle args = new Bundle();
            args.putSerializable(LOCAL_BUNDLE_KEY, local);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        if (getArguments() != null) {
            local = (Local) getArguments().getSerializable(LOCAL_BUNDLE_KEY);
        }
        mResultReceiver = new AddressResultReceiver(new Handler());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        initializeData();
        initializeView(getActivity());
        fetchLocal();
    }

    @Override
    public void onLocationSelected(LatLng latLng) {
        Intent intent = FetchAddressIntentService.makeIntent(getActivity(), mResultReceiver, latLng);
        getActivity().startService(intent);
    }

    private void initializeData() {
        String[] estados = getResources().getStringArray(R.array.states);
        estadosAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_item, estados);
        estadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initializeView(FragmentActivity activity) {
        cepEditText = (EditText) activity.findViewById(R.id.editTextCep);
        // Estado
        estadoSpinner = (Spinner) activity.findViewById(R.id.spinnerEstado);
        if (estadosAdapter != null) {
            estadoSpinner.setAdapter(estadosAdapter);
        }
        cidadeEditText = (EditText) activity.findViewById(R.id.editTextCidade);
        bairroEditText = (EditText) activity.findViewById(R.id.editTextBairro);
        logradouroEditText = (EditText) activity.findViewById(R.id.editTextLogradouro);
        numeroEditText = (EditText) activity.findViewById(R.id.editTextNumero);
        complementoEditText = (EditText) activity.findViewById(R.id.editTextComplemento);
    }

    private void fetchLocal() {
        if (this.local != null) {
            cepEditText.setText(this.local.getCep());
            if (estadosAdapter != null) {
                estadoSpinner.setSelection(estadosAdapter.getPosition(this.local.getEstado()));
            }
            cidadeEditText.setText(this.local.getCidade());
            bairroEditText.setText(this.local.getBairro());
            logradouroEditText.setText(this.local.getLogradouro());
            numeroEditText.setText(this.local.getNumero());
            complementoEditText.setText(this.local.getComplemento());
        }
    }

    /**
     *
     * @return
     */
    public boolean doValidate() {
        boolean valido = true;

        valido = ViewUtils.checkNotEmpty(getContext(), cepEditText) && valido;
        valido = ViewUtils.checkSelected(getContext(),
                    (TextView)getActivity().findViewById(R.id.textViewEstado),
                    estadoSpinner)
                && valido;
        valido = ViewUtils.checkNotEmpty(getContext(), cidadeEditText) && valido;
        valido = ViewUtils.checkNotEmpty(getContext(), bairroEditText) && valido;
        valido = ViewUtils.checkNotEmpty(getContext(), logradouroEditText) && valido;

        return valido;
    }

    /**
     *
     * @return
     */
    public Local getLocal() {
        if (local == null) {
            local = new Local();
        }
        local.setCep(cepEditText.getText().toString());
        local.setEstado(estadosAdapter.getItem(estadoSpinner.getSelectedItemPosition()));
        local.setCidade(cidadeEditText.getText().toString());
        local.setBairro(bairroEditText.getText().toString());
        local.setLogradouro(logradouroEditText.getText().toString());
        local.setNumero(numeroEditText.getText().toString());
        local.setComplemento(complementoEditText.getText().toString());
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
                fetchLocal();
                if (local != null) {
                    Toast.makeText(getActivity(), R.string.msg_check_address, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.msg_set_address_manually, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
