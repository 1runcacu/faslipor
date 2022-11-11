<template>
    <div id="box">
        <div class="header">
            <el-page-header @back="onBack">
                <template #content>
                    <span class="text-large font-600 mr-3 ban-select-font" style="color:#f7f7f7"> {{$store.state.params.room?$store.state.params.room.name||"":""}} </span>
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
        <div id="cvs">
            <div id="layout" class="lay">
                <canvas id="pencil" ></canvas>
            </div>
            <div id="layout" >
                <canvas id="canvas" ref="BUFFER" ></canvas>
            </div>
        </div>
        <vueOpener :toolShow="toolShow" @open="openHandle">
            <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item title="工程" name="0">
                <div class="icons">
                    <img v-for="item in toolsConfig.project" :src="item.src" v-click2="()=>makeAct(item)"/>
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
                    <img v-for="item in toolsConfig.layout" :src="item.src" />
                </div>
            </el-collapse-item>
            <el-collapse-item title="形状" name="2">
                <div class="icons">
                    <img v-for="item in toolsConfig.shape" :src="item.src" v-click2="()=>makeShape(item)"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="样式表" name="3">
                <div>

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
        <vueEditVue @KD="kd" ref="edit"
            v-model:lineWidth="EditBox.lineWidth"
            v-model:fontSize="EditBox.fontSize"
            v-model:fontColor="EditBox.fontColor"
        />
        <vueConsoleVue :show="consoleShow" @close="consoleHandle" />
    </div>
</template>


<script setup>
import { ElPageHeader,ElCollapse,ElCollapseItem} from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { inject, ref,computed,onBeforeUnmount,getCurrentInstance } from 'vue';
import { onMounted } from 'vue'
import { fabric } from 'fabric'
import vueConsoleVue from '@/components/vue-console.vue';
import vueOpener from '@/components/vue-opener.vue'
import vueEditVue from '@/components/vue-edit.vue';
import { useStore } from 'vuex';
import {throttle,shakeProof} from "@/api/util";

const ctx = getCurrentInstance().appContext.config.globalProperties;

const socket = inject("socket");
const store = useStore();
const router = useRouter();

let FIRST = true;

var config = computed(()=>store.state.params||{room:{},user:{}});

const onBack = () => {
    try{
        router.go(-1);
    }catch(err){
        router.push({path:"/"});
    }
}

const zindex = ref(0);

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
        src:require("../assets/icon/添加.png"),
        label:"添加"
    },{
        src:require("../assets/icon/删除.png"),
        label:"删除"
    }],
    shape:[{
        src:require("../assets/icon/编辑.png"),
        label:"编辑"
    },{
        src:require("../assets/icon/三角形.png"),
        label:"三角形"
    },{
        src:require("../assets/icon/椭圆形.png"),
        label:"椭圆形"
    },{
        src:require("../assets/icon/矩形.png"),
        label:"矩形"
    },{
        src:require("../assets/icon/文字.png"),
        label:"文字"
    },{
        src:require("../assets/icon/直线.png"),
        label:"直线"
    },{
        src:require("../assets/icon/曲线.png"),
        label:"曲线"
    }],
    style:[],
    more:[]
});

let ctrlFlag = false;

const kd = (v,state=false)=>{
    switch(v){
        case 13:
            let obj = canvas.getActiveObject();
            if(obj&&obj.type==="i-text"){
                obj.exitEditing();
                obj.selected=false;
            }
            zindex.value = 0;
            pencil.clear();
            break;
        case 17:
            ctrlFlag = state;
            break;
    }
}

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

const ID = ()=>Date.now().toString(36);

function sendCanvas(){
    const {room:{rid},user:{uid}} = config.value;
    socket.emit("stream",{
        event:"all",
        rid,uid,
        frame:JSON.stringify(canvas.toJSON())
    });
}


const modified = shakeProof((frame,syn=false)=>{
    const {room:{rid},user:{uid}} = config.value;
    socket.emit("stream",{
        event:"edit",
        rid,uid,syn,
        frame
    });
},2);

const BUFFER = ref(null);

