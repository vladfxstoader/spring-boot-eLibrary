package com.example.eLibrary.config;

import com.example.eLibrary.utils.converter.AuthorDtoConverter;
import com.example.eLibrary.utils.converter.CategoryDtoConverter;
import com.example.eLibrary.utils.converter.PublisherDtoConverter;
import com.example.eLibrary.utils.converter.AuthorDtoConverter;
import com.example.eLibrary.utils.converter.CategoryDtoConverter;
import com.example.eLibrary.utils.converter.PublisherDtoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PublisherDtoConverter publisherDtoConverter;
    private final CategoryDtoConverter categoryDtoConverter;
    private final AuthorDtoConverter authorDtoConverter;

    public WebConfig(PublisherDtoConverter publisherDtoConverter, CategoryDtoConverter categoryDtoConverter,
                     AuthorDtoConverter authorDtoConverter) {
        this.publisherDtoConverter = publisherDtoConverter;
        this.categoryDtoConverter = categoryDtoConverter;
        this.authorDtoConverter = authorDtoConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(publisherDtoConverter);
        registry.addConverter(categoryDtoConverter);
        registry.addConverter(authorDtoConverter);
    }
}
