<template>
    <div id="box">
        <div class="header">
            <el-page-header :icon="ArrowLeft" @back="onBack">
                <template #content>
                    <span class="text-large font-600 mr-3"> Title </span>
                </template>
            </el-page-header>
        </div>
    </div>
  </template>
  
<script setup>
import { ElPageHeader } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter();

const onBack = () => {
    router.go(-1);
}
</script>

<style scoped>
#box{
    display: flex;
    flex-direction: column;
    height: 100%;
}
.header{
    background-color: #409EFF;
    padding-bottom: 20px;
}
span,.el-page-header{
    color: white;
}

</style>