package bdbe.bdbd.file;

import bdbe.bdbd.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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

    @Getter
    @Setter
    public static class FileDTO {

        @NotNull(message = "File name is required.")
        private String name;

        @NotNull(message = "File URL cannot is required.")
        private String url;

        @PastOrPresent(message = "Uploaded time must be in the past or present.")
        private LocalDateTime uploadedAt;

        private Carwash carwash;

        public File toEntity() {
            return File.builder()
                    .name(this.name)
                    .url(this.url)
                    .uploadedAt(this.uploadedAt)
                    .carwash(this.carwash)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class FileUpdateDTO {

        @NotNull(message = "File URL cannot is required.")
        private String url;

        @PastOrPresent(message = "Uploaded time must be in the past or present.")
        private LocalDateTime uploadedAt;



        public FileUpdateDTO(String url, LocalDateTime uploadedAt) {
            this.url = url;
            this.uploadedAt = uploadedAt;
        }
    }
}