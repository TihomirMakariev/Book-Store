package bg.tihomir.bookstore.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AddOrUpdateBookDTO {

    private Long id;
    @NotEmpty (message = "Name must not be empty. ")
    @Size(min = 2, max = 30, message = "Size must be between 2 and 30 characters. ")
    private String name;
    @NotEmpty (message = "Author must not be empty. ")
    @Size(min = 2, max = 30, message = "Size must be between 2 and 30 characters. ")
    private String author;
    @NotEmpty (message = "Price must not be empty. ")
    @Size(min = 2, max = 5, message = "Size must be between 2 and 5 characters. ")
    private String price;

    public AddOrUpdateBookDTO() {
    }

    public Long getId() {
        return id;
    }

    public AddOrUpdateBookDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AddOrUpdateBookDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public AddOrUpdateBookDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public AddOrUpdateBookDTO setPrice(String price) {
        this.price = price;
        return this;
    }
}
