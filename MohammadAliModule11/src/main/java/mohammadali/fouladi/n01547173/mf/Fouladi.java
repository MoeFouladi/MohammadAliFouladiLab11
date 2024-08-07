package mohammadali.fouladi.n01547173.mf;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fouladi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fouladi extends Fragment implements OnMapReadyCallback {
//      Mohammad Ali Fouladi N01547173
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;
    private TextView locationTextView;
    private ExecutorService executorService;
    private Handler handler;




    public Fouladi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fouladi.
     */
    // TODO: Rename and change types and number of parameters
    public static Fouladi newInstance(String param1, String param2) {
        Fouladi fragment = new Fouladi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fouladi, container, false);
        locationTextView = view.findViewById(R.id.MaptextView);
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync( this);
        }

        return view;    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Humber College, Toronto, and zoom the camera
        LatLng humber = new LatLng(43.7289, -79.6076); // Humber location
        mMap.addMarker(new MarkerOptions().position(humber).title(getString(R.string.humber_college)).snippet(getString(R.string.toronto)+getString(R.string.name)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(humber, 15));
        mMap.setOnMapClickListener(this::handleMapClick);

    }
    private void handleMapClick(LatLng point) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(point).title("Selected Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
        reverseGeocode(point);

    }
    private void reverseGeocode(LatLng point) {
        executorService.submit(() -> {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses;
            String addressText = "";

            try {
                addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    addressText = String.format("%s, %s, %s",
                            address.getAddressLine(0),
                            address.getLocality(),
                            address.getCountryName());
                    mMap.addMarker(new MarkerOptions().position(point).title(address.getLocality()));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String finalAddressText = addressText;
            handler.post(() -> locationTextView.setText(finalAddressText));
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }


    }