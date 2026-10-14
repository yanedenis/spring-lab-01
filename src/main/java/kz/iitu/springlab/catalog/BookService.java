package kz.iitu.springlab.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return repository.findAll();
        }
        String query = author.toLowerCase();
        return repository.findAll().stream()
                        .filter(book -> book.author().toLowerCase().contains(query))
                        .toList();
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        return repository.save(new Book(null, book.title(), book.author(), book.year()));
    }

    public Optional<Book> update(long id, Book book) {
        return repository.findById(id)
                        .map(existingBook -> repository.save(new Book(null, book.title(), book.author(), book.year())));
    }

    public boolean delete(long id) {
        return repository.deleteById(id);
    }
}
