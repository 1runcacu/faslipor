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
                        <el-popover placement="left" trigger="click" :width="50">
                            <template #reference>
                                <img src="@/assets/icon/线型.png"/>
                            </template>
                            <input class="edit" type="number" max="10" min="0.1" placeholder="线宽大小" v-model="LW"
                            />
                        </el-popover>
                        <el-popover placement="left" trigger="click" :width="50">
                            <template #reference>
                                <img src="@/assets/icon/字体大小.png"/>
                            </template>
                            <input class="edit" type="number" max="10" min="0.1" placeholder="字体大小" v-model="FS"
                            />
                        </el-popover>
                        <input type="color" placeholder="字体颜色" v-model="FC"
                        />
                        <div class="block"></div>
                        <el-button :type="CTL?'success':'info'" link @click="key(17)">CTL</el-button><br/>
                        <el-button :type="ESC?'success':'info'" link @click="key(13)">ESC</el-button>
                        <div class="block"></div>
                        <img src="@/assets/icon/保存.png" v-click2="()=>handle('保存')"/>
                        <!-- <el-icon><Operation/></el-icon> -->
                        <!-- <img src="@/assets/icon/复制.png"/> -->
                        <el-popover placement="left" trigger="click" :width="50">
                            <template #reference>
                                <img src="@/assets/icon/功能.png"/>
                            </template>
                            <div class="more">
                                <img src="@/assets/icon/放大.png" @click="handle('放大')"/> 
                                <img src="@/assets/icon/缩小.png" @click="handle('缩小')"/> 
                                <img src="@/assets/icon/复制.png" v-click2="()=>handle('复制')"/> 
                            </div>
                        </el-popover>
                        <img src="@/assets/icon/撤销.png" v-click2="()=>handle('撤销')"/>
                        <img src="@/assets/icon/重做.png" v-click2="()=>handle('重做')"/>
                        <img src="@/assets/icon/删除.png" v-click2="()=>handle('删除')"/>
                    </div>
                </el-scrollbar>
            </div>
        </div>
    </transition>
</template>

<script>
import {Close} from '@element-plus/icons-vue'
import { ElPopover } from 'element-plus';

export default{
    data(){
        return {
            select:false,
            CTL:false,
            ESC:false,
            LW:"1.1",
            FS:"1.2",
            FC:"#233456"
        };
    },
    emits:['update:lineWidth','update:fontSize','update:fontColor'],
    props:{
        lineWidth:Number,
        fontSize:Number,
        fontColor:String,
    },
    methods:{
        reset(lw,fs,fc){
            console.log(lw,fs,fc);
            this.LW = lw;
            this.FS = fs;
            this.FC = fc;
        },
        change(){
            this.select = !this.select;
            this.$emit("open",!this.select);
        },
        key(k){
            // KeyEvent(k);
            switch(k){
                case 17:this.CTL = !this.CTL;this.$emit("KD",17,this.CTL);break;
                case 13:this.ESC = true;setTimeout(() => {
                    this.ESC = false;
                }, 500);this.$emit("KD",13,this.ESC);break;
            }
        },
        handle(e,data){
            this.$emit("edit",e,data);
        }
    },
    components:{
        Close,
        ElPopover
    },
    props:{
        toolShow:Boolean
    },
    watch:{
        toolShow:function(now,old){
            this.select = !now;
        },
        LW:function(now){
            this.$emit('update:lineWidth',now);
        },
        FS:function(now){
            this.$emit('update:fontSize',now);
        },
        FC:function(now){
            this.$emit('update:fontColor',now);
        }
    },
    mounted(){

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
    height: 550px;
}

.edits>*{
    width: 30px;
    height: 30px;
    margin-top: 20px;
    border-radius: 1rem;
}
.edits img{
    filter: invert(60%);
    width: 30px;
    height: 30px;
}
img:hover,img:active{
    filter: invert(0%);
}

.block{
    flex: 1;
}

.fast{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.edit{
    background-color: rgba(255,255,255,0);
    width: 100%;
    border: 1px solid #0F141C;
    border-radius: .5rem;
    padding: 3px;
}

.more{
    display: flex;
    justify-content: space-between;
}

.more>img{
    width: 30px;
    height: 30px;
    filter: invert(60%);
}
.more>img:hover,.more>img:active{
    filter: invert(0%);
}

</style>