package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.domain.FileDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /*******************************************************************************************
     * 파일 정보 확인
     *******************************************************************************************/
    FileDto getFileInfo(MultipartFile file);

    /*******************************************************************************************
     * 파일 다운로드
     *******************************************************************************************/
    InputStreamResource downloadFile(String savePath);

    /*******************************************************************************************
     * 단일 파일 업로드
     *******************************************************************************************/
    FileDto uploadSingleFile(String fullPath, MultipartFile file);
    FileDto uploadSingleFile(String fullPath, String fileName, MultipartFile file);
    // FileDto uploadSingleFile(String baseDir, String extraPath, int userIdx, MultipartFile file);

    /*******************************************************************************************
     * 복수 파일 업로드
     *******************************************************************************************/
    List<FileDto> uploadMultiFiles(String fullPath, MultipartFile[] file);

    /*******************************************************************************************
     * 파일 삭제
     *******************************************************************************************/
    void removeFile(String fullPath);
}
