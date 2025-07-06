<template>
    <div class="drag-box" v-if="!winShow" v-dbclick2="open">
      <div class="ban-select-font" v-drag>
        <img src="@/assets/logo.png" id="logo"/>
      </div>
    </div>
    <winUiVue :resizeAble="true" @close="closeWin" :width="WH" height="40vh" v-show="winShow"
        minWidth="30vh" minHeight="280px" 
    >
        <template #head>
          <div><b>{{title}}</b></div>
          <!-- <el-button link @click="isCollapse=false" v-show="isCollapse"><el-icon><CaretRight /></el-icon></el-button>
          <el-button link @click="isCollapse=true" v-show="!isCollapse"><el-icon><CaretLeft /></el-icon></el-button> -->
        </template>
        <div class="body">
            <div>
                <h3>更多信息</h3>
                <p>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/README.html">跳转文档</a></p>
            </div>
            <div>
                <h3>扫码访问</h3>
                <img id="qcode" src="/qcode.png"/>
            </div>
            <!-- <el-scrollbar class="left" @touchmove.stop>
                <el-menu
                    default-active="2"
                    class="el-menu-vertical-demo"
                    :collapse="isCollapse"
                    background-color="rgba(255,255,255,0)"
                    @open="handleOpen"
                    @close="handleClose"
                >
                    <el-sub-menu index="1">
                    <template #title>
                        <el-icon><location /></el-icon>
                        <span>Navigator One</span>
                    </template>
                    <el-menu-item-group>
                        <template #title><span>Group One</span></template>
                        <el-menu-item index="1-1">item one</el-menu-item>
                        <el-menu-item index="1-2">item two</el-menu-item>
                    </el-menu-item-group>
                    <el-menu-item-group title="Group Two">
                        <el-menu-item index="1-3">item three</el-menu-item>
                    </el-menu-item-group>
                    <el-sub-menu index="1-4">
                        <template #title><span>item four</span></template>
                        <el-menu-item index="1-4-1">item one</el-menu-item>
                    </el-sub-menu>
                    </el-sub-menu>
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
            <el-scrollbar class="right" @touchmove.stop>
                <el-collapse v-model="activeNames" @change="handleChange">
                    <el-collapse-item title="Consistency" name="1">
                        <div>
                        这里可以写点文档，但作者太懒了
                        </div>
                        <div>
                        Consistent within interface: all elements should be consistent, such
                        as: design style, icons and texts, position of elements, etc.
                        </div>
                    </el-collapse-item>
                    <el-collapse-item title="Feedback" name="2">
                        <div>
                        Operation feedback: enable the users to clearly perceive their
                        operations by style updates and interactive effects;
                        </div>
                        <div>
                        Visual feedback: reflect current state by updating or rearranging
                        elements of the page.
                        </div>
                    </el-collapse-item>
                    <el-collapse-item title="Efficiency" name="3">
                        <div>
                        Simplify the process: keep operating process simple and intuitive;
                        </div>
                        <div>
                        Definite and clear: enunciate your intentions clearly so that the
                        users can quickly understand and make decisions;
                        </div>
                        <div>
                        Easy to identify: the interface should be straightforward, which helps
                        the users to identify and frees them from memorizing and recalling.
                        </div>
                    </el-collapse-item>
                    <el-collapse-item title="Controllability" name="4">
                        <div>
                        Decision making: giving advices about operations is acceptable, but do
                        not make decisions for the users;
                        </div>
                        <div>
                        Controlled consequences: users should be granted the freedom to
                        operate, including canceling, aborting or terminating current
                        operation.
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </el-scrollbar> -->
        </div>
    </winUiVue>
</template>

<script setup>
import { ElMenu,ElSubMenu,ElMenuItemGroup,ElMenuItem,ElCollapse,ElCollapseItem } from 'element-plus';
import { ref,computed, onMounted } from 'vue';
import winUiVue from './win-ui.vue';
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  CaretRight,
  CaretLeft
} from '@element-plus/icons-vue'

const winShow = ref(false);
const title = ref("FASLIPOR精灵");

const isCollapse = ref(false)
const handleOpen = (key, keyPath) => {
//   console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
//   console.log(key, keyPath)
}

const closeWin = ()=>{
    winShow.value = false;
}

const open = ()=>{
    winShow.value = true;
}

const activeNames = ref(['1'])
const handleChange = (val) => {
//   console.log(val)
}

const WH = computed(()=>{
    return (window.innerWidth>500?500:window.innerWidth)+"px"
});

</script>

<style scoped>

#logo{
  width: 50px;
  height: 50px;
  -webkit-user-drag: none;
  filter: invert(80%);
}
.drag-box:hover,.drag-box:active{
  transform: scale(1.2,1.2);
  filter: invert(100%);
  background-color: rgba(38, 35, 35, 1);
}

.drag-box{
    position: fixed;
    width: 50px;
    height: 50px;
    bottom: 120px;
    right: 120px;
    box-sizing: border-box;
    overflow: hidden;
    border: 1px solid white;
    border-radius: 25px;
    backdrop-filter: blur(5px);
    box-shadow: rgb(255, 255, 255) 0px 0px 5px;
}
.drag-box>div{
  width: 100%;
  height: 100%;
}
.body{
    width: 100%;
    height: 100%;
    display: flex;
    overflow: hidden;
    align-items: stretch;
    flex-direction: column;
}
.left{
    border-right: 1px solid black;
}
.right{
    flex: 1;
    padding: 5px;
}

.el-menu,.el-sub-menu,.el-menu-item-group,.el-menu-item,.el-collapse,.is-opend,
.el-collapse-item__header
{
    background-color: rgba(255,255,255,0) !important;
}
.el-collapse{
    --el-collapse-content-bg-color: rgba(255,255,255,0) !important;
}
#qcode {
    width: 100px;
    height: 100px;
    user-select: none;
    -webkit-user-drag: none;
}
</style>

<style>
.el-collapse-item__header
{
    background-color: rgba(255,255,255,0) !important;
}
</style>