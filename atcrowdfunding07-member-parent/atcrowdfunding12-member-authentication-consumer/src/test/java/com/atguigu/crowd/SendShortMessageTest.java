package com.atguigu.crowd;
import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SendShortMessageTest {
    @Test
    public void test(){
        CrowdUtil.sendShortMessage(
                "https://gyyyx1.market.alicloudapi.com",
                "/sms/smsSend",
                "POST",
                "55171ba8d35747d4ae7da901941dfb3c",
                "18170488637",
                "2e65b1bb3d054466b82f0c9d125465e2",
                "908e94ccf08b4476ba6c876d13f084ad"
                );
    }
}
