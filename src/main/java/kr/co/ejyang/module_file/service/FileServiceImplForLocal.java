package kr.co.ejyang.module_file.service;

import kr.co.ejyang.module_file.config.FileConfig;
import kr.co.ejyang.module_file.exception.FileModuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import kr.co.ejyang.module_file.domain.FileDto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImplForLocal implements FileService {

    // 프로퍼티 ( prefix = 'file' )
    private final FileConfig fileConfig;

    // #########################################################################################
    //                                      [ PUBLIC ]
    // #########################################################################################

    /*******************************************************************************************
     * 파일 조회
     * @param fullPath      : 전체 경로
     *******************************************************************************************/
    @Override
    public byte[] getFile(String fullPath) {
        return get(fullPath);
    }

    /*******************************************************************************************
     * 파일 다운로드 (1) - 경로 Only
     * @param fullPath      : 전체 경로
     *******************************************************************************************/
    @Override
    public InputStreamResource downloadFile(String fullPath) {
        return new InputStreamResource(download(fullPath));
    }

    /*******************************************************************************************
     * 파일 다운로드 (2) - 경로 + 상세
     * @param fullPath      : 전체 경로
     * @param description   : 다운로드 파일명
     *******************************************************************************************/
    @Override
    public InputStreamResource downloadFile(String fullPath, String description) {
        return new InputStreamResource(download(fullPath), description);
    }

    /*******************************************************************************************
     * 단일 파일 업로드 (1)
     * @param saveDirPath   : 저장 경로
     * @param file          : 저장 파일
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String saveType, String saveDirPath, MultipartFile file) {
        return save(saveType, saveDirPath, file);
    }

    /*******************************************************************************************
     * 단일 파일 업로드 (2)
     * @param saveDirPath   : 저장 경로
     * @param fileName      : 저장 파일명
     * @param file          : 저장 파일
     *******************************************************************************************/
    @Override
    public FileDto uploadSingleFile(String saveType, String saveDirPath, String fileName, MultipartFile file) {
        return save(saveType, saveDirPath, fileName, file);
    }

    /*******************************************************************************************
     * 복수 파일 업로드
     * @param saveDirPath           : 저장 경로
     * @param files                 : 저장 파일 리스트
     *
     * @return uploadedFileList     : 업로드된 파일 리스트
     *******************************************************************************************/
    @Override
    public List<FileDto> uploadMultiFiles(String saveType, String saveDirPath, MultipartFile[] files) {

        String targetFileName = ""; // 업로드 중 에러 발생 시, 로깅에 사용
        List<FileDto> uploadedFileList = new ArrayList<>();

        // 파일 업로드 반복문
        try {
            for (MultipartFile file : files) {
                targetFileName = file.getOriginalFilename();
                FileDto fileDto = save(saveType, saveDirPath, file);

                // 파일 업로드 성공 > 반환 객체 추가
                uploadedFileList.add(fileDto);
            }
        } catch (FileModuleException e) {
            // 파일 업로드 실패 > 기존 업로드 파일 삭제 > 에러 반환
            List<String> saveFullPathList = uploadedFileList.stream()
                    .map(FileDto::getFullPath)
                    .collect(Collectors.toList());

            for (String path : saveFullPathList) {
                remove(path);
            }

            throw new FileModuleException(String.format("다중 파일 업로드에 실패했습니다. ( %s )", targetFileName));
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
     * @param saveType      : 저장 타입 ( public, private )
     * @param saveDirPath   : 저장 경로
     * @param file          : 업로드 요청 파일
     *
     * @return fileDto      : 저장된 파일 객체 정보
     *******************************************************************************************/
    private FileDto save(String saveType, String saveDirPath, MultipartFile file) {
        try {
            // 기본 경로 ( = /{storageEndPoint}/{saveType}/{saveDirPath} )
            String dirPath = setFileUploadPath(saveType, saveDirPath);

            // 저장 디렉토리
            File dir = new File(dirPath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                if (!isDirCreated) {
                    throw new FileModuleException("지정된 경로에 디렉토리를 생성하는데 실패했습니다.");
                }
            }

            // 저장 경로 + 파일명
            String fullPath = dirPath + "/" + file.getOriginalFilename();   // 절대 경로 > 파일 저장

            File storeFile = new File(fullPath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .orgName(file.getOriginalFilename())
                    .saveName(file.getOriginalFilename())
                    .rootDirPath(fileConfig.getEndPoint())
                    .saveDirPath(saveDirPath)
                    .fullPath(fullPath)
                    .url(fileConfig.getUrl())
                    .fileUri(fileConfig.getUrl() + fullPath)
                    .size(file.getSize())
                    .fileType(saveType)
                    .extType(FilenameUtils.getExtension(file.getOriginalFilename()))
                    .build();

        } catch (IOException e) {
            throw new FileModuleException(String.format("파일 저장에 실패했습니다. ( %s, %s )", saveDirPath, file.getOriginalFilename()));
        }
    }

    /*******************************************************************************************
     * 파일 저장 (2)
     *  - 파일명 외부 주입
     *  - 지정 경로 파일 저장
     *
     * @param saveType      : 저장 타입 ( public, private )
     * @param saveDirPath   : 저장 상대 경로
     * @param file          : 업로드 요청 파일
     * @param fileName      : 사용자 지정 파일명
     *
     * @return fileDto      : 저장된 파일 객체 정보
     *******************************************************************************************/
    private FileDto save(String saveType, String saveDirPath, String fileName, MultipartFile file) {
        try {
            // 기본 경로 ( = /{storageEndPoint}/{saveType}/{saveDirPath} )
            String dirPath = setFileUploadPath(saveType, saveDirPath);

            // 저장 디렉토리
            File dir = new File(dirPath);

            // 디렉토리가 확인되지 않을 경우 > 디렉토리 생성
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();

                if (!isDirCreated) {
                    throw new FileModuleException("지정된 경로에 디렉토리를 생성하는데 실패했습니다.");
                }
            }

            // 저장 경로 + 파일명
            String fullPath = dirPath + "/" + fileName;   // 절대 경로 > 파일 저장

            File storeFile = new File(fullPath);
            file.transferTo(storeFile);

            // 저장 정보 반환
            return FileDto.builder()
                    .orgName(file.getOriginalFilename())
                    .saveName(fileName)
                    .rootDirPath(fileConfig.getEndPoint())
                    .saveDirPath(saveDirPath)
                    .fullPath(fullPath)
                    .url(fileConfig.getUrl())
                    .fileUri(fileConfig.getUrl() + fullPath)
                    .size(file.getSize())
                    .fileType(saveType)
                    .extType(FilenameUtils.getExtension(file.getOriginalFilename()))
                    .build();

        } catch (IOException e) {
            throw new FileModuleException(String.format("파일 저장에 실패했습니다. ( %s, %s )", saveDirPath, fileName));
        }
    }

    /*******************************************************************************************
     * 파일 삭제
     *
     * @param fullPath  : 저장 전체 경로
     *******************************************************************************************/
    private void remove(String fullPath) {
        // 파일 삭제
        File targetFile = new File(fullPath);
        boolean isRemoved = targetFile.delete();

        // 파일 삭제 실패
        if (!isRemoved) {
            throw new FileModuleException(String.format("해당 경로에 파일이 존재하지 않습니다. ( %s )", fullPath));
        }
    }

    /*******************************************************************************************
     * 파일 다운로드
     *
     * @param fullPath  : 저장 전체 경로
     *******************************************************************************************/
    private FileInputStream download(String fullPath) {
        try {
            // 반환 Input Stream 설정
            File file = new File(fullPath);

            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileModuleException(String.format("해당 경로에 파일이 존재하지 않습니다. ( %s )", fullPath));
        }

    }

    /*******************************************************************************************
     * 파일 조회
     *
     * @param fullPath  : 저장 전체 경로
     *******************************************************************************************/
    private byte[] get(String fullPath) {
        try {
            // 반환 Input Stream 설정
            File file = new File(fullPath);

            // InputStream
            InputStream inputStream = new FileInputStream(file);

            // byte[]
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileModuleException(String.format("해당 경로의 파일을 읽어올 수 없습니다. ( %s )", fullPath));
        }

    }

    /*******************************************************************************************
     * 파일 업로드 경로 설정 ( = /{storageEndPoint}/{saveType}/{saveDirPath} )
     *
     * @param saveType      : 파일 타입 ( public, private )
     * @param saveDirPath   : 파일 저장 경로 ( 사용자 입력 )
     *******************************************************************************************/
    private String setFileUploadPath(String saveType, String saveDirPath) {
        return fileConfig.getEndPoint() + "/" + saveType + "/" + saveDirPath;
    }


}