package kr.ac.yonsei.fyea.web.model;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class FrequencyStatisticsData {

    private final Map<String, List<Long>> frequencyMap;
    private final AnswerMap conditionAnswerMap;
    private final AnswerMap surveyAnswerMap;
}
