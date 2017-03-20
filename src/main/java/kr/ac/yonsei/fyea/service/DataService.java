package kr.ac.yonsei.fyea.service;

import kr.ac.yonsei.fyea.domain.AnswerMap;
import kr.ac.yonsei.fyea.domain.CodeMap;
import kr.ac.yonsei.fyea.domain.Student;
import kr.ac.yonsei.fyea.util.DataType;
import kr.ac.yonsei.fyea.util.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static kr.ac.yonsei.fyea.util.ColumnTypes.*;
import static kr.ac.yonsei.fyea.util.DataUtils.*;

@CommonsLog
@RequiredArgsConstructor
@Service
public class DataService {

    private final AnswerMapService answerMapService;
    private final CodeMapService codeMapService;
    private final StudentService studentService;

    @Transactional
    public void loadCodeMap(Iterable<CSVRecord> records) {
        logger.info("Load CodeMap from file");
        CodeMap codeMap = codeMapService.get();

        records.forEach(record -> {
            Map<String, String> recordMap = record.toMap();
            updateCodeMap(codeMap, recordMap);
            updateAnswerMap(recordMap);
        });

        codeMapService.save(codeMap);
        
        logger.info("CodeMap loaded from file");
    }

    @Transactional
    public void loadData(Iterable<CSVRecord> records, Iterable<CSVRecord> codeMap) {
        logger.info("Load data from file");
        BidiMap<String, String> columnMap = new DualHashBidiMap<>(loadColumnMap(codeMap));

        records.forEach(record -> {
            Map<String, String> recordMap = record.toMap();
            updateAdmissionYear(recordMap, columnMap);
            updateAdmission(recordMap, columnMap);
            updateGrade(recordMap, columnMap);
            updateMapping(recordMap, columnMap);
            updateMapping(recordMap, columnMap);

            Student student = findStudent(recordMap, columnMap);
            updateStudent(student, recordMap, columnMap);
            updateAnswerMap(recordMap, columnMap);
            studentService.save(student);
        });

        logger.info("Data loaded from file");
    }

    private static void updateAdmissionYear(Map<String, String> recordMap, BidiMap<String, String> columnMap) {
        columnMap.entrySet().stream().filter(entry -> entry.getValue().equals(ID)).forEach(entry -> {
            String key = entry.getKey();
            recordMap.put(ADMISSION_YEAR, recordMap.get(key).substring(0, 4));
        });
    }

    private static void updateAdmission(Map<String, String> recordMap, BidiMap<String, String> columnMap) {
        columnMap.entrySet().stream().filter(entry -> entry.getValue().equals(ADMISSION)).forEach(entry -> {
            String key = entry.getKey();
            recordMap.put(key, recordMap.get(key).substring(0, 2));
        });
    }

    private static void updateGrade(Map<String, String> recordMap, BidiMap<String, String> columnMap) {
        columnMap.entrySet().stream().filter(entry -> entry.getValue().startsWith(GRADE)).forEach(entry -> {
            String key = entry.getKey();
            recordMap.put(key, getGrade(recordMap.get(key)));
        });
    }

    private void updateMapping(Map<String, String> recordMap, BidiMap<String, String> columnMap) {
        columnMap.entrySet().stream().filter(entry -> entry.getKey().startsWith(MAPPING)).forEach(entry -> {
            String key = entry.getKey().replaceAll(MAPPING + "_", "");
            updateRecord(recordMap, answerMapService.findOrCreate(key), columnMap);
        });
    }

    private void updateAnswerMap(Map<String, String> record) {
        String key = record.get(KEY);
        String value = record.get(VALUE);
        String code = record.get(CODE);
        DataType dataType = DataUtils.getDataType(key);

        AnswerMap answerMap = answerMapService.findOrCreate(key);

        if (value != null && code != null) {
            answerMap.getCodeMap().addRecord(value, code);
        }

        if (dataType == DataType.SURVEY || dataType == DataType.GRADE) {
            IntStream.rangeClosed(1, 5).mapToObj(String::valueOf)
                    .filter(answer -> !Objects.equals(record.get(answer), ""))
                    .forEach(answer -> answerMap.getCodeMap().addRecord(answer, record.get(answer)));
        }

        answerMapService.save(answerMap);
    }

    private void updateAnswerMap(Map<String, String> record, BidiMap<String, String> columnMap) {
        columnMap.entrySet().forEach(entry -> Optional.ofNullable(record.get(entry.getKey())).filter(value -> !Objects.equals(value, "")).ifPresent(value -> {
            String key = entry.getValue();
            answerMapService.save(answerMapService.findOrCreate(key).addAnswer(value));
        }));
    }

    private Student findStudent(Map<String, String> record, BidiMap<String, String> columnMap) {
        String id = record.get(columnMap.getKey(ID));
        return studentService.findOrCreate(id);
    }

}
