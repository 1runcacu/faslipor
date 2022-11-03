package fastclip;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import fastclip.domain.HelloUid;
import fastclip.server.ServerInitiazer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.alibaba.fastjson.JSON;

@SpringBootApplication
@Slf4j
public class ClipApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ClipApplication.class, args);
        log.info("123");
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8102);
        SocketIOServer server = new SocketIOServer(config);
        log.info("123");
        server.addConnectListener(new ConnectListener() {// 添加客户端连接监听器
            @Override
            public void onConnect(SocketIOClient client) {
                log.info(client.getRemoteAddress() + " web客户端接入");
                HelloUid myData = new HelloUid();
                myData.msg = "hello";
                log.info(JSON.toJSONString(myData));
                client.sendEvent("message", JSON.parseObject(JSON.toJSONString(myData)));
            }
        });
        // 握手请求

        server.addEventListener("message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) throws Exception {

                // 握手
                log.info(data);
                HelloUid myData = new HelloUid();
                myData.msg = data;
                // send message back to client with ack callback
                // WITH data
                client.sendEvent("message", JSON.parseObject(JSON.toJSONString(myData)));

            }

        });

        server.start();
        log.info("123");
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}

