package com.project.library.repository;

import com.project.library.entity.Book;
import com.project.library.models.BookDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Book findByIsbn(String isbn);

    @Query("select new com.project.library.models.BookDetails(b.isbn, b.title, b.author, " +
            "b.publishedYear, b.availabilityStatus) from Book b where (:author is null " +
            "or :author = '' or lower(b.author) = :author) and (:publishedYear is null " +
            "or b.publishedYear = :publishedYear) order by b.title, b.createdAt ")
    Page<BookDetails> getAllBooks(String author, Integer publishedYear,
                                  Pageable pageable);
}
