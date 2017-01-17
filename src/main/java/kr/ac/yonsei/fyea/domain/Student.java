package kr.ac.yonsei.fyea.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Student {

    @Id
    private final String id;

    private final RecordMap personalInfo = new RecordMap();
    private final RecordMap surveyInfo = new RecordMap();
    private final List<RecordMap> counselingInfo = new ArrayList<>();

    //    private Map<String, String> gradeInfo;
//    private final Map<String, String> surveyInfo = new HashMap<>();
//
//    public void addSurvey(String key, String value) {
//        surveyInfo.put(key, value);
//    }
}