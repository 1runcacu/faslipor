<template>
    <div class="drag-opener" v-if="select" @click="change"></div>
    <transition name="drag-win" v-show="!select">
        <div class="drag-box" >
            <div id="box" class="ban-select-font" v-drag>
                <div class="header">
                    <!-- <strong>快捷</strong> -->
                    <el-icon class="close" @click="change"><Close/></el-icon>
                </div>
                <el-scrollbar class="body">
                    <div class="edits">
                        <img src="@/assets/icon/曲线大小.png"/>
                        <img src="@/assets/icon/字体大小.png"/>
                        <input type="color"/>
                        <div class="block"></div>
                        <img src="@/assets/icon/保存.png"/>
                        <img src="@/assets/icon/复制.png"/>
                        <img src="@/assets/icon/撤销.png"/>
                        <img src="@/assets/icon/重做.png"/>
                        <img src="@/assets/icon/删除.png"/>
                    </div>
                </el-scrollbar>
            </div>
        </div>
    </transition>
</template>

<script>
import {Close} from '@element-plus/icons-vue'

export default{
    data(){
        return {
            select:false,
            tools:[
                {
                    label:""
                }
            ]
        };
    },methods:{
        change(){
            this.select = !this.select;
            this.$emit("open",!this.select);
        }
    },
    components:{
        Close
    },
    props:{
        toolShow:Boolean
    },
    watch:{
        toolShow:function(now,old){
            this.select = !now;
        }
    }
}

</script>

<style scoped>

.header{
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.body{
    width: 100%;
    flex: 1;
}

.drag-box{
    position: fixed;
    width: 60px;
    height: 600px;
    right: 0;
    top: calc(50% - 300px);
    box-sizing: border-box;
    padding: 8px;
    overflow: hidden;
    color: #000;
    border: 1px solid black;
    border-radius: 5px;
    backdrop-filter: blur(5px);
    background-color: rgba(233, 237, 240, 0.8);
    box-shadow: rgb(204, 204, 204) 0px 0px 5px;
}
#box{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}
.drag-opener {
    position: fixed;
    right: 0;
    top: 45%;
    height: 80px;
    width: 15px;
    overflow: hidden;
    border: 0.5px solid black;
    border-radius:50px 0 0 50px;
    backdrop-filter: blur(5px);
    background-color: rgba(15, 20, 28, 0.84);
    box-shadow: rgba(15, 20, 28, 0.286) 0px 0px 5px;
    
}
.drag-opener:hover,.drag-opener:active{
    transform: scale(1.05,1.05);
    background-color: rgba(15, 20, 28, 0.868);
}
.close{
    margin-right: 5px;
}
.close:hover,.close:active{
    transform: scale(1.05,1.05);
    color: red;
}
.edits{
    display: flex;
    flex-direction: column;
    padding: 3px;
    overflow: hidden;
    justify-content: stretch;
    align-items: flex-start;
    height: 500px;
}

.edits>*{
    width: 30px;
    height: 30px;
    margin-top: 20px;
    border-radius: 1rem;
}
.edits>img{
    filter: invert(60%);
}
img:hover,img:active{
    filter: invert(0%);
}

.block{
    flex: 1;
}

</style>