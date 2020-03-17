package org.elastos.wallet.ela.utils;


public class Constant {
    public static final String CONTACTSHOW = "contact_show";
    public static final String CONTACTADD = "contact_add";
    public static final String CONTACTEDIT = "contact_edit";
    public static final String INNER = "inner";//油钱包列表打开
    public static final String SIDEWITHDRAW = "sideWithdraw";//侧链充值
    public static final String TRANFER = "transfer";//普通充值
    public static final String SUPERNODESIGN = "supernodesign";//超级节点参选注册
    public static final String CRSIGNUP = "crsignup";//cr注册
    public static final String DIDSIGNUP = "didsignup";//did注册
    public static final String DIDUPDEATE = "didupdate";//did注册
    public static final String CRVOTE = "crvote";//cr投票
    public static final String SUPERNODEVOTE = "nodecrvote";//cr投票
    public static final String TOWHOL = "towhole";//零钱换整
    public static final String UPDATENODEINFO = "updatenodeinfo";//超级节点更新参选信息
    public static final String CRUPDATE = "crupdate";//cr更新参选信息
    public static final String UNREGISTERSUPRRNODE = "unregistersupernode";//超级节点注销
    public static final String UNREGISTERCR = "unregistercr";//cr注销
    public static final String WITHDRAWSUPERNODE = "withdrawsupernode";//超级节点提取
    public static final String WITHDRAWCR = "withdrawcr";//cr提取
    public static final String Email = "wallet@elastos.org";
    public static final String UpdateLog = "https://download.elastos.org/app/release-notes/ela-wallet/index.html";
    public static final String FRAGMENTTAG = "commonwebview";
    public static final int CREATEREADONLY = 0x1;//打开创建单签只读钱包页面
    public static final int CREATEMUL = 0x2;//打开创建多签钱包页面 已经打开了的情况直接填入
    public static final int SIGN = 0x3;//打开签名页面
    public static final int TRANSFER = 0x4;//打开转账页面
    public static final String SERVERLIST_BASE = "http://54.223.158.189:5739/";


    public static final String EDITCREDENTIAL = "editPersonalinfo";//保留凭证  新增或编辑did  从凭证信息进入三部分凭证
}
