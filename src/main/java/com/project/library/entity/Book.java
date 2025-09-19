package com.project.library.entity;

import com.project.library.enums.AvailabilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank(message = "ISBN is Required")
    @Column(name = "isbn", nullable = false)
    private String isbn;

    @NotBlank(message = "Title is Required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Author is Required")
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull(message = "Published Year is Required")
    @Column(name = "published_year")
    private Integer publishedYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status")
    private AvailabilityStatus availabilityStatus;

    @Column(name = "deleted")
    private boolean deleted;

    @CreationTimestamp
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
