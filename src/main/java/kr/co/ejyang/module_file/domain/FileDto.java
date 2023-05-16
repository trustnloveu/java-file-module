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
    private String orgName;

    @Null
    private String saveName;

    @Null
    private String savePath;

    @NotNull
    private long size;

    @NotNull
    private String extType;

    @Null
    private String saveType;
}
