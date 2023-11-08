package bdbe.bdbd.service.file;

import bdbe.bdbd._core.exception.BadRequestError;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class S3ProxyUploadService {

    private final AmazonS3 s3Client;
    private final String bucketName;
    static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");
    @Autowired
    private FileService fileService;


    public S3ProxyUploadService(
            @Value("${cloud.aws.credentials.accessKey}") String accessKey,
            @Value("${cloud.aws.credentials.secretKey}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("krmp-proxy.9rum.cc") String proxyHost,
            @Value("3128") int proxyPort,
            @Value("${cloud.aws.s3.bucket}") String bucketName) {

        log.info("accessKey:" + accessKey);
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region);

        if ("prod".equals(System.getProperty("spring.profiles.active")) &&
                proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setProxyHost(proxyHost);
            clientConfiguration.setProxyPort(proxyPort);
            builder.withClientConfiguration(clientConfiguration);
        }

        this.s3Client = builder.build();
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        fileService.validateFiles(new MultipartFile[]{file}); // This will throw BadRequestError if validation fails

        String originalFilename = file.getOriginalFilename();
        String mimeType = file.getContentType();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();

        String uniqueFilename = UUID.randomUUID().toString() + extension;
        String keyName = "uploads/" + uniqueFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mimeType);
        metadata.setContentLength(file.getSize());
        metadata.setContentDisposition("inline");

        s3Client.putObject(bucketName, keyName, file.getInputStream(), metadata);
        return s3Client.getUrl(bucketName, keyName).toExternalForm();
    }

    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> uploadResults = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String result = uploadFile(file);
                uploadResults.add(result);
            } catch (BadRequestError e) {
                log.error("File upload failed: {}", e.getMessage());
            }
        }
        return uploadResults;
    }
}