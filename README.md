# module-file-util
파일 관련 공통 유틸

## 프로젝트 구성
```shell
module-file-util
└── src
    └── main
        └── java
            └── module_file_util
        ├── config
        │   └── CommonConsts.java
        └── util
          └── FileCommonUtil.java
```

## 유틸관련 상수 관리 ( CommonConsts.java )

    + 파일 검증 상수값 관리 ( 용량, 확장자, 스토리지 디렉토리명, etc ... )

## 유틸리티 함수 ( FileCommonUtil.java )

외부에서 호출할 수 있는 public 함수 선언

    + isValidFile : 파일 유효성 검증
    + isValidDirType : 스토리지 디렉토리 검증
    + convertFileName : 파일명 변환
