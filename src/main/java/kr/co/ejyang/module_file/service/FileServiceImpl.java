package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.config.FileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ejyang.module_file.util.FileCommonUtil;
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

    private final FileCommonUtil fileCommonUtil;

    // 프로퍼티 ( prefix = 'file' )
    private final FileConfig fileConfig;

    // 생성자
    @Autowired
    FileServiceImpl(FileConfig fileConfig, FileCommonUtil fileCommonUtil) {
        this.fileConfig = fileConfig;
        this.fileCommonUtil = fileCommonUtil;
    }

    /*******************************************************************************************
     * 파일 가져오기 ( by 저장 경로 )
     *******************************************************************************************/
    @Override
    public FileDto getFile(String savePath) {
        System.out.println("No Cache");
//        return Optional.ofNullable(fileMapper.getFile("temp")).orElseThrow(RuntimeException::new);
        return null;
    }

    /*******************************************************************************************
     * 파일 정보 가져오기 ( 확장자, 용량, 파일명 )
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
     * 단일 파일 업로드
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String dirType, MultipartFile file, int userIdx) {

        // 업로드 타입 검증 ( public, private, static )
        if (!fileCommonUtil.isValidDirType(dirType)) return null; // TODO ::: throw 에러

        // 파일 검증 ( Null, 파일명, 확장자, 용량 )
        if (!fileCommonUtil.isValidFile(file)) return null; // TODO ::: throw 에러

        String saveDirPath = null;

        // 타입별 저장경로 분기처리
        switch (dirType) {
            case PUBLIC: case STATIC:
                saveDirPath = fileConfig.getStorageEndPoint() + "/" + dirType;
                break;
            case PRIVATE:
                saveDirPath = fileConfig.getStorageEndPoint() + "/" + dirType + "/" + userIdx;
                break;
            default:
                break;

        }

        return saveFile(saveDirPath, file);
    }

    /*******************************************************************************************
     * 복수 파일 업로드
     *******************************************************************************************/
    @Override
    public List<FileDto> uploadMultiFile(MultipartFile[] files) {
        return null;
    }


    /*******************************************************************************************
     * 파일 저장
     *  - 파일명 변환
     *******************************************************************************************/
    private FileDto saveFile(String saveDirPath, MultipartFile file) {
        try {
            // 저장 디렉토리
            File dir = new File(saveDirPath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                // TODO ::: throw 에러
                if (!isDirCreated) return null;
            }

            // 파일명 변환 > 등록
            String convertedFileName = fileCommonUtil.convertFileName(file);
            String storedFilePath = saveDirPath + "/" + convertedFileName;

            File storeFile = new File(storedFilePath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .savePath(storedFilePath)
                    .orgName(file.getOriginalFilename())
                    .saveName(convertedFileName)
                    .extType(FilenameUtils.getExtension(file.getOriginalFilename()))
                    .size(file.getSize())
                    .build();

        } catch (IOException e) {
            // TODO ::: throw 에러
            return null;
        }

    }

}
