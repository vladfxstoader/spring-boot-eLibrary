package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.AuthorDetailsDto;
import com.example.eLibrary.model.AuthorDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorDetailsMapper {
    public AuthorDetails map(AuthorDetailsDto authorDetailsDto) {
        AuthorDetails authorDetails = new AuthorDetails();
        authorDetails.setId(authorDetailsDto.getId());
        authorDetails.setBio(authorDetailsDto.getBio());
        return authorDetails;
    }

    public AuthorDetailsDto map(AuthorDetails authorDetails) {
        AuthorDetailsDto authorDetailsDto = new AuthorDetailsDto();
        authorDetailsDto.setId(authorDetails.getId());
        authorDetailsDto.setBio(authorDetails.getBio());
        return authorDetailsDto;
    }

    public List<AuthorDetails> mapListToAuthorDetails(List<AuthorDetailsDto> authorDetailsDtos) {
        return authorDetailsDtos.stream().map(authorDetailsDto -> map(authorDetailsDto)).collect(Collectors.toList());
    }

    public List<AuthorDetailsDto> mapListToAuthorDetailsDto(List<AuthorDetails> authorDetails) {
        return authorDetails.stream().map(authorDetails1 -> map(authorDetails1)).collect(Collectors.toList());
    }
}