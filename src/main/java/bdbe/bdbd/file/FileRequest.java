package bdbe.bdbd.file;

import bdbe.bdbd.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class FileRequest {

    @Getter
    @Setter
    public static class FileUploadRequestDTO {
        private MultipartFile file;

        public FileUploadRequestDTO(MultipartFile file) {
            this.file = file;
        }
    }

    // File 엔티티 생성
    @Getter
    @Setter
    public static class FileDTO {
        private String name;
        private String url;
//        private String path;
        private LocalDateTime uploadedAt;
        private Carwash carwash;

        public File toEntity() {
            return File.builder()
                    .name(this.name)
                    .url(this.url)
//                    .path(this.path)
                    .uploadedAt(this.uploadedAt)
                    .carwash(this.carwash)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class FileUpdateDTO {
        private String url;
//        private String path;
        private LocalDateTime uploadedAt;



        public FileUpdateDTO(String url, LocalDateTime uploadedAt) {
            this.url = url;
//            this.path = path;
            this.uploadedAt = uploadedAt;
        }
    }
}