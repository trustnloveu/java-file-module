package kr.co.ejyang.module_file.config;

public interface CommonConsts {

    // 디렉토리 타입
    public static final String PUBLIC       = "public";
    public static final String PRIVATE      = "private";
    public static final String STATIC       = "static";
    public static final String[] DIR_TYPES  = { PUBLIC, PRIVATE, STATIC };

    // 허용 확장자 타입
    public static final String[] ALLOW_EXT_TYPES    = { "jpg", "jpeg", "png", "pdf", "xlsx", "xls", "hwp", "hwpx" };

}
