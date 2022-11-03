<template>
    <transition name="drag-win">
      <div
        class="drag-dialog ban-select-font"
        ref="dragWin"
        v-resize="resizeAble"
        v-if="closeShow"
      >
        <div class="drag-bar" :style="headStyle" v-drag="dragAble">
          <slot name="head" />
          <div style="margin-left:auto;"></div>
          <el-icon @click="closehandle"><Minus /></el-icon>
          <el-icon  @click="fullScreen"><FullScreen /></el-icon>
          <el-icon class="close" @click="closehandle"><Close/></el-icon>
        </div>
        <div class="drag-main" :style="mainStyle">
          <slot/>
        </div>
      </div>
    </transition>
</template>

<script>
import { ElIcon } from 'element-plus';
import { FullScreen,Close,Minus} from '@element-plus/icons-vue'


const recordBox = {
    width: 0,
    height: 0,
    top: 0,
    left: 0,
    fill: false
};

let dragWin = null;

export default{
    name:'win-ui',
    components:{
        ElIcon,
        FullScreen,Close,Minus
    },
    props:{
        width:{
            type:String,
            default:"500px"
        },
        height:{
            type:String,
            default:"40vh"
        },
        headHeight:{
            type:String,
            default:"35px"
        },
        headStyle:{
            type:String,
            default:""
        },
        mainStyle:{
            type:String,
            default:""
        },
        resizeAble:{
            type:Boolean,
            default:true
        },
        dragAble:{
            type:Boolean,
            default:true
        },
        closeShow:{
            type:Boolean,
            default:true
        },
        fullShow:{
            type:Boolean,
            default:true
        }
    },
    mounted(){
        dragWin = this.$refs['dragWin'];
    },
    methods:{
        fullScreen(){
            const tmp = dragWin;
            const style = dragWin.style;
            // 宽的样式 如果被手动缩小或者放大，则表示非全屏状态，则将状态置为false
            if (!style.width || style.width !== "100vw") {
                recordBox.fill = false;
            }
            // 全屏或是还原
            if (recordBox.fill) {
                style.width = `${recordBox.width}px`;
                style.height = `${recordBox.height}px`;
                style.top = `${recordBox.top}px`;
                style.left = `${recordBox.left}px`;
            } else {
                // 记录一下原来的样式
                recordBox.width = tmp.offsetWidth;
                recordBox.height = tmp.offsetHeight;
                recordBox.top = tmp.offsetTop;
                recordBox.left = tmp.offsetLeft;
                //全屏样式
                style.width = "100vw";
                style.height = "100vh";
                style.top = "0px";
                style.left = "0px";
            }
            recordBox.fill = !recordBox.fill; // 全屏状态变换
        },
        closehandle(){
            this.$emit("close",true);
        }
    }
}

</script>

  
<style>
.ban-select-font {
    -moz-user-select: none; /*火狐*/
    -webkit-user-select: none; /*webkit浏览器*/
    -ms-user-select: none; /*IE10*/
    -khtml-user-select: none; /*早期浏览器*/
    user-select: none;
}
</style>

<style scoped>
   /* 禁止选中文字 */


.drag-dialog {
    position: fixed;
    width: v-bind("width");
    height: v-bind("height");
    left: calc(50% - v-bind("width") / 2);
    top: calc(50% - v-bind("height") / 2);
    box-sizing: border-box;
    padding: 8px;
    overflow: hidden;
    color: #000;
    min-width: 200px;
    min-height: 200px;
    max-width: 100vw;
    max-height: 100vh;
    border: 1px solid black;
    border-radius:10px 0 10px 0;
    backdrop-filter: blur(5px);
    background-color: rgba(233, 237, 240, 0.8);
    box-shadow: rgb(204, 204, 204) 0px 0px 5px;
}
  
.drag-bar {
    width: 100%;
    cursor: move;
    height: v-bind("headHeight");
    border-bottom: 1px solid #000;
    box-sizing: border-box;
    display: flex;
    /* background-color: #919395; */
}

.el-icon{
    width: 25px;
    height: 25px;
    margin-left: 10px;
    /* line-height:25px;
    border-radius: 30px; */
    cursor: default;
    border-radius: .5rem;
}
.el-icon:hover{
    color: #E8E8E8;
    background-color: grey;
    transform: scale(1.2,1.2);
}
.close:hover{
    background-color: #f2473e;
}

.drag-main {
    width: 100%;
    height: calc(100% - v-bind("headHeight"));
    box-sizing: border-box;
    overflow: auto;
    font-size: 13px;
    line-height: 1.6;
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
</style>