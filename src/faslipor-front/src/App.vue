<template>
  <div id="app">
    <VueSnow/>
    <router-view />
    <vueBookVue/>
  </div>
</template>

<script setup>
import VueSnow from '@/components/vue-snow.vue';
import vueBookVue from '@/components/vue-book.vue';
import { inject,computed,getCurrentInstance,onMounted, onUnmounted } from "vue";
import { useRouter } from 'vue-router'
import { useStore } from 'vuex';
import { throttle } from '@/api/util';
import { setAllow } from "@/state";

const ctx = getCurrentInstance().appContext.config.globalProperties;
const socket = inject("socket");
const router = useRouter();
const store = useStore();

socket.on("message", (res={}) => {
    console.log("message:",res);
    ctx.$message(res);
});

socket.on("asset", (res={}) => {
    const {event,state,data} = res;
    if(state===200){
      switch(event){
        case "list":store.commit("setRooms",res.data);console.log("asset:",res);break;
        case "sync":
          console.log(res.data);
          store.commit("setParams",res.data);break;
        default:ctx.$message({message:"事件异常!!!",type:"error"});
      }
    }else{
      ctx.$message({message:"服务端异常!!!",type:"error"});
    }
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
    console.log("redirect:",res);
    store.commit("setParams",res.params);
    setAllow(true);
    router.push({path:res.path});
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
</style>
