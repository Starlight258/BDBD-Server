package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileRestController {

    private final FileService fileService;

    @Autowired
    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("carwashId") Long carwashId) throws Exception {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file");
        }
        FileResponse.SimpleFileResponseDTO response = fileService.uploadFile(file, carwashId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/uploadMultipleFile")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                         @RequestParam("carwashId") Long carwashId) throws Exception {
        if (files == null || files.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No files provided");
        }
        List<FileResponse.SimpleFileResponseDTO> response = fileService.uploadFiles(files, carwashId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{file_id}")
    public ResponseEntity<?> deleteFile(@PathVariable("file_id") Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
