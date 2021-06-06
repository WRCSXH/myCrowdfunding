package com.atguigu.crowd.util;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrowdUtil {

    /**
     * 专门负责上传文件到OSS 服务器的工具方法
     * @param endpoint OSS 参数
     * @param accessKeyId OSS 参数
     * @param accessKeySecret OSS 参数
     * @param inputStream 要上传的文件的输入流
     * @param bucketName OSS 参数
     * @param bucketDomain OSS 参数
     * @param originalName 要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在OSS 上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {

        // 创建OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在OSS 服务器上保存时的文件名
        // 使用UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
            // 调用OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if(responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.failed(" 当前响应状态码="+statusCode+" 错误消息="+errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if(ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }

    /**
     * 给第三方接口发送请求将短信验证码返回给用户手机上
     * @param host 第三方接口的请求 url 地址
     * @param path 具有发送短信功能的请求路径
     * @param method 请求方式
     * @param appCode 第三方接口的 appCode
     * @param mobile 用户手机号码
     * @param smsSignId 签名编号，签命如【尚硅谷】
     * @param templateId 模板编号，模板如 尊敬的用户，您的注册验证码为：**code**，请勿泄漏于他人！
     * @return 返回用户注册需要的验证码
     */
    public static ResultEntity<String> sendShortMessage(
            String host,
            String path,
            String method,
            String appCode,
            String mobile,
            String smsSignId,
            String templateId
    ){
        Map<String, String> headers = new HashMap<String, String>();
        // 最后在 header 中的格式(中间是英文空格)为 Authorization:appCode 83359fd73fe94948385f570e3c139105
        // 封装消息头
        headers.put("Authorization", "APPCODE " + appCode);
        // 封装其它参数
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        // 自己生成验证码
        // Math.random：随机生成【0-1】的 double 数
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * 10);
            stringBuilder.append(random);
        }
        String code = stringBuilder.toString();
        querys.put("param", "**code**:"+code+",**minute**:5");

        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            // 获取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                // 如果状态码为 200，说明发送短信获取验证码成功，返回成功结果和验证码
                return ResultEntity.successWithData(code);
            }
            // 状态码不为 200，返回错误原因
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            return ResultEntity.failed(reasonPhrase);
        } catch (Exception e) {
            // 出异常返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 对明文字符串进行MD5加密
     * @param source 需要加密的明文
     * @return 加密后的密文
     */
    public static String md5(String source){
        // 1、判断source是否有效
        if (source == null || source.length() == 0){
            // 2、如果不是有效的字符串抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        try {
            // 3、获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 4、获取明文字符串对应的字节数组
            byte[] input = source.getBytes();
            // 5、执行加密
            byte[] output = messageDigest.digest(input);
            // 6、创建BigInteger对象
            int sigNum = 1;
            BigInteger bigInteger = new BigInteger(sigNum, output);
            // 7、按照16进制将BigInteger的值转换成字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断当前请求是否是ajax请求
     * @param request
     * @return true，当前请求是ajax请求；false，当前请求不是ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request){
        // 1、获取消息头的信息
        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        // 2、判断当前请求是否是ajax请求
        return (accept != null && accept.length()>0 && accept.contains("application/json"))
        || (xRequestedWith != null && xRequestedWith.length()>0 && "XMLHttpRequest".equals(xRequestedWith));
    }

    /**
     * 获取系统当前时间
     * @return
     */
    public static String getSysTime(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        String dateStr = simpleDateFormat.format(date);

        return dateStr;

    }
}
