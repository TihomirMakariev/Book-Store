package bg.tihomir.bookstore.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "my_books")
public class MyBookEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String imageFileName;

    @Column(name = "user_id")
    private Long userEntity;

    public MyBookEntity() {
    }

    public Long getId() {
        return id;
    }

    public MyBookEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MyBookEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public MyBookEntity setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public MyBookEntity setPrice(String price) {
        this.price = price;
        return this;
    }

    public Long getUserEntity() {
        return userEntity;
    }

    public MyBookEntity setUserEntity(Long userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public MyBookEntity setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
        return this;
    }
}
