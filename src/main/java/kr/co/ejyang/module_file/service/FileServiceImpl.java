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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        // 추가 경로
        baseDir +=
                extraPath != null && !"".equals(extraPath)
                        ? "/" + extraPath
                        : "";

        // private 파일 경로 후처리 ( user idx 추가 )
        if (PRIVATE.equals(baseDir)) {
            baseDir += "/" + userIdx;
        }

        return saveFile(baseDir, file);
    }

    /*******************************************************************************************
     * 복수 파일 업로드 (1)
     * @param fullPath              : 저장 경로 ( 파일명 포함 )
     * @param files                 : 저장 파일 리스트
     *
     * @return uploadedFileList     : 업로드된 파일 리스트
     *******************************************************************************************/
    @Override
    public List<FileDto> uploadMultiFile(String fullPath, MultipartFile[] files) {

        List<FileDto> uploadedFileList = new ArrayList<>();

        // 파일 업로드 반복문
        try {
            for (MultipartFile file : files) {
                FileDto fileDto = saveFile(fullPath, file);

                // 파일 업로드 성공 > 반환 객체 추가
                uploadedFileList.add(fileDto);
            }
        } catch (FileUploadException e) {
            // 파일 업로드 실패 > 기존 업로드 파일 삭제 > 에러 반환
            List<String> savePathList = uploadedFileList.stream()
                    .map(FileDto::getSavePath)
                    .collect(Collectors.toList());

            for (String savePath : savePathList) {
                removeFile(savePath);
            }

            throw new FileUploadException("파일 저장에 실패했습니다.");
        }

        // 업로드 파일 정보 반환
        return uploadedFileList;
    }

    /*******************************************************************************************
     * 복수 파일 업로드 (2)
     * @param baseDir               : 저장 디렉토리 타입 ( public, private, static )
     * @param extraPath             : 추가 경로 ( baseDir 이후 경로 )
     * @param userIdx               : 유저 IDX ( private 파일 업로드, 사용자 디렉토리명 )
     * @param files                 : 저장 파일 리스트
     *
     * @return uploadedFileList     : 업로드된 파일 리스트
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
            // 기본 경로
            String saveFullPath = fileConfig.getEndPoint() + "/" + savePath;

            // 저장 디렉토리
            File dir = new File(saveFullPath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                if (!isDirCreated) {
                    throw new FileUploadException("지정된 경로에 디렉토리를 생성하는데 실패했습니다.");
                }
            }

            // 저장 경로 + 파일명
            String storedFilePath = saveFullPath + "/" + file.getOriginalFilename();

            // 파일명 변환 > 등록
            // String convertedFileName = fileCommonUtil.convertFileName(file);
            // String storedFilePath = savePath + "/" + convertedFileName;

            // TODO ::: 중복체크 ( 동일 경로 & 파일명 )
            // ...

            File storeFile = new File(storedFilePath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .savePath(storedFilePath)
                    .orgName(file.getOriginalFilename())
                    .saveName(file.getOriginalFilename())
                    .extType(FilenameUtils.getExtension(file.getOriginalFilename()))
                    .size(file.getSize())
                    .build();

        } catch (IOException e) {
            throw new FileUploadException("파일 저장에 실패했습니다.");
        }
    }

    /*******************************************************************************************
     * 파일 삭제
     *
     * @param saveFilePath  : 저장 파일 경로 ( 디렉토리 + 파일명 )
     *******************************************************************************************/
    private void removeFile(String saveFilePath) {
        // 파일 삭제
        File targetFile = new File(saveFilePath);
        boolean isRemoved = targetFile.delete();

        // 파일 삭제 실패
        if (!isRemoved) {
            throw new FileUploadException("파일 삭제에 실패했습니다.");
        }
    }

}