package com.xiaoyang.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomService extends Service {
    public CustomService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return customBinder;
    }

    private static Binder customBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            Log.e("Custom", data.readString());
            return true;
        }
    };
}