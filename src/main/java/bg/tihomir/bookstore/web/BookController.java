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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("addBookDTO", addBookDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addBookDTO",
                            bindingResult);

            return "redirect:/books/add";
        }

        bookService.addBook(addBookDTO);

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


//        // Get the current user's ID from the Principal object
//        String userId = principal.getName(); // Assuming the user ID is stored in the username field
//
//        // Retrieve the list of books belonging to the current user
//        List<MyBookEntity> myBooks = myBookService.getAllMyBooks();


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
