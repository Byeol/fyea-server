package kr.ac.yonsei.fyea.web;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.repository.AnswerMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@RestController
public class AnswersController {

    private final AnswerMapRepository answerMapRepository;

    @GetMapping("/answers")
    public List<String> getAvailableAnswers() {
        return answerMapRepository.findAll()
                .stream()
                .filter(AnswerMap::hasAnswer)
                .map(AnswerMap::getId)
                .sorted()
                .collect(Collectors.toList());
    }

}
