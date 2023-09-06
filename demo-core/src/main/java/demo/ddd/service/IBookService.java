package demo.ddd.service;

import demo.ddd.model.Book;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

public interface IBookService {
    Optional<Long> count(@NonNull Map<String, Object> queryString);

    Optional<Book> get(@NonNull Map<String, Object> queryString, boolean decrypt);

    Optional<Book> getById(String id, boolean decrypt);

    Optional<Book> add(Book book, boolean decrypt);

    Optional<Book> update(String id, Book book, boolean decrypt);

    Optional<Boolean> delete(String id);

    Optional<Book> updateOne(Map<String, Object> q, Map<String, Object> u);

//    void encryptData(Book config);
//
//    String encryptData(String value);
//
//    void decryptData(Book config);
//
//    String decryptData(String value);

}
