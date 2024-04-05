package bg.tihomir.bookstore.web;

import bg.tihomir.bookstore.service.MyBookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyBookController {

    private final MyBookService myBookService;

    public MyBookController(MyBookService myBookService) {
        this.myBookService = myBookService;
    }

    @RequestMapping("/books/deleteMyList/{id}")
    public String deleteMyList(@PathVariable("id") Long id) {
        myBookService.deleteById(id);
        return "redirect:/books/my-books";
    }



}
