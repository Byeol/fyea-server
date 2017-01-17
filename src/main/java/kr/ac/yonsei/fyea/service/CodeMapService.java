package kr.ac.yonsei.fyea.service;

import kr.ac.yonsei.fyea.domain.CodeMap;
import kr.ac.yonsei.fyea.repository.CodeMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CodeMapService {

    private static final String DEFAULT_ID = "default";

    private final CodeMapRepository repository;

    public CodeMap get() {
        return Optional.ofNullable(repository.findOne(DEFAULT_ID)).orElse(new CodeMap(DEFAULT_ID));
    }

    public void save(CodeMap codeMap) {
        repository.save(codeMap);
    }
}
