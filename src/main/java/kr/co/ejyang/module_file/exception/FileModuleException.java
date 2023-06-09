package kr.co.ejyang.module_file.exception;

public class FileModuleException extends RuntimeException {

    // 생성자 1
//    FileUploadException() { }

    // 생성자 2
    public FileModuleException(String message) {
        super(message);
    }

}
