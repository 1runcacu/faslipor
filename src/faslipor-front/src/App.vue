<template>
  <VueSnow/>
  <router-view />
  <vueBookVue/>
</template>

<script setup>
import VueSnow from '@/components/vue-snow.vue';
import vueBookVue from '@/components/vue-book.vue';
import { inject,getCurrentInstance } from "vue";
import { useRouter } from 'vue-router'
import { useStore } from 'vuex';

const ctx = getCurrentInstance().appContext.config.globalProperties;
const socket = inject("socket");
const router = useRouter();
const store = useStore();

socket.on("message", (res={}) => {
    console.log("message:",res);
    ctx.$message(res);
});

socket.on("asset", (res={}) => {
    console.log("asset:",res);
    const {event,state,data} = res;
    if(state===200){
      switch(event){
        case "list":store.commit("setRooms",res.data);break;
        default:ctx.$message({message:"事件异常!!!",type:"error"});
      }
    }else{
      ctx.$message({message:"服务端异常!!!",type:"error"});
    }
});

socket.on("disconnect",res=>{
  ctx.$log({message:"连接断开",type:"error"});
});

socket.on("reconnect",res=>{
  ctx.$log({message:"重连成功",type:"success"});
});

socket.on("redirect", (res={}) => {
    console.log("redirect:",res);
    store.commit("setParams",res.params);
    router.push({path:res.path});
});

</script>

<style>
html,body{
  overflow: hidden;
  width: 100%;
  height: 100%;
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
  width: 100%;
  height: 100%;
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
</style>
