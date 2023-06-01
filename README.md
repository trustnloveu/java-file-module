# module-file
파일 스토리지 관리 모듈

## 프로젝트 구성
```shell
module_file
└── src
    └── main
        ├── module_file
        │   ├── config
        │   ├── domain
        │   ├── exception
        │   └── service
        └── resources
            └── application-{profile}.properties
```

## 서비스 함수 ( FileServiceImlpForLocal.java )

* 외부에서 호출할 수 있는 public 함수 선언
* 서버 로컬 스토리지 파일 관리

```shell
- downloadFile : 파일 다운로드
- uploadSingleFile : 단일 파일 업로드
- uploadMultiFiles : 복수 파일 업로드
- removeFile : 파일 삭제
```

## 서비스 함수 ( FileServiceImlpForAWS.java ) - 미구현

* 외부에서 호출할 수 있는 public 함수 선언
* AWS 클라우드 스토리지 파일 관리


## 프로젝트 프로퍼티 ( FileConfig.java )

* `file.endPoint` : 파일 저장 경로 ( Storage EndPoint )