package kr.co.ejyang.service;

import kr.co.ejyang.domain.FileDto;
import kr.co.ejyang.repository.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private final FileMapper fileMapper;

    @Override
    @Cacheable(value = "errorCode" , key = "{#code}", cacheManager = "commonModuleCacheManager")
    public FileDto getFile(String code) {
        System.out.println("no cache");
        return Optional.ofNullable(fileMapper.getFile("temp")).orElseThrow(RuntimeException::new);
    }
}
