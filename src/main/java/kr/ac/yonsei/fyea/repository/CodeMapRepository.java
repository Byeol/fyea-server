package kr.ac.yonsei.fyea.repository;

import kr.ac.yonsei.fyea.domain.CodeMap;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CodeMapRepository extends MongoRepository<CodeMap, String> {
}
