package com.example.eLibrary.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookDto {
    private Integer id;
    @NotEmpty
    private String title;
    private Integer year;
    private Integer stock;
    private PublisherDto publisher;
    private List<AuthorDto> authors;
    private List<CategoryDto> categories;
}
