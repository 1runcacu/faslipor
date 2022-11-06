<template>
    <div id="box">
        <div class="header">
            <el-page-header @back="onBack">
                <template #content>
                    <span class="text-large font-600 mr-3 ban-select-font" style="color:#f7f7f7"> 房间名称 </span>
                    <el-button :type="toolShow?'success':'info'" class="console" link @click="toolsHandle">工具栏</el-button>
                    <el-button :type="consoleShow?'success':'info'" class="console" link @click="consoleShow=!consoleShow">控制台</el-button>
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
            <el-collapse-item title="工程" name="0">
                <div class="icons">
                    <img v-for="item in toolsConfig.project" :src="item.src"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="图层" name="1">
                <div class="input">
                    <select v-model="value" size="2" class="block">
                        <option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </select>
                    <input placeholder="图层名称"/>
                </div>
                <div class="icons">
                    <img v-for="item in toolsConfig.layout" :src="item.src"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="形状" name="2">
                <div class="icons">
                    <img v-for="item in toolsConfig.shape" :src="item.src" @dblclick="makeShape(item)"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="自定义" name="3">
                <div class="icons">
                    <img v-for="item in toolsConfig.custom" :src="item.src"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="样式表" name="4">
                <div>

                </div>
            </el-collapse-item>
            <el-collapse-item title="更多信息" name="5">
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
        <vueConsoleVue :show="consoleShow" @close="consoleHandle"/>
    </div>
</template>


<script setup>
import { ElPageHeader,ElCollapse,ElCollapseItem,ElSelect,ElOption } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { inject, ref } from 'vue';
import { onMounted } from 'vue'
import { fabric } from 'fabric'
import vueConsoleVue from '@/components/vue-console.vue';
import vueOpener from '@/components/vue-opener.vue'

const socket = inject("socket");

const router = useRouter();

const onBack = () => {
    router.push({name:"index"});
}

const width = ref(1000);
const height = ref(1000);

const consoleClose =ref((val)=>{
    console.log(val);
});

const value = ref();
const options = ref([{
    value:1,
    label:1
},{
    value:2,
    label:2
},{
    value:3,
    label:3
},{
    value:4,
    label:4
},{
    value:5,
    label:5
}]);

const toolsConfig = ref({
    project:[{
        src:require("../assets/icon/保存.png"),
        label:"保存"
    },{
        src:require("../assets/icon/导出.png"),
        label:"导出"
    },{
        src:require("../assets/icon/导入.png"),
        label:"导入"
    },{
        src:require("../assets/icon/撤销.png"),
        label:"撤销"
    },{
        src:require("../assets/icon/重做.png"),
        label:"重做"
    }],
    layout:[{
        src:require("../assets/icon/plus.png"),
        label:"添加"
    },{
        src:require("../assets/icon/delete.png"),
        label:"删除"
    }],
    shape:[{
        src:require("../assets/icon/直线.png"),
        label:"直线"
    },{
        src:require("../assets/icon/曲线.png"),
        label:"曲线"
    },{
        src:require("../assets/icon/三角形.png"),
        label:"三角形"
    },{
        src:require("../assets/icon/椭圆形.png"),
        label:"椭圆形"
    },{
        src:require("../assets/icon/矩形.png"),
        label:"矩形"
    }],
    custom:[{
        src:require("../assets/icon/编辑.png"),
        label:"编辑"
    },{
        src:require("../assets/icon/文字.png"),
        label:"文字"
    }],
    style:[],
    more:[]
});

const list = ref([{
    src:require("../assets/icon/plus.png"),
    label:"添加"
},{
    src:require("../assets/icon/delete.png"),
    label:"删除"
}]);

const activeNames = ref(['2'])
const handleChange = (val) => {
//   console.log(val)
}
const toolShow = ref(true);

const toolsHandle = () => {
    toolShow.value = !toolShow.value;
}
const openHandle = (val) => {
    toolShow.value = val;
}

const consoleShow = ref(false);

