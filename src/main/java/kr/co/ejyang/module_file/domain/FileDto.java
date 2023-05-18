package kr.co.ejyang.module_file.domain;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class FileDto {

    @NotNull
    private String orgName; // 원본 파일명

    @Null
    private String saveName; // 저장 파일명

    @Null
    private String savePath; // 저장 경로 ( 디렉토리 + 파일명 )
//
//    @Null
//    private String absolutePath; // 저장 절대 경로
//
//    @Null
//    private String relativePath; // 저장 상대 경로

    @NotNull
    private long size; // 파일욜량 ( = byte )

    @NotNull
    private String extType; // 파일 확장자

    @Null
    private String saveType; // 저장 유형

//    @Null
//    private boolean isUploaded = false; // 업로드 완료 유무 ( 복수 업로드 실패 > 롤백 시 사용 )
}
