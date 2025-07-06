<template>
  <div id="app">
    <VueSnow/>
    <router-view />
    <vueBookVue/>
    <el-dialog v-model="winShow" title="输入密码" width="30%" draggable v-drag>
      <vueInputVue v-if="winShow" class="v-input" value="" @input="inRoomPsw" hint="房间密码" t="password"/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeWin">取消</el-button>
          <el-button type="primary" @click="enter">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import VueSnow from '@/components/vue-snow.vue';
import vueBookVue from '@/components/vue-book.vue';
import { inject,computed,getCurrentInstance,onMounted, onUnmounted, ref } from "vue";
import { useRouter } from 'vue-router'
import { useStore } from 'vuex';
import { throttle,download } from '@/api/util';
import { setAllow } from "@/state";
import { ElDialog } from 'element-plus';
import vueInputVue from '@/components/vue-input.vue';

const ctx = getCurrentInstance().appContext.config.globalProperties;
const socket = inject("socket");
const router = useRouter();
const store = useStore();

let rid = "";
let psw = "";

const inRoomPsw = value=>psw=typeof(value)==='string'?value:psw;

const winShow = ref(false);
const closeWin = ()=>{
  winShow.value = false;
  psw = "";
  rid = "";
};

const enter = ()=>{
  socket.emit("query",{
    event:"select",
    params:{
      rid,
      psw
    }
  });
  closeWin();
}

socket.on("verify", (res={}) => {
    winShow.value = true;
    rid = res.rid;
});

socket.on("message", (res={}) => {
    // console.log("message:",res);
    // ctx.$message(res);
    ctx.$log(res);
});

socket.on("asset", (res={}) => {
    const {event,data,params} = res;
    switch(event){
      case "list":store.commit("setRooms",data);break;//console.log("asset:",res);
      case "sync":
        console.log(params);
        store.commit("setParams",params);break;
      default:ctx.$message({message:"事件异常!!!",type:"error"});
    }
});

socket.on("file",(res={})=>{
  let {name,url} = res;
  ctx.$confirm(`是否下载${name}?`,"", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
  }).then(()=>{
      // console.log(name,url);
      download(name,url);
  }).catch(err=>{
    console.log(err);
  });
});

const disconnect = throttle(()=>{
  ctx.$log({message:"连接断开",type:"error"});
  setAllow(true);
  router.push({name:"index"});
},300);

socket.on("disconnect",res=>{
  disconnect();
});

const reconnect = throttle(()=>{
  ctx.$log({message:"重连成功",type:"success"});
},300);

socket.on("reconnect",res=>{
  reconnect();
});

socket.on("redirect", (res={}) => {
    store.commit("setParams",res.params);
    try{
      if(/panel/.test(res.path)){
        const {user:{uid}} = res.params;
        if(uid)store.commit("lock",uid);
        setAllow(true);
        router.push({path:res.path});
      }else if(/\//.test(res.path)&&window.location.pathname!=='/'){
        window.location.href = '/';
      }
    }catch(err){ 
      console.log(err);
    }
});

const height = computed(()=>store.state.window.innerHeight+"px");

const {availWidth,availHeight} = screen;

const resize = ()=>{
  if (/(iPad|iPod|iOS)/i.test(navigator.userAgent)) {
    store.commit("setWindow",{innerWidth:availWidth,innerHeight:availHeight});
  } else if (/(iPhone|Android)/i.test(navigator.userAgent)) {
    store.commit("setWindow",{innerWidth:availWidth,innerHeight:availHeight});
  } else {
    store.commit("setWindow",window);
  }
  // store.commit("setWindow",window);
};


  // const resize = ()=>{
  //   store.commit("setWindow",{innerWidth,innerHeight});
  // }


onMounted(()=>{
  window.addEventListener("resize",resize);
})

onUnmounted(()=>{
  window.removeEventListener(resize);
})


// window.onpopstate = function () {
//         /// 当点击浏览器的 后退和前进按钮 时才会被触发， 
//     window.history.pushState('forward', null, '');
//     window.history.forward(1);
// };

</script>

<style>
html,body{
  overflow: hidden;
  width: 100%;
  /* height: 100%; */
  height: v-bind(height) !important;
  margin: 0;
  box-sizing: border-box;
  background-color: #F5F7F9;
}
html{
  touch-action: none;
}
body{
  touch-action: auto;
}

body {
  height: 100vh;
  background: radial-gradient(ellipse at bottom, #1b2735 0%, #090a0f 100%);
  overflow: hidden;
  filter: drop-shadow(0 0 10px white);
}

#app {
  height: v-bind(height) !important;
}

  /* vue渐入渐出样式 */
.drag-win-enter-from,
.drag-win-leave-to {
  opacity: 0;
  transform: scale(0);
}
.drag-win-enter-to,
.drag-win-leave-from {
  opacity: 1;
}
.drag-win-enter-active,
.drag-win-leave-active {
  transition: all 0.5s ease;
}

.block::-webkit-scrollbar {
  width: 0.5em;
  background-color: #d9d9d9;
}

.block::-webkit-scrollbar-thumb {
  border-radius: 0.25em;
  background-color: #b9b9b9;
}

.v-input{
  margin-bottom: 20px;
}

</style>
