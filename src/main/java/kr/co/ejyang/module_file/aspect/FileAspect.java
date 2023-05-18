package kr.co.ejyang.module_file.aspect;

import kr.co.ejyang.module_file.util.FileCommonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class FileAspect {

    private final FileCommonUtil fileCommonUtil;

    FileAspect(@Autowired FileCommonUtil fileCommonUtil) {
        this.fileCommonUtil = fileCommonUtil;
    }

    @Before("execution(* kr.co.ejyang.module_file.service.FileServiceImpl.*(..))")
    public void doBefore(JoinPoint joinPoint) {

        Object args = joinPoint.getArgs();
        Object targets = joinPoint.getTarget();
        System.out.println("Before Args ::: " + args.toString());
        System.out.println("Before Targets ::: " + targets.toString());
//        // 업로드 타입 검증 ( public, private, static )
//            if (!fileCommonUtil.isValidDirType(baseDir)) {
//            throw new FileUploadException("잘못된 파일입니다.");
//        }
//
//        // 파일 검증 ( Null, 파일명, 확장자, 용량 )
//            if (!fileCommonUtil.isValidFile(file)) {
//            throw new FileUploadException("잘못된 파일입니다.");
//        }
    }
}
