package bdbe.bdbd.file;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class S3ProxyUploadService {

    private final AmazonS3 s3Client;
    private final String bucketName;

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

        // prod 프로파일이 활성화된 경우에만 프록시 설정을 적용합니다.
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

    public String uploadFile(MultipartFile file) {
        try {
            String keyName = "uploads/" + file.getOriginalFilename();
            s3Client.putObject(bucketName, keyName, file.getInputStream(), null);
            return s3Client.getUrl(bucketName, keyName).toExternalForm();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> uploadResults = new ArrayList<>();
        for (MultipartFile file : files) {
            String result = uploadFile(file);
            uploadResults.add(result);
        }
        return uploadResults;
    }


}
