<template>
  <canvas width="600" height="600" id="canvas" style="border: 1px solid #ccc;"></canvas>
</template>
 
<script setup>
import { onMounted } from 'vue'
import { fabric } from 'fabric'
 
function init() {
  let canvas = new fabric.Canvas('canvas') // 实例化fabric，并绑定到canvas元素上
 
  // 圆
  let circle = new fabric.Circle({
    left: 100,
    top: 100,
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
  canvas.add(circle)
}
 
onMounted(() => {
  init()
})
</script>