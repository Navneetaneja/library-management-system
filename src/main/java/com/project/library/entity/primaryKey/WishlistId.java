package com.project.library.entity.primaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistId implements Serializable {
    private UUID userId;
    private UUID bookId;
}
