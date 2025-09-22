package com.project.library.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDetails {
    @NotBlank(message = "ISBN is Required")
    private String isbn;

    @NotBlank(message = "Title is Required")
    private String title;

    @NotBlank(message = "Author is Required")
    private String author;

    @NotNull(message = "Published Year is Required")
    private Integer publishedYear;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AvailabilityStatus availabilityStatus;
}