const consoleHandle = (val)=>{
    consoleShow.value = false;
}

const makeShape = item=>{
    const {label} = item;
    const [left,top] = [window.innerWidth/2,window.innerHeight/3];
    let graph = null;
    switch(label){
        case "直线":
            graph = new fabric.Line([10, 10, 100, 100], {
                left,
                top,
                fill: 'gray',
                stroke: 'gray'
            });
            break;
        case "曲线":
            graph = new fabric.Polyline([ 
            { 
                x:200, 
                y:10 
            }, 
            { 
                x:250, 
                y:50 
            }, { 
                x:250, 
                y:180 
            }, { 
                x:150, 
                y:180 
            }, { 
                x:150, 
                y:50 
            }], { 
                left,
                top,
                fill: 'gray'
            });
            break;
        case "三角形":
            graph = new fabric.Triangle({ top, left, width: 50, height: 50, fill: 'gray' });
            break;
        case "椭圆形":
            graph = new fabric.Circle({
                left,
                top,
                radius: 50,
                fill: 'gray'
            })
            break;
        case "矩形":
            graph = new fabric.Rect({ top, left, width: 50, height: 50, fill: 'gray' });
            break;
    }
    console.log(graph);
    if(graph)canvas.add(graph);
}

socket.on("stream",res=>{
    console.log(res)
});

var canvas; 

function init() {
    canvas = new fabric.Canvas('canvas') // 实例化fabric，并绑定到canvas元素上
    // 圆
    // let circle = new fabric.Circle({
    //   left: 200,
    //   top: 200,
    //   radius: 50,
    // })
   
    // 线性渐变
    // let gradient = new fabric.Gradient({
    //   type: 'linear', // linear or radial
    //   gradientUnits: 'pixels', // pixels or pencentage 像素 或者 百分比
    //   coords: { x1: 0, y1: 0, x2: circle.width, y2: 0 }, // 至少2个坐标对(x1，y1和x2，y2)将定义渐变在对象上的扩展方式
    //   colorStops:[ // 定义渐变颜色的数组
    //     { offset: 0, color: 'red' },
    //     { offset: 0.2, color: 'orange' },
    //     { offset: 0.4, color: 'yellow' },
    //     { offset: 0.6, color: 'green' },
    //     { offset: 0.8, color: 'blue' },
    //     { offset: 1, color: 'purple' },
    //   ]
    // })
    // circle.set('fill', gradient);
    // canvas.add(circle);
    // canvas.add(new fabric.Circle({
    //   left: 100,
    //   top: 100,
    //   radius: 50,
    //   fill: 'gray'
    // }))
    // canvas.add(new fabric.Circle({
    //   left: 200,
    //   top: 100,
    //   radius: 50,
    //   fill: 'blue'
    // }))
    // canvas.add(new fabric.Circle({
    //   left: 100,
    //   top: 200,
    //   radius: 30,
    //   fill: 'red'
    // }))
    // canvas.add(new fabric.Triangle({
    //     width: 20, height: 30, fill: 'blue', left: 50, top: 50
    // }))
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
    init();
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
.back:hover,.back:active{
    transform: scale(1.8,1.8);
    color: #409EFF;
}

#canvas{
    position: absolute;
    background-color: white;
}

.console{
    margin-left: 10px;
}
.icons{
    margin-top: 5px;
}
.icons>img{
    width: 32px;
    height: 32px;
    filter: invert(50%);
}
.icons>img:hover,.icons>img:active{
    transform: scale(1.2,1.2);
    filter: invert(10%);    
    /* background-color: white; */
}

.el-collapse{
    --el-collapse-content-bg-color: rgba(255,255,255,0);
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

.input{
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: stretch;
}
.input>input{
    margin-top: 5px;
    border: 1px solid gray;
    border-radius: 3px;
    background-color: rgba(255, 255, 255, 0.9);;
}
.input>select{
    height: 100px;
    background-color: rgba(255, 255, 255, 0.2);
}
</style>
