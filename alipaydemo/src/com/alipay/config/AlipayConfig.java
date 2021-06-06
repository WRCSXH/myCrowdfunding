package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 类名：AlipayConfig
 * 功能：基础配置类
 * 详细：设置帐户有关信息及返回路径
 * 修改日期：2017-04-05
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000117637148";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCLT7cx8xvkYW/hV3wi+gebRqHTDRqffAcHSyt2fJz9RwZd+6lzij0FcB0HwMWanllQNrCjyHQ62VwId0reZ0hGffhXDY/eobgEBpQrKodF2Owq4bCLFeik1RlQF8xZYXobqGad/Y3aPvfjXnJW4nCn8M15ZMMS49Q/1LbzO71kgsbgJjnGsIijp5oiN/oDoAbxh7X568wmE6F7a64BrMsaPevlR4v1gHCjSOBdUX4UaRwZ1jVl6+5LVyKsbLzONvJGH+IAKisxsiNAFjxGk2po7Atj2Gb83I2M9vkfgF8aWCcK8GxEGP5M8K71mmOk/mxNewOi6vG++rVNfVcAqZZpAgMBAAECggEAJkkrJJwWb0Zj8/8SC57BVmgr8w+n4Gq6l8ON10tZ7bWGR2/xWuKD8KmmIMpWxC5+hy/wjaJ6Zu3zgBQgzEyALXdTN8FvZ5T0OI8CS3atpIGALizGUwMbY6l5Amz36RyD7XPzd7oGMRObFq6PJ9eldFIWdiU+A0P/wecYvHIPk759dm3bQ799ofDGgd+kD8jC+j+t09CmkRGbEr2qUEugNLwq4YSKEl2VY7weKa4FjFKmWMuDHkJEfMh048er1CTjsHcaVFj7picOM7HQE6fAl0e5jzlfquI+9wvx/Ioo+w4oavYZ8/+IGEZnPf6pXZnTbR6IARw8kiYKoWX7NR6D3QKBgQD32Exdb4Al4I/AR51Iq8FkUuO6HJGIDRzb4GtyPON+UZAjviKo5DnFjShSVBYRVeyK5EXU2JlXOX2L63owvUu+78Cxwv3wGY17Xk+eTA2SXGkHRDpPf96FS6c7BbuMYKQV2m1wePkbgct0ELTVLXxRxeFuCEEQ3QtJVkZFGR9mRwKBgQCP5TGiNny1AnrQQCTHNjqOvBwvx5leYeB/TOa4KocWDoEvazFJejSMrd0ZTZxfQCPAZFB1ylJWfzWu20NQYtXaqxZjvg6CqFRZ8+I5GTZYGSEAeKqSoXOGAygvkxuTPqP/xraCRCyiVoCHzEQZjogLZJ772OMUCBcCc81iOUWFzwKBgQDHpnT1u5E2cew6zzWzSbCTGXabANJ3D2EU+nZzYvs7UYbUVCDlflvPeMNndpcERfwzcmmSaR01N3mcEgevZX20BLxsNhCLrnnbTIDEmq1AHrL4iIyUaAQfym6fTCHllh/3/qm0Na8t6mOzTpXtOCPyV34ePgX0CKxD2nzmwrMZlwKBgArmLVBAGEoNmgfUE/US4nSVQv1LfQ5tE3gQ9xjT/89n4dkZoW4TV4+gS7Ly8fk0/oBAGOgTHxF2KTbJXNvl4JTjiJqddrPm1BzMIEJ66mbZ4GoMub7Jg9qOuqh/2ALbJ8jj4EcVqtvQ+YNPsYjOBvf18yTYVLrbsgJ5iwD5nY55AoGADkHyXJ8WkVJH90/H/weaAvGhspBwPLWSQtCpV8yJraAnysiaFiY4Atz4z0DDN4dEdygUtbg1TA8MzFyStTyfLaIHFr164i1PKegXm6uA3D+CB4ZYXQG0B+2ji8DhL7ptfYMpoeRDGRHR0I98W2wQhWMVQmeM2rtMpcq804Y4GGQ=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw7gkulAMg8BAyCu19Pi+H/ij1wI4T37erbCvB35ARE8C7b9MEXMs52PTE+9f72+GRUliwze46c/OxUrYthh94zAEypksvCN89RPdZBg0kk5e7sz7XViSl6l03mGoRj/gNEPQRVIso7VeLunriB8K36LL71/2THqeSdr6acVpqYM58h7vBKL+80ytvXh37g277NjnkgyQjtL1f36ckm2i/kZNMELkym8ONHup46lSlmc9is2Fs0zDYawHMB/iGYEZ8nN76AKBwVeMRyNPLM6J8ErOxJVZ5c82tXqezsRPX2Ql+BgxvLkvwPVmTyilU1Vb6WDmA2GUXNq+/nvEK8GORwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 工程公网访问地址使用 natapp 客户端提供的内存穿透的地址
    public static String notify_url = "http://v86raj.natappfree.cc/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://v86raj.natappfree.cc/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 日志地址
    public static String log_path = "E:\\myJava\\myAtcrowdfunding\\";


// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

