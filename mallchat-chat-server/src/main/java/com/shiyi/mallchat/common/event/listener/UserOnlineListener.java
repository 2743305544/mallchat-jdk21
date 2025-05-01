package com.shiyi.mallchat.common.event.listener;


import com.shiyi.mallchat.common.event.UserOnlineEvent;
import com.shiyi.mallchat.common.event.UserRegisterEvent;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.domain.enums.ItemEnum;
import com.shiyi.mallchat.user.domain.enums.UserActiveStatusEnum;
import com.shiyi.mallchat.user.service.IpService;
import com.shiyi.mallchat.user.service.UserBackpackService;
import com.shiyi.mallchat.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserOnlineListener {
    @Resource
    private UserService userService;

    @Resource
    private IpService ipService;



    @TransactionalEventListener(classes = UserOnlineEvent.class , fallbackExecution = true)
    public void saveDp(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setIpInfo(user.getIpInfo());
        update.setId(user.getId());
        update.setUpdateTime(user.getLastOptTime());
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userService.updateById(update);

        ipService.refreshIpDetailAsync(user.getId());
    }
}
