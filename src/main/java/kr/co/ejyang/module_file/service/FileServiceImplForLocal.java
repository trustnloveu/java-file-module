package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.config.FileConfig;
import kr.co.ejyang.module_file.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import kr.co.ejyang.module_file.domain.FileDto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import static kr.co.ejyang.module_file.config.CommonConsts.*;

@Slf4j
@Service
public class FileServiceImplForLocal implements FileService {

    // 프로퍼티 ( prefix = 'file' )
    private final FileConfig fileConfig;

    // 생성자
    @Autowired
    FileServiceImplForLocal(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    // #########################################################################################
    //                                      [ PUBLIC ]
    // #########################################################################################

    /*******************************************************************************************
     * 파일 다운로드
     *******************************************************************************************/
    @Override
    public InputStreamResource downloadFile(String savePath) {
        return new InputStreamResource(download(savePath));
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
     * @param savePath      :  저장 파일 상대 경로 ( 파일명 포함 )
     * @param file          : 저장 파일
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String savePath, MultipartFile file) {
        return save(savePath, file);
    }

    /*******************************************************************************************
     * 단일 파일 업로드 (2)
     * @param savePath      :  저장 파일 상대 경로 ( 파일명 포함 )
     * @param file          : 저장 파일
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String savePath, String fileName, MultipartFile file) {
        return save(savePath, fileName, file);
    }

    /*******************************************************************************************
     * 복수 파일 업로드
     * @param savePath              : 저장 파일 상대 경로 ( 파일명 포함 )
     * @param files                 : 저장 파일 리스트
     *
     * @return uploadedFileList     : 업로드된 파일 리스트
     *******************************************************************************************/
    @Override
    public List<FileDto> uploadMultiFiles(String savePath, MultipartFile[] files) {

        List<FileDto> uploadedFileList = new ArrayList<>();

        // 파일 업로드 반복문
        try {
            for (MultipartFile file : files) {
                FileDto fileDto = save(savePath, file);

                // 파일 업로드 성공 > 반환 객체 추가
                uploadedFileList.add(fileDto);
            }
        } catch (FileUploadException e) {
            // 파일 업로드 실패 > 기존 업로드 파일 삭제 > 에러 반환
            List<String> savePathList = uploadedFileList.stream()
                    .map(FileDto::getSavePath)
                    .collect(Collectors.toList());

            for (String path : savePathList) {
                remove(path);
            }

            throw new FileUploadException("파일 저장에 실패했습니다.");
        }

        // 업로드 파일 정보 반환
        return uploadedFileList;
    }


    /*******************************************************************************************
     * 파일 삭제
     *
     * @param savePath  : 저장된 파일 상대 경로 ( 디렉토리 + 파일명 )
     *******************************************************************************************/
    public void removeFile(String savePath) {
        remove(savePath);
    }


    // #########################################################################################
    //                                      [ PRIVATE ]
    // #########################################################################################

    /*******************************************************************************************
     * 파일 저장 (1)
     *  - 파일명 변환
     *  - 지정 경로 파일 저장
     *
     * @param savePath      : 저장 상대 경로
     * @param file          : 업로드 요청 파일
     *
     * @return fileDto      : 저장된 파일 객체 정보
     *******************************************************************************************/
    private FileDto save(String savePath, MultipartFile file) {
        try {
            // 기본 경로
            String dirPath = fileConfig.getEndPoint() + savePath;

            // 저장 디렉토리
            File dir = new File(dirPath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                if (!isDirCreated) {
                    throw new FileUploadException("지정된 경로에 디렉토리를 생성하는데 실패했습니다.");
                }
            }

            // 저장 경로 + 파일명
            String absolutePath = dirPath + "/" + file.getOriginalFilename();   // 절대 경로 > 파일 저장
            String relativePath = savePath + "/" + file.getOriginalFilename();  // 상대 경로 > DB 저장

            File storeFile = new File(absolutePath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .savePath(relativePath)
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
     * 파일 저장 (2)
     *  - 파일명 외부 주입
     *  - 지정 경로 파일 저장
     *
     * @param savePath      : 저장 상대 경로
     * @param file          : 업로드 요청 파일
     * @param fileName      : 사용자 지정 파일명
     *
     * @return fileDto      : 저장된 파일 객체 정보
     *******************************************************************************************/
    private FileDto save(String savePath, String fileName, MultipartFile file) {
        try {
            // 기본 경로
            String dirPath = fileConfig.getEndPoint() + savePath;

            // 저장 디렉토리
            File dir = new File(dirPath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                if (!isDirCreated) {
                    throw new FileUploadException("지정된 경로에 디렉토리를 생성하는데 실패했습니다.");
                }
            }

            // 저장 경로 + 파일명
            String absolutePath = dirPath + "/" + fileName;   // 절대 경로 > 파일 저장
            String relativePath = savePath + "/" + fileName;  // 상대 경로 > DB 저장

            File storeFile = new File(absolutePath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .savePath(relativePath)
                    .orgName(file.getOriginalFilename())    // 원본 파일명
                    .saveName(fileName)                     // 입력받은 파일명
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
     * @param savePath  : 저장된 파일 상대 경로 ( 디렉토리 + 파일명 )
     *******************************************************************************************/
    private void remove(String savePath) {
        // 저장 절대 경로
        String fullPath = fileConfig.getEndPoint() + savePath;

        // 파일 삭제
        File targetFile = new File(fullPath);
        boolean isRemoved = targetFile.delete();

        // 파일 삭제 실패
        if (!isRemoved) {
            throw new FileUploadException("파일 삭제에 실패했습니다.");
        }
    }

    /*******************************************************************************************
     * 파일 다운로드
     *
     * @param savePath  : 저장된 파일 상대 경로 ( 디렉토리 + 파일명 )
     *******************************************************************************************/
    private FileInputStream download(String savePath) {
        try {
            // 저장 절대 경로
            String fullPath = fileConfig.getEndPoint() + savePath;

            // 반환 Input Stream 설정
            File file = new File(fullPath);

            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileUploadException("해당 경로에 파일이 존재하지 않습니다.");
        }

    }

}