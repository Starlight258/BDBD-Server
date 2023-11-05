package bdbe.bdbd._core.errors.utils;

import bdbe.bdbd._core.errors.exception.NotFoundError;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.file.FileJPARepository;
import bdbe.bdbd.file.FileRequest;
import bdbe.bdbd.file.FileResponse;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class FileUploadUtil {

    private final AmazonS3 amazonS3;
    private final FileJPARepository fileRepository;
    private final CarwashJPARepository carwashRepository;

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public FileUploadUtil(AmazonS3 amazonS3, FileJPARepository fileRepository, CarwashJPARepository carwashRepository) {
        this.amazonS3 = amazonS3;
        this.fileRepository = fileRepository;
        this.carwashRepository = carwashRepository;
    }
    /**
     * MultipartFile 객체를 java.io.File 객체로 변환하는 메서드
     * 변환된 파일은 시스템의 임시 디렉토리에 저장된다.
     *
     * @param multipart MultipartFile 객체, 웹 요청으로부터 받은 업로드 파일
     * @return File 객체, 변환된 파일
     * @throws IOException 파일 변환 중에 IO 오류가 발생하면 예외가 발생된다.
     */
    private File convertMultiPartToFile(MultipartFile multipart) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipart.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipart.getBytes());
        }
        return convFile;
    }
    /**
     * 주어진 MultipartFile 객체의 원본 파일명을 가져와 공백을 밑줄('_')로 바꾼 후 반환
     *
     * @param multiPart MultipartFile 객체, 파일 업로드에 사용되는 Spring의 파일 래퍼 객체
     * @return 공백이 밑줄('_')로 변경된 파일명을 반환
     */
    private String generateFileName(MultipartFile multiPart) {
        return multiPart.getOriginalFilename().replace(" ", "_");
    }
    /**
     * 주어진 파일을 Amazon S3 버킷에 업로드
     * Amazon S3의 'putObject' 메서드를 사용하여 파일을 업로드하고, 로그를 기록하여 업로드가 성공적으로 완료되었음을 확인
     *
     * @param fileName 업로드 될 파일의 이름, S3 버킷 내에서 파일의 키
     * @param file     업로드 될 파일 객체, 로컬 파일 시스템에 임시로 저장된 후 S3 버킷에 업로드된다.
     */
    private void uploadFileToS3Bucket(String fileName, File file) {
        try {
            logger.info("Starting upload to S3 bucket: {}", fileName);
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
            logger.info("File successfully uploaded to S3 bucket: {}", fileName);
        } catch (SdkClientException e) {
            // 예외의 메시지와 스택 트레이스를 로그에 남김
            logger.error("File upload to S3 bucket failed: {}", e.getMessage(), e);
            throw e;  // 예외를 다시 던져서 호출자가 알 수 있도록 함
        }
    }

    public FileResponse.SimpleFileResponseDTO uploadFile(MultipartFile multipartFile, Long carwashId) throws Exception {
        Carwash carwash = carwashRepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError("Carwash not found"));
        log.info("start to convert file");
        File file = convertMultiPartToFile(multipartFile);
        log.info("start to generate fileName");
        String fileName = generateFileName(multipartFile);

        try {
            log.info("start to upload file");
            uploadFileToS3Bucket(fileName, file);
        } catch (SdkClientException e) {
            logger.error("File upload failed: {}", e.getMessage());
            throw e;
        }

        log.info("start to make file entity");
        try (S3Object s3Object = amazonS3.getObject(bucketName, fileName)) {
            System.out.println(s3Object.toString());
        } catch (Exception e) {
            System.out.println("Unable to get object from Amazon S3: " + e);
        }

        bdbe.bdbd.file.File newFile = bdbe.bdbd.file.File.builder()
                .name(fileName)
                .url(amazonS3.getUrl(bucketName, fileName).toExternalForm())
//                .path(file.getPath())
                .uploadedAt(LocalDateTime.now())
                .carwash(carwash)
                .build();
        newFile = fileRepository.save(newFile);
        log.info("start to delete file");
        file.delete();  // 로컬 파일 삭제
        log.info("start to make file entity");
        return new FileResponse.SimpleFileResponseDTO(
                newFile,
                newFile.getCarwash().getId()
        );
    }

    /**
     * 여러 개의 MultipartFile 객체를 처리하여 각각을 Amazon S3에 업로드한 후,
     * 각 파일에 대한 메타데이터를 데이터베이스에 저장
     *
     * @param multipartFiles MultipartFile 객체의 배열, 웹 요청으로부터 받은 업로드 파일들
     * @param carwashId 업로드할 파일들과 연관된 Carwash 엔터티의 ID
     * @return List<FileResponse.SimpleFileResponseDTO> 업로드된 파일들의 메타데이터를 담은 DTO 객체의 리스트
     * @throws Exception 파일 업로드 및 메타데이터 저장 중에 예외가 발생하면 던짐
     */
    public List<FileResponse.SimpleFileResponseDTO> uploadFiles(MultipartFile[] multipartFiles, Long carwashId) throws Exception {
        logger.info("start!");
        Carwash carwash = carwashRepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError("Carwash not found"));
        logger.info("file list");
        List<FileResponse.SimpleFileResponseDTO> fileResponseList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            log.info("start to convert file");
            File file = convertMultiPartToFile(multipartFile);
            log.info("start to generate fileName");
            String fileName = generateFileName(multipartFile);

            try {
                log.info("start to upload file");
                uploadFileToS3Bucket(fileName, file);
            } catch (SdkClientException e) {
                logger.error("File upload failed: {}", e.getMessage());
                throw e;
            }
            log.info("start to make file entity");
            FileRequest.FileDTO fileDTO = new FileRequest.FileDTO();
            fileDTO.setName(fileName);
            fileDTO.setUrl(amazonS3.getUrl(bucketName, fileName).toExternalForm());
//            fileDTO.setPath(file.getPath());
            fileDTO.setUploadedAt(LocalDateTime.now());
            fileDTO.setCarwash(carwash);
            log.info("start to make file entity");
            bdbe.bdbd.file.File newFile = fileDTO.toEntity();
            newFile = fileRepository.save(newFile);
            log.info("start to delete file");
            file.delete();  // 로컬 파일 삭제
            log.info("start to response dto");
            FileResponse.SimpleFileResponseDTO fileResponse = new FileResponse.SimpleFileResponseDTO(
                    newFile,
                    newFile.getCarwash().getId()
            );
            fileResponseList.add(fileResponse);
        }

        return fileResponseList;
    }
}
