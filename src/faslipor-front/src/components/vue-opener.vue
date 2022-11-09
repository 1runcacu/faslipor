<template>
    <div class="drag-opener" v-if="select" @click="change"></div>
    <transition name="drag-win" v-show="!select">
        <div class="drag-box ban-select-font" >
            <div class="header" v-drag>
                <strong>工具栏</strong>
                <el-icon class="close" @click="change"><Close/></el-icon>
            </div>
            <div id="box" v-drag="PC">                
                <el-scrollbar class="body">
                    <slot/>
                </el-scrollbar>
            </div>
        </div>
    </transition>
</template>

<script>
import { Close} from '@element-plus/icons-vue'

export default{
    data(){
        return {
            select:false
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
    },
    computed:{
        PC:function(){
            if (/(iPad|iPod)/i.test(navigator.userAgent)) {
                return false;
            } else if (/(iPhone|Android|iOS)/i.test(navigator.userAgent)) {
                return false;
            }
            return true;
        }
    }
}

</script>

<style scoped>

.header{
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 35px;
}

.body{
    width: 100%;
    overflow: auto;
    flex: 1;
}

.drag-box{
    position: fixed;
    width: 200px;
    height: 600px;
    left: 0;
    top: calc(50% - 300px);
    box-sizing: border-box;
    padding: 8px;
    overflow: hidden;
    color: #000;
    border: 1px solid black;
    border-radius: 0 30px 0 0;
    backdrop-filter: blur(5px);
    background-color: rgba(233, 237, 240, 0.8);
    box-shadow: rgb(204, 204, 204) 0px 0px 5px;
}
#box{
    width: 100%;
    height: calc(100% - 35px);
    display: flex;
    flex-direction: column;
}
.drag-opener {
    position: fixed;
    left: 0;
    top: 45%;
    height: 80px;
    width: 15px;
    overflow: hidden;
    border: 0.5px solid black;
    border-radius:0 50px 50px 0;
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
</style>