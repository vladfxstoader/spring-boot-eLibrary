package com.example.eLibrary.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PublisherDto {
    private Integer id;
    @NotEmpty
    private String name;
}
