package kr.ac.yonsei.fyea.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CodeMap {

    @Id
    private final String id;

    private final RecordMap personalInfo = new RecordMap();
    private final RecordMap surveyInfo = new RecordMap();
}
