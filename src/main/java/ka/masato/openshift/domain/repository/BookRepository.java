package ka.masato.openshift.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ka.masato.openshift.domain.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
