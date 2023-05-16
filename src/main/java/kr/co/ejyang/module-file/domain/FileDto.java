package kr.co.ejyang.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class FileDto {

    @NotNull
    @Max(100)
    private String fileName;

    @NotNull
    @Max(200)
    private String filePath;
}
