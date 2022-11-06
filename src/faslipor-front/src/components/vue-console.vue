<template>
    <winUiVue :resizeAble="true" @close="closeWin" width="50vh" height="40vh" v-show="show"
        minWidth="400px" minHeight="280px"
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
                        <el-icon><location /></el-icon>
                        <template #title>Navigator Two</template>
                    </el-menu-item>
                    <el-menu-item index="2">
                        <el-icon><icon-menu /></el-icon>
                        <template #title>Navigator Two</template>
                    </el-menu-item>
                    <el-menu-item index="3" disabled>
                        <el-icon><document /></el-icon>
                        <template #title>Navigator Three</template>
                    </el-menu-item>
                    <el-menu-item index="4">
                        <el-icon><setting /></el-icon>
                        <template #title>Navigator Four</template>
                    </el-menu-item>
                </el-menu>
            </el-scrollbar>
            <el-scrollbar class=right>

            </el-scrollbar>
        </div>
    </winUiVue>
</template>

<script setup>
import { ElMenu,ElMenuItem} from 'element-plus';
import { ref,defineProps,defineEmits } from 'vue';
import winUiVue from './win-ui.vue';
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  CaretRight,
  CaretLeft
} from '@element-plus/icons-vue'

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