package mohammadali.fouladi.n01547173.mf;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Mf extends Fragment {
    // Mohammad Ali Fouladi N01547173

    private EditText phoneEditText;
    private EditText messageEditText;
    private Button sendSmsButton;
    private String phoneNumber;
    private String message;

    // Permission request code
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;

    // ActivityResultLauncher for requesting SMS permission
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mf, container, false);

        phoneEditText = view.findViewById(R.id.MoephoneEditText);
        messageEditText = view.findViewById(R.id.MoemessageEditText);
        sendSmsButton = view.findViewById(R.id.MoesendSmsButton);

        // Initialize the permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted, send SMS
                        sendSMS(phoneNumber, message);
                    } else {
                        // Permission denied
                        Toast.makeText(getContext(), R.string.sms_permission_denied, Toast.LENGTH_SHORT).show();
                    }
                });

        sendSmsButton.setOnClickListener(v -> {
            phoneNumber = phoneEditText.getText().toString();
            message = messageEditText.getText().toString();

            // Validate input
            if (phoneNumber.isEmpty()) {
                phoneEditText.setError(getString(R.string.phone_number_cannot_be_empty));
                return;
            }
            if (phoneNumber.length() < 10) {
                phoneEditText.setError(getString(R.string.phone_number_must_be_10_digits));
                return;
            }
            if (message.isEmpty()) {
                messageEditText.setError(getString(R.string.message_cannot_be_empty));
                return;
            }

            // Check SMS permission
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            } else {
                // Permission already granted, send SMS
                sendSMS(phoneNumber, message);
            }
        });

        return view;
    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(requireContext(), 0,
                new Intent(SENT), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(requireContext(), 0,
                new Intent(DELIVERED), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Register receiver for sending
        BroadcastReceiver sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), R.string.sms_sent, Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getContext(), R.string.generic_failure, Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getContext(), R.string.no_service, Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getContext(), R.string.null_pdu, Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getContext(), R.string.radio_off, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        requireContext().registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT), Context.RECEIVER_NOT_EXPORTED);

        // Register receiver for delivery
        BroadcastReceiver deliveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), R.string.sms_delivered, Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getContext(), R.string.sms_not_delivered, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        requireContext().registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED), Context.RECEIVER_NOT_EXPORTED);

        // Send SMS
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
