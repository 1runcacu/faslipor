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
            <el-collapse v-model="activeNames">
            <el-collapse-item title="工程" name="0">
                <div class="icons">
                    <img v-for="item in toolsConfig.project" :src="item.src" v-click2="()=>makeAct(item)"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="图层" name="1">
                <div class="input">
                    <div class="ipt-name">
                        <select v-model="layoutID" class="block" @change="LChangeHandle">
                            <option
                                v-for="item in config.layout"
                                :key="item.lid"
                                :label="item.name"
                                :value="item.lid"
                            />
                        </select>
                        <el-button type="danger" :icon="Delete" @click="delLayout" circle :disabled="LOCK"/>
                    </div>
                    <div class="ipt-name">
                        <input placeholder="请输入图层名称" v-model="Lyname"/>
                        <el-button type="success" :icon="Check" circle :disabled="nameUseable||LOCK" @click="catLayout"/>
                    </div>
                </div>
                <!-- <div class="icons">
                    <img v-for="item in toolsConfig.layout" :src="item.src" v-click2="()=>makeAct(item)"/>
                </div> -->
            </el-collapse-item>
            <el-collapse-item title="形状" name="2">
                <div class="icons">
                    <img v-for="item in toolsConfig.shape" :src="item.src" v-click2="()=>makeShape(item)"/>
                </div>
            </el-collapse-item>
            <el-collapse-item title="样式表" name="3">
                <div :key="UPDATE">
                    <div v-for="item in styleSheet"><b>{{item.name}}:</b>&nbsp;{{item.value}}<br/></div>
                </div>
            </el-collapse-item>
            <el-collapse-item title="更多信息" name="4">
                <div :key="INFO">
                    <div v-for="(item,key) of config.room">
                        <b>{{code_room[key]}}:</b>{{item}}<br/>
                    </div>
                </div>
                <div>
                    <div v-for="(item,key) of config.user">
                        <b>{{code_user[key]}}</b>{{code_user[key]?': '+item:''}}<br/>
                    </div>
                </div>
            </el-collapse-item>
            </el-collapse>
        </vueOpener>
        <vueEditVue @KD="kd" ref="edit"
            @edit="editHandle"
            v-model:lineWidth="EditBox.lineWidth"
            v-model:fontSize="EditBox.fontSize"
            v-model:fontColor="EditBox.fontColor"
        />
        <vueConsoleVue :show="consoleShow" @close="consoleHandle" />
    </div>
</template>


<script setup>
import { ElPageHeader,ElCollapse,ElCollapseItem} from 'element-plus';
import { ArrowLeft,Check,Refresh,Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { inject, ref,computed,onBeforeUnmount,getCurrentInstance,watch } from 'vue';
import { onMounted } from 'vue'
import { fabric } from 'fabric'
import vueConsoleVue from '@/components/vue-console.vue';
import vueOpener from '@/components/vue-opener.vue'
import vueEditVue from '@/components/vue-edit.vue';
import { useStore } from 'vuex';
import {throttle,shakeProof,loader} from "@/api/util";

const ctx = getCurrentInstance().appContext.config.globalProperties;

const socket = inject("socket");
const store = useStore();
const router = useRouter();

const code_room = ref({
    "rid":"房间ID",
    "uid":"用户ID",
    "psw":"房间密码",
    "name":"名称",
    "description":"简介",
    "state":"可加入",
    "limit":"最大人数",
    "stats":"用户数",
    "lid":"图层ID",
    "lock":"只读模式"
});

const code_user = ref({
    "uid":"用户ID",
    "lid":"图层ID",
    "state":"管理员"
});

let FIRST = true;

const config = computed(()=>{
    return store.state.params||{room:{},user:{},layout:[]};
});

let lock = false;

const LOCK = computed(()=>config.value.room.lock);

const INFO = ref("");

watch(()=>[LOCK.value],p=>{
    lock = p[0]||false;
});

const onBack = () => {
    router.push({name:"index"});
}

const Lyname = ref("Sheet");
const layoutID = ref(null);
const nameUseable = computed(()=>{
    if(Lyname.value.length===0){
        return true;
    }
    if(Lyname.value.length>10){
        return true;
    }
    return false;
});

watch(
    ()=>[layoutID.value],
    p=>{
        config.value.user.lid = p[0];
    }
);

const LChangeHandle = ()=>{
    // if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
    layoutID.value&&setLayout(layoutID.value);
}
const delLayout = ()=>{
    if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
    let lid = config.value.user.lid;
    let name = config.value.layout.find(v=>v.lid===lid).name||"";
    if(!name)return;
    window.confirm(`确定删除图层[${name}]`,"", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
    }).then(v=>{
        setLayout(config.value.user.lid,"删除");
    }).catch(()=>{});
}
const catLayout = ()=>{
    if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
    let name = Lyname.value;
    let lot = config.value.layout.find(v=>v.name===name);
    let hint = "创建";
    if(lot){
        hint = "切换"
    }
    window.confirm(`确定${hint}图层[${name}]`,"", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
    }).then(v=>{
        if(hint=="创建"){
            setLayout(null,hint,name);
        }else{
            setLayout(lot.lid,hint,name);
        }
    }).catch(()=>{});
};

