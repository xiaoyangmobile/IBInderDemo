# Android AIDL原理讲解
## 如何使用
这部分很简单，我们只大概说一下步骤，如果不太清楚的话可以查看官方文档：https://developer.android.com/guide/components/aidl
* 创建AIDL文件
* 实现接口
* 为client公开接口
* 复制AIDL文件和其他相关文件到Client
* 实现ServiceConnection
* bindService
* 通过ServiceConnection的onServiceConnection获取IBinder实例
## 源码解析
AIDL文件与编译后的Java文件：
当我们声明了一个AIDL文件编译之后就会生成一个名字一样后缀为`.java`的文件，我们使用的就是该文件。先来分析下该文件，例子如下：
IBookInterface.aidl
```
package com.xiaoyang.server;

// Declare any non-default types here with import statements
import com.xiaoyang.server.Book;

interface IBookInterface {
    void addBook(in Book book);

    List<Book> bookCount();
}
```
IBookInterface.java
```
public interface IBookInterface extends android.os.IInterface
{
  /** Default implementation for IBookInterface. */
  public static class Default implements com.xiaoyang.server.IBookInterface
  {
    @Override public void addBook(com.xiaoyang.server.Book book) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<com.xiaoyang.server.Book> bookCount() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.xiaoyang.server.IBookInterface
  {
    private static final java.lang.String DESCRIPTOR = "com.xiaoyang.server.IBookInterface";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.xiaoyang.server.IBookInterface interface,
     * generating a proxy if needed.
     */
    public static com.xiaoyang.server.IBookInterface asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.xiaoyang.server.IBookInterface))) {
        return ((com.xiaoyang.server.IBookInterface)iin);
      }
      return new com.xiaoyang.server.IBookInterface.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_addBook:
        {
          data.enforceInterface(descriptor);
          com.xiaoyang.server.Book _arg0;
          if ((0!=data.readInt())) {
            _arg0 = com.xiaoyang.server.Book.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.addBook(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_bookCount:
        {
          data.enforceInterface(descriptor);
          java.util.List<com.xiaoyang.server.Book> _result = this.bookCount();
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.xiaoyang.server.IBookInterface
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void addBook(com.xiaoyang.server.Book book) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((book!=null)) {
            _data.writeInt(1);
            book.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addBook(book);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<com.xiaoyang.server.Book> bookCount() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<com.xiaoyang.server.Book> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_bookCount, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().bookCount();
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(com.xiaoyang.server.Book.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static com.xiaoyang.server.IBookInterface sDefaultImpl;
    }
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_bookCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(com.xiaoyang.server.IBookInterface impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Stub.Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.xiaoyang.server.IBookInterface getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void addBook(com.xiaoyang.server.Book book) throws android.os.RemoteException;
  public java.util.List<com.xiaoyang.server.Book> bookCount() throws android.os.RemoteException;
}
```
代码比较多，先大概看一眼就行，下面会仔细讲解。
`IBookInterface.java`是继承自IInterface,其中有两个我们定义的方法
```
  public void addBook(com.xiaoyang.server.Book book) throws android.os.RemoteException;
  public java.util.List<com.xiaoyang.server.Book> bookCount() throws android.os.RemoteException;
```
内部有两个静态内部类'Default'和静态抽象内部类`Stub`.
先看一下'Default'静态内部类的实现。
```
  public static class Default implements com.xiaoyang.server.IBookInterface
  {
    @Override public void addBook(com.xiaoyang.server.Book book) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<com.xiaoyang.server.Book> bookCount() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
```
可以看到是'IBookInterface'的一个实现。再看一下'Stub':
```
public static abstract class Stub extends android.os.Binder implements com.xiaoyang.server.IBookInterface
  {
    private static final java.lang.String DESCRIPTOR = "com.xiaoyang.server.IBookInterface";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
    }
    /**
     * Cast an IBinder object into an com.xiaoyang.server.IBookInterface interface,
     * generating a proxy if needed.
     */
    public static com.xiaoyang.server.IBookInterface asInterface(android.os.IBinder obj)
    {
    }
    @Override public android.os.IBinder asBinder()
    {
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
    }
    private static class Proxy implements com.xiaoyang.server.IBookInterface
    {
    }
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_bookCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(com.xiaoyang.server.IBookInterface impl) {
    }
    public static com.xiaoyang.server.IBookInterface getDefaultImpl() {
    }
  }
```
可以看到有以下几个实现：
* 构造方法
* 静态方法asInterface，用来返回当前实例或代理
* asBinder方法，返回自己
* onTransaction，数据传输的核心方法
* setDefaultImpl，设置实例
* Proxy，代理类
该类集成了Binder，并实现了IBookInterface接口。即它是一个Binder实例并实现了IBookInterface。下面我们逐个分析。
#### 构造方法
```
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
```
就是给Binder做了一个绑定。
#### asInterface
```
    public static com.xiaoyang.server.IBookInterface asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR); // A
      if (((iin!=null)&&(iin instanceof com.xiaoyang.server.IBookInterface))) {
        return ((com.xiaoyang.server.IBookInterface)iin);
      }
      return new com.xiaoyang.server.IBookInterface.Stub.Proxy(obj);
    }
```
这是一个静态方法，通过该方法可以获取到Binder实例或代理。先看注释A处，'obj.queryLocalInterface'这个上面的构造方法中的'this.attachInterface'方法相呼应的，意思是通过DESCRIPTOR去取Binder实例。如果存在就返回实例，不存在就返回代理类。啥意思呢？简单说就是如果跨进程了就返回Proxy代理类，否则就直接返回实例。
#### asBinder
```
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
```
#### onTransact
```
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_addBook:
        {
          data.enforceInterface(descriptor);
          com.xiaoyang.server.Book _arg0;
          if ((0!=data.readInt())) {
            _arg0 = com.xiaoyang.server.Book.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.addBook(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_bookCount:
        {
          data.enforceInterface(descriptor);
          java.util.List<com.xiaoyang.server.Book> _result = this.bookCount();
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
```
可以看到我们之前定义的两个接口实现在这里，发起RPC调用时通过这里传输，然后在调用本地相关方法。
我们看到Stub还有个内部静态类Proxy。这个就是我们前面提到的代理类，如果server和client不在同一个进程中，就会返回改代理类实例，该类的内部方法都是基于代理实现的。并且Stub中有的方法，它都有。使用逻辑是，先调用改代理类的实现方法，然后通过代理类内部remote调用服务端的相应的方法。主要代码：
```
boolean _status = mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
```
而这个过程就是一个RPC过程。

### bindService
```
context.bindService->ServiceManager->ActivityManagerService->AMS.bindIsolatedService->ActiveServices->ActiveServices.bindServiceLocked->找到client，然后调用client的connection并返回binder。
```