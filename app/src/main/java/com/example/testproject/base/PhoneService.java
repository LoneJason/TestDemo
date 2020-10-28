package com.example.testproject.base;

import android.os.Build;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PhoneService extends CallScreeningService {
    @Override
    public void onScreenCall(@NonNull Call.Details details) {
        Log.d("mytest","有电话");
        CallScreeningService.CallResponse response =
                new CallScreeningService.CallResponse.Builder()
                        .setDisallowCall(true)
                        .setRejectCall(true)
                        .setSkipCallLog(true)
                        .setSkipNotification(true)
                        .build();
        respondToCall(details, response);

    }
}
