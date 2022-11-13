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
                    @select="selectItems"
                    default-active="1"
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
            <!-- <el-scrollbar class=right>
                <vueChatVue/>
            </el-scrollbar> -->
            <vueChatVue class="right" v-show="select=='1'"/>
            <div v-show="select=='2'" class="right">
                控制键盘-》》》疯狂搬砖中ing...
            </div>
            <div v-show="select=='3'" class="right">
                高级管理-》》》疯狂搬砖中ing...
            </div>
            <div v-show="select=='4'" class="right">
                <div style="margin:10px">
                    <el-button type="primary" 
                    v-if="!config.room.lock"
                    :disabled="config.user.state!=='管理员'"
                    @click="ctrlHandle(true)"
                    >
                        只读模式
                    </el-button>
                    <el-button type="primary" 
                    v-if="config.room.lock"
                    :disabled="config.user.state!=='管理员'"
                    @click="ctrlHandle(false)"
                    >
                        协作模式
                    </el-button>
                </div>
            </div>
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
import vueChatVue from './vue-chat.vue';
import { useStore } from 'vuex';

const store = useStore();
const socket = inject("socket");

const select = ref("1");
var config = computed(()=>store.state.params||{room:{},user:{},layout:[]});

const console = data=>{

}

const emit = defineEmits(["open","test"]);

const props = defineProps({
    show:{
        type:Boolean,
        default:true
    }
});

const ctrlHandle = (v)=>{
    const {room:{rid},user:{uid,lid}} = config.value;
    socket.emit("console",{
        rid,uid,lid,event:"lock",
        frame:{
            lock:v
        }
    });
}

const selectItems = (index)=>{
    select.value = index;
}

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
    socket.on("console",console);
});

onUnmounted(()=>{
    socket.off("console",console);
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