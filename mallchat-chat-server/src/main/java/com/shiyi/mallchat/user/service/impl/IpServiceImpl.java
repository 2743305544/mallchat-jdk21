package com.shiyi.mallchat.user.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.shiyi.mallchat.common.domain.vo.resp.ApiResult;
import com.shiyi.mallchat.common.utils.JsonUtils;
import com.shiyi.mallchat.user.domain.entity.IpDetail;
import com.shiyi.mallchat.user.domain.entity.IpInfo;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.service.IpService;
import com.shiyi.mallchat.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class IpServiceImpl implements IpService , DisposableBean {

    private static final ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Resource
    private UserService userService;

    @Override
    public void refreshIpDetailAsync(Long id) {
        executor.execute(() -> {
            User user = userService.getById(id);
            IpInfo ipInfo = user.getIpInfo();
            if (Objects.isNull(ipInfo)) return;
            String ip = ipInfo.needRefreshIp();
            if(StringUtils.isBlank(ip)) return;
            IpDetail ipDetail = tryGetIpDetailOrNullTreeTimes(ip);
            if(Objects.nonNull(ipDetail)){
                ipInfo.refreshIpDetail(ipDetail);
                User updateUser = new User();
                updateUser.setId(id);
                updateUser.setIpInfo(ipInfo);
                userService.updateById(updateUser);
            }
        });
    }

    private static IpDetail tryGetIpDetailOrNullTreeTimes(String ip) {
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if(Objects.nonNull(ipDetail)) return ipDetail;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("tryGetIpDetailOrNullTreeTimes sleep error", e);
            }
        }
        return null;
    }

    // https://ip.taobao.com/outGetIpInfo?ip=112.96.166.230&accessKey=alibaba-inc
    private static IpDetail getIpDetailOrNull(String ip) {
        try {
            String url = "https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc";
            String data = HttpUtil.get(url);
            ApiResult<IpDetail> result = JSONUtil.toBean(data, new TypeReference<>() {
            }, true);
            return result.getData();
        }catch (Exception e){
            return null;
        }
    }

    public static void main(String[] args) {
        Date begin = new Date();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.execute(() -> {
                IpDetail ipDetail = tryGetIpDetailOrNullTreeTimes("117.85.133.4");
                System.out.println(ipDetail);
                if(Objects.nonNull(ipDetail)){
                    Date end = new Date();
                    System.out.println("第 " + finalI + "次耗时:" + (end.getTime() - begin.getTime()));
                }
            });
        }
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
        if(!executor.awaitTermination(30, TimeUnit.SECONDS)){
            if(log.isErrorEnabled()){
                log.error("线程池优雅关闭失败");
            }
        }
    }
}
