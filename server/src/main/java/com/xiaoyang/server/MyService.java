package com.xiaoyang.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private String TAG = "MyService";

    private List<Book> books;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        books = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return stub;
    }

    private IBookInterface.Stub stub = new IBookInterface.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            Log.e(TAG, "addBook");
            books.add(book);
        }

        @Override
        public List<Book> bookCount() throws RemoteException {
            Log.e(TAG, "bookCount");
            return books;
        }
    };
}