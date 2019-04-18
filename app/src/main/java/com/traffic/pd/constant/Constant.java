package com.traffic.pd.constant;

public class Constant {

    public static String HOST = "http://47.254.155.98";
    public static String Identity_Name = "Identity_Name";

    public static String Val_Consigner = "Val_Consigner";
    public static String Val_Drivers = "Val_Drivers";
    public static String Val_Company = "Val_Company";

    public static String USER_REGISTER = HOST + "/api/user/reg";
    public static String USER_LOGIN = HOST + "/api/user/do_login";

    public static String GET_CAR_TYPE = HOST + "/api/user/car";

    public static String REGIST_SUCESS = "REGIST_SUCESS";
    public static String LOGIN_SUCESS = "LOGIN_SUCESS";

    public static String USER_SIGN = "USER_SIGN";
    public static String USER_NUM = "USER_NUM";
    public static String UP_IMG = HOST + "/api/Uploadify/imageUp";

    public static String DIVER_STATUS = HOST + "/api/user/driver_status";
    public static String COMPANY_STATUS = HOST + "/api/user/company_status";

    public static String DIVER_UPINFO = HOST + "/api/user/driver";
    public static String COMPANY_UPINFO = HOST + "/api/user/company";
    public static String DIVER_EDIT = HOST + "/api/user/driver_edit";
    public static String COMPANY_EDIT = HOST + "/api/user/company_edit";

    public static String USER_INFO = "USER_INFO";
    public static String UP_ORDER = HOST + "/api/user/order";

    public static String GET_ORDER_LIST = HOST + "/api/user/order_list";
    public static String GET_ORDER_GRAB_LIST = HOST + "/api/user/grab_list";
    public static String GET_Driver_LIST = HOST + "/api/user/company_drivers";
    public static String GET_ADD_Driver = HOST + "/api/user/company_driver_add";

    public static String GET_COMPANY_LIST = HOST + "/api/user/driver_join_list";

    public static String BIND_COMPANY = HOST + "/api/user/driver_join";

    public static String GRAB_ORDER = HOST + "/api/user/grab_order";

    public static String ORDER_DTIVERS = HOST + "/api/user/order_drivers";

    public static String GRAB_DEL = HOST + "/api/user/grab_del";


    public static String ORDER_EDIT = HOST + "/api/user/order_edit";

    public static String ORDER_CONFIRM = HOST + "/api/user/order_driver_confirm";

    public static final int REQUEST_CODE = 1024;
}
