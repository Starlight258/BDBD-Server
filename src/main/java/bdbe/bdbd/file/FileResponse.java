package bdbe.bdbd.file;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class FileResponse {
    @Getter
    @Setter
    public static class SimpleFileResponseDTO {
        private Long id;
        private String name;
        private String url;
//        private String path;
        private LocalDateTime uploadedAt;
        private Long carwashId;

        public SimpleFileResponseDTO(File file, Long carwashId) {
            this.id = file.getId();
            this.name = file.getName();
            this.url = file.getUrl();
//            this.path = file.getPath();
            this.uploadedAt = LocalDateTime.now();
            this.carwashId = carwashId;
        }
    }

}
