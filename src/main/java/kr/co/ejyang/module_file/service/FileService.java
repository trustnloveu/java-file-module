package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.domain.FileDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /*******************************************************************************************
     * 파일 조회
     *******************************************************************************************/
    byte[] getPrivateFile(String fullPath);
    byte[] getPublicFile(String fullPath);

    /*******************************************************************************************
     * 파일 다운로드
     *******************************************************************************************/
    InputStreamResource downloadFile(String fullPath);
    InputStreamResource downloadFile(String fullPath, String description);

    /*******************************************************************************************
     * 단일 파일 업로드
     *******************************************************************************************/
    FileDto uploadSingleFile(String saveType, String fullPath, MultipartFile file);
    FileDto uploadSingleFile(String saveType, String fullPath, String fileName, MultipartFile file);
    // FileDto uploadSingleFile(String baseDir, String extraPath, int userIdx, MultipartFile file);

    /*******************************************************************************************
     * 복수 파일 업로드
     *******************************************************************************************/
    List<FileDto> uploadMultiFiles(String saveType, String fullPath, MultipartFile[] file);

    /*******************************************************************************************
     * 파일 삭제
     *******************************************************************************************/
    void removeFile(String fullPath);
}
