package ka.masato.openshift.domain.service;
import java.util.List;

import org.springframework.stereotype.Service;

import ka.masato.openshift.domain.model.Book;
import ka.masato.openshift.domain.repository.BookRepository;

@Service
public class BookService {

	private final BookRepository bookRepository;
	
	
	public BookService(BookRepository bookRepository){
		this.bookRepository = bookRepository;
	}
	
	
	public Book save(Book book){
		
		bookRepository.save(book);
		return book;
		
	}
	
	public Book getBookwithId(Integer id){
		
		return bookRepository.getOne(id);
		
	}
	
	public List<Book> getAllBook(){
		
		return bookRepository.findAll();
		
	}
	
	
}
