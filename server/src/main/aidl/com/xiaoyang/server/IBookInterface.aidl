// IBookInterface.aidl
package com.xiaoyang.server;

// Declare any non-default types here with import statements
import com.xiaoyang.server.Book;

interface IBookInterface {
    void addBook(in Book book);

    List<Book> bookCount();
}