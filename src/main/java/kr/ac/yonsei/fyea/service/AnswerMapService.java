package kr.ac.yonsei.fyea.service;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.repository.AnswerMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerMapService {

    private final AnswerMapRepository repository;

    public AnswerMap findOrCreate(String id) {
        return Optional.ofNullable(repository.findOne(id)).orElse(new AnswerMap(id));
    }

    public void save(AnswerMap entity) {
        repository.save(entity);
    }
}
