package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.domain.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    // 파일 정보 가져오기
    FileDto getFileInfo(MultipartFile file);

    // 파일 가져오기
    FileDto getFile(String savePath);

    // 단일 파일 업로드
    FileDto uploadSingleFile(String dirType, MultipartFile file, int userIdx);

    // 복수 파일 업로드
    List<FileDto> uploadMultiFile(MultipartFile[] files);

}
