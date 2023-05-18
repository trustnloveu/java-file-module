package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.config.FileConfig;
import kr.co.ejyang.module_file.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import kr.co.ejyang.module_file.domain.FileDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static kr.co.ejyang.module_file.config.CommonConsts.*;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    // 프로퍼티 ( prefix = 'file' )
    private final FileConfig fileConfig;

    // 생성자
    @Autowired
    FileServiceImpl(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }


    /*******************************************************************************************
     * 파일 다운로드
     *******************************************************************************************/
    @Override
    public FileDto downloadFile(String savePath) {
//        return Optional.ofNullable(fileMapper.getFile("temp")).orElseThrow(RuntimeException::new);
        return null;
    }

    /*******************************************************************************************
     * 파일 정보 확인 ( 확장자, 용량, 파일명 )
     *******************************************************************************************/
    @Override
    public FileDto getFileInfo(MultipartFile file) {
        return FileDto.builder()
                .orgName(file.getOriginalFilename())
                .extType(FilenameUtils.getExtension(file.getOriginalFilename()))
                .size(file.getSize())
                .build();
    }

    /*******************************************************************************************
     * 단일 파일 업로드 (1)
     * @param fullPath      : 저장 경로 ( 파일명 포함 )
     * @param file          : 저장 파일
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String fullPath, MultipartFile file) {
        return saveFile(fullPath, file);
    }

    /*******************************************************************************************
     * 단일 파일 업로드 (2)
     * @param baseDir       : 저장 디렉토리 타입 ( public, private, static )
     * @param extraPath     : 추가 경로 ( baseDir 이후 경로 )
     * @param userIdx       : 유저 IDX ( private 파일 업로드, 사용자 디렉토리명 )
     * @param file          : 저장 파일
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String baseDir, String extraPath, int userIdx, MultipartFile file) {
        // 기본 저장 경로
        String saveDirPath = fileConfig.getEndPoint() + "/" + baseDir;

        // 추가 경로
        saveDirPath +=
                extraPath != null && !"".equals(extraPath)
                        ? "/" + extraPath
                        : "";

        // private 파일 경로 후처리 ( user idx 추가 )
        if (PRIVATE.equals(baseDir)) {
            saveDirPath += "/" + userIdx;
        }

        return saveFile(saveDirPath, file);
    }

    /*******************************************************************************************
     * 복수 파일 업로드 (1)
     * @param fullPath      : 저장 경로 ( 파일명 포함 )
     * @param files         : 저장 파일 리스트
     *******************************************************************************************/
    @Override
    public List<FileDto> uploadMultiFile(String fullPath, MultipartFile[] files) {
        return null;
    }

    /*******************************************************************************************
     * 복수 파일 업로드 (2)
     * @param baseDir       : 저장 디렉토리 타입 ( public, private, static )
     * @param extraPath     : 추가 경로 ( baseDir 이후 경로 )
     * @param userIdx       : 유저 IDX ( private 파일 업로드, 사용자 디렉토리명 )
     * @param files         : 저장 파일 리스트
     *******************************************************************************************/
    @Override
    public List<FileDto> uploadMultiFile(String baseDir, String extraPath, int userIdx, MultipartFile[] files) {
        return null;
    }


    /*******************************************************************************************
     * 파일 저장
     *  - 파일명 변환
     *  - 지정 경로 파일 저장
     *******************************************************************************************/
    private FileDto saveFile(String savePath, MultipartFile file) {
        try {
            // 저장 디렉토리
            File dir = new File(savePath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                if (!isDirCreated) {
                    throw new FileUploadException("잘못된 파일입니다.");
                }
            }

            // 저장 경로 + 파일명
            String storedFilePath = savePath + "/" + file.getOriginalFilename();

            // 파일명 변환 > 등록
            // String convertedFileName = fileCommonUtil.convertFileName(file);
            // String storedFilePath = savePath + "/" + convertedFileName;

            File storeFile = new File(storedFilePath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .savePath(storedFilePath)
                    .orgName(file.getOriginalFilename())
            //        .saveName(convertedFileName)
                    .extType(FilenameUtils.getExtension(file.getOriginalFilename()))
                    .size(file.getSize())
                    .build();

        } catch (IOException e) {
            throw new FileUploadException("잘못된 파일입니다.");
        }
    }

}
