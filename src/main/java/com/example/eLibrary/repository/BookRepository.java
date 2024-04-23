package com.example.eLibrary.repository;

import com.example.eLibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByTitleIgnoreCase(String title);

    Book findByTitleAndPublisher_NameIgnoreCase(String title, String name);
    Book findByTitleAndPublisher_NameAndAuthors_FirstNameAndAuthors_LastNameIgnoreCase(String title, String name, String firstName, String lastName);

    @Query(" from Book where upper(title) like %:title%")
    List<Book> findAllBooksByTitleIgnoreCase(String title);

    List<Book> findAllByAuthors_FirstNameAndAuthors_LastNameIgnoreCase(String firstName, String LastName);

    List<Book> findAllByCategories_NameIgnoreCase(String name);
    List<Book> findAllByPublisher_NameIgnoreCase(String name);
    List<Book> findAllByYear(Integer year);

    List<Book> findAllByStockGreaterThan(Integer stock);
}
