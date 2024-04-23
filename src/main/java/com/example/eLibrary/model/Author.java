package com.example.eLibrary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String firstName;

    @Column
    private String lastName;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_details_id", referencedColumnName = "id")
    private AuthorDetails authorDetails;
}