package kr.co.ejyang.module_file.exception;

public class FileUploadException extends RuntimeException {

    // 생성자 1
    FileUploadException() { }

    // 생성자 2
    public FileUploadException(String message) {
        super(message);
    }

}
