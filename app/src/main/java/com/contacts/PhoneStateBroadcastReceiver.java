package com.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

//public class CallEndListener extends PhoneStateListener {
//    @Override
//    public void onCallStateChanged(int state, String incomingNumber) {
//        super.onCallStateChanged(state, incomingNumber);
//
//        switch (state) {
//            case TelephonyManager.CALL_STATE_IDLE:
//                // Call has ended
//                Log.d("CallEndListener", "Call ended");
//                // Add your code to handle the call end event here
//                break;
//            case TelephonyManager.CALL_STATE_OFFHOOK:
//                // Call is in progress
//                break;
//            case TelephonyManager.CALL_STATE_RINGING:
//                // Incoming call
//                break;
//        }
//    }
//}

public class PhoneStateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStateBroadcastReceiver";
    Context mContext;
    String incoming_number;
    private int prev_state;

    public void onReceive(@NonNull Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //TelephonyManager object
        CustomPhoneStateListener customPhoneListener = new CustomPhoneStateListener();
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE); //Register our listener with TelephonyManager

        Bundle bundle = intent.getExtras();
        String phoneNr = bundle.getString("incoming_number");
        Log.v(TAG, "phoneNr: " + phoneNr);
        mContext = context;
    }

    public class CustomPhoneStateListener extends PhoneStateListener {

        private static final String TAG = "CustomPhoneStateListener";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (incomingNumber != null && incomingNumber.length() > 0)
                incoming_number = incomingNumber;

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "CALL_STATE_RINGING");
                    prev_state = state;
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "CALL_STATE_OFFHOOK");
                    prev_state = state;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:

                    Log.d(TAG, "CALL_STATE_IDLE==>" + incoming_number);

                    if ((prev_state == TelephonyManager.CALL_STATE_OFFHOOK)) {
                        prev_state = state;
                    }
                    if ((prev_state == TelephonyManager.CALL_STATE_RINGING)) {
                        prev_state = state;
                        //Rejected or Missed call
                    }
                    break;
            }
        }
    }
}