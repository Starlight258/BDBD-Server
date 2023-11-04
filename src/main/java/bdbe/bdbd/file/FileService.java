package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
public class FileService {

    private final FileUploadUtil fileUploadUtil;

    @Autowired
    private FileJPARepository fileJPARepository;

    public FileService(FileUploadUtil fileUploadUtil) {
        this.fileUploadUtil = fileUploadUtil;
    }

    public FileResponse.SimpleFileResponseDTO uploadFile(MultipartFile multipartFile, Long carwashId) throws Exception {
        return fileUploadUtil.uploadFile(multipartFile, carwashId);
    }

    public List<FileResponse.SimpleFileResponseDTO> uploadFiles(MultipartFile[] multipartFile, Long carwashId) throws Exception {
        return fileUploadUtil.uploadFiles(multipartFile, carwashId);
    }

    public void deleteFile(Long fileId) {

        File file = fileJPARepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다. id=" + fileId));
        file.changeDeletedFlag(true); //삭제에 대한 플래그
        fileJPARepository.save(file);
    }


}
