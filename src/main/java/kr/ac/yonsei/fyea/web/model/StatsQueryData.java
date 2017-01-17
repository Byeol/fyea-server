package kr.ac.yonsei.fyea.web.model;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.CodeMap;
import kr.ac.yonsei.fyea.domain.Student;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class StatsQueryData {
    private final List<Student> students;
    private final AnswerMap conditionAnswerMap;
    private final List<String> keys;
    private final AnswerMap surveyAnswerMap;

    private final CodeMap codeMap;

    public StatsQueryData(List<Student> students, AnswerMap conditionAnswerMap, List<String> keys, CodeMap codeMap) {
        this(students, conditionAnswerMap, keys, null, codeMap);
    }

    public StatsQueryData(List<Student> students, AnswerMap conditionAnswerMap, AnswerMap surveyAnswerMap) {
        this(students, conditionAnswerMap, null, surveyAnswerMap, null);
    }
}