let SID;
let MODE;
let path = [];
let SBOX = {
    mx:null,
    my:null,
    Mx:null,
    My:null
};

const makeShape = item=>{
    const {label} = item;
    switch(label){
        case "直线":
            MODE = 0;break;
        case "曲线":
            MODE = 1;break;
        case "编辑":
            MODE = 2;break;
            return;
        case "三角形":
            MODE = 3;
            break;
        case "椭圆形":
            MODE = 4;
            break;
        case "矩形":
            MODE = 5;
            break;
        case "文字":
            MODE = 6;
            break;
        default:return;
    }
    SID = ID();
    path = [];
    zindex.value = 2;
    toolShow.value=false;
    SBOX = {
        mx:null,
        my:null,
        Mx:null,
        My:null
    };
    if(FIRST){
        ctx.$message({
            message:"触碰画布构建图元~",
            type:"success"
        });
        FIRST = false;
    }
    // if(!create){
    //     return;
    // }
    // const graph = new fabric[create.type](...create.params);
    // if(graph){
    //     try{
    //         const pixel = graph.toJSON(["gid","date"])
    //         modified({
    //             type:"创建",
    //             pixel
    //         });
    //         addPixel(pixel);
    //     }catch(err){}
    // }
}

const makeAct = item=>{
    const {label} = item;
    const {room:{rid},user:{uid}} = config.value;
    let event = "error";
    switch(label){
        case "保存":
            event="save";
            break;
        case "导出":
            event="export";
            break;
        case "导入":
            event="import";
            break;
        case "撤销":
            event="undo";
            break;
        case "重做":
            event="redo"
            break;
    }
    socket.emit("stream",{
        event,
        rid,uid
    });
}

function addPixel(...args){
    fabric.util.enlivenObjects(args, function(objects) {
        var origRenderOnAddRemove = canvas.renderOnAddRemove;
        canvas.renderOnAddRemove = false;
        objects.forEach(function(o) {
            if(o.type==="path"){
                o.fill = null;
            }
            canvas.add(o);
            const editcall =(syn=false)=>{
                o.date = Date.now();
                modified({
                    type:"编辑",
                    pixel:o.toJSON(["gid","date"]),
                    syn
                });
            };
            if(o.type==="i-text"){
                o.onDeselect = function(){
                    this.isEditing&&this.exitEditing();
                    this.selected=false;
                    editcall(true);
                };
                let func = o.onInput.bind(o);
                o.onInput = function(e){
                    func(e);
                    editcall(false);
                }
            }
            o.on("moving",editcall);
            o.on("scaling",editcall);
            o.on("rotating",editcall);
        });
        canvas.renderOnAddRemove = origRenderOnAddRemove;
        canvas.renderAll();
    });
}

const stream = data=>{
    try{
        const {rid,uid,event,frame} = data;
        if(event!=="refresh"&&uid === config.value.user.uid){
            return;
        }
        switch(event){
            case "increment":
                const create = frame;
                const graph = new fabric[create.type](...create.params);
                canvas.add(graph);
                break;
            case "all":
                canvas.loadFromJSON(frame);
                break;
            case "edit":
                const Objs = canvas.getObjects();
                const {pixel} = frame;
                if(!Objs.find(v=>{
                    if(v.gid===pixel.gid){
                        canvas.remove(v);
                        addPixel(pixel);
                    }
                    return v.gid===pixel.gid
                })){
                    addPixel(pixel);
                }
                break;
            case "refresh":
                canvas.clear();
                addPixel(...Object.values(frame));
                break;
        }
    }catch(err){console.log(err)};
}

var canvas; 
var pencil;

var edit = ref(null);

const EditBox = ref({
    lineWidth:2,
    fontSize:2,
    fontColor:"#234567"
});

const send = shakeProof(()=>sendCanvas(),30);

