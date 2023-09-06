package demo.ddd.service;

import demo.ddd.model.Book;
import demo.ddd.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class BookService implements IBookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Optional<Long> count(@NonNull Map<String, Object> queryString) {
        return Optional.empty();
    }

    @Override
    public Optional<Book> get(@NonNull Map<String, Object> queryString, boolean decrypt) {
        return Optional.empty();
    }

    @Override
    public Optional<Book> getById(String id, boolean decrypt) {
        Book result = repository.getById(id).orElse(null);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<Book> add(Book book, boolean decrypt) {
        Book result = repository.add(book).orElse(null);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<Book> update(String id, Book book, boolean decrypt) {
        Book result = repository.update(id, book).orElse(null);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<Boolean> delete(String id) {
        return repository.delete(id);
    }

    @Override
    public Optional<Book> updateOne(Map<String, Object> q, Map<String, Object> u) {
        return repository.updateOne(q, u);
    }
}
