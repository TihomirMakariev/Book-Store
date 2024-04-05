package bg.tihomir.bookstore.service;

import bg.tihomir.bookstore.model.dto.AddOrUpdateBookDTO;
import bg.tihomir.bookstore.model.entity.BookEntity;
import bg.tihomir.bookstore.model.entity.MyBookEntity;
import bg.tihomir.bookstore.repository.MyBookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyBookService {

    private final MyBookRepository myBookRepository;
    private final ModelMapper modelMapper;

    public MyBookService(MyBookRepository myBookRepository,
                         ModelMapper modelMapper) {
        this.myBookRepository = myBookRepository;
        this.modelMapper = modelMapper;
    }

    public void saveMyBook (MyBookEntity book) {
        myBookRepository.save(book);
    }

    public List<MyBookEntity> getAllMyBooks() {
        return myBookRepository.findAll();
    }

    public void deleteById(Long id) {
        myBookRepository.deleteById(id);
    }


    public AddOrUpdateBookDTO getBookById(Long id) {

        return myBookRepository.
                findById(id)
                .map(book -> {
                    AddOrUpdateBookDTO dto = new AddOrUpdateBookDTO();
                    dto = modelMapper.map(book, AddOrUpdateBookDTO.class);
                    dto.setId(book.getId());
                    dto.setName(book.getName());
                    dto.setAuthor(book.getAuthor());
                    dto.setPrice(book.getPrice());
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Book not found " + id));

    }


//    public List<MyBookEntity> getMyBooksByUserId(String userId) {
//        return myBookRepository.findMyBookEntityByName(userId);
//
//    }
}
