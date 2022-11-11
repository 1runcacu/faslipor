package fastclip.Service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import fastclip.domain.House;
import fastclip.domain.Message;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static fastclip.server.MessageEventHandler.nowAllMap;
import static fastclip.server.MessageEventHandler.socketIOClientMap1;

@Slf4j
@Service
public class StreamService {
    @Autowired
    RedisService redisService;

   public Message save(SocketIOClient client, AckRequest request, JSONObject data0) throws IOException {
       String uid = socketIOClientMap1.get(client);
       House myRoom = redisService.get(uid + "Room", House.class);
       if(!nowAllMap.containsKey(myRoom.rid)) return null;
       //部署时注意这里需要改
       Message message=new Message();
       message.type="info";
       String filePath="/www/web/app/Room";
       File dir=new File(filePath);
       if(!dir.exists()){
           dir.mkdirs();
       }
       File checkFile=new File(filePath+"/"+uid+".fsl");
       FileWriter writer=null;
       try{
           if(!checkFile.exists()){
               checkFile.createNewFile();
           }
           writer=new FileWriter(checkFile,false);
           writer.append(JSON.toJSONString(nowAllMap.get(myRoom.rid)));
           writer.flush();
       } catch (IOException e) {
           e.printStackTrace();
       }finally {
           if(null!=writer)
               writer.close();
       }
       message.message="/Room"+"/"+uid+".fsl";
       log.info(JSON.toJSONString(message));
       return message;
   }

}
