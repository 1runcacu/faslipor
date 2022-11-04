<template>
    <div id="box">
        <div class="header">
            <el-page-header @back="onBack">
                <template #content>
                    <span class="text-large font-600 mr-3"> 房间名称 </span>
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
            <div class="left"></div>
            <canvas id="canvas" class="right"></canvas>
        </div>
    </div>
</template>



<script setup>
import { ElPageHeader } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue';
import { onMounted } from 'vue'
import { fabric } from 'fabric'

const router = useRouter();

const onBack = () => {
    router.go(-1);
}

const width = ref(1000);
const height = ref(1000);

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
    canvas.setWidth(window.innerWidth - 100);
    canvas.setHeight(window.innerHeight - 83);
    window.onresize = (canvas=>{
        return function(){
            canvas.setWidth(window.innerWidth - 100);
            canvas.setHeight(window.innerHeight - 83);
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
    background-color: #F7F7F7;
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

.body{
    flex: 1;
    display: flex;
    justify-content: stretch;
    padding: 10px 0 10px 0;
    overflow: hidden;
}
.left{
    width: 50px;
    border-radius: 0 10px 10px 0;
    background-color: rgba(255, 255, 255, 0.895);
}
.header:hover,.left:hover,.right:hover{
    box-shadow: #F7F7F7 2px 2px 6px;
}

.right{
    margin: 0 20px 0 30px;
    flex: 1;
    border: 1px solid black;
    border-radius: 10px;
}

.left,.right{
    backdrop-filter: blur(15px);
    border: 1px solid white;
    box-shadow: #F7F7F7 2px 2px 3px;
}

</style>
