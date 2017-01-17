package kr.ac.yonsei.fyea.web;

import kr.ac.yonsei.fyea.repository.AnswerMapRepository;
import kr.ac.yonsei.fyea.repository.CodeMapRepository;
import kr.ac.yonsei.fyea.repository.StudentRepository;
import kr.ac.yonsei.fyea.service.DataService;
import kr.ac.yonsei.fyea.service.DataStorageService;
import kr.ac.yonsei.fyea.service.StudentService;
import kr.ac.yonsei.fyea.web.model.DataQueryModel;
import kr.ac.yonsei.fyea.web.view.ExcelDataView;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Log
@RequiredArgsConstructor
@RestController
public class DataController {

    private final StudentRepository studentRepository;
    private final CodeMapRepository codeMapRepository;
    private final AnswerMapRepository answerMapRepository;

    private final StudentService studentService;
    private final DataService dataService;
    private final ExcelDataView excelDataView;

    private final DataStorageService dataStorageService;

    @GetMapping("/data/clear")
    public ResponseEntity<?> clearData() {
        studentRepository.deleteAll();
        codeMapRepository.deleteAll();
        answerMapRepository.deleteAll();
        dataStorageService.initData();

        return ResponseEntity.ok(null);
    }

    @GetMapping("/data/init")
    public ResponseEntity<?> init() {
        dataStorageService.initData();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/data/import")
    public ResponseEntity<?> importData(@RequestBody DataQueryModel queryModel) {
        Workbook workbook = dataStorageService.readFile(queryModel.getDataFile(), queryModel.getPassword());
        Workbook codeMap = dataStorageService.readFile(queryModel.getCodeMapFile());
        dataService.loadData(workbook, codeMap);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/data/export")
    public ModelAndView exportData() {
        return new ModelAndView(excelDataView, "data", studentService.getAllStudent());
    }
}
