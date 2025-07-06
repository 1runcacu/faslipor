<template>
    <div id="chat">
        <div id="msg" class="box" ref="messageBox">
            <template v-for="(item,index) in List" :key="index">
                <div v-if="timerShow(item,index)" class="timer">{{new Date(parseInt(item.date)).toLocaleString()}}</div>
                <vueMsgVue :message="item.message" :uid="item.uid" :name="item.name" :uuid="config.user.uid" :s="config.user.ctrlId===item.uid" />
            </template>
            <div ref="last"></div>
        </div>
        <div id="input">
            <div class="bar">
                <el-button size="small" type="primary" @click="sendMessage">发送</el-button>
            </div>
            <textarea v-model="Msg" cols="2" rows="6" class="box" style="resize: none;" placeholder="请输入要发送的消息"></textarea>
        </div>
    </div>
</template>

<script setup>
import { getCurrentInstance, inject,onMounted,onUnmounted,computed,ref,nextTick } from 'vue';
import vueMsgVue from './vue-msg.vue';
import { useStore } from 'vuex';

const ctx = getCurrentInstance().appContext.config.globalProperties;

const store = useStore();
var config = computed(()=>store.state.params||{room:{},user:{},layout:[]});
const socket = inject('socket');

const last = ref(null);
const messageBox = ref(null);

const message = data=>{
    const {rid,uid,event,frame} = data;
    switch(event){
        case "refresh":
            MessageList = frame.data;
            break;
        default:
            MessageList.push(frame);
            break;
    }
    RFS.value = ID();
}

const dt = 3*60*1000;

const timerShow = (item,index)=>{
    if(index===0)return true;
    let last = MessageList[index-1];
    return (parseInt(item.date)-parseInt(last.date))>=dt;
}

var ID = ()=>Date.now().toString(36);

const RFS = ref("");

let MessageList = [];

const Msg = ref("");

const List = computed(()=>{
    RFS.value;
    nextTick(()=>{
        messageBox.value._value.scrollTop = last.value._value.offsetTop;
    });
    return MessageList;
});

const sendMessage = ()=>{
    if(Msg.value){
        chat("broadCast",{
            message:Msg.value
        });
        Msg.value = "";
    }else{
        ctx.$message({type:"warning",message:"发送内容不为空哦~"});
    }
}

const chat = (event,frame={})=>{
    let {room:{rid},user:{uid}} = config.value;
    frame.uid = uid;
    frame.date = Date.now();
    socket.emit("chat",{
        rid,uid,event,frame
    });
};



onMounted(()=>{
    socket.on("chat",message);
    chat("refresh");
});

onUnmounted(()=>{
    socket.off("chat",message);
});

</script>

<style scoped>
#chat{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    overflow: hidden;
}
#msg{
    flex: 1;
    width: 100%;
    overflow-y: auto; 
}
#msg>.timer{
    text-align: center;
    border-bottom: 1px solid rgba(199, 199, 199, 0.733);
    margin-left: 5px;
}
#input{
    height: 80px;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: stretch;
    align-items: flex-start;
    padding: 10px;
}
#input>textarea{
    width: calc(100% - 20px);
    flex: 1;
    border-radius: .2rem;
    box-shadow: 1px 1px 3px gray;
}
#input>.bar{
    display: flex;
    width: calc(100% - 20px);
    padding-right: 20px;
    flex-direction: row-reverse;
    margin-bottom: 5px;
}

/* 滚动条和滑块 */
.box::-webkit-scrollbar,
.box::-webkit-scrollbar-thumb{ 
    width: 8px;
    height: 8px;
    border-radius: 10px;
}
/* 滑块背景色 */
.box::-webkit-scrollbar-thumb{
   background: #D2D5D9;
}
/* 其余相关样式设置跟滑块一个背景色 */
.box::-webkit-scrollbar,
.box::-webkit-scrollbar-corner,
.box::-webkit-resizer,
.box::-webkit-scrollbar-track,
.box::-webkit-scrollbar-track-piece{
    background: #EDF1F3;
}
/* 隐藏滚动条按钮 */
.box::-webkit-scrollbar-button{
    display: none;
}

</style>