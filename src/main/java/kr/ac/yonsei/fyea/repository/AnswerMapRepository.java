package kr.ac.yonsei.fyea.repository;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerMapRepository extends MongoRepository<AnswerMap, String> {
}
