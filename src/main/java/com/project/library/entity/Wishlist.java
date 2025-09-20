package com.project.library.entity;

import com.project.library.entity.primaryKey.WishlistId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(WishlistId.class)
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Id
    @Column(name = "book_id", nullable = false)
    private UUID bookId;
}
