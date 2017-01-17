package kr.ac.yonsei.fyea.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class AnswerMap {

    @Id
    private final String id;

    private final Set<String> answers = new HashSet<>();

    public AnswerMap addAnswer(String answer) {
        answers.add(answer);
        return this;
    }

    public List<String> getAnswers() {
        if (answers.size() == 0) {
            return codeMap.getRecords().keySet().stream().sorted().collect(Collectors.toList());
        }

        return answers.stream().sorted().collect(Collectors.toList());
    }

    private final RecordMap codeMap = new RecordMap();
}