const UPDATE = ref("");

const zindex = ref(0);

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
        src:require("../assets/icon/刷新.png"),
        label:"刷新"
    },{
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
    if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
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

const editHandle =(e,data)=>{
    const {room:{rid},user:{uid,lid}} = config.value;
    let actObj = canvas.getActiveObject();
    switch(e){
        case "放大":
            setCvsZoom(0.05);
            break;
        case "缩小":
            setCvsZoom(-0.05);
            break;
        case "复制":
            // ctx.$message({
            //     type:"info",
            //     message:"此功能尚未开发"
            // });
            if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
            if(actObj){
                let pixel = JSON.parse(JSON.stringify(actObj.toJSON(["gid","date"])));
                pixel.date = Date.now();
                pixel.gid = ID();
                addPixel(pixel);
                modified({
                    type:"创建",pixel
                },true);
                ctx.$message({
                    message:"拷贝成功~",
                    type:"success"
                });
            }else{
                ctx.$message({
                    message:"当前未选中图元喔~",
                    type:"warning"
                });
            }
            break;
        case "撤销":
            if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
            socket.emit("stream",{
                event:"undo",
                rid,uid,lid,lock
            });
            break;
        case "重做":
            if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
            socket.emit("stream",{
                event:"redo",
                rid,uid,lid,lock
            });
            break;
        case "删除":
            if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
            if(actObj){
                let pixel = actObj.toJSON(["gid","date"]);
                pixel.date = Date.now();
                canvas.remove(actObj);
                // modifiedSync({
                //     type:"删除",pixel
                // });
                modified({
                    type:"删除",pixel
                })
            }else{
                ctx.$message({
                    message:"当前未选中图元喔~",
                    type:"warning"
                });
            }
            break;
        case "保存":
            if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
            socket.emit("stream",{
                event:"save",
                rid,uid,lock
            });
            break;
    }
}

const activeNames = ref(['0','2']);

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

var ID = ()=>Date.now().toString(36);

function sendCanvas(){
    const {room:{rid},user:{uid,lid}} = config.value;
    socket.emit("stream",{
        event:"all",
        rid,uid,lid,lock,
        frame:JSON.stringify(canvas.toJSON())
    });
}

var modified = shakeProof((frame,syn=false)=>{
    const {room:{rid},user:{uid,lid}} = config.value;
    socket.emit("stream",{
        event:"edit",
        rid,uid,lid,syn,
        frame,lock
    });
},2);

var modifiedSync = throttle((func)=>{
    func();
},100);

var BUFFER = ref(null);

var SID;
var MODE;
var path = [];
var SBOX = {
    mx:null,
    my:null,
    Mx:null,
    My:null
};

const styleSheet = computed(()=>{
    UPDATE.value;
    return [
        {
            name:"图元边距",
            value:gloStyle.lineWidth
        },{
            name:"填充颜色",
            value:gloStyle.fontSize
        },{
            name:"边框颜色",
            value:gloStyle.fontColor
        },{
            name:"字体边距",
            value:textStyle.fontSize
        },{
            name:"字体背景",
            value:textStyle.fontSize
        },{
            name:"字体颜色",
            value:textStyle.fontColor
        }
    ];
});

const userSheet = computed(()=>{
    store.state
    return [
        {
            name:"图元边距",
            value:gloStyle.lineWidth
        },{
            name:"填充颜色",
            value:gloStyle.fontSize
        },{
            name:"边框颜色",
            value:gloStyle.fontColor
        },{
            name:"字体边距",
            value:textStyle.fontSize
        },{
            name:"字体背景",
            value:textStyle.fontSize
        },{
            name:"字体颜色",
            value:textStyle.fontColor
        }
    ];
});

const makeShape = item=>{
    if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
    const {label} = item;
    switch(label){
        case "直线":
            MODE = 0;
            break;
        case "曲线":
            MODE = 1;
            break;
        case "编辑":
            MODE = 2;
            break;
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

const handleFiles = (event)=>{
    loader(event.target.files).then(v=>{
        sendFile({
            file:v
        });
    })
}

function sendFile(frame){
    const {room:{rid},user:{uid,lid}} = config.value;
    socket.emit("file",{
        event:"refresh",
        rid,uid,lid,
        frame
    });
}

var nowZoom = 1;

const makeAct = (item)=>{
    if(lock){ctx.$message({type:"info",message:"当前为只读模式"});return;}
    let {label} = item;
    let {room:{rid},user:{uid,lid}} = config.value;
    let event = "error";
    switch(label){
        case "保存":
            event="save";
            break;
        case "导出":
            event="export";
            break;
        case "导入":
            {
                event="import";
                const ipt = document.createElement('input');
                ipt.onchange = handleFiles;
                ipt.accept = ".fsl";
                ipt.type = 'file';
                ipt.click();
            }
            return;
        case "撤销":
            event="undo";
            break;
        case "重做":
            event="redo"
            break;
    }
    socket.emit("stream",{
        event,
        rid,uid,lid,lock
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
            if(lock){
                o.evented = false;
            }else{
                const editcall =(syn=false)=>{
                    o.date = Date.now();
                    if(syn){
                        // modifiedSync({
                        //     type:"编辑",
                        //     pixel:o.toJSON(["gid","date"])
                        // });
                        modified({
                            type:"编辑",
                            pixel:o.toJSON(["gid","date"])
                        },true);
                    }else{
                        modified({
                            type:"编辑",
                            pixel:o.toJSON(["gid","date"])
                        });
                    }
                };
                if(o.type==="i-text"){
                    o.onDeselect = function(){
                        this.isEditing&&this.exitEditing();
                        this.selected=false;
                        editcall(true);
                        selectPixel("layout");
                    };
                    let func = o.onInput.bind(o);
                    o.onInput = function(e){
                        func(e);
                        editcall(false);
                    }
                }
                o.onSelect = ()=>{
                    selectPixel(o.type,o);
                }
                o.on("moving",()=>editcall(false));
                o.on("scaling",()=>editcall(false));
                o.on("rotating",()=>editcall(false));
            }
            canvas.add(o);
        });
        canvas.renderOnAddRemove = origRenderOnAddRemove;
        canvas.renderAll();
    });
}

const stream = data=>{
    try{
        const {rid,lid,uid,event,frame} = data;
        config.value.user.lid = lid;
        layoutID.value = lid;
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
                const {type,pixel} = frame;
                if(!Objs.find(v=>{
                    if(v.gid===pixel.gid){
                        canvas.remove(v);
                        if(type!="删除"){
                            addPixel(pixel);
                        }
                    }
                    return v.gid===pixel.gid;
                })){
                    if(type!="删除"){
                        addPixel(pixel);
                    }
                }
                break;
            case "refresh":
                canvas.clear();
                addPixel(...Object.values(frame));
                // showMSG("刷新成功","success");
                break;
        }
    }catch(err){console.log(err)}
}

