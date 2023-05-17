package kr.co.ejyang.module_file.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

import static kr.co.ejyang.module_file.config.CommonConsts.*;

@Component("CommonUtil")
public class FileCommonUtil {

    @Value("${file.maxSize}")
    private static long maxSize; // 50 MB, 최대 사이즈

    @Value("${file.minSize}")
    private static long minSize; // 1 KB, 최소 사이즈

    @Value("${file.maxLength}")
    private static long maxLength; // 80, 최대 파일명

    // 생성자
    FileCommonUtil() { }

    /*******************************************************************************************
     * 파일 검증 ( Null, 파일명, 용량, 확장자 )
     *******************************************************************************************/
    public boolean isValidFile(MultipartFile file) {
        return !Objects.isNull(file) && isValidLength(file) && isValidSize(file) && isValidLength(file) && isValidExtType(file);
    }


    /*******************************************************************************************
     * 디렉토리 타입 검증 ( public, private, static )
     *******************************************************************************************/
    public boolean isValidDirType(String dirType) {
        return Arrays.asList(DIR_TYPES).contains(dirType);
    }


    /*******************************************************************************************
     * 파일명 길이 확인 ( isValidLength )
     *******************************************************************************************/
    private boolean isValidLength(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).length() <= maxLength;
    }


    /*******************************************************************************************
     * 파일 용량 확인 ( isValidLength ) - 최대/최소 사이즈 체크
     *******************************************************************************************/
    private boolean isValidSize(MultipartFile file) {
        return file.getSize() <= maxSize && file.getSize() >= minSize;
    }


    /*******************************************************************************************
     * 파일 확장자 확인 ( isValidExtType )
     *******************************************************************************************/
    private boolean isValidExtType(MultipartFile file) {

        String originalName = file.getOriginalFilename();                           // 실제 파일명
        String originalNameExtension = FilenameUtils.getExtension(originalName);    // 실제 파일명 확장자

        // 허용 확장자 검증
        return Arrays.stream(ALLOW_EXT_TYPES).anyMatch(s -> s.equalsIgnoreCase(originalNameExtension));
    }


    /*******************************************************************************************
     * 파일명 변환 - 타임스탬프 + 랜덤 정수 ( convertFileName )
     *******************************************************************************************/
    public String convertFileName(MultipartFile file) {

        String originalName = file.getOriginalFilename();                           // 실제 파일명
        String originalNameExtension = FilenameUtils.getExtension(originalName);    // 실제 파일명 확장자

        // 파일명 변환 (타임스템프)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmssSSS");
        String formatDateTime = now.format(formatter);
        int ranInt = (int) (Math.random() * 10000); // 랜덤 정수

        // 최종 파일명
        return ranInt + formatDateTime + "." + Objects.requireNonNull(originalNameExtension).toLowerCase();
    }

}
