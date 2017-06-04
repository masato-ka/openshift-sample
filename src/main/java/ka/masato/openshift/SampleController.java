package ka.masato.openshift;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ka.masato.openshift.domain.model.Book;
import ka.masato.openshift.domain.service.BookService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path="/api/v1")
public class SampleController {

	private final BookService bookService;
	
	public SampleController(BookService bookService){
		this.bookService = bookService;
	}
	
	@GetMapping(path="/hello")
	public String hello(){
		return "hello";
	}
	
	@GetMapping(path="/books")
	public List<Book> getAllBooks(){
		return bookService.getAllBook(); 
	}
	
	@PostMapping(path="/books")
	public Book addNewBook(@RequestBody Book book){
		log.info(book.toString());
		return bookService.save(book);
	}
	
}
