package org.bupt.aiop.restapi.bean;

/**
 * 常量
 * Created by zlren on 2017/6/6.
 */
public class Constant {

    public static String AUTH_CODE = "1993";
    public static String DEFAULT_PASSWORD = "123456";


    public static Integer CRUD_SUCCESS = 1;
    public static Integer CRUD_FAILURE = 0;
    public static String SUCCESS = "SUCCESS";
    public static String FAILURE = "FAILURE";
    public static String IDENTITY = "IDENTITY";
    public static String TOKEN = "TOKEN";



    public static String PAGE_NOW = "pageNow";
    public static String PAGE_SIZE = "pageSize";


    //角色
    public static String ADMIN = "系统管理员";
    public static String DEVELOPER = "开发者";


    //配置文件
    public static String LEVEL_PROPERTIES_FILE_PATH = "level.properties";

    //系统环境配置
    public static Integer SMS_CODE_EXPIRE = 60;
    public static Integer SMS_CODE_LEN = 4;

    //Token
    public static String TOKEN_ISSUER = "aiop";
    public static Long TOKEN_DURATION = 6000000L;
    public static String TOKEN_API_KEY_SECRET = "yaochenkun_19931108";



    //文件服务器
    public static String FILE_PATH = "C:\\eyes_ims\\frontend\\resources\\";
    //public static String FILE_PATH = "/Users/ken/Documents/";
}
