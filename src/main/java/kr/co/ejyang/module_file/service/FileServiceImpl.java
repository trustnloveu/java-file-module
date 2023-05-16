package kr.co.ejyang.module_file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ejyang.module_file.util.CommonUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import kr.co.ejyang.module_file.domain.FileDto;

import java.io.File;
import java.io.IOException;

import static kr.co.ejyang.module_file.config.CommonConsts.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private static CommonUtil commonUtil;
    private static String storageEndPoint;

    FileServiceImpl(
            CommonUtil commonUtil,
            @Value("${storage.endpoint}") String storageEndPoint
    ) {
        FileServiceImpl.commonUtil = commonUtil;
        FileServiceImpl.storageEndPoint = storageEndPoint;
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
    public FileDto uploadSingleFile(String dirType, int userIdx, MultipartFile file) {

        // 업로드 타입 검증 ( public, private, static )
        if (!commonUtil.isValidDirType(dirType)) return null; // TODO ::: throw 에러

        // 파일 검증 ( Null, 파일명, 확장자, 용량 )
        if (!commonUtil.isValidFile(file)) return null; // TODO ::: throw 에러

        String saveDirPath = null;

        // 타입별 저장경로 분기처리
        switch (dirType) {
            case PUBLIC: case STATIC:
                saveDirPath = storageEndPoint + "/" + dirType;
                break;
            case PRIVATE:
                saveDirPath = storageEndPoint + "/" + dirType + "/" + userIdx;
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
    public void uploadMultiFile(MultipartFile[] files) {

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
            String convertedFileName = commonUtil.convertFileName(file);
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
