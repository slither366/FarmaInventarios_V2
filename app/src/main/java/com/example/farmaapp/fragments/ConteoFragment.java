package com.example.farmaapp.fragments;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.farmaapp.activity.MainActivity;
import com.example.farmaapp.dao.ProductoInventarioDAO;
import com.example.farmaapp.entity.ProductoInventario;
import com.example.farmaapp.util.Constantes;
import com.example.farmaapp.util.FileUtils;
import com.example.farmaapp.db.AppDatabase;
import com.example.farmaapp.entity.Producto;
import com.example.farmaapp.entity.ProductoBarra;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState;
import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.StatusData.ScannerStates;
import com.symbol.emdk.barcode.StatusData;

import android.util.DisplayMetrics;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

import com.example.farmaapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConteoFragment extends Fragment implements View.OnClickListener,EMDKListener, DataListener, StatusListener, ScannerConnectionListener, OnCheckedChangeListener {

    View v;
    Integer vCantEntero, vCantFraccion;

    FloatingActionButton fab_entero_menos, fab_entero_mas, fab_fraccion_menos, fab_fraccion_mas;
    EditText et_entero, et_fraccion , et_codProd;
    TextView tv_codigo, tv_nombre, tv_entero_antes, tv_fraccion_antes, tv_desEntero, tv_desFraccion, tv_laboratorio, tv_changeAnaquel;
    Button btn_guardar, btn_cancelar;

    AppDatabase database;

    Producto datosProducto;
    ProductoInventario datosProdInventariado;
    ProductoBarra productoBarra;

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;

    private EditText et_codScaneado = null;

    private Spinner spinnerScannerDevices = null;

    private List<ScannerInfo> deviceList = null;

    private int scannerIndex = 0; // Keep the selected scanner
    private int defaultIndex = 0; // Keep the default scanner
    private String statusString = "";

    private boolean bSoftTriggerSelected = false;
    private boolean bDecoderSettingsChanged = false;
    private boolean bExtScannerDisconnected = false;
    private final Object lock = new Object();

    public ConteoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_contador, container, false);

        inicializarUI();

        iniciarEventos();

        deviceList = new ArrayList<ScannerInfo>();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setDefaultOrientation();

        EMDKResults results = EMDKManager.getEMDKManager(getActivity().getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            updateStatus("EMDKManager object request failed!");
            return v;
        }
        addSpinnerScannerDevicesListener();

        database = Room.databaseBuilder(getContext(), AppDatabase.class, Constantes.DB_NAME)
                .allowMainThreadQueries()
                .build();

        //importarDB();

        return v;

    }

    private void inicializarUI() {

        fab_entero_mas = (FloatingActionButton) v.findViewById(R.id.fab_entero_mas);
        fab_entero_menos = (FloatingActionButton) v.findViewById(R.id.fab_entero_menos);
        fab_fraccion_mas = (FloatingActionButton) v.findViewById(R.id.fab_fraccion_mas);
        fab_fraccion_menos = (FloatingActionButton) v.findViewById(R.id.fab_fraccion_menos);

        et_entero = v.findViewById(R.id.et_entero);
        et_fraccion = v.findViewById(R.id.et_fraccion);
        et_codProd = v.findViewById(R.id.et_codScaneado);
/*
        et_entero.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et_fraccion.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et_codProd.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
*/
        tv_codigo = v.findViewById(R.id.tv_codigo);
        tv_nombre = v.findViewById(R.id.tv_nombre);
        tv_entero_antes = v.findViewById(R.id.tv_entero_antes);
        tv_fraccion_antes = v.findViewById(R.id.tv_fraccion_antes);
        tv_desEntero = v.findViewById(R.id.tv_desEntero);
        tv_desFraccion = v.findViewById(R.id.tv_desFraccion);
        tv_laboratorio = v.findViewById(R.id.tv_laboratorio);
        tv_changeAnaquel = v.findViewById(R.id.tv_changeAnaquel);

        et_codScaneado = (EditText) v.findViewById(R.id.et_codScaneado);
        spinnerScannerDevices = (Spinner) v.findViewById(R.id.spinnerScannerDevices);

        //btn_cancelar = (Button) v.findViewById(R.id.btn_cancelar);
        btn_guardar = (Button) v.findViewById(R.id.btn_guardar);

        this.vCantEntero = 0;
        this.vCantFraccion = 0;

    }

    private void iniciarEventos() {

        fab_entero_mas.setOnClickListener(this);
        fab_entero_menos.setOnClickListener(this);
        fab_fraccion_mas.setOnClickListener(this);
        fab_fraccion_menos.setOnClickListener(this);
        et_entero.setOnClickListener(this);
        et_fraccion.setOnClickListener(this);
        et_codProd.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);
        tv_changeAnaquel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab_entero_mas:
                if(this.vCantEntero<1000) {
                    aumentarUnEntero();
                }
                break;

            case R.id.fab_entero_menos:
                if(this.vCantEntero>0) {
                    disminuirUnEntero();
                }
                break;

            case R.id.fab_fraccion_mas:
                if(this.vCantFraccion<1000) {
                    aumentarUnaFraccion();
                }
                break;

            case R.id.fab_fraccion_menos:
                if(this.vCantFraccion>0) {
                    disminuirUnaFraccion();
                }
                break;

            case R.id.et_entero:
                et_entero.selectAll();
                break;

            case R.id.et_fraccion:
                et_fraccion.selectAll();
                break;

            case R.id.et_codScaneado:
                et_codProd.selectAll();
                break;

            case R.id.btn_guardar:
                guardarDiferenciasInventario();
                break;

            case R.id.tv_changeAnaquel:
                Toast.makeText(v.getContext(), "Cambiaras Anaquel", Toast.LENGTH_SHORT);
                Log.d("LogFP: ","Imprimio");
                break;

        }

        //this.tv_entero.setText(this.vCantEntero);
        //this.tv_fraccion.setText(this.vCantFraccion);

    }

    private void guardarDiferenciasInventario() {

        /*
        Producto datosProducto;
        ProductoInventario datosProdInventariado;
        ProductoBarra productoBarra;
        */
        int cantEnteros = 0;
        int cantFracciones = 0;
        String anaquel = "0";
        String anaquelConcat = "0";
        String caNuAnaquelConcatOld = "";

        //Ver si el objeto productoInventarios tiene datos
        if(datosProdInventariado != null){

            // Si tiene datos modificar los campos de ProductoInventario
            //Update

            String codigoProd = datosProdInventariado.getCoproducto() + "";

            final ProductoInventario productoInventario = database.getProductoInventarioDao().getProdInventariadoWithCodigo(codigoProd);

            cantEnteros = Integer.parseInt(et_entero.getText().toString());
            if(!datosProducto.getInProdFraccionado().equals("N")){
                cantFracciones = Integer.parseInt(et_fraccion.getText().toString());
            }

            int caEnterosOld = datosProdInventariado.getCaEntero();
            int caFraccionOld = datosProdInventariado.getCaFraccion();
            anaquel = tv_changeAnaquel.getText().toString();
            caNuAnaquelConcatOld = datosProdInventariado.getNuAnaquelConcat() + "," + anaquel;

            cantEnteros = cantEnteros + caEnterosOld;
            cantFracciones = cantFracciones + caFraccionOld;

            /*productoInventario.setCoproducto(datosProdInventariado.getCoproducto().toString());
            productoInventario.setDeproducto(datosProdInventariado.getDeproducto().toString());
            productoInventario.setCoLaboratorio(datosProdInventariado.getCoLaboratorio().toString());
            productoInventario.setInProdFraccionado(datosProdInventariado.getInProdFraccionado());
            productoInventario.setVaFraccion(datosProdInventariado.getVaFraccion());*/
            productoInventario.setCaEntero(cantEnteros);
            productoInventario.setCaFraccion(cantFracciones);
            productoInventario.setNuAnaquel(anaquel);
            productoInventario.setNuAnaquelConcat(caNuAnaquelConcatOld);

            database.getProductoInventarioDao().update(productoInventario);

            updateData("00000000000");
            et_codScaneado.setText("");

        }else{
            //Si el productoInventario no tiene el producto escaneado registrarlo
            //INSERT
            ProductoInventarioDAO productoInventarioDAO = database.getProductoInventarioDao();

            cantEnteros = Integer.parseInt(et_entero.getText().toString());
            if(!datosProducto.getInProdFraccionado().equals("N")){
                cantFracciones = Integer.parseInt(et_fraccion.getText().toString());
            }

            anaquel = tv_changeAnaquel.getText().toString();
            anaquelConcat = tv_changeAnaquel.getText().toString();

            ProductoInventario productoInventario = new ProductoInventario();

            productoInventario.setCoproducto(datosProducto.getCoProducto().toString());
            productoInventario.setDeproducto(datosProducto.getDeProducto().toString());
            productoInventario.setCoLaboratorio(datosProducto.getCoLaboratorio().toString());
            productoInventario.setInProdFraccionado(datosProducto.getInProdFraccionado().toString());
            productoInventario.setVaFraccion(Integer.parseInt(datosProducto.getVaFraccion().toString()));
            productoInventario.setCaEntero(cantEnteros);
            productoInventario.setCaFraccion(cantFracciones);
            productoInventario.setNuAnaquel(anaquel);
            productoInventario.setNuAnaquelConcat(anaquelConcat);

            productoInventarioDAO.insert(productoInventario);

            updateData("00000000000");
            et_codScaneado.setText("");

        }

    }

    private void importarDB(){
        // sd: "/storage/emulated/0"
        File sd = Environment.getExternalStorageDirectory();
        // data: "/data"
        File data = Environment.getDataDirectory();

        // com.example.farmaapp
        String packageName = v.getContext().getPackageName();

        // /data/com.example.farmaapp/databases/eckerd
        String internalDBPath = "/data/" + packageName + "/databases/" + Constantes.DB_NAME;
        String externaDCIMDB = "/DCIM/" + Constantes.DB_NAME;

        File externalFileDB = new File(sd,externaDCIMDB);
        File moveToDirectory = new File(data, internalDBPath);

        try {
            FileUtils.copiarArchivo(externalFileDB,moveToDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void aumentarUnEntero() {
        this.vCantEntero = Integer.parseInt(et_entero.getText().toString());
        this.vCantEntero = this.vCantEntero + 1;
        this.et_entero.setText(this.vCantEntero + "");
        et_entero.requestFocus();
        et_entero.selectAll();
    }

    private void disminuirUnEntero() {
        this.vCantEntero = Integer.parseInt(et_entero.getText().toString());
        this.vCantEntero = this.vCantEntero - 1;
        this.et_entero.setText(this.vCantEntero + "");
        et_entero.requestFocus();
        et_entero.selectAll();
    }

    private void aumentarUnaFraccion() {
        this.vCantFraccion = Integer.parseInt(et_fraccion.getText().toString());
        this.vCantFraccion = this.vCantFraccion + 1;
        this.et_fraccion.setText(this.vCantFraccion + "");
        et_fraccion.requestFocus();
        et_fraccion.selectAll();
    }
    
    private void disminuirUnaFraccion() {
        this.vCantFraccion = Integer.parseInt(et_fraccion.getText().toString());
        this.vCantFraccion = this.vCantFraccion - 1;
        this.et_fraccion.setText(this.vCantFraccion + "");
        et_fraccion.requestFocus();
        et_fraccion.selectAll();
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        updateStatus("EMDK open success!");
        this.emdkManager = emdkManager;
        initBarcodeManager();
        enumerateScannerDevices();
        spinnerScannerDevices.setSelection(defaultIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (emdkManager != null) {
            initBarcodeManager();
            enumerateScannerDevices();
            spinnerScannerDevices.setSelection(scannerIndex);
            initScanner();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        deInitScanner();
        deInitBarcodeManager();
    }

    @Override
    public void onClosed() {
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
        updateStatus("EMDK closed unexpectedly! Please close and restart the application.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    //Obtiene la data guardada y lo setea en un <scrollViewData>
    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList <ScanData> scanData = scanDataCollection.getScanData();
            for(ScanData data : scanData) {
                //updateData("<font color='red'>" + data.getLabelType() + "</font> : " + data.getData());
                updateData(data.getData());
            }

        }
    }

    @Override
    public void onStatus(StatusData statusData) {
        ScannerStates state = statusData.getState();
        switch(state) {
            case IDLE:
                statusString = statusData.getFriendlyName()+" is enabled and idle...";
                updateStatus(statusString);

                if(bSoftTriggerSelected) {
                    scanner.triggerType = TriggerType.SOFT_ONCE;
                    bSoftTriggerSelected = false;
                } else {
                    scanner.triggerType = TriggerType.HARD;
                }

                if(bDecoderSettingsChanged) {
                    setDecoders();
                    bDecoderSettingsChanged = false;
                }

                if(!scanner.isReadPending() && !bExtScannerDisconnected) {
                    try {
                        scanner.read();
                    } catch (ScannerException e) {
                        updateStatus(e.getMessage());
                    }
                }
                break;
            case WAITING:
                statusString = "Scanner is waiting for trigger press...";
                updateStatus(statusString);
                break;
            case SCANNING:
                statusString = "Scanning...";
                updateStatus(statusString);
                break;
            case DISABLED:
                statusString = statusData.getFriendlyName()+" is disabled.";
                updateStatus(statusString);
                break;
            case ERROR:
                statusString = "An error has occurred.";
                updateStatus(statusString);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectionChange(ScannerInfo scannerInfo, ConnectionState connectionState) {
        String status;
        String scannerName = "";
        String statusExtScanner = connectionState.toString();
        String scannerNameExtScanner = scannerInfo.getFriendlyName();
        if (deviceList.size() != 0) {
            scannerName = deviceList.get(scannerIndex).getFriendlyName();
        }
        if (scannerName.equalsIgnoreCase(scannerNameExtScanner)) {
            switch(connectionState) {
                case CONNECTED:
                    bSoftTriggerSelected = false;
                    synchronized (lock) {
                        initScanner();
                        bExtScannerDisconnected = false;
                    }
                    break;
                case DISCONNECTED:
                    bExtScannerDisconnected = true;
                    synchronized (lock) {
                        deInitScanner();
                    }
                    break;
            }
            status = scannerNameExtScanner + ":" + statusExtScanner;
            updateStatus(status);
        }
        else {
            bExtScannerDisconnected = false;
            status =  statusString + " " + scannerNameExtScanner + ":" + statusExtScanner;
            updateStatus(status);
        }
    }

    private void initScanner() {
        if (scanner == null) {
            if ((deviceList != null) && (deviceList.size() != 0)) {
                if (barcodeManager != null)
                    scanner = barcodeManager.getDevice(deviceList.get(scannerIndex));
            }
            else {
                updateStatus("Failed to get the specified scanner device! Please close and restart the application.");
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    updateStatus(e.getMessage());
                    deInitScanner();
                }
            }else{
                updateStatus("Failed to initialize the scanner device.");
            }
        }
    }

    private void deInitScanner() {
        if (scanner != null) {
            try{
                scanner.disable();
                scanner.release();
            } catch (Exception e) {
                updateStatus(e.getMessage());
            }
            scanner = null;
        }
    }

    private void initBarcodeManager(){
        barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
        if (barcodeManager != null) {
            barcodeManager.addConnectionListener(this);
        }
    }

    private void deInitBarcodeManager(){
        if (emdkManager != null) {
            emdkManager.release(FEATURE_TYPE.BARCODE);
        }
    }

    private void addSpinnerScannerDevicesListener() {
        spinnerScannerDevices.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if ((scannerIndex != position) || (scanner==null)) {
                    scannerIndex = position;
                    bSoftTriggerSelected = false;
                    bExtScannerDisconnected = false;
                    deInitScanner();
                    initScanner();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    // Obtienes los nombres del tipo de Barra existen CODEBARRA y QR y otros como bluetooth
    // 2D Barcode Imager
    private void enumerateScannerDevices() {
        if (barcodeManager != null) {
            List<String> friendlyNameList = new ArrayList<String>();
            int spinnerIndex = 0;
            deviceList = barcodeManager.getSupportedDevicesInfo();
            if ((deviceList != null) && (deviceList.size() != 0)) {
                Iterator<ScannerInfo> it = deviceList.iterator();
                while(it.hasNext()) {
                    ScannerInfo scnInfo = it.next();
                    friendlyNameList.add(scnInfo.getFriendlyName());
                    if(scnInfo.isDefaultScanner()) {
                        defaultIndex = spinnerIndex;
                    }
                    ++spinnerIndex;
                }
            }
            else {
                updateStatus("Failed to get the list of supported scanner devices! Please close and restart the application.");
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, friendlyNameList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerScannerDevices.setAdapter(spinnerAdapter);
        }
    }

    private void setDecoders() {
        if (scanner != null) {
            try {
                ScannerConfig config = scanner.getConfig();
                // Set EAN8
                config.decoderParams.ean8.enabled = true;//checkBoxEAN8.isChecked();
                // Set EAN13
                config.decoderParams.ean13.enabled = true; //checkBoxEAN13.isChecked();
                // Set Code39
                config.decoderParams.code39.enabled = true; //checkBoxCode39.isChecked();
                //Set Code128
                config.decoderParams.code128.enabled = true; //checkBoxCode128.isChecked();
                scanner.setConfig(config);
            } catch (ScannerException e) {
                updateStatus(e.getMessage());
            }
        }
    }

    public void softScan(View view) {
        bSoftTriggerSelected = true;
        cancelRead();
    }

    private void cancelRead(){
        if (scanner != null) {
            if (scanner.isReadPending()) {
                try {
                    scanner.cancelRead();
                } catch (ScannerException e) {
                    updateStatus(e.getMessage());
                }
            }
        }
    }

    private void updateStatus(final String status){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("LogFP: ", status);
            }
        });
    }

    // Setea el codigo de barras leido
    private void updateData(final String result){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result != null) {
                    et_codScaneado.setText(result);
                    buscarDatosProducto(buscarCodigoProducto(result));
                }else{
                    return;
                }
            }
        });
    }

    // Retorna el codigo del producto
    private String buscarCodigoProducto(String codBarra){
        String codProd = "";
        productoBarra = database.getProductoBarraDao().getProductoWithBarra(codBarra);

        if(productoBarra!=null){
            codProd = productoBarra.getCoProducto().toString();
            return codProd;
        }else{
            return null;
        }
    }

    // Setea los datos del producto buscado por producto en los campos
    private void buscarDatosProducto(String codProducto) {

        datosProducto = database.getProductoDao().getProductoWithCodigo(codProducto);
        datosProdInventariado = database.getProductoInventarioDao().getProdInventariadoWithCodigo(codProducto);

        if(datosProdInventariado != null){
            tv_entero_antes.setText(datosProdInventariado.getCaEntero().toString());
            tv_fraccion_antes.setText(datosProdInventariado.getCaFraccion().toString());
            tv_changeAnaquel.setText(datosProdInventariado.getNuAnaquel().toString());

            this.vCantEntero = datosProdInventariado.getCaEntero();
            this.vCantFraccion = datosProdInventariado.getCaFraccion();

            if(datosProducto.getInProdFraccionado().equals("N")){
                fab_fraccion_mas.hide();
                fab_fraccion_menos.hide();
                et_fraccion.setEnabled(false);
            }

        }else{
            this.vCantEntero = 0;
            this.vCantFraccion = 0;
            tv_entero_antes.setText("0");
            tv_fraccion_antes.setText("0");
            tv_changeAnaquel.setText("0");
            et_entero.setText("");
            et_fraccion.setText("");
        }

        if (datosProducto != null) {
            tv_codigo.setText(datosProducto.getCoProducto());
            tv_nombre.setText(datosProducto.getDeProducto());
            tv_desEntero.setText(datosProducto.getDeUnidad());
            tv_desFraccion.setText(datosProducto.getDeUnidadFraccion());
            tv_laboratorio.setText(datosProducto.getDeLaboratorio());

            et_entero.setText("0");
            et_fraccion.setText("0");
            et_entero.requestFocus();
            et_entero.selectAll();

            mostrarBotones();
            enableETCantidades();

            if(datosProducto.getInProdFraccionado().equals("N")){
                fab_fraccion_mas.hide();
                fab_fraccion_menos.hide();
                et_fraccion.setText("");
                et_fraccion.setEnabled(false);
            }

        }else{
            ocultarBotones();
            vaciarCampos();
            disableETCantidades();
            return;
        }
    }

    private void disableETCantidades() {
        et_entero.setEnabled(false);
        et_fraccion.setEnabled(false);
    }

    private void enableETCantidades() {
        et_entero.setEnabled(true);
        et_fraccion.setEnabled(true);
    }

    private void vaciarCampos() {
        //et_codScaneado.setText("");
        tv_nombre.setText("");
        tv_codigo.setText("");
        tv_laboratorio.setText("");
        et_entero.setText("");
        et_fraccion.setText("");
    }

    private void ocultarBotones() {
        fab_entero_mas.hide();
        fab_entero_menos.hide();
        fab_fraccion_mas.hide();
        fab_fraccion_menos.hide();
    }

    private void mostrarBotones() {
        fab_entero_mas.show();
        fab_entero_menos.show();
        fab_fraccion_mas.show();
        fab_fraccion_menos.show();
    }

    private void setDefaultOrientation(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if(width > height){
            //setContentView(R.layout.activity_main_landscape);
        } else {
            //getActivity().setContentView(R.layout.fragment_inventario);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        bDecoderSettingsChanged = true;
        cancelRead();
    }
}
