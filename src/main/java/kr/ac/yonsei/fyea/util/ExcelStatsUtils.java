package kr.ac.yonsei.fyea.util;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.CodeMap;
import kr.ac.yonsei.fyea.domain.Student;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.inference.TestUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static kr.ac.yonsei.fyea.util.StatisticsUtils.*;
import static kr.ac.yonsei.fyea.util.WorkbookUtils.getCellStyle;

@UtilityClass
public class ExcelStatsUtils {

    private static final String TOTAL = "전체";
    private static final String MEAN = "평균";
    private static final String STDEV = "표준편차";
    private static final String COUNT = "인원(명)";
    private static final String PERCENTAGE = "비율(%)";
    private static final String F_VALUE = "F비";
    private static final String P_VALUE = "p값";
    private static final String CHI_SQUARE_VALUE = "카이제곱";
    private static final String DEFAULT_VALUE = "(무응답)";

    public static void updateSheet2(Sheet sheet, List<Student> students, String columnName, List<String> answers, List<String> keys, CodeMap codeMap) {
//        Row columnRow = sheet.createRow(0);
//        for (int i = 0; i < answers.size(); i++) {
//            Cell cell = columnRow.createCell(i+1);
//            cell.setCellValue(answers.get(i));
//        }
//
//        Integer totalColumn = answers.size()+1;
//        columnRow.createCell(totalColumn).setCellValue(TOTAL);
//
//        Row numberRow = sheet.createRow(1);
//        Integer currentRowNum = 2;
//
//        for (String key : keys) {
//            Row row = sheet.createRow(currentRowNum);
//            Row nextRow = sheet.createRow(currentRowNum+1);
//
//            String description = codeMap.getSurveyInfo().getRecords().get(key);
//            Map<String, DescriptiveStatistics> statisticsMap = StatisticsUtils.getStatistics(students, columnName, answers, key);
//
//            row.createCell(0).setCellValue(description);
//            sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, 0, 0));
//
//            for (int i = 0; i < answers.size(); i++) {
//                Cell cell = row.createCell(i+1);
//                Cell nextCell = nextRow.createCell(i+1);
//                Cell numberCell = numberRow.createCell(i+1);
//
//                DescriptiveStatistics stats = statisticsMap.get(answers.get(i));
//                cell.setCellValue(stats.getMean());
//                nextCell.setCellValue(stats.getStandardDeviation());
//                numberCell.setCellValue(stats.getN());
//            }
//
//            Cell cell = row.createCell(totalColumn);
//            Cell nextCell = nextRow.createCell(totalColumn);
//
//            StatisticalSummary aggregatedStats = getAggregatedStats(statisticsMap);
//            cell.setCellValue(aggregatedStats.getMean());
//            nextCell.setCellValue(aggregatedStats.getStandardDeviation());
//
//            Cell numberCell = numberRow.createCell(totalColumn);
//            numberCell.setCellValue(aggregatedStats.getN());
//
//            double fStatistic = TestUtils.oneWayAnovaFValue(getValues(statisticsMap));
//            double pValue = TestUtils.oneWayAnovaPValue(getValues(statisticsMap));
//
//            Cell fStatCell = row.createCell(totalColumn+1);
//            Cell pValueCell = nextRow.createCell(totalColumn+1);
//            fStatCell.setCellValue(fStatistic);
//            pValueCell.setCellValue(pValue);
//
//            currentRowNum += 2;
//        }
    }

