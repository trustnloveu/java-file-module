package kr.co.ejyang.repository;

import kr.co.ejyang.domain.FileDto;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper {

    public FileDto getFile(String filePath);
}
