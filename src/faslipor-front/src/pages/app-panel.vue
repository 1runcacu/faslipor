<template>
    <div id="box">
        <div class="header">
            <el-page-header @back="onBack">
                <template #content>
                    <span class="text-large font-600 mr-3" style="color:#f7f7f7"> 房间名称 </span>
                    <el-button :type="toolShow?'success':'info'" class="console" link @click="toolsHandle">工具栏</el-button>
                    <el-button type="success" class="console" link>控制台</el-button>
                </template>
                <template #icon>
                    <el-icon class="back"><ArrowLeft/></el-icon>
                </template>
                <template #title>
                    <div></div>
                </template>
                <template #extra>
                    
                </template>
            </el-page-header>
        </div>
        <canvas id="canvas"></canvas>
        <vueOpener :toolShow="toolShow" @open="openHandle">
            <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item title="形状" name="1">
                <div class="icons">
                    <img src="@/assets/icon/椭圆形.png"/>
                    <img src="@/assets/icon/三角形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                    <img src="@/assets/icon/矩形.png"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="自定义" name="2">
                <div class="icons">
                    <img src="@/assets/icon/编辑.png"/>
                    <img src="@/assets/icon/直线.png"/>
                    <img src="@/assets/icon/曲线.png"/>
                    <img src="@/assets/icon/文字.png"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="样式表" name="3">
                <div>
                Simplify the process: keep operating process simple and intuitive;
                </div>
                <div>
                Easy to identify: the interface should be straightforward, which helps
                the users to identify and frees them from memorizing and recalling.
                </div>
            </el-collapse-item>
            <el-collapse-item title="更多信息" name="4">
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
        </vueOpener>
        <!-- <vueToolsVue/> -->
        <!-- <winUiVue :resizeAble="false" @close="consoleClose" :closeShow="true" width="30vh" height="30vh">
            <template #head>
                <div><b>控制台</b></div>
            </template>
        </winUiVue> -->
    </div>
</template>


<script setup>
import { ElPageHeader,ElCollapse,ElCollapseItem } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue';
import { onMounted } from 'vue'
import { fabric } from 'fabric'
import winUiVue from '@/components/win-ui.vue';
import vueToolsVue from '@/components/vue-tools.vue';
import vueOpener from '@/components/vue-opener.vue'

const router = useRouter();

const onBack = () => {
    router.push({name:"index"});
}

const width = ref(1000);
const height = ref(1000);

const consoleClose = ()=>{

};

const activeNames = ref(['1'])
const handleChange = (val) => {
  console.log(val)
}

const toolShow = ref(true);

const toolsHandle = () => {
    toolShow.value = !toolShow.value;
}
const openHandle = () => {
    toolShow.value = true;
}

function init() {
    const canvas = new fabric.Canvas('canvas') // 实例化fabric，并绑定到canvas元素上
   
    // 圆
    let circle = new fabric.Circle({
      left: 200,
      top: 200,
      radius: 50,
    })
   
    // 线性渐变
    let gradient = new fabric.Gradient({
      type: 'linear', // linear or radial
      gradientUnits: 'pixels', // pixels or pencentage 像素 或者 百分比
      coords: { x1: 0, y1: 0, x2: circle.width, y2: 0 }, // 至少2个坐标对(x1，y1和x2，y2)将定义渐变在对象上的扩展方式
      colorStops:[ // 定义渐变颜色的数组
        { offset: 0, color: 'red' },
        { offset: 0.2, color: 'orange' },
        { offset: 0.4, color: 'yellow' },
        { offset: 0.6, color: 'green' },
        { offset: 0.8, color: 'blue' },
        { offset: 1, color: 'purple' },
      ]
    })
    circle.set('fill', gradient);
    canvas.add(circle);
    canvas.add(new fabric.Circle({
      left: 100,
      top: 100,
      radius: 50,
      fill: 'gray'
    }))
    canvas.add(new fabric.Circle({
      left: 200,
      top: 100,
      radius: 50,
      fill: 'blue'
    }))
    canvas.add(new fabric.Circle({
      left: 100,
      top: 200,
      radius: 30,
      fill: 'red'
    }))
    canvas.add(new fabric.Triangle({
        width: 20, height: 30, fill: 'blue', left: 50, top: 50
    }))
    canvas.setWidth(window.innerWidth);
    canvas.setHeight(window.innerHeight);
    window.onresize = (canvas=>{
        return function(){
            canvas.setWidth(window.innerWidth);
            canvas.setHeight(window.innerHeight);
        }
    })(canvas);
  }
   
  onMounted(() => {
    init()
  })

</script>

<style scoped>
#box{
    display: flex;
    flex-direction: column;
    height: 100%;
}
.header{
    background-color: #0F141C;
    color: #F7F7F7;
    padding: 0 20px 20px 20px;
    border-bottom: 1px solid gray;
    box-shadow: #F7F7F7 1px 1px 3px;
}

.back{
    transform: scale(1.5,1.5);
}
.back:hover{
    transform: scale(1.8,1.8);
    color: #409EFF;
}

#canvas{
    position: absolute;
    background-color: white;
}

.console{
    margin-left: 50px;
}
.icons{
    margin-top: 5px;
}
.icons>img{
    width: 32px;
    height: 32px;
    filter: invert(50%);
}
.icons>img:hover{
    transform: scale(1.2,1.2);
    filter: invert(10%);    
    /* background-color: white; */
}

.el-collapse{
    --el-collapse-content-bg-color: rgba(255,255,255,0);
}
</style>
