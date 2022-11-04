package fastclip;

import com.corundumstudio.socketio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class NettySocketioApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettySocketioApplication.class, args);
    }

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... strings) {
        socketIOServer.start();
        log.info("socket.io启动成功！");
    }
}

