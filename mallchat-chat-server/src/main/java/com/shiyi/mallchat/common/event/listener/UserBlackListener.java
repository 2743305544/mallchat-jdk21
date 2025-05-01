package com.shiyi.mallchat.common.event.listener;

import com.shiyi.mallchat.common.event.UserBlackEvent;
import com.shiyi.mallchat.common.event.UserRegisterEvent;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.domain.enums.ItemEnum;
import com.shiyi.mallchat.user.service.UserBackpackService;
import com.shiyi.mallchat.user.service.UserService;
import com.shiyi.mallchat.user.service.cache.UserCache;
import com.shiyi.mallchat.websocket.service.WebSocketService;
import com.shiyi.mallchat.websocket.service.adapter.WebSocketAdapter;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author 34011
 */
@Component
public class UserBlackListener {

    @Resource
    private UserService userService;

    @Resource
    private WebSocketService webSocketService;

    @Resource
    private UserCache userCache;



    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class)
    public void sendMessage(UserBlackEvent event) {
        var user = event.getUser();
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlack(user));
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class)
    public void changeUserStatus(UserBlackEvent event) {
        userService.invaildUid(event.getUser().getId());
    }

    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class)
    public void sendCard(UserBlackEvent event) {
        userCache.evitBlackMap();
    }


}