var canvas; 
var pencil;

var edit = ref(null);

const EditBox = ref({
    lineWidth:1,
    fontSize:"#A1ABBF",
    fontColor:"#A1ABBF"
});

watch(
    ()=>[EditBox.value.lineWidth,EditBox.value.fontSize,EditBox.value.fontColor],
    p=>{
        let [lineWidth,fontSize,fontColor] = p;
        modifiedSync(()=>{
            if(selectObj!=null){
                selectObj.date = Date.now();
                // console.log("save");
                modified({
                    type:"编辑",
                    pixel:selectObj.toJSON(["gid","date"])
                });
            }
        });
        if(selectObj==null){
            Object.assign(gloStyle,{
                lineWidth,fontSize,fontColor
            });
            pencil.freeDrawingBrush.color = fontColor;
        }else{
            if(selectObj.type==="i-text"){
                Object.assign(textStyle,{
                    lineWidth,fontSize,fontColor
                });
                selectObj.set("strokeWidth",lineWidth);
                selectObj.set("textBackgroundColor",fontSize);
                selectObj.set("fill",fontColor);
            }else{
                selectObj.set("strokeWidth",lineWidth);
                selectObj.set("fill",fontSize);
                selectObj.set("stroke",fontColor);
            }
            canvas.renderAll();
            selectObj.date = Date.now();
            modified({
                type:"编辑",
                pixel:selectObj.toJSON(["gid","date"])
            });
        }
        UPDATE.value = ID();
    }
);

