package kr.co.ejyang.module_file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import kr.co.ejyang.module_file.domain.FileDto;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    /*******************************************************************************************
     * 파일 가져오기 ( by 저장 경로 )
     *******************************************************************************************/
//    @Cacheable(value = "errorCode" , key = "{#code}", cacheManager = "commonModuleCacheManager")
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
                .name(file.getOriginalFilename())
                .type(FilenameUtils.getExtension(file.getOriginalFilename()))
                .size(file.getSize())
                .build();
    }

    /*******************************************************************************************
     * 단일 파일 업로드
     *******************************************************************************************/
    @Override
    public void uploadSingleFile(MultipartFile file) {

    }

    /*******************************************************************************************
     * 복수 파일 업로드
     *******************************************************************************************/
    @Override
    public void uploadMultiFile(MultipartFile[] files) {

    }

}
