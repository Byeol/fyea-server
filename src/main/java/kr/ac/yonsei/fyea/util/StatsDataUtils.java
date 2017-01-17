package kr.ac.yonsei.fyea.util;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.Statistics;
import kr.ac.yonsei.fyea.domain.Student;
import kr.ac.yonsei.fyea.web.model.ChartData;
import kr.ac.yonsei.fyea.web.model.DescriptiveStatisticsData;
import kr.ac.yonsei.fyea.web.model.FrequencyStatisticsData;
import kr.ac.yonsei.fyea.web.model.StatsQueryData;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class StatsDataUtils {

    public static List<List<String>> toList(ChartData chartData) {
        List<List<String>> list = new ArrayList<>();
        list.add(getRow("", chartData.getLabels()));

        chartData.getDatasets().stream()
                .map(dataset -> getRow(dataset.getLabel(), dataset.getData()))
                .forEach(list::add);

        return list;
    }

    public static Object getStatistics(StatsQueryData queryData) {
        if (queryData.getSurveyAnswerMap() != null) {
            return getStats(queryData.getStudents(), queryData.getConditionAnswerMap(), queryData.getSurveyAnswerMap());
        }

        return getStats(queryData.getStudents(), queryData.getConditionAnswerMap(), queryData.getKeys());
    }

    private static List<String> getRow(String label, List<?> data) {
        List<String> row = new ArrayList<>();

        row.add(label);
        row.addAll(data.stream()
                .map(x -> (x == null ? "" : x))
                .map(Object::toString)
                .collect(Collectors.toList())
        );

        return row;
    }

    private static DescriptiveStatisticsData getStats(List<Student> students, AnswerMap conditionAnswerMap, List<String> keys) {
        Map<String, List<Statistics>> map = new HashMap<>();
        keys.stream().map(AnswerMap::new).forEach(surveyAnswerMap -> {
            Map<String, DescriptiveStatistics> statisticsMap = StatisticsUtils.getStatistics(students, conditionAnswerMap, surveyAnswerMap);

            statisticsMap.entrySet().forEach(entry -> {
                List list = map.getOrDefault(entry.getKey(), new ArrayList<>());
                list.add(new Statistics(entry.getValue()));
                map.put(entry.getKey(), list);
            });
        });

        return new DescriptiveStatisticsData(map);
    }

    private static FrequencyStatisticsData getStats(List<Student> students, AnswerMap conditionAnswerMap, AnswerMap surveyAnswerMap) {
        Map<String, List<Long>> map = new HashMap<>();
        Map<String, Frequency> frequencyMap = StatisticsUtils.getFrequencyMap(students, conditionAnswerMap, surveyAnswerMap);

        surveyAnswerMap.getAnswers().forEach(surveyAnswer -> {
            List<Long> frequencyList = new ArrayList<>();
            conditionAnswerMap.getAnswers().forEach(conditionAnswer ->
                    frequencyList.add(frequencyMap.get(conditionAnswer).getCount(surveyAnswer))
            );
            map.put(surveyAnswer, frequencyList);
        });

        return new FrequencyStatisticsData(map, conditionAnswerMap, surveyAnswerMap);
    }
}
