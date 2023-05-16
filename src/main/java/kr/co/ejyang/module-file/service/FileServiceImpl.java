package kr.co.ejyang.service;

import kr.co.ejyang.domain.FileDto;
import kr.co.ejyang.repository.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements kr.co.ejyang.service.FileService {

    @Autowired
    private final FileMapper fileMapper;

    @Override
//    @Cacheable(value = "errorCode" , key = "{#code}", cacheManager = "commonModuleCacheManager")
    public FileDto getFile(String code) {
        System.out.println("No Cache");
        return Optional.ofNullable(fileMapper.getFile("temp")).orElseThrow(RuntimeException::new);
    }
}
