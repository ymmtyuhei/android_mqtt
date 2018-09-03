package com.paulapithon.mqtt;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BrokerFragment extends Fragment {

    private boolean brokerOn;
    private boolean canClick;
    private int CHECK_INTERNET = 3333;

    public BrokerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_broker, container, false);

        final TextView text = root.findViewById(R.id.text_broker);
        Button start = root.findViewById(R.id.start_broker);

        requestPermission();

        final WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        startServer(text);

        start.setOnClickListener(v -> {
            //if (canClick) {
                if (brokerOn) {
                    getActivity().stopService(new Intent(getActivity(), BrokerService.class));
                    text.setText(R.string.broker_off);
                    brokerOn = false;
                } else {
                    startServer(text);
                }
            //}
        });
        return root;
    }

    private void startServer(TextView text){
        getActivity().startService(new Intent(getActivity(), BrokerService.class));
        text.setText("start server");
        brokerOn = true;
    }

    private void requestPermission () {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_WIFI_STATE}, CHECK_INTERNET);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CHECK_INTERNET);
            canClick = false;
        } else {
            canClick = true;
        }
    }

//    void requestPermission2 (){
//        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
//        if (ContextCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_WIFI_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_WIFI_STATE)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_WIFI_STATE},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        if (requestCode == CHECK_INTERNET) {
            canClick = true;
        }

    }

}