function init() {
    canvas = new fabric.Canvas('canvas');
    pencil = new fabric.Canvas('pencil');
    let panning = false;
    
    canvas.selection = false;
    pencil.selection = false;

    canvas.on('mouse:down', function(options) {
        if(options.e.ctrlKey||ctrlFlag) {
          panning = true;
        //   canvas.selection = false;
        }
        // edit.value.reset(1,2,"#232312");
    });

    let previousTouch = null;

    // 鼠标抬起时
    canvas.on('mouse:up', function(options) {
        // send();
        panning = false;
        previousTouch = null;
    });
    // 鼠标移动时
    canvas.on('mouse:move', function(options) {
        // previousTouch = previousTouch?previousTouch:canvas.getPointer(options.e);
        // let now = canvas.getPointer(options.e);
        // let [dx,dy] = [now.x - previousTouch.x,now.y - previousTouch.y];
        if (panning && options && options.e) {
            if(options.e.movementX!=undefined&&options.e.movementY!=undefined){

            }else{
                const touch = options.e.touches[0];
                if(previousTouch!=undefined){
                    options.e.movementX = touch.screenX - previousTouch.screenX;
                    options.e.movementY = touch.screenY - previousTouch.screenY;
                }else{
                    previousTouch = touch;
                    return;
                }
                previousTouch = touch;
            }
            let delta = new fabric.Point(options.e.movementX, options.e.movementY);
            if(delta.x==undefined||delta.y==undefined){
                return;
            }
            canvas.relativePan(delta);
            pencil.relativePan(delta);
        }
        // send();
    });

    pencil._onMouseMoveInDrawingMode = function(e) {
      if (pencil._isCurrentlyDrawing) {
        var pointer = pencil.getPointer(e);
        pencil.freeDrawingBrush.onMouseMove(pointer, { e: e, pointer: pointer });
        path.push(["Q",pointer.x,pointer.y]);
        if(SBOX.mx!=null){
            SBOX.mx = Math.min(SBOX.mx,pointer.x);
        }else{ SBOX.mx = pointer.x;}
        if(SBOX.my!=null){
            SBOX.my = Math.min(SBOX.my,pointer.y);
        }else{ SBOX.my = pointer.y;}
        if(SBOX.Mx!=null){
            SBOX.Mx = Math.max(SBOX.Mx,pointer.x);
        }else{ SBOX.Mx = pointer.x;}
        if(SBOX.My!=null){
            SBOX.My = Math.max(SBOX.My,pointer.y);
        }else{ SBOX.My = pointer.y;}
        path[0][0] = "M";
        path[path.length-1][0] = "L";
        let OBJ = new fabric.Path(path,{
            stroke:"pink",
            backgroundColor:"",
            strokeWidth:3,
            fillRule:"nonzero",
            fill:null,
            gid:SID,
            date:Date.now()
        });
        
        modified({
            type:"编辑",
            pixel:OBJ.toJSON(["gid","date"])
        });
      }
      pencil.setCursor(pencil.freeDrawingCursor);
      pencil._handleEvent(e, 'move');
    }

    pencil.on('mouse:up', function(options) {
        let {lineWidth,fontSize,fontColor} = EditBox.value;
        toolShow.value=true;
        zindex.value = 0;
        let obj = pencil.getObjects()[0];
        let json = obj.toJSON();
        pencil.clear();
        let c = json.path.pop();
        let width = 50,height = 50,left = 0,top = 0,radius=50;
        if(fontSize==null)fontSize = 1.3;
        if(fontColor=="")fontColor = "pink";
        if(lineWidth==null)lineWidth = 1.3;
        if(SBOX.Mx!=undefined&&SBOX.My!=undefined&&SBOX.mx!=undefined&&SBOX.my!=undefined){
            left = SBOX.mx;top = SBOX.my;
            width = SBOX.Mx - SBOX.mx;
            height = SBOX.My - SBOX.my;
            radius = ~~(Math.min(height,width)/2);
        }
        const style = { 
            top,left,width, height,fill:fontColor,radius,
        }
        switch(MODE){
            case 0:
                json.path.length = 1;
                json.path.push(c);
                break;
            case 1:
                let len = json.path.length;
                let a = json.path[Math.floor(len/3)];
                let b = json.path[Math.floor(len*2/3)];
                json.path.length = 1;
                json.path.push(["C",a[1],a[2],b[1],b[2],c[1],c[2]]);
            case 2:break;
            case 3:
                json = new fabric.Triangle(style).toJSON();
                break;
            case 4:
                json = new fabric.Circle(style).toJSON();
                break;
            case 5:
                json = new fabric.Rect(style).toJSON();
                break;
            case 6:
                json = new fabric.IText('TEXT',{top,left,width, height,fill:fontColor}).toJSON();
                console.log(new fabric.IText('TEXT',{top,left,width, height,fill:fontColor}));
                break;
            default:return;
        }
        json.gid = SID;
        json.date = Date.now();
        json.strokeWidth = lineWidth;
        json.stroke = fontColor;
        addPixel(json);
        modified({
            type:"添加",
            pixel:json
        },true);
        // let path = new fabric.Path(json.path,json);
        // canvas.add(path);
    });
    
    canvas.on("mouse:wheel", function(options) {
        let zoom = (options.e.deltaY > 0 ? -0.1 : 0.1) + canvas.getZoom();
        zoom = Math.max(0.1, zoom); //最小为原来的1/10
        zoom = Math.min(3, zoom); //最大是原来的3倍
        const zoomPoint = new fabric.Point(options.e.pageX, options.e.pageY);
        canvas.zoomToPoint(zoomPoint, zoom);
        pencil.zoomToPoint(zoomPoint, zoom);
    });

    pencil.on("mouse:wheel", function(options) {
        let zoom = (options.e.deltaY > 0 ? -0.1 : 0.1) + canvas.getZoom();
        zoom = Math.max(0.1, zoom); //最小为原来的1/10
        zoom = Math.min(3, zoom); //最大是原来的3倍
        const zoomPoint = new fabric.Point(options.e.pageX, options.e.pageY);
        canvas.zoomToPoint(zoomPoint, zoom);
        pencil.zoomToPoint(zoomPoint, zoom);
    });

    canvas.on("object:modified",function(option){
        if(option.target._objects){
            // option.target._objects.forEach(obj=>{
            //     modified({
            //         type:"编辑",
            //         pixel:obj.toJSON(["gid","date"])
            //     });
            // });
        }else{
            option.target.date = Date.now();
            modified({
                type:"编辑",
                pixel:option.target.toJSON(["gid","date"])
            },true);
        }
    });

    //线段
//https://juejin.cn/post/7026941253845516324

    //画笔
//https://blog.csdn.net/jamesclark/article/details/123125753


//event
//http://fabricjs.com/events

//curve
//http://fabricjs.com/quadratic-curve
    // canvas.on('after:render', function (options) {
    //     console.log(canvas.getActiveObject())
    // });

    pencil.freeDrawingBrush.width = 3;
    pencil.freeDrawingBrush.color = 'pink';
    pencil.isDrawingMode = 1; 

    canvas.setWidth(window.innerWidth);
    canvas.setHeight(window.innerHeight);
    pencil.setWidth(window.innerWidth);
    pencil.setHeight(window.innerHeight);
    window.onresize = (canvas=>{
        return function(){
            canvas.setWidth(window.innerWidth);
            canvas.setHeight(window.innerHeight);
            pencil.setWidth(window.innerWidth);
            pencil.setHeight(window.innerHeight);
        };
    })(canvas);
}

function refresh(){
    const {room:{rid},user:{uid}} = config.value;
    socket.emit("stream",{
        event:"refresh",
        rid,uid
    });
}


onMounted(() => {
    init();
    socket.on("stream",stream);
    refresh();
    document.onkeydown=function(event){
        event = event|| window.event;
        // console.log(event.keyCode );
   }
});

onBeforeUnmount(()=>{
    socket.off("stream",stream);
});

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
#pencil{
    position: absolute;
    background-color: rgba(255,255,255,0.5);
}

#cvs{
    position: relative;
}
/* .canvas-container{
    position: absolute !important;   
} */

#layout{
    position: absolute;
}

.lay{
    z-index: v-bind(zindex) !important;
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
