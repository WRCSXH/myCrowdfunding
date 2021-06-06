package com.atguigu.security.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Component
public class AppPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        // 对明文密码进行加密
        return privateEncode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 对明文密码进行加密
        String formPassword = privateEncode(rawPassword);
        // 声明数据库密码
        String databasePassword = encodedPassword;
        // 返回比较结果
        return Objects.equals(formPassword,databasePassword);
    }

    /**
     * 私有化加密方法
     * @param rawPassword
     * @return
     */
    private String privateEncode(CharSequence rawPassword){
        try {
            // 创建MessageDigest对象
            String algorithm = "MD5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 获取rawPassword的字节数组
            byte[] input = ((String)rawPassword).getBytes();
            // 加密
            byte[] output = messageDigest.digest(input);
            // 转换为16进制对应的字符（大写）
            String encoded = new BigInteger(1,output).toString(16).toUpperCase();
            // 返回密文
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String encoded = new AppPasswordEncoder().privateEncode("123123"); // 4297F44B13955235245B2497399D7A93
        System.out.println(encoded);
    }
}
