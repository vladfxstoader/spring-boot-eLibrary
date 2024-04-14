package com.example.eLibrary.controller;

import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.model.Publisher;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.PublisherService;
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
public class PublisherController {
    private PublisherService publisherService;
    private BookService bookService;
    private UserService userService;

    public PublisherController(PublisherService publisherService, BookService bookService, UserService userService) {
        this.publisherService = publisherService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/add-publisher")
    public String showRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        PublisherDto publisher = new PublisherDto();
        model.addAttribute("publisher", publisher);
        return "add-publisher";
    }

    @PostMapping("/add-publisher/save")
    public String registration(@Valid @ModelAttribute("publisher") PublisherDto publisherDto,
                               BindingResult result,
                               Model model) {
        String name = publisherDto.getName();
        if (name == null) {
            result.rejectValue("name", null, "Publisher name cannot be empty");
        }
        Publisher existingPublisher = publisherService.findByName(publisherDto.getName());

        if(existingPublisher!=null) {
            result.rejectValue("name", null, "There is already a publisher registered with the same name");
        }

        if(result.hasErrors()) {
            model.addAttribute("publisher", publisherDto);
            return "/add-publisher";
        }

        publisherService.save(publisherDto);
        return "redirect:/add-publisher?success";

    }

    @GetMapping("/publishers")
    public String publishers(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        List<PublisherDto> publishers = publisherService.findAllPublishers();
        if(publishers.size() == 0) {
            return "publishers?noData";
        }
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    @PostMapping("/delete-publisher/{id}")
    public String deletePublisher(@PathVariable("id") Integer id) {
        PublisherDto publisher = publisherService.findById(id);
        List<BookDto> books = bookService.findAllByPublisherName(publisher.getName());
        if(books.size() > 0) {
            return "redirect:/publishers?error";
        }
        publisherService.deletePublisherById(id);
        return "redirect:/publishers";
    }

    @GetMapping("/edit-publisher/{id}")
    public String showEditPublisherForm(@PathVariable("id") Integer id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        PublisherDto publisher = publisherService.findById(id);
        model.addAttribute("publisher", publisher);
        return "edit-publisher";
    }

    @PostMapping("/edit-publisher/{id}/save")
    public String saveEditedCategory(@PathVariable("id") Integer id,
                                     @ModelAttribute("publisher") PublisherDto publisherDto,
                                     BindingResult result,
                                     Model model) {
        Publisher existingPublisher = publisherService.findByName(publisherDto.getName());
        if (existingPublisher != null && !existingPublisher.getId().equals(id)) {
            return "redirect:/edit-publisher/" + id + "?error";
        }

        publisherDto.setId(id);
        publisherService.save(publisherDto);
        return "redirect:/publishers";
    }
}
