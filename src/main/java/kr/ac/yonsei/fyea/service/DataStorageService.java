package kr.ac.yonsei.fyea.service;

import com.google.common.io.Files;
import kr.ac.yonsei.fyea.storage.FileSystemStorageService;
import kr.ac.yonsei.fyea.util.DataStorageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static kr.ac.yonsei.fyea.util.DataStorageUtils.readAsCSV;

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

    public Iterable<CSVRecord> readFile(String filename) {
        return readFile(filename, null);
    }

    public Iterable<CSVRecord> readFile(String filename, String password) {
        String fileExt = Files.getFileExtension(filename);

        try {
            return readAsCSV(DataStorageUtils.readFile(loadFile(filename), fileExt, password));
        } catch (IOException e) {
            throw new RuntimeException("Could not read file");
        }
    }

    private InputStream loadFile(String filename) throws IOException {
        return storageService.loadAsResource(filename).getInputStream();
    }
}
