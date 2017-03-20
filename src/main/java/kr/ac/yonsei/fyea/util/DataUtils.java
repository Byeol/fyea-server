package kr.ac.yonsei.fyea.util;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.CodeMap;
import kr.ac.yonsei.fyea.domain.RecordMap;
import kr.ac.yonsei.fyea.domain.Student;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static kr.ac.yonsei.fyea.util.ColumnTypes.*;
import static kr.ac.yonsei.fyea.util.WorkbookUtils.convert;

@UtilityClass
public class DataUtils {

    private static final DataType DEFAULT_TYPE = DataType.PERSONAL;

    public static DataType getDataType(String key) {
        for (DataType type : DataType.values()) {
            if (key.startsWith(type.toString())) {
                return type;
            }
        }

        return DEFAULT_TYPE;
    }

    public static StringReader loadWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        return new StringReader(convert(sheet));
    }

    public static Map<String, String> loadColumnMap(Iterable<CSVRecord> records) {
        Map<String, String> columnMap = new HashMap<>();
        records.forEach(record -> columnMap.put(record.get(COLUMN), record.get(TYPE)));
        return columnMap;
    }

    public static void updateCodeMap(CodeMap codeMap, Map<String, String> record) {
        String key = record.get(KEY);
        String value = record.get(DESCRIPTION);

        if (key == null || value == null) {
            return;
        }

        RecordMap recordMap;
        DataType dataType = DataUtils.getDataType(key);

        switch (dataType) {
            case SURVEY:
                recordMap = codeMap.getSurveyInfo();
                break;

            case COUNSELING:
                recordMap = codeMap.getSurveyInfo();
                break;

            case GRADE:
                recordMap = codeMap.getSurveyInfo();
                break;

            default:
                recordMap = codeMap.getPersonalInfo();
                break;
        }

        recordMap.addRecord(key, value);
    }

    public static void updateRecord(Map<String, String> record, AnswerMap answerMap, BidiMap<String, String> columnMap) {
        String value = record.get(columnMap.getKey(answerMap.getId()));
        value = getValue(answerMap, value);

        if (Objects.equals(value, "")) {
            value = null;
        }

        record.put(ColumnTypes.MAPPING + "_" + answerMap.getId(), value);
    }

    public static void updateStudent(Student student, Map<String, String> record, BidiMap<String, String> columnMap) {
        RecordMap counselingRecord = new RecordMap();

        columnMap.entrySet().forEach(entry -> Optional.ofNullable(record.get(entry.getKey())).filter(value -> !Objects.equals(value, "")).ifPresent(value -> {
            String key = entry.getValue();

            RecordMap recordMap;
            DataType dataType = DataUtils.getDataType(key);

            switch (dataType) {
                case SURVEY:
                    recordMap = student.getSurveyInfo();
                    break;

                case COUNSELING:
                    recordMap = counselingRecord;
                    break;

                case GRADE:
                    recordMap = student.getSurveyInfo();
                    break;

                default:
                    recordMap = student.getPersonalInfo();
                    break;
            }

            recordMap.addRecord(key, value);
        }));

        if (!counselingRecord.getRecords().isEmpty()) {
            student.getCounselingInfo().add(counselingRecord);
        }
    }

    public static String getGrade(String value) {
        try {
            Double grade = Math.floor(Double.parseDouble(value));

            if (grade < 1) {
                grade = 1.0;
            }

            return String.valueOf(grade.intValue());
        } catch (Exception e) {
            return value;
        }
    }

    public static String getValue(AnswerMap answerMap, String answer) {
        String defaultValue = answerMap.getCodeMap().getRecords().get("");
        return answerMap.getCodeMap().getRecords().getOrDefault(answer, defaultValue);
    }
}
