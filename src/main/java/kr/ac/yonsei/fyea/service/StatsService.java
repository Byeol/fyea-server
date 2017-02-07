package kr.ac.yonsei.fyea.service;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.Student;
import kr.ac.yonsei.fyea.repository.StudentRepository;
import kr.ac.yonsei.fyea.web.model.StatsQueryData;
import kr.ac.yonsei.fyea.web.model.StatsQueryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsService {

    private final StudentRepository studentRepository;
    private final CodeMapService codeMapService;
    private final AnswerMapService answerMapService;

    public StatsQueryData getStatsQueryData(StatsQueryModel queryModel) {
        List<Student> students = getStudents(queryModel);
        String columnName = queryModel.getCondition();
        List<String> keys = queryModel.getSurveys();

        AnswerMap conditionAnswerMap = answerMapService.findOrCreate(columnName);

        if (Objects.equals(queryModel.getFrequency(), true) && keys.size() == 1) {
            AnswerMap surveyAnswerMap = answerMapService.findOrCreate(keys.get(0));
            return new StatsQueryData(students, conditionAnswerMap, surveyAnswerMap);
        }

        return new StatsQueryData(students, conditionAnswerMap, keys, codeMapService.get());
    }

    public List<Student> getStudents(StatsQueryModel queryModel) {
        List<String> idStartsWith = queryModel.getIdStartsWith();

        if (idStartsWith == null || idStartsWith.isEmpty()) {
            return studentRepository.findAll();
        }

        return idStartsWith.stream()
                .map(studentRepository::findByIdStartsWith)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
