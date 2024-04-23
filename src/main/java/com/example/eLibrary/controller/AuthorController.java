package com.example.eLibrary.controller;

import com.example.eLibrary.dto.AuthorDetailsDto;
import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.model.Author;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.AuthorService;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthorController {

    private AuthorService authorService;
    private BookService bookService;
    private UserService userService;

    public AuthorController(AuthorService authorService, BookService bookService, UserService userService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/add-author")
    public String showRegistrationForm(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        AuthorDto author = new AuthorDto();
        author.setAuthorDetails(new AuthorDetailsDto());
        model.addAttribute("author", author);
        return "add-author";
    }

    @PostMapping("/add-author/save")
    public String registration(@Valid @ModelAttribute("author") AuthorDto authorDto,
                               BindingResult result,
                               Model model){
        String firstName = authorDto.getFirstName().trim();
        String lastName = authorDto.getLastName().trim();
        if (firstName == null || firstName.isEmpty()) {
            result.rejectValue("firstName", null, "First name cannot be empty");
        }
        if (lastName == null || firstName.isEmpty()) {
            result.rejectValue("lastName", null, "Last name cannot be empty");
        }
        Author existingAuthor = authorService.findByFirstNameAndLastName(authorDto.getFirstName().trim(), authorDto.getLastName().trim());

        if(existingAuthor != null){
            result.rejectValue("lastName", null,
                    "There is already an author registered with the same name");
        }

        if(result.hasErrors()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            User user = userService.findByUsername(name);
            if(!user.getStatus().equals("ACCEPTED")) {
                model.addAttribute("userStatus", "declined");
            }
            else {
                model.addAttribute("userStatus", "accepted");
            }
            model.addAttribute("author", authorDto);
            return "add-author";
        }

        authorService.save(authorDto);
        return "redirect:/add-author?success";
    }

    @GetMapping("/authors")
    public String authors(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        List<AuthorDto> authors = authorService.findAllAuthors();
        if(authors.size() == 0) {
            return "authors?noData";
        }
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/delete-author/{id}")
    public String deleteAuthor(@PathVariable("id") Integer id) {
        AuthorDto authorDto = authorService.findById(id);
        List<BookDto> books = bookService.findAllByAuthorName(authorDto.getFirstName(), authorDto.getLastName());
        if(books.size() > 0) {
            return "redirect:/authors?error";
        }
        authorService.deleteAuthorById(id);
        return "redirect:/authors";
    }

    @GetMapping("/edit-author/{id}")
    public String showEditAuthorForm(@PathVariable("id") Integer id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        AuthorDto author = authorService.findById(id);
        model.addAttribute("author", author);
        return "edit-author";
    }

    @PostMapping("/edit-author/{id}/save")
    public String saveEditedAuthor(@PathVariable("id") Integer id,
                                     @ModelAttribute("author") AuthorDto authorDto,
                                     BindingResult result,
                                     Model model) {
        Author existingAuthor = authorService.findByFirstNameAndLastName(authorDto.getFirstName().trim(), authorDto.getLastName().trim());
        if (existingAuthor != null && !existingAuthor.getId().equals(id)) {
            return "redirect:/edit-author/" + id + "?error";
        }

        authorDto.setId(id);
        authorService.save(authorDto);
        return "redirect:/authors";
    }
}
