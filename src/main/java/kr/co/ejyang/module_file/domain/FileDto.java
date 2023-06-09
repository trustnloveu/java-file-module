package kr.co.ejyang.module_file.domain;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class FileDto {

    private String storageKey;      // Storage 키값
    private String orgName;         // 원본 파일명
    private String saveName;        // 저장 파일명
    private String saveDirPath;     // 저장 경로 2 ( 사용자 지정 디렉토리 )
    private String fullPath;        // 전체 경로 3 ( 사용자 지정 경로 + 파일명 )
    private String url;             // 서버 URL
    private long size;              // 파일용량 ( = byte )
    private String fileType;        // 파일 타입 ( public, private )
    private String extType;         // 파일 확장자

}
