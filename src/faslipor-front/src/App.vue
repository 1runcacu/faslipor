<template>
  <VueSnow></VueSnow>
  <router-view />
</template>

<script setup>
import VueSnow from '@/components/vue-snow.vue';

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
</style>
