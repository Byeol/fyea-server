package kr.ac.yonsei.fyea.service;

import com.google.common.io.Files;
import kr.ac.yonsei.fyea.storage.FileSystemStorageService;
import kr.ac.yonsei.fyea.util.WorkbookUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@RequiredArgsConstructor
@Service
public class DataStorageService {

    private final DataService dataService;
    private final FileSystemStorageService storageService;

    public void initData() {
        storageService.loadAll()
                .map(Path::toString)
                .filter(x -> x.startsWith("mapping"))
                .map(this::readFile)
                .forEach(dataService::loadCodeMap);
    }

    public Workbook readFile(String filename) {
        return readFile(filename, null);
    }

    public Workbook readFile(String filename, String password) {
        String fileExt = Files.getFileExtension(filename);

        try {
            switch (fileExt) {
                case "xlsx":
                    return WorkbookUtils.readFile(loadFile(filename), password);

                case "xls":
                    return WorkbookUtils.readFileHSSF(loadFile(filename), password);

                default:
                    throw new RuntimeException("Not supported file!");
            }
        } catch (IOException e) {
            return null;
        }
    }

    private InputStream loadFile(String filename) {
        try {
            return storageService.loadAsResource(filename).getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
