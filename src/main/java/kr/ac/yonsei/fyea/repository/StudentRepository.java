package kr.ac.yonsei.fyea.repository;

import kr.ac.yonsei.fyea.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {

    List<Student> findByIdStartsWith(String id);
}
