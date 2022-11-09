<template>
    <winUiVue :resizeAble="true" @close="closeWin" :width="WH" height="40vh" v-show="show"
        minWidth="200px" minHeight="280px"
    >
        <template #head>
          <div><b>{{title}}</b></div>
        </template>
        <div class="body">
            <el-scrollbar class="left">
                <el-menu
                    default-active="2"
                    class="el-menu-vertical-demo"
                    :collapse="true"
                    background-color="rgba(255,255,255,0)"
                    @open="handleOpen"
                    @close="handleClose"
                >
                    <el-menu-item index="1">
                        <el-icon><ChatDotRound /></el-icon>
                        <template #title>消息日志</template>
                    </el-menu-item>
                    <el-menu-item index="2">
                        <el-icon><Monitor /></el-icon>
                        <template #title>控制键盘</template>
                    </el-menu-item>
                    <el-menu-item index="3">
                        <el-icon><ElementPlus /></el-icon>
                        <template #title>高级管理</template>
                    </el-menu-item>
                    <el-menu-item index="4">
                        <el-icon><setting /></el-icon>
                        <template #title>房间设置</template>
                    </el-menu-item>
                </el-menu>
            </el-scrollbar>
            <el-scrollbar class=right>
                <div v-for="i in 100">{{i}}</div>
            </el-scrollbar>
        </div>
    </winUiVue>
</template>

<script setup>
import { ElMenu,ElMenuItem} from 'element-plus';
import { ref,defineProps,defineEmits, inject, onMounted, onUnmounted,computed } from 'vue';
import winUiVue from './win-ui.vue';
import {
    ElementPlus,
    Monitor,
    ChatDotRound,
    Setting
} from '@element-plus/icons-vue'

const socket = inject("socket");

const message = data=>{
    const {rid,uid,event,frame} = data;
    try{
        switch(event){
            case "":
        }
    }catch(err){}
}

const emit = defineEmits(["open","test"]);

const props = defineProps({
    show:{
        type:Boolean,
        default:true
    }
});

const title = ref("控制台");

const isCollapse = ref(false)

const handleOpen = (key, keyPath) => {
//   console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
//   console.log(key, keyPath)
}

const closeWin = ()=>{
    emit("close");
}

onMounted(()=>{
    socket.on("stream",message);
});

onUnmounted(()=>{
    socket.off("stream",message);
});

const WH = computed(()=>(window.innerWidth>500?500:window.innerWidth)+"px");

</script>

<style scoped>
.body{
    width: 100%;
    height: 100%;
    display: flex;
    overflow: hidden;
    align-items: stretch;
}
.left{
    border-right: 1px solid black;
}
.right{
    flex: 1;
}

.el-menu,.el-sub-menu,.el-menu-item-group,.el-menu-item,.el-collapse,.is-opend,
.el-collapse-item__header
{
    background-color: rgba(255,255,255,0) !important;
}
.el-collapse{
    --el-collapse-content-bg-color: rgba(255,255,255,0) !important;
}

</style>