package mohammadali.fouladi.n01547173.mf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link N01547173#newInstance} factory method to
 * create an instance of this fragment.
 */
// Mohammad Ali Fouladi n01547173
public class N01547173 extends Fragment {
    // Mohammad Ali Fouladi N01547173
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WebView webView;
    private AdView adView;
    private int adClickCounter = 0;

    public N01547173() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment N01547173.
     */
    // TODO: Rename and change types and number of parameters
    public static N01547173 newInstance(String param1, String param2) {
        N01547173 fragment = new N01547173();
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
        View view = inflater.inflate(R.layout.fragment_n01547173, container, false);

        Spinner websiteSpinner = view.findViewById(R.id.MoewebsiteSpinner);
        webView = view.findViewById(R.id.MoewebView);
        adView = view.findViewById(R.id.MoeadView);

        // Spinner with website names
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.websites_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        websiteSpinner.setAdapter(adapter);

        websiteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected website URL
                String selectedWebsite = getResources().getStringArray(R.array.websites_url_array)[position];
                loadWebsite(selectedWebsite);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Initialize Mobile Ads SDK
        MobileAds.initialize(getContext(), initializationStatus -> {});

        List<String> testDeviceIds = Arrays.asList(AdRequest.DEVICE_ID_EMULATOR, "B3EEABB8EE11C2BE770B684D95219ECB");
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build();
        MobileAds.setRequestConfiguration(configuration);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClicked() {
                adClickCounter++;
                Toast.makeText(getContext(), getString(R.string.name) + getString(R.string.click_count) + adClickCounter, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void loadWebsite(String url) {
        // Configure the WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (url.equals("file:///android_asset/welcome.html")) {
            // Load local HTML file initially
            webView.loadUrl(url);
        } else {
            // Load external website
            webView.loadUrl(url);
        }
    }
}