function setCvsZoom(dt=0.1){
    nowZoom = nowZoom + dt;
    nowZoom = Math.max(0.1, nowZoom); //最小为原来的1/10
    nowZoom = Math.min(3, nowZoom); //最大是原来的3倍
    let zoomPoint = canvas.getVpCenter();
    // if(canvas._previousPointer){
    //     zoomPoint = new fabric.Point(canvas._previousPointer.x,canvas._previousPointer.y);
    // }
    // let obj = canvas.getActiveObject();
    // if(obj){
    //     let lp = obj.__lastPointer;
    //     console.log(obj);
    //     if(lp){
    //         zoomPoint = new fabric.Point(lp.x,lp.y);
    //     }
    // }
    canvas.zoomToPoint(zoomPoint, nowZoom);
    pencil.zoomToPoint(zoomPoint, nowZoom);
}

const send = shakeProof(()=>sendCanvas(),30);

var gloStyle = {
    lineWidth:1,
    fontSize:"#A1ABBF",
    fontColor:"#A1ABBF"
}

var textStyle = {
    lineWidth:1,
    fontSize:"#ffffff",
    fontColor:"#A1ABBF"
}


var selectObj = null;

function selectPixel(type,o){
    switch(type){
        case "layout":
            selectObj = null;
            edit.value.reset(gloStyle.lineWidth,gloStyle.fontSize,gloStyle.fontColor);
            break;
        case "i-text":
            selectObj = o;
            edit.value.reset(textStyle.lineWidth||1,textStyle.fontSize||"#ffffff",textStyle.fontColor||"#A1ABBF");
            break;
        default:
            selectObj = o;
            edit.value.reset(o.strokeWidth||1,o.fill||"#ffffff",o.stroke||"#A1ABBF");
            // edit.value.reset(1,2,"#232312");
    }
}

