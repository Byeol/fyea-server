package kr.ac.yonsei.fyea.storage.web;

import kr.ac.yonsei.fyea.storage.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class StorageController {

    private final FileSystemStorageService storageService;

    @GetMapping("/storage")
    public List<String> getStorage() {
        return storageService.loadAll().map(Path::toString).collect(Collectors.toList());
    }
}
