package com.tonghang.web.common.util;

/**
 * 涵盖了server端要使用的所有常量，其中继承了环信Util,负责初始化获得 app管理员的TOKEN
 * @author Administrator
 *
 */
public class Constant extends HuanXinUtil{

	private Constant(){
		
	}
	
	public static final String ISOLATED = "您已被同行管理员封号，如有疑问请联系我们";
	public static final String PASSWORD_INVALID = "账号或密码不正确";
	public static final String NOUSER = "用户不存在";
	public static final String USER_ISOLATED = "该用户已被封号！";
	public static final String EMAIL = "邮件已发送";
	public static final String EMAIL_EXIST = "该邮箱已被注册";
	
	public static final String LOGIN_FAIL = "登录失败！";
	
	public static final String AGREE_ADD_MSG = "同意添加您为好友"; 
	public static final String REFUSE_ADD_MSG = "拒绝添加您为好友";
	public static final String INVITATION_MSG = "请求添加您为好友";
	public static final String NEWBE_MSG = "是和您行业相近的新用户";
	public static final String ISOLATE_MSG = "您已被管理员封号，详情请咨询客服tonghangtinghang@126.com";
	public static final String DEISOLATE_MSG = "您已被管理员解封，欢迎您再次使用";
	public static final String DELETE_TOPIC_MSG = "该话题已被房主解散";
	public static final String EXCHANGE_CARD_MSG = "请求和您交换名片";
	public static final String AGREE_CARD_MSG = "同意和您交换名片";
	public static final String EXCHANGE_SALARY_MSG = "请求和您交换薪资";
	public static final String AGREE_SALARY_MSG = "同意和您交换薪资";
	
	public static final String SM_VALID_MSG = "您的注册验证码是：";
	public static final String PHONE_NOT_EXISTS = "该手机号没有注册同行";
	public static final String EMAIL_NOT_EXISTS = "该邮箱号没有注册同行";
	public static final String PHONE_ALREADY_EXISTS = "该手机号已被注册过";
	public static final String VALIDECODE_ERROR = "验证码错误";
	public static final String MODIFY_SUCCESS = "修改成功！";
	public static final String VALIDECODE_SUCCESS = "验证码正确";
	public static final String VALIDATE_PASSWORD_ERROR = "密码错误！";
	
	public static final String VALIDATE_SECURETY = "未通过安全验证！用户未授权！";
	
	public static final String NO_VALIDCODE = "您还没有获取验证码！";
	public static final String INVALID_CODE = "验证码错误！";
	public static final String VALID_CODE_TIMEOUT = "验证码已失效，请重新获取";
	
	public static final String PICTURE_SERVER = "http://114.215.143.83:8080/tonghang-serverapi";
	public static final String ADV_SERVER = "http://114.215.143.83:8080/tonghang";
	public static final String SHARE_SDK_URL ="https://web.sms.mob.com/sms/verify";
	public static final String APP_LINK = "http://114.215.143.83:8080/tonghang/home/app/Peer_umeng_unsign_signed.apk";
	public static final String NATIVE_ADV_SERVER = "http://192.168.23.1:8080/tonghang";
	public static final String HUANXIN_URL = "http://a1.easemob.com/tonghang/tonghang/";
	public static final String IMAGE_PATH = "/images/";
	public static final String IMAGE_NAME = "sign.jpg";
	public static final String ADV_NAME = "advertise_";
	public static final String ADV_PATH = "/resources/";
	
	public static String JPushBasic="ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==";
	public static final String CLIENT_ID = "YXA6zpmeoHX8EeS5LFOLSMeZrA";
	public static final String CLIENT_SECRET = "YXA62xaG_k1OsmSdGYtjIKE3XbO0ahw";
	public static final String CHANGER_ID = "0ba20eab27544eb8867e543b4db8c13e";
	public static final String SHARE_SDK_APPKEY = "b18fc111a689";
	public static final String CHUANGLAN_PWD = "Tch147269";
	
	/** Gap size*/
	public static final int PAGESIZE = 10;
	public static final int MONTH_GAP = 3;
	//人民币
	public static final int SALARY_GAP_1 = 1000;
	public static final int SALARY_GAP_2 = 10000;
	public static final int SALARY_GAP_3 = 5000;
	//单位：千元
	public static final int SALARY_SIZE = 28;
	
	//邮箱验证码时长
	public static final long EMAIL_CODETIME = 2*60*1000;
	public static final long PHONE_CODETIME = 1*60*1000;
	
	//登录方式
	public static String EMAIL_LOGIN = "email";
	public static String PHONE_LOGIN = "phone";
	
	public static String CREATEGROUP = "server create group";
	public static String HUANXIN_SALT = "HUANXIN-SECRET";
	
	/** METHOD_DELETE value:GET */
	public static String METHOD_GET = "GET";

	/** METHOD_DELETE value:POST */
	public static String METHOD_POST = "POST";

	/** METHOD_DELETE value:PUT */
	public static String METHOD_PUT = "PUT";

	/** METHOD_DELETE value:DELETE */
	public static String METHOD_DELETE = "DELETE";
	

	/** fail code*/
	public static int ERROR = 500;
	public static int SUCCESS = 200;
	public static int UNAUTHORIZED = 401;
	public static int PASSWORD_ERROR = 402;
	
	public static int EMAIL_NO_EXIST_CODE= 410;
	public static int EMAIL_ALREADY_EXIST_CODE= 411;
	public static int PHONE_ALREADY_EXISTS_CODE= 412;
	public static int PHONE_VALIDATE_ERROR_CODE= 413;
	public static int EMAIL_VALIDATE_ERROR_CODE= 414;
	public static int VALIDATE_TIMEOUT_ERROR_CODE= 415;
	public static int NO_VALIDATECODE_ERROR_CODE= 416;
	
	
	
	/**JPUSH Type*/
	/**好友邀请的推送标记*/
	public static final String INVITATION = "0";
	/**同意添加为好友的推送标记*/
	public static final String AGREE_ADD_FRIEND = "1";
	/**拒绝添加为好友的推送标记*/
	public static final String REFUSE_ADD_FRIEND = "2";
	/**新人推荐的推送标记*/
	public static final String RECOMMEND_NEWBE = "3";
	/**封号的推送标记*/
	public static final String ISOLATE = "4";
	/**解封的推送标记*/
	public static final String DEISOLATE = "5";
	/**删除话题的推送标记*/
	public static final String DELETE_TOPIC = "6";
	/**请求交换名片推送标记*/
	public static final String REQUESTCARD = "7";
	/**同意交换名片推送标记*/
	public static final String AGREEEXCHANGECARD = "8";
	/**请求交换薪资推送标记*/
	public static final String REQUESTSALARY= "9";
	/**同意交换薪资推送标记*/
	public static final String AGREEEXCHANGESALARY= "10";
	
}
