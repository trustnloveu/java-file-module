package kr.co.ejyang.module_file.repository;

import kr.co.ejyang.module_file.domain.FileDto;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper {

    FileDto getFile(String filePath);
}
