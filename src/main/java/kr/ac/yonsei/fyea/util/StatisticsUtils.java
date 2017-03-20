package kr.ac.yonsei.fyea.util;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.Student;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues;
import org.apache.commons.math3.stat.inference.TestUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class StatisticsUtils {

    public static final String TOTAL = "전체";

    public static double chiSquareTest(Map<String, DescriptiveStatistics> statisticsMap, Map<String, Frequency> frequencyMap, Collection<String> conditions, Collection<String> answers) {
        Long totalN = statisticsMap.values().stream().map(DescriptiveStatistics::getN).mapToLong(Long::longValue).sum();
        Frequency aggregatedFrequency = getAggregatedFrequency(frequencyMap);

        List<Double> expected = new ArrayList<>();
        List<Long> observed = new ArrayList<>();

        answers.forEach(surveyAnswer -> {
            for (String condition : conditions) {
                Frequency frequency = frequencyMap.get(condition);
                if (frequency.getCount(surveyAnswer) == 0) {
                    continue;
                }

                observed.add(frequency.getCount(surveyAnswer));

                double expectedPct = (double) aggregatedFrequency.getCount(surveyAnswer) / totalN;
                long N = statisticsMap.get(condition).getN();
                expected.add(N * expectedPct);
            }
        });

        return TestUtils.chiSquare(Doubles.toArray(expected), Longs.toArray(observed));
    }

//    public static Map<String, Frequency> getFrequencyMap(List<Student> students, String columnName, List<String> answers, String key) {
//        Map<String, Frequency> frequencyMap = new HashMap<>();
//
//        answers.forEach(answer -> {
//            Frequency frequency = new Frequency();
//
//            students.stream()
//                    .filter(student -> answer.equals(student.getPersonalInfo().getRecords().get(columnName)))
//                    .map(student -> student.getSurveyInfo().getRecords().get(key))
//                    .filter(Objects::nonNull)
//                    .forEach(frequency::addValue);
//
//            frequencyMap.put(answer, frequency);
//        });
//
//        return frequencyMap;
//    }

    private static Stream<String> mapRecords(Stream<Student> stream, AnswerMap surveyAnswerMap) {
        return mapRecords(stream, surveyAnswerMap.getId());
    }

    private static Stream<String> mapRecords(Stream<Student> stream, String key) {
        switch (DataUtils.getDataType(key)) {
            case COUNSELING:
                if (key.equals(ColumnTypes.COUNSELING_COUNT)) {
                    return stream.map(student -> student.getCounselingInfo().size()).map(Object::toString);
                }

                if (key.equals(ColumnTypes.COUNSELING_TOPIC)) {
                    return stream.flatMap(student -> student.getCounselingInfo().stream()
                            .flatMap(counselingInfo -> counselingInfo.getRecords().entrySet().stream()
                                    .filter(record -> record.getKey().startsWith(key)).map(Map.Entry::getValue)));
                }

                return stream.flatMap(student -> student.getCounselingInfo().stream().map(counselingInfo -> counselingInfo.getRecords().get(key)));

            case GRADE:
                return stream.map(student -> student.getSurveyInfo().getRecords().get(key));

            case SURVEY:
                return stream.map(student -> student.getSurveyInfo().getRecords().get(key));
        }

        return null;
    }

    private static String getAnswer(Student student, AnswerMap conditionAnswerMap) {
        return getAnswer(student, conditionAnswerMap.getId());
    }

    private static String getAnswer(Student student, String key) {
        return student.getPersonalInfo().getRecords().get(key);
    }

    public static Map<String, Frequency> getFrequencyMap(List<Student> students, AnswerMap conditionAnswerMap, AnswerMap surveyAnswerMap) {
        Map<String, Frequency> frequencyMap = new HashMap<>();

        conditionAnswerMap.getAnswers().forEach(answer -> {
            Frequency frequency = new Frequency();

            Stream<Student> stream = students.stream().filter(student -> answer.equals(getAnswer(student, conditionAnswerMap)));
            mapRecords(stream, surveyAnswerMap).filter(Objects::nonNull).forEach(frequency::addValue);

            frequencyMap.put(answer, frequency);
        });

        frequencyMap.put(TOTAL, getAggregatedFrequency(frequencyMap));
        return frequencyMap;
    }

    public static Map<String, DescriptiveStatistics> getStatistics(Collection<Student> students, AnswerMap conditionAnswerMap, AnswerMap surveyAnswerMap) {
        Map<String, DescriptiveStatistics> statisticsMap = new HashMap<>();

        conditionAnswerMap.getAnswers().forEach(answer -> {
            Stream<Student> stream = students.stream().filter(student -> answer.equals(getAnswer(student, conditionAnswerMap)));

            List<String> results = mapRecords(stream, surveyAnswerMap)
                    .filter(Objects::nonNull)
                    .filter(x -> !Objects.equals(x, ""))
                    .collect(Collectors.toList());

            double[] array = results.stream().mapToDouble(Double::parseDouble).toArray();

            DescriptiveStatistics statistics = new DescriptiveStatistics(array);
            statisticsMap.put(answer, statistics);
        });

        statisticsMap.put(TOTAL, getTotalStatistics(students, surveyAnswerMap));
        return statisticsMap;
    }

    public static DescriptiveStatistics getTotalStatistics(Collection<Student> students, AnswerMap surveyAnswerMap) {
        Stream<Student> stream = students.stream();

        List<String> results = mapRecords(stream, surveyAnswerMap)
                .filter(Objects::nonNull)
                .filter(x -> !Objects.equals(x, ""))
                .collect(Collectors.toList());

        double[] array = results.stream().mapToDouble(Double::parseDouble).toArray();
        return new DescriptiveStatistics(array);
    }

    public static Map<String, Long> getN(Collection<Student> students, AnswerMap conditionAnswerMap, AnswerMap surveyAnswerMap) {
        Map<String, Long> countMap = new HashMap<>();

        conditionAnswerMap.getAnswers().forEach(answer -> {
            Stream<Student> stream = students.stream().filter(student -> answer.equals(getAnswer(student, conditionAnswerMap)));

            Long count = mapRecords(stream, surveyAnswerMap)
                    .filter(Objects::nonNull)
                    .filter(x -> !Objects.equals(x, ""))
                    .count();

            countMap.put(answer, count);
        });

        return countMap;
    }

    public static Collection<double[]> getValues(Map<String, DescriptiveStatistics> statisticsMap) {
        return statisticsMap.values().stream()
                .map(DescriptiveStatistics::getValues)
                .filter(x -> x.length != 0)
                .collect(Collectors.toList());
    }

    public static Frequency getAggregatedFrequency(Map<String, Frequency> frequencyMap) {
        Frequency frequency = new Frequency();
        frequency.merge(frequencyMap.values());
        return frequencyMap.getOrDefault(TOTAL, frequency);
    }

    public static StatisticalSummaryValues getAggregatedStats(Map<String, DescriptiveStatistics> statisticsMap) {
        return AggregateSummaryStatistics.aggregate(statisticsMap.values());
    }
}
