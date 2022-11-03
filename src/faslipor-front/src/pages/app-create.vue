<template>
    <div id="box">
        <div class="header">
            <el-page-header @back="onBack">
                <template #content>
                    <span class="text-large font-600 mr-3"> 房间创建 </span>
                </template>
                <template #icon>
                    <el-icon class="back"><ArrowLeft/></el-icon>
                </template>
                <template #title>
                    <div></div>
                </template>
            </el-page-header>
        </div>
        <div class="body">
            <el-form class="form">
                <el-input v-model="roomName"/>
                <el-input v-model="roomID"/>
            </el-form>
        </div>
    </div>
  </template>
  
<script setup>
import { ElPageHeader } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue';

const router = useRouter();

const onBack = () => {
    router.go(-1);
}

const roomName = ref("");
const roomID = ref("");

</script>

<style scoped>
#box{
    display: flex;
    flex-direction: column;
    height: 100%;
}
.header{
    background-color: #F7F7F7;
    padding: 0 20px 20px 20px;
    border-bottom: 1px solid gray;
}

.back{
    transform: scale(1.5,1.5);
}
.back:hover{
    transform: scale(1.8,1.8);
    color: #409EFF;
}

.body{
    flex: 1;

    display: flex;
    justify-content: center;
    align-items: center;
}
.form{
    width: 800px;
    height: 600px;
    border-radius: 1rem;
    backdrop-filter: blur(5px);
    background-color: rgba(255, 255, 255, 0.1);
    box-shadow: rgb(204, 204, 204) 0px 0px 8px;
    padding: 20px;
    display: flex;
    flex-direction: column;
}
.form>.el-input{
    margin-bottom: 20px;
}
</style>
