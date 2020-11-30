package com.xiaoyang.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.xiaoyang.server.Book;
import com.xiaoyang.server.IBookInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private IBookInterface iBookInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaoyang.server", "com.xiaoyang.server.MyService"));
//        intent.setClassName("com.xiaoyang.server", "com.xiaoyang.server.MyService");
//        startService(intent);
        boolean is = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.e(TAG, is + "");
    }

    public void addBook(View view) {
        try {
            iBookInterface.addBook(new Book("123"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getBooks(View view) {
        Log.e(TAG, "get books");
        try {
            List books = iBookInterface.bookCount();
            Log.e(TAG, books.size() + "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "Connected");
            iBookInterface = IBookInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBookInterface = null;
        }
    };
}