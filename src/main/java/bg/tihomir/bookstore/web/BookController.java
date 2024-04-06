package bg.tihomir.bookstore.web;

import bg.tihomir.bookstore.model.dto.AddOrUpdateBookDTO;
import bg.tihomir.bookstore.model.entity.BookEntity;
import bg.tihomir.bookstore.model.entity.MyBookEntity;
import bg.tihomir.bookstore.model.entity.UserEntity;
import bg.tihomir.bookstore.repository.MyBookRepository;
import bg.tihomir.bookstore.repository.UserRepository;
import bg.tihomir.bookstore.service.BookService;
import bg.tihomir.bookstore.service.MyBookService;
import bg.tihomir.bookstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class BookController {


    private final BookService bookService;
    private final MyBookService myBookService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MyBookRepository myBookRepository;

    public BookController(BookService bookService,
                          MyBookService myBookService,
                          UserService userService,
                          UserRepository userRepository,
                          MyBookRepository myBookRepository) {
        this.bookService = bookService;
        this.myBookService = myBookService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.myBookRepository = myBookRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @ModelAttribute("addBookDTO")
    public AddOrUpdateBookDTO addBookDTO() {
        return new AddOrUpdateBookDTO();
    }

    @ModelAttribute("updateBookDTO")
    public AddOrUpdateBookDTO updateBookDTO() {
        return new AddOrUpdateBookDTO();
    }

    @GetMapping("/books/add")
    public String bookRegister() {
        return "book-add";
    }

    @PostMapping("/books/add")
    public String confirmRegister(@Valid AddOrUpdateBookDTO addBookDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if (addBookDTO.getImageFileName().isEmpty()) {
            bindingResult.addError(
                    new FieldError("addBookDTO", "imageFileName", "The image file is required"));
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("addBookDTO", addBookDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addBookDTO",
                            bindingResult);

            return "redirect:/books/add";
        }


        // Save image file
        MultipartFile image = addBookDTO.getImageFileName();
        LocalDate createdAt = LocalDate.now();
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String storageFileName = formattedDate + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }


        bookService.addBook(addBookDTO, createdAt, storageFileName);

        redirectAttributes.addFlashAttribute("message", "Тhe book " + addBookDTO.getName() + " has been successfully added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/books/add";
    }


    @GetMapping("/books/all")
    public String allBooks(Model model) {

        model.addAttribute("allBooks", bookService.getAllBooks());
        return "book-all";
    }


    @GetMapping("/books/my-books")
    public String getMyBook(Model model,
                            Principal principal) {
        String username = principal.getName();
        UserEntity currentUser = userService.findUserByUsername(username);

        List<MyBookEntity> myBooks = myBookRepository.findByUserEntity(currentUser.getId());
        model.addAttribute("myBooks", myBooks);

        model.addAttribute("myBooks", myBooks);

        return "book-myBooks";
    }

    @RequestMapping("/books/my-books/{id}")
    public String getMyList(@PathVariable("id") Long id,
                            Principal principal) {
        String username = principal.getName();
        UserEntity currentUser = userRepository
                .findByEmail(username).orElse(null);

        BookEntity book = bookService.findBookById(id);
        MyBookEntity myBook = new MyBookEntity();
        myBook.setName(book.getName());
        myBook.setAuthor(book.getAuthor());
        myBook.setPrice(book.getPrice());
        myBook.setImageFileName(book.getImageFileName());

        myBook.setUserEntity(currentUser.getId());

        myBookService.saveMyBook(myBook);

        return "redirect:/books/my-books";

    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable("id") Long id,
                           Model model) {
        AddOrUpdateBookDTO book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/books/my-books"; // Redirect to appropriate page if book is not found
        }

        model.addAttribute("updateBookDTO",
                bookService.getBookById(id));

        return "book-edit";
    }

    @PostMapping("/books/edit/{id}")
    public String confirmEditBook(@PathVariable("id") Long id,
                                  @Valid AddOrUpdateBookDTO updateBookDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("updateBookDTO", updateBookDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.updateBookDTO",
                            bindingResult);

            return "redirect:/books/edit/" + id;
        }

        bookService.updateBook(updateBookDTO);

        redirectAttributes.addFlashAttribute("message", "Тhe book " + updateBookDTO.getName() + " has been successfully edit");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/books/all";
    }

    @RequestMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/books/all";

    }


}
