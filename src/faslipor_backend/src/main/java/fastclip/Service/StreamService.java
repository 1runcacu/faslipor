package fastclip.Service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import fastclip.domain.*;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fastclip.server.MessageEventHandler.*;

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

   public Refresh refresh(SocketIOClient client, String data){
       String uid = socketIOClientMap1.get(client);
       House myRoom = redisService.get(uid + "Room", House.class);
       Usr myUser=redisService.get(uid+"User",Usr.class);
       myUser.lid=data;
       redisService.set(uid+"User",myUser);
       if(!nowAllMap.containsKey(myRoom.rid+myUser.lid)) return null;
       if(nowAllMap.get(myRoom.rid+myUser.lid).toString().equals(JSONObject.parseObject("null").toString())){
           return null;
       }
       Refresh myRe=new Refresh();
       myRe.rid=myRoom.rid;
       myRe.uid=uid;
       myRe.lid=myUser.lid;
       if(nowAllMap.get(myRoom.rid+myUser.lid)==null)
       {myRe.frame=new JSONObject();
       nowAllMap.put(myRoom.rid+myUser.lid,myRe.frame);
       }else{
           myRe.frame=nowAllMap.get(myRoom.rid+myUser.lid);
       }
       log.info(JSONObject.toJSONString(myRe));
       return myRe;
   }
   public Result create(SocketIOClient client,Istream data){
       Layout myLayout = new Layout();
       Date time0 = new Date();
       myLayout.lid = ((Long) time0.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
       myLayout.name = data.frame.name;
       if(myLayout.name==null) myLayout.name="sheet";
       nowAllMap.put(data.rid + myLayout.lid , null);
       List<Layout> layouts1 = redisService.get(data.rid+"layout", List.class);
       List<Layout> layouts=new ArrayList<>(layouts1);
       log.info(JSONObject.toJSONString(layouts));
       log.info(JSONObject.toJSONString(myLayout));
       boolean f= layouts.add(myLayout);
       log.info(String.valueOf(f));
       redisService.set(data.rid + "layout", layouts);
       redisService.set(data.uid + "layout", myLayout);
       Result myResult = new Result();
       Usr myUsr=redisService.get(data.uid+"User",Usr.class);
       myUsr.lid=myLayout.lid;
       myResult.event="sync";
       myResult.params.room=redisService.get(data.rid,House.class);
       myResult.params.user=myUsr;
       redisService.set(data.uid+"User",myUsr);
       myResult.params.layout = layouts;
       return myResult;

   }

}
