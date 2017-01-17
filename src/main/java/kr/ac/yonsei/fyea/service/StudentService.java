package kr.ac.yonsei.fyea.service;

import kr.ac.yonsei.fyea.domain.CodeMap;
import kr.ac.yonsei.fyea.domain.Student;
import kr.ac.yonsei.fyea.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final CodeMapService codeMapService;
    private final StudentRepository repository;

    public Student findOrCreate(String id) {
        return Optional.ofNullable(repository.findOne(id)).orElse(new Student(id));
    }

    public void save(Student student) {
        repository.save(student);
    }

    public List<List<String>> getAllStudent() {
        List<List<String>> list = new ArrayList<>();
        List<String> column = new ArrayList<>();

        CodeMap codeMap = codeMapService.get();

        List<String> personalInfoMap = codeMap.getPersonalInfo().getRecords().keySet().stream().sorted().collect(Collectors.toList());
        List<String> surveyInfoMap = codeMap.getSurveyInfo().getRecords().keySet().stream().sorted().collect(Collectors.toList());
        column.addAll(personalInfoMap);
        column.addAll(surveyInfoMap);
        list.add(column);

        List<Student> students = repository.findAll();

        students.forEach(student -> {
            List<String> record = new ArrayList<>();
            personalInfoMap.forEach(key -> record.add(Optional.ofNullable(student.getPersonalInfo().getRecords().get(key)).orElse("")));
            surveyInfoMap.forEach(key -> record.add(Optional.ofNullable(student.getSurveyInfo().getRecords().get(key)).orElse("")));
            list.add(record);
        });

        return list;
    }

}
