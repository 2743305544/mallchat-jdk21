package com.shiyi.mallchat.common.event.listener;

import com.shiyi.mallchat.common.event.UserRegisterEvent;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.domain.enums.ItemEnum;
import com.shiyi.mallchat.user.service.UserBackpackService;
import com.shiyi.mallchat.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserRegisterListener {

    @Resource
    private UserService userService;

    @Resource
    private UserBackpackService userBackpackService;

    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class)
    public void sendCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId()+"");
    }

    @TransactionalEventListener(classes = UserRegisterEvent.class)
    public void sendPlanet(UserRegisterEvent event) {
        User user = event.getUser();
        long count = userService.count();
        if(count < 10) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID, user.getId()+"");
        } else if (count < 100) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID, user.getId()+"");
        }
    }

}
