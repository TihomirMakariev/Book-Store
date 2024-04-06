package bg.tihomir.bookstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table (name = "books")
public class BookEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String price;
    @Column
    private LocalDate createdAt;
    @Column(nullable = false)
    private String imageFileName;


    public BookEntity() {
    }

    public String getName() {
        return name;
    }

    public BookEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BookEntity setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public BookEntity setPrice(String price) {
        this.price = price;
        return this;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public BookEntity setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public BookEntity setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
        return this;
    }
}
