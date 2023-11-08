package bdbe.bdbd.controller.file;

import bdbe.bdbd.dto.file.FileResponse;
import bdbe.bdbd.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
                                        @RequestParam("carwashId") Long carwashId) {
        try {
            FileResponse.SimpleFileResponseDTO response = fileService.uploadFile(file, carwashId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while uploading the file.");
        }
    }

    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                         @RequestParam("carwashId") Long carwashId) {
        try {
            if (files == null || files.length == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No files provided");
            }
            List<FileResponse.SimpleFileResponseDTO> response = fileService.uploadFiles(files, carwashId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while uploading the files.");
        }
    }
}
