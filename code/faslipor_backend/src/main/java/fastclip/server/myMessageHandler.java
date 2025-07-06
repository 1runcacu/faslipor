package fastclip.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class myMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用于记录和管理channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端传来的信息
        //String text = msg.text();
        System.out.println(msg);

        clients.stream().forEach(s->s.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息]"+ LocalDateTime.now()+"--"+msg)));

        //这个和上面的方法一致
//        clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息]"+ LocalDateTime.now()+"--"+msg));
    }

    /*
     *客户端打开连接
     * */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.add(channel);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发 这个我们的channelgroup会自动清楚这个
        //clients.remove(ctx.channel());
        System.out.println(ctx.channel().id().asLongText());
        System.out.println(ctx.channel().id().asShortText());
    }


}


