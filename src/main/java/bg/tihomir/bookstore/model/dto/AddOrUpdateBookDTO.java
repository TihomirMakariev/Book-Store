package bg.tihomir.bookstore.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

public class AddOrUpdateBookDTO {

    private Long id;
    @NotEmpty (message = "Name must not be empty. ")
    @Size(min = 2, max = 60, message = "Size must be between 2 and 60 characters. ")
    private String name;
    @NotEmpty (message = "Author must not be empty. ")
    @Size(min = 2, max = 60, message = "Size must be between 2 and 60 characters. ")
    private String author;
    @NotEmpty (message = "Price must not be empty. ")
    @Size(min = 2, max = 5, message = "Size must be between 2 and 5 characters. ")
    private String price;
    private LocalDate createdAt;

    @NotNull(message = "The image file is required")
    private MultipartFile imageFileName;


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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public AddOrUpdateBookDTO setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public MultipartFile getImageFileName() {
        return imageFileName;
    }

    public AddOrUpdateBookDTO setImageFileName(MultipartFile imageFileName) {
        this.imageFileName = imageFileName;
        return this;
    }
}