function init() {
    canvas = new fabric.Canvas('canvas');
    pencil = new fabric.Canvas('pencil');
    let panning = false;
    nowZoom = canvas.getZoom();
    canvas.selection = false;
    pencil.selection = false;

    selectPixel("layout");

    canvas.on('mouse:down', function(options) {
        if(options.e.ctrlKey||ctrlFlag) {
          panning = true;
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
            stroke:gloStyle.fontColor,
            backgroundColor:"",
            strokeWidth:gloStyle.lineWidth||3,
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
        let {lineWidth,fontSize,fontColor} = gloStyle;
        toolShow.value=true;
        zindex.value = 0;
        let obj = pencil.getObjects()[0];
        let json = obj.toJSON();
        pencil.clear();
        let c = json.path.pop();
        let width = 50,height = 50,left = 0,top = 0,radius=50;
        if(fontSize==null)fontSize = "#A1ABBF";
        if(fontColor=="")fontColor = "#A1ABBF";
        if(lineWidth==null)lineWidth = 1.3;
        if(SBOX.Mx!=undefined&&SBOX.My!=undefined&&SBOX.mx!=undefined&&SBOX.my!=undefined){
            left = SBOX.mx;top = SBOX.my;
            width = SBOX.Mx - SBOX.mx;
            height = SBOX.My - SBOX.my;
            radius = ~~(Math.min(height,width)/2);
        }
        const style = { 
            top,left,width, height,fill:fontColor,radius,stroke:fontColor
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
                json = new fabric.IText('TEXT',{top,left,width,height,fontSize:height,fill:textStyle.fontColor,textBackgroundColor:textStyle.fontSize}).toJSON();
                break;
            default:return;
        }
        json.gid = SID;
        json.date = Date.now();
        json.strokeWidth = lineWidth;
        addPixel(json);
        // modifiedSync({
        //     type:"添加",
        //     pixel:json
        // });
        modified({
            type:"添加",
            pixel:json
        },true);
    });
    
    canvas.on("mouse:wheel", function(options) {
        nowZoom = (options.e.deltaY > 0 ? -0.1 : 0.1) + nowZoom;//canvas.getZoom();
        nowZoom = Math.max(0.1, nowZoom);
        nowZoom = Math.min(3, nowZoom);
        const zoomPoint = new fabric.Point(options.e.pageX, options.e.pageY);
        canvas.zoomToPoint(zoomPoint, nowZoom);
        pencil.zoomToPoint(zoomPoint, nowZoom);
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
            // modifiedSync({
            //     type:"编辑",
            //     pixel:option.target.toJSON(["gid","date"])
            // });
            modified({
                type:"编辑",
                pixel:option.target.toJSON(["gid","date"])
            },true)
        }
    });

    pencil.freeDrawingBrush.width = 13;
    pencil.freeDrawingBrush.color = "#A1ABBF";
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
    const {room:{rid},user:{uid,lid}} = config.value;
    socket.emit("stream",{
        event:"refresh",
        rid,uid,lid,lock,
        frame:{}
    });  
}
function setLayout(lid,type="切换",name){
    const {room:{rid},user:{uid}} = config.value;
    switch(type){
        case "删除":
            socket.emit("stream",{
                event:"refresh",lock,
                rid,uid,lid,
                frame:{
                    type:"删除"
                }
            });
            return;
        case "创建":
            socket.emit("stream",{
                event:"refresh",lock,
                rid,uid,lid:null,
                frame:{
                    type,name
                }
            });
            return;
        default:
            config.value.user.lid = lid;
            // console.log(lid);
            socket.emit("stream",{
                event:"refresh",lock,
                rid,uid,lid,
                frame:{
                    type,name
                }
            });
    }
}

onMounted(() => {
    init();
    socket.on("stream",stream);
    refresh();
    lock = config.value.room.lock;
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

.ipt-name>input{
    width: calc(100% - 60px);
    border: 1px solid gray;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.9);
    padding: 3px;
}
.ipt-name>select{
    width: calc(100% - 52px);
    border: 1px solid gray;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.9);
    padding: 3px;
}

.ipt-name>.el-button{
    width: 25px;
    height: 25px;
    margin-left: 10px;
}
.act-btn>.el-button{
    width: calc(100% - 20px);
    height: 30px;
}
.ipt-name{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 50px;
    overflow: hidden;
}
.act-btn{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 50px;
    overflow: hidden;
}

/* .input>select{
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 5px;
    padding: 3px;
    width: calc(100% - 53px);
    margin-left: 10px;
} */

</style>
