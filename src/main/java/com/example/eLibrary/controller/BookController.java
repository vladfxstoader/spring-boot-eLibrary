package com.example.eLibrary.controller;

import com.example.eLibrary.dto.*;
import com.example.eLibrary.mapper.AuthorMapper;
import com.example.eLibrary.mapper.BookMapper;
import com.example.eLibrary.mapper.CategoryMapper;
import com.example.eLibrary.mapper.PublisherMapper;
import com.example.eLibrary.model.*;
import com.example.eLibrary.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private BookService bookService;
    private PublisherService publisherService;
    private CategoryService categoryService;
    private AuthorService authorService;
    private PublisherMapper publisherMapper;
    private AuthorMapper authorMapper;
    private CategoryMapper categoryMapper;
    private LoanService loanService;
    private BookMapper bookMapper;
    private UserService userService;

    public BookController(BookService bookService, PublisherService publisherService, AuthorService authorService, CategoryService categoryService,
                          PublisherMapper publisherMapper, AuthorMapper authorMapper, CategoryMapper categoryMapper, LoanService loanService,
                          BookMapper bookMapper, UserService userService) {
        this.bookService = bookService;
        this.publisherMapper = publisherMapper;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.authorMapper = authorMapper;
        this.categoryMapper = categoryMapper;
        this.loanService = loanService;
        this.bookMapper = bookMapper;
        this.userService = userService;
    }

    @GetMapping("/add-book")
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
        BookDto book = new BookDto();
        List<PublisherDto> publishers = publisherService.findAllPublishers();
        List<CategoryDto> categories = categoryService.findAllCategories();
        List<AuthorDto> authors = authorService.findAllAuthors();
        model.addAttribute("book", book);
        model.addAttribute("publishers", publishers);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        return "add-book";
    }

    @PostMapping("/add-book/save")
    public String registration(@Valid @ModelAttribute("book") BookDto bookDto,
                               BindingResult result,
                               Model model){
        Publisher publisher = publisherMapper.map(publisherService.findById(bookDto.getPublisher().getId()));
        List<Integer> categoryIds = bookDto.getCategories().stream().map(categoryDto -> categoryDto.getId()).collect(Collectors.toList());
        List<Category> categories = categoryService.findAllById(categoryIds);

        List<Integer> authorIds = bookDto.getAuthors().stream().map(authorDto -> authorDto.getId()).collect(Collectors.toList());
        List<Author> authors = authorService.findAllById(authorIds);

        Integer year = bookDto.getYear();
        Integer stock = bookDto.getStock();

        if(year == null) {
            result.rejectValue("year", null, "Year cannot be null");
        }

        if(stock == null) {
            result.rejectValue("stock", null, "Stock cannot be null");
        }

        if (publisher == null) {
            result.rejectValue("publisher", null, "Invalid publisher");
        }

        if (categories.size() == 0) {
            result.rejectValue("categories", null, "Invalid categories");
        }

        if (authors.size() == 0) {
            result.rejectValue("authors", null, "Invalid authors");
        }

        bookDto.setPublisher(publisherMapper.map(publisher));
        bookDto.setCategories(categoryMapper.mapListToCategoryDto(categories));
        bookDto.setAuthors(authorMapper.mapListToAuthorDto(authors));

        if(result.hasErrors()){
            model.addAttribute("book", bookDto);
            List<PublisherDto> publishers1 = publisherService.findAllPublishers();
            List<CategoryDto> categories1 = categoryService.findAllCategories();
            List<AuthorDto> authors1 = authorService.findAllAuthors();
            model.addAttribute("publishers", publishers1);
            model.addAttribute("authors", authors1);
            model.addAttribute("categories", categories1);
            return "/add-book";
        }

        bookService.save(bookDto);
        return "redirect:/add-book?success";
    }

    @GetMapping("/books")
    public String books(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        List<BookDto> books = bookService.findAllBooks();
        if(books.size() == 0) {
            return "books?noData";
        }
        model.addAttribute("books", books);
        return "books.html";
    }

    @PostMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        Book book = bookService.findById(id);
        List<Loan> loans = book.getLoans();
        if(loans.size() > 0) {
            return "redirect:/books?error";
        }
        bookService.deleteBookById(id);
        return "redirect:/books";
    }

    @GetMapping("/search-book")
    public String search(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        return "search-books";
    }

    @GetMapping("/display-search-book")
    public String search(@RequestParam(required = false, defaultValue = "") String category, @RequestParam(required = false, defaultValue = "") Integer year,
                         @RequestParam(required = false, defaultValue = "") String publisher, @RequestParam(required = false, defaultValue = "") String author,
                         Model model){
        List<BookDto> allBooks = bookService.findAllBooks();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        List<LoanDto> loans = loanService.findAllLoansByUser(name);
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        Set<BookDto> booksBorrowedByUser = new HashSet<>();

        for(LoanDto loanDto : loans)
            if (!loanDto.getStatus().equals("DECLINED")) {
                Integer bookId = loanDto.getBookId();
                Book book = bookService.findById(bookId);
                booksBorrowedByUser.add(bookMapper.map(book));
            }

        List<LoanDto> loansByOtherUsers = loanService.findAllLoans();
        Set<BookDto> booksBorrowedByOtherUsers = new HashSet<>();

        for(LoanDto loanDto : loansByOtherUsers)
            if (!loanDto.getStatus().equals("DECLINED")) {
                if(!loanDto.getUsername().equals(name)) {
                    Integer bookId = loanDto.getBookId();
                    Book book = bookService.findById(bookId);
                        booksBorrowedByOtherUsers.add(bookMapper.map(book));
                }
            }
        Boolean useCategory = false;
        Boolean usePublisher = false;
        Boolean useAuthor = false;
        Boolean useYear = false;
        if(!category.equals(""))
            useCategory = true;
        if(!publisher.equals(""))
            usePublisher = true;
        if(!author.equals(""))
            useAuthor = true;
        if(year != null)
            useYear = true;

        Set<BookDto> allBooksToReturn = new HashSet<>();
        for(BookDto bookDto : allBooks) {
            Boolean useCategoryCurrentBook = false;
            Boolean usePublisherCurrentBook = false;
            Boolean useAuthorCurrentBook = false;
            Boolean useYearCurrentBook = false;
            if(!category.equals("")) {
                List<CategoryDto> categoryDtos = bookDto.getCategories();
                Integer numberOfCorrespondingCategories = 0;
                for(CategoryDto categoryDto : categoryDtos) {
                    if(categoryDto.getName().equalsIgnoreCase(category))
                        numberOfCorrespondingCategories += 1;
                }
                if(numberOfCorrespondingCategories > 0) {
                    useCategoryCurrentBook = true;
                }
            }
            if(!publisher.equals("")) {
                PublisherDto publisherDto = bookDto.getPublisher();
                if(publisherDto.getName().equalsIgnoreCase(publisher)) {
                    usePublisherCurrentBook = true;
                }
            }
            if(!author.equals("")) {
                String[] authorNames = author.split(" ");
                List<AuthorDto> authorDtos = bookDto.getAuthors();
                Integer numberOfCorrespondingAuthors = 0;
                for(AuthorDto authorDto : authorDtos) {
                    if(authorDto.getFirstName().equalsIgnoreCase(authorNames[0]) &&
                            authorDto.getLastName().equalsIgnoreCase(authorNames[1])) {
                        numberOfCorrespondingAuthors += 1;
                    }
                }
                if(numberOfCorrespondingAuthors > 0) {
                    useAuthorCurrentBook = true;
                }
            }
            if(year != null) {
                Integer bookYear = bookDto.getYear();
                if(year.equals(bookYear)) {
                    useYearCurrentBook = true;
                }
            }
            if(useCategory == useCategoryCurrentBook && usePublisher == usePublisherCurrentBook &&
                useYear == useYearCurrentBook && useAuthor == useAuthorCurrentBook) {
                    allBooksToReturn.add(bookDto);
            }
        }

        Set<BookDto> booksBorrowedByUserToReturn = new HashSet<>();
        for(BookDto bookDto : booksBorrowedByUser) {
            Boolean useCategoryCurrentBook = false;
            Boolean usePublisherCurrentBook = false;
            Boolean useAuthorCurrentBook = false;
            Boolean useYearCurrentBook = false;
            if(!category.equals("")) {
                List<CategoryDto> categoryDtos = bookDto.getCategories();
                Integer numberOfCorrespondingCategories = 0;
                for(CategoryDto categoryDto : categoryDtos) {
                    if(categoryDto.getName().equalsIgnoreCase(category))
                        numberOfCorrespondingCategories += 1;
                }
                if(numberOfCorrespondingCategories > 0) {
                    useCategoryCurrentBook = true;
                }
            }
            if(!publisher.equals("")) {
                PublisherDto publisherDto = bookDto.getPublisher();
                if(publisherDto.getName().equalsIgnoreCase(publisher)) {
                    usePublisherCurrentBook = true;
                }
            }
            if(!author.equals("")) {
                String[] authorNames = author.split(" ");
                List<AuthorDto> authorDtos = bookDto.getAuthors();
                Integer numberOfCorrespondingAuthors = 0;
                for(AuthorDto authorDto : authorDtos) {
                    if(authorDto.getFirstName().equalsIgnoreCase(authorNames[0]) &&
                            authorDto.getLastName().equalsIgnoreCase(authorNames[1])) {
                        numberOfCorrespondingAuthors += 1;
                    }
                }
                if(numberOfCorrespondingAuthors > 0) {
                    useAuthorCurrentBook = true;
                }
            }
            if(year != null) {
                Integer bookYear = bookDto.getYear();
                if(year == bookYear) {
                    useYearCurrentBook = true;
                }
            }
            if(useCategory == useCategoryCurrentBook && usePublisher == usePublisherCurrentBook &&
                    useYear == useYearCurrentBook && useAuthor == useAuthorCurrentBook) {
                booksBorrowedByUserToReturn.add(bookDto);
            }
        }

        Set<BookDto> booksBorrowedByOtherUsersToReturn = new HashSet<>();

        for(BookDto bookDto : booksBorrowedByOtherUsers) {
            Boolean useCategoryCurrentBook = false;
            Boolean usePublisherCurrentBook = false;
            Boolean useAuthorCurrentBook = false;
            Boolean useYearCurrentBook = false;
            if(!category.equals("")) {
                List<CategoryDto> categoryDtos = bookDto.getCategories();
                Integer numberOfCorrespondingCategories = 0;
                for(CategoryDto categoryDto : categoryDtos) {
                    if(categoryDto.getName().equalsIgnoreCase(category))
                        numberOfCorrespondingCategories += 1;
                }
                if(numberOfCorrespondingCategories > 0) {
                    useCategoryCurrentBook = true;
                }
            }
            if(!publisher.equals("")) {
                PublisherDto publisherDto = bookDto.getPublisher();
                if(publisherDto.getName().equalsIgnoreCase(publisher)) {
                    usePublisherCurrentBook = true;
                }
            }
            if(!author.equals("")) {
                String[] authorNames = author.split(" ");
                List<AuthorDto> authorDtos = bookDto.getAuthors();
                Integer numberOfCorrespondingAuthors = 0;
                for(AuthorDto authorDto : authorDtos) {
                    if(authorDto.getFirstName().equalsIgnoreCase(authorNames[0]) &&
                            authorDto.getLastName().equalsIgnoreCase(authorNames[1])) {
                        numberOfCorrespondingAuthors += 1;
                    }
                }
                if(numberOfCorrespondingAuthors > 0) {
                    useAuthorCurrentBook = true;
                }
            }
            if(year != null) {
                Integer bookYear = bookDto.getYear();
                if(year == bookYear) {
                    useYearCurrentBook = true;
                }
            }
            if(useCategory == useCategoryCurrentBook && usePublisher == usePublisherCurrentBook &&
                    useYear == useYearCurrentBook && useAuthor == useAuthorCurrentBook) {
                booksBorrowedByOtherUsersToReturn.add(bookDto);
            }
        }

        if(allBooksToReturn.size() == 0) {
            model.addAttribute("booksInDatabase", "false");
        }
        else {
            model.addAttribute("booksInDatabase", "true");
        }

        if(booksBorrowedByUserToReturn.size() == 0) {
            model.addAttribute("booksInDatabaseByUser", "false");
        }
        else {
            model.addAttribute("booksInDatabaseByUser", "true");
        }

        if(booksBorrowedByOtherUsersToReturn.size() == 0) {
            model.addAttribute("booksInDatabaseByOtherUsers", "false");
        }
        else {
            model.addAttribute("booksInDatabaseByOtherUsers", "true");
        }

        if(useCategory || useAuthor || usePublisher || useYear) {
            model.addAttribute("useFilters", "true");
        }
        else {
            model.addAttribute("useFilters", "false");
        }

        model.addAttribute("books", allBooksToReturn);
        model.addAttribute("booksBorrowedByUser", booksBorrowedByUserToReturn);
        model.addAttribute("booksBorrowedByOtherUsers", booksBorrowedByOtherUsersToReturn);
        model.addAttribute("year", year);
        model.addAttribute("publisher", publisher);
        model.addAttribute("author", author);
        model.addAttribute("category", category);
        return "display-search-book";
    }

    @GetMapping("/edit-book/{id}")
    public String showEditBookForm(@PathVariable("id") Integer id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        BookDto book = bookMapper.map(bookService.findById(id));
        List<PublisherDto> publishers = publisherService.findAllPublishers();
        List<CategoryDto> categories = categoryService.findAllCategories();
        List<AuthorDto> authors = authorService.findAllAuthors();
        model.addAttribute("book", book);
        model.addAttribute("publishers", publishers);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        return "edit-book";
    }

    @PostMapping("/edit-book/{id}/save")
    public String saveEditedBook(@PathVariable("id") Integer id,
                                     @ModelAttribute("book") BookDto bookDto,
                                     BindingResult result,
                                     Model model) {

        bookDto.setId(id);
        bookService.save(bookDto);
        return "redirect:/books";
    }
}
