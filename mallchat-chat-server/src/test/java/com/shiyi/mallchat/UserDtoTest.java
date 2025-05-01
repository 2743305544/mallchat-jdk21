package com.shiyi.mallchat;


import com.shiyi.mallchat.common.utils.JwtUtils;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.domain.enums.ItemEnum;
import com.shiyi.mallchat.user.mapper.UserMapper;
import com.shiyi.mallchat.user.service.LoginService;
import com.shiyi.mallchat.user.service.UserBackpackService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest()
@RunWith(SpringRunner.class)
public class UserDtoTest {

    public static final long UID = 11001L;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private LoginService loginService;

    @Autowired
    private UserBackpackService userBackpackService;


    @Test
    public void redis() {
        redisTemplate.opsForValue().set("name","卷心菜");
        String name = (String) redisTemplate.opsForValue().get("name");
        System.out.println(name); //卷心菜
    }

    @Test
    public void acquireItem() {
        userBackpackService.acquireItem(UID, ItemEnum.PLANET.getId(), IdempotentEnum.UID, UID+"");
    }

    @Test
    public void redisson() {
        RLock lock = redissonClient.getLock("123");
        lock.lock();
        System.out.println();
        lock.unlock();
    }

    @Test
    public void jwt() {
        String token = loginService.login(UID);
        System.out.println(token);
    }

    @Test
    public void jwt2() {
        String s = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExMDAxLCJjcmVhdGVUaW1lIjoxNzQ0MDA5OTc0fQ.3kyLeuT7Coyl2YaAzPsFIER3aF8cNNQTHNvVqqPvto8";
        Long validUid = loginService.getValidUid(s);
        System.out.println(validUid);
    }

        @Test
    public void test() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }
}
