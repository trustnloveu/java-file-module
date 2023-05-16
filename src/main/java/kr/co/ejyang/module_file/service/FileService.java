package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.domain.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    // 파일 정보 가져오기
    FileDto getFileInfo(MultipartFile file);

    // 파일 가져오기
    FileDto getFile(String savePath);

    // 단일 파일 업로드
    void uploadSingleFile(MultipartFile file);

    // 복수 파일 업로드
    void uploadMultiFile(MultipartFile[] files);

}
