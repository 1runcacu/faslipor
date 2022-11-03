package fastclip.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.websocket.MessageHandler;

public class ServerInitiazer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //websocket基于http协议 所以要有一个http编解码器
        pipeline.addLast(new HttpServerCodec());
        //对于一个大数据流读写的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对于http message进行一些聚合 生成FullHttpRequest huo FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //====================以上用于支持http支持===========================
        // websocket 服务器处理协议 用于指定给客户端连接访问的路由
        /*
         * 本handler会帮你处理一些繁重复杂的事 会帮你完成握手操作 handshanking(close ping pong)=心跳
         * 对于websocket来说 都是以frames来传输的 不同的数据类型对应的framers也不同
         * */
        pipeline.addLast(new WebSocketServerProtocolHandler("/faslipor"));

        //添加自定义的助手类
        pipeline.addLast(new myMessageHandler());
    }
}


