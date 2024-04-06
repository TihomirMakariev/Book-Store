package bg.tihomir.bookstore.service;

import bg.tihomir.bookstore.model.dto.AddOrUpdateBookDTO;
import bg.tihomir.bookstore.model.entity.BookEntity;
import bg.tihomir.bookstore.repository.BookRepository;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository,
                       ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public void save(BookEntity book) {

        bookRepository.save(book);
    }

    public void addBook(AddOrUpdateBookDTO addOrUpdateBookDTO,
                        LocalDate createdAt,
                        String storageFileName) {
        BookEntity newBook = new BookEntity();
        newBook.setName(addOrUpdateBookDTO.getName());
        newBook.setAuthor(addOrUpdateBookDTO.getAuthor());
        newBook.setPrice(addOrUpdateBookDTO.getPrice());
        newBook.setCreatedAt(createdAt);
        newBook.setImageFileName(storageFileName);

        this.bookRepository.save(newBook);
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public AddOrUpdateBookDTO getBookById(Long id) {
        return bookRepository
                .findById(id)
                .map(book -> {
                    AddOrUpdateBookDTO dto = modelMapper.map(book, AddOrUpdateBookDTO.class);
                    dto.setId(book.getId());
                    dto.setName(book.getName());
                    dto.setAuthor(book.getAuthor());
                    dto.setPrice(book.getPrice());
                    return dto;
                })
                .orElseThrow(()->new RuntimeException("Book not found " + id));
    }


    public void updateBook(AddOrUpdateBookDTO updateBookDTO) {

        BookEntity bookEntity = new BookEntity();
        bookRepository
                .findById(updateBookDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        updateBookDTO.getId(),
                        "Book with id " + updateBookDTO.getId() + " not found"));

        bookEntity.setId(updateBookDTO.getId());
        bookEntity.setName(updateBookDTO.getName());
        bookEntity.setAuthor(updateBookDTO.getAuthor());
        bookEntity.setPrice(updateBookDTO.getPrice());
        bookEntity.setImageFileName(updateBookDTO.getImageFileName().toString());

        this.bookRepository.save(bookEntity);
    }

    public void deleteById(Long id){
        bookRepository.deleteById(id);
    }


    public BookEntity findBookById(Long id) {
        return bookRepository.findById(id)
                .orElse(null);
    }
}
