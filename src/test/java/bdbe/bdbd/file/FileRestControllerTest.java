//package bdbe.bdbd.file;
//
//import bdbe.bdbd._core.utils.FileUploadUtil;
//import bdbe.bdbd.model.carwash.Carwash;
//import bdbe.bdbd.repository.carwash.CarwashJPARepository;
//import bdbe.bdbd.dto.file.FileResponse;
//import bdbe.bdbd.model.file.File;
//import bdbe.bdbd.service.file.FileService;
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.SdkClientException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class FileRestControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private FileService fileService;
//    @Autowired
//    private CarwashJPARepository carwashJPARepository;
//    @Autowired
//    private FileUploadUtil fileUploadUtil;
//
//    @Test
//    public void testUploadFileSuccess() throws Exception {
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
//        Carwash carwash = carwashJPARepository.findFirstBy();
//        File file = File.builder()
//                .url("http://example.com/s3/test.txt")
//                .name("test.txt")
//                .uploadedAt(LocalDateTime.now())
//                .carwash(carwash)
//                .build();
//
//        Long carwashId = carwash.getId();
//        FileResponse.SimpleFileResponseDTO successfulResponse = new FileResponse.SimpleFileResponseDTO(
//                file, carwashId
//        );
//
//        when(fileUploadUtil.uploadFile(any(), eq(carwashId))).thenReturn(successfulResponse);
//
//        mockMvc.perform(multipart("/api/files/upload")
//                        .file(multipartFile)
//                        .param("carwashId", carwashId.toString())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(successfulResponse.getId()))
//                .andExpect(jsonPath("$.name").value("test.txt"))
//                .andExpect(jsonPath("$.url").value("http://example.com/s3/test.txt"))
//                .andExpect(jsonPath("$.carwashId").value(carwashId));
//
//        verify(fileUploadUtil, times(1)).uploadFile(any(), eq(carwashId));
//    }
//
//    @Test
//    public void testUploadFileAmazonServiceException() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
//        Long carwashId = 1L;
//
//        AmazonServiceException amazonServiceException = new AmazonServiceException("Error message");
//        amazonServiceException.setStatusCode(500);
//        amazonServiceException.setErrorType(AmazonServiceException.ErrorType.Unknown);
//        amazonServiceException.setErrorCode("Internal Error");
//
//        when(fileUploadUtil.uploadFile(any(), eq(carwashId))).thenThrow(amazonServiceException);
//
//        mockMvc.perform(multipart("/api/files/upload")
//                        .file(file)
//                        .param("carwashId", carwashId.toString())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isInternalServerError());
//
//        verify(fileUploadUtil, times(1)).uploadFile(any(), eq(carwashId));
//    }
//
//    @Test
//    public void testUploadFileSdkClientException() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
//        Long carwashId = 1L;
//
//        SdkClientException sdkClientException = new SdkClientException("Error message");
//
//        when(fileUploadUtil.uploadFile(any(), eq(carwashId))).thenThrow(sdkClientException);
//
//        mockMvc.perform(multipart("/api/files/upload")
//                        .file(file)
//                        .param("carwashId", carwashId.toString())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isInternalServerError());
//
//        verify(fileUploadUtil, times(1)).uploadFile(any(), eq(carwashId));
//    }
//}
