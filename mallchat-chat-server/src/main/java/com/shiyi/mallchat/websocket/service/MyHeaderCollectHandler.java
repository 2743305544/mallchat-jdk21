package com.shiyi.mallchat.websocket.service;

import cn.hutool.core.net.url.UrlBuilder;
import com.shiyi.mallchat.websocket.utlis.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Optional;

public class MyHeaderCollectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest httpRequest){
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(httpRequest.uri());
            Optional<String> token = Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(query -> query.get("token"))
                    .map(CharSequence::toString);
            token.ifPresent(s -> NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, s));
            httpRequest.setUri(urlBuilder.getPath().toString());
            String ip = httpRequest.headers().get("X-Real-IP");
            if(StringUtils.isBlank(ip)){
                InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = inetSocketAddress.getAddress().getHostAddress();
            }
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
            ctx.pipeline().remove(this);
            ctx.fireChannelRead(httpRequest);
        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
