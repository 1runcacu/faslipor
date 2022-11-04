<template>
  <VueSnow></VueSnow>
  <router-view />
</template>

<script setup>
import VueSnow from '@/components/vue-snow.vue';

import { inject,getCurrentInstance } from "vue";


const ctx = getCurrentInstance().appContext.config.globalProperties;
const socket = inject("socket");

socket.on("message", (res={}) => {
    console.log("message:",res);
    ctx.$message(res);
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