    public static void updateSheet(Sheet sheet, List<Student> students, AnswerMap conditionAnswerMap, List<String> keys, CodeMap codeMap) {
        List<String> answers = conditionAnswerMap.getAnswers();

        Row columnRow = sheet.createRow(0);
        for (int i = 0; i < answers.size(); i++) {
            Cell cell = columnRow.createCell((i*2)+1);
            cell.setCellValue(answers.get(i) + " (N=" + StatisticsUtils.getStatistics(students, conditionAnswerMap, new AnswerMap(keys.get(0))).get(answers.get(i)).getN()+ ")");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, (i*2)+1, (i*2)+2));
        }

        Integer totalColumn = answers.size()*2+1;
        columnRow.createCell(totalColumn).setCellValue(TOTAL);

        Row numberRow = sheet.createRow(1);
        Integer currentRowNum = 2;

        for (String key : keys) {
            Row row = sheet.createRow(currentRowNum);
            Row nextRow = sheet.createRow(currentRowNum+1);

            String description = codeMap.getSurveyInfo().getRecords().get(key);
            Map<String, DescriptiveStatistics> statisticsMap = StatisticsUtils.getStatistics(students, conditionAnswerMap, new AnswerMap(key));

            row.createCell(0).setCellValue(description);
            sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, 0, 0));

            for (int i = 0; i < answers.size(); i++) {
                Cell cell = row.createCell((i*2)+1);
                Cell nextCell = row.createCell((i*2)+2);
                Cell numberCell = numberRow.createCell((i*2)+1);
                Cell numberNextCell = numberRow.createCell((i*2)+2);
                sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, (i*2)+1, (i*2)+1));
                sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, (i*2)+2, (i*2)+2));

                DescriptiveStatistics stats = statisticsMap.get(answers.get(i));
                cell.setCellValue(stats.getMean());
                nextCell.setCellValue(stats.getStandardDeviation());
                numberCell.setCellValue(MEAN);
                numberNextCell.setCellValue(STDEV);

                cell.setCellStyle(getCellStyle(sheet, "0.00"));
                nextCell.setCellStyle(getCellStyle(sheet, "0.00"));
            }

            Cell cell = row.createCell(totalColumn);
            Cell nextCell = row.createCell(totalColumn+1);
            Cell numberCell = numberRow.createCell(totalColumn);
            Cell numberNextCell = numberRow.createCell(totalColumn+1);
            sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, totalColumn, totalColumn));
            sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, totalColumn+1, totalColumn+1));

            StatisticalSummary aggregatedStats = getAggregatedStats(statisticsMap);
            cell.setCellValue(aggregatedStats.getMean());
            nextCell.setCellValue(aggregatedStats.getStandardDeviation());
            numberCell.setCellValue(MEAN);
            numberNextCell.setCellValue(STDEV);

            cell.setCellStyle(getCellStyle(sheet, "0.00"));
            nextCell.setCellStyle(getCellStyle(sheet, "0.00"));

            try {
                double fStatistic = TestUtils.oneWayAnovaFValue(getValues(statisticsMap));
                double pValue = TestUtils.oneWayAnovaPValue(getValues(statisticsMap));

                numberRow.createCell(totalColumn + 2).setCellValue(F_VALUE + "/" + P_VALUE);
                Cell fStatCell = row.createCell(totalColumn + 2);
                Cell pValueCell = nextRow.createCell(totalColumn + 2);
                fStatCell.setCellValue(fStatistic);
                pValueCell.setCellValue(pValue);
            } catch (Exception e) {
                
            }

            currentRowNum += 2;
        }
    }

    public static String getAnswerDescription(AnswerMap answerMap, String answer) {
        String description = answerMap.getCodeMap().getRecords().getOrDefault(answer, answer);

        if (Objects.equals(description, "")) {
            description = DEFAULT_VALUE;
        }

        return description;
    }

    public static void updateSheet(Sheet sheet, List<Student> students, AnswerMap conditionAnswerMap, AnswerMap surveyAnswerMap) {
        Map<String, Long> countMap = getN(students, conditionAnswerMap, surveyAnswerMap);
        Long totalN = countMap.values().stream().mapToLong(Long::longValue).sum();

        Map<String, Frequency> frequencyMap = getFrequencyMap(students, conditionAnswerMap, surveyAnswerMap);
        Frequency aggregatedFrequency = getAggregatedFrequency(frequencyMap);

        List<String> answers = conditionAnswerMap.getAnswers();

        Row columnRow = sheet.createRow(0);
        for (int i = 0; i < answers.size(); i++) {
            Cell cell = columnRow.createCell((i*2)+1);
            cell.setCellValue(MessageFormat.format("{0} (N={1})", answers.get(i), countMap.get(answers.get(i))));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, (i*2)+1, (i*2)+2));
        }

        Integer totalColumn = answers.size()*2+1;
        columnRow.createCell(totalColumn).setCellValue(MessageFormat.format("{0} (N={1})", TOTAL, totalN));

        Row numberRow = sheet.createRow(1);

        AtomicInteger curRowNum = new AtomicInteger(2);
        List<String> surveyAnswers = surveyAnswerMap.getAnswers().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        surveyAnswers.forEach(answer -> {
            Integer currentRowNum = curRowNum.get();
            Row row = sheet.createRow(currentRowNum);
            Row nextRow = sheet.createRow(currentRowNum+1);

            String description = getAnswerDescription(surveyAnswerMap, answer);

            row.createCell(0).setCellValue(description);
            sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, 0, 0));

            for (int i = 0; i < answers.size(); i++) {
                Frequency frequency = frequencyMap.get(answers.get(i));

                Cell cell = row.createCell((i*2)+1);
                Cell nextCell = row.createCell((i*2)+2);
                Cell numberCell = numberRow.createCell((i*2)+1);
                Cell numberNextCell = numberRow.createCell((i*2)+2);

                if (answer.equals("")) {
                    sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, (i*2)+1, (i*2)+2));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + 1, (i * 2) + 1, (i * 2) + 1));
                    sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + 1, (i * 2) + 2, (i * 2) + 2));
                }

                cell.setCellStyle(getCellStyle(sheet, "0"));
                cell.setCellValue(frequency.getCount(answer));

                if (!answer.equals("")) {
                    nextCell.setCellStyle(getCellStyle(sheet, "0.00%"));

                    long N = countMap.get(answers.get(i));
                    if (N == 0) {
                        nextCell.setCellValue(0);
                    } else {
                        nextCell.setCellValue((double) frequency.getCount(answer) / N);
                    }
                }
                numberCell.setCellValue(COUNT);
                numberNextCell.setCellValue(PERCENTAGE);
            }

            Cell cell = row.createCell(totalColumn);
            Cell nextCell = row.createCell(totalColumn+1);
            Cell numberCell = numberRow.createCell(totalColumn);
            Cell numberNextCell = numberRow.createCell(totalColumn+1);

            if (answer.equals("")) {
                sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, totalColumn, totalColumn+1));
            } else {
                sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, totalColumn, totalColumn));
                sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum+1, totalColumn+1, totalColumn+1));

            }
            cell.setCellStyle(getCellStyle(sheet, "0"));
            cell.setCellValue(aggregatedFrequency.getCount(answer));

            if (!answer.equals("")) {
                nextCell.setCellStyle(getCellStyle(sheet, "0.00%"));
                nextCell.setCellValue((double) aggregatedFrequency.getCount(answer) / totalN);
            }
            numberCell.setCellValue(COUNT);
            numberNextCell.setCellValue(PERCENTAGE);
            curRowNum.addAndGet(2);
        });

        if (DataUtils.getDataType(surveyAnswerMap.getId()) == DataType.SURVEY) {
            Map<String, DescriptiveStatistics> statisticsMap = getStatistics(students, conditionAnswerMap, surveyAnswerMap);
            double chiSquareValue = chiSquareTest(statisticsMap, frequencyMap, answers, surveyAnswerMap.getAnswers());

            numberRow.createCell(totalColumn+2).setCellValue(CHI_SQUARE_VALUE);
            Cell chiSquareCell = sheet.getRow(2).createCell(totalColumn+2);
            sheet.addMergedRegion(new CellRangeAddress(2, (surveyAnswerMap.getAnswers().size()*2)-1, totalColumn+2, totalColumn+2));
            chiSquareCell.setCellValue(chiSquareValue);
        }
    }
}
