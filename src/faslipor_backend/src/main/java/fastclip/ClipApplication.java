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

@SpringBootApplication
@Slf4j
public class ClipApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ClipApplication.class, args);

            Configuration config = new Configuration();
            config.setHostname("localhost");
            config.setPort(8102);
            SocketIOServer server = new SocketIOServer(config);
            server.addConnectListener(new ConnectListener() {// 添加客户端连接监听器
                @Override
                public void onConnect(SocketIOClient client) {
                    log.info(client.getRemoteAddress() + " web客户端接入");
                    client.sendEvent("helloPush", "hello");
                }
            });
            // 握手请求
            server.addEventListener("helloevent", HelloUid.class, new DataListener<HelloUid>() {
                @Override
                public void onData(final SocketIOClient client, HelloUid data, AckRequest ackRequest) {
                    // 握手
                    if (data.getMessage().equals("hello")) {
                        int userid = data.getUid();
                        log.info(Thread.currentThread().getName() + "web读取到的userid：" + userid);


                        // send message back to client with ack callback
                        // WITH data
                        client.sendEvent("hellopush", new AckCallback<String>(String.class) {
                            @Override
                            public void onSuccess(String result) {
                                log.info("ack from client: " + client.getSessionId() + " data: " + result);
                            }
                        }, 1000);


                    } else {
                        log.info("行情接收到了不应该有的web客户端请求1111...");
                    }
                }
            });

            server.start();

            Thread.sleep(Integer.MAX_VALUE);

            server.stop();
        }

    }
