package bdbe.bdbd.dto.file;

import bdbe.bdbd.model.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
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

        @NotBlank(message = "File name is required")
        private String name;

        @NotBlank(message = "File URL is required")
        private String url;

        @PastOrPresent(message = "Uploaded time must be in the past or present")
        private LocalDateTime uploadedAt;

        private Carwash carwash;
    }

    @Getter
    @Setter
    public static class FileUpdateDTO {
        @NotBlank(message = "File URL is required")
        private String url;

        @PastOrPresent(message = "Uploaded time must be in the past or present")
        private LocalDateTime uploadedAt;

        public FileUpdateDTO(String url, LocalDateTime uploadedAt) {
            this.url = url;
            this.uploadedAt = uploadedAt;
        }
    }
}