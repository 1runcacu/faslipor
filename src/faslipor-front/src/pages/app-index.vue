<template>
  <div id="box">
    <div id="card">
      <el-input
        v-model="search"
        class="w-50 m-2"
        size="large"
        placeholder="房间搜索"
        :suffix-icon="Search"
        style="width: 300px;"
      />
      <div id="rooms">
        <el-card :body-style="{ height: '100%' }" v-for="(item,index) in rooms" :class="{disabled:!item.state||(item.stats>=item.limit)}" :key="index" :disabled="!item.state||(item.stats>=item.limit)">
          <template #header>
            <div class="card-header">
              <span><b>{{item.name}}</b><div style="font-size:0.2rem;">[{{item.stats}}/{{item.limit}}]</div></span>
              <el-button :disabled="!lock" type="success" class="button" text @click="handleSendMessage(item)" v-if="item.state&&(item.stats<item.limit)">进入</el-button>
            </div>
          </template>
          <div id="Room">
            &nbsp;&nbsp;&nbsp;&nbsp;{{item.description}}
          </div>
        </el-card>
        <el-card :body-style="{ height: '100%' }">
          <div id="addRoom" @click="addRoomHandle">
            <el-icon><Plus/></el-icon>
          </div>
        </el-card>
      </div>
      <el-pagination background layout="prev, pager, next" :total="total" v-model:current-page="current"/>
    </div>
    <el-dialog v-model="winShow" title="创建房间" width="30%" draggable v-drag>
      <vueInputVue class="v-input" value="FASLIPOR" @input="inRoomName" hint="房间名称"/>
      <vueInputVue class="v-input" @input="inDescription" hint="房间简介"/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="winShow = false">取消</el-button>
          <el-button type="primary" @click="createRoomHandle">
            创建
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
  
</template>

<script setup>
import { ElPagination,ElMessageBox,ElDialog } from 'element-plus';
import {Search,Plus} from '@element-plus/icons-vue'
import { onMounted,ref,computed,inject,getCurrentInstance } from 'vue';
import vueInputVue from '@/components/vue-input.vue';
import winUI from '@/components/win-ui.vue';
import { useRouter } from 'vue-router'
import { useStore } from 'vuex';
// import { getTerminalType } from '@/router';

const ctx = getCurrentInstance().appContext.config.globalProperties;

const search = ref("");
const total = ref(1);
const current = ref(1);

let roomName = "FASLIPOR";
let description = "";

const dragAble = ref(true);

const socket = inject("socket");
const store = useStore();

const winShow = ref(false);
const closeWin = ()=>{
  winShow.value = false;
};

const router = useRouter();

let lock = ref(false);

const inRoomName = value=>roomName=typeof(value)==='string'?value:roomName;
const inDescription = value=>description=typeof(value)==='string'?value:description;

const handleSendMessage = (evt) => {
  ElMessageBox.confirm('确定加入此房间?',"", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
    .then(v=>{
      console.log(evt)
      // router.push({name:"panel"});
      lockHandle();
      socket.emit("query",{
        event:"select",
        params:{
          rid:evt.rid
        }
      });
    }).catch(()=>{});
  // socket.emit("message", "客户端发送的消息");
};

const addRoomHandle = ()=>{
  winShow.value = true;
}

var lockHandle = ()=>{
  lock.value = false;
  setTimeout(() => {
    lock.value = true;
  }, 3000);
}

const createRoomHandle = ()=>{
  if(roomName===""){
    ctx.$message({
      message:"房间名称不为空~",
      type:"error"
    });return;
  }
  if(roomName.split('').length>20){
    ctx.$message({
      message:"房间名称太长啦~",
      type:"warning"
    });return;
  }
  if(roomName!=""){
    ElMessageBox.confirm(`确定创建【${roomName}】?`,"", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
    .then(v=>{
      lockHandle();
      socket.emit("query",{
        event:"add",
        params:{
          roomName,
          description:description||"主人很懒，什么都没有写喔~"
        }
      });
    }).catch(()=>{});
  }
}

const ID = ()=>Date.now().toString(36)+Math.random().toString(36).substr(3,7);

function check(str,max=5){
    return str.length>max?`${str.substr(0,max)}...`:str;
}

let rooms = computed(()=>{
  let key = search.value;
  let reg = RegExp(key,'i');
  let result = store.state.rooms.filter(v=>{
    return !key||reg.test(v.name);
  })
  const len = window.innerWidth<=960?3:7;
  let i = (current.value - 1)*len;
  total.value = Math.floor((result.length/len)*10);
  return result.slice(i,i+len).map(v=>{
        let obj = Object.assign({},v);
        obj.name = check(obj.name);
        obj.description = check(obj.description,100);
        return obj;
    });
});

onMounted(() => {
  socket.emit("query",{
    event:"list"
  });
  lock.value = true;
});


</script>

<style>

.el-dialog{
  --el-dialog-bg-color:rgba(255,255,255,0) !important;
  background:rgba(255,255,255,0) !important;
  --el-dialog-width: 300px !important;
}

.el-dialog__close{
  color: rgb(35, 3, 3) !important;
}
.el-dialog__close:hover,.el-dialog__close:active{
  transform: scale(1.1,1.1);
  color: rgb(172, 26, 26) !important;
}

.el-dialog__header {
  background-color: rgba(130, 137, 142, 0.954) !important;
  margin-right: 0 !important;
}

/* 弹出层设置背景色 底部*/

.el-dialog__footer {
  backdrop-filter: blur(5px);
  background-color: rgba(233, 237, 240, 0.8) !important;
}
 /* 弹出层设置背景色 身体部份*/
.el-dialog__body{
  backdrop-filter: blur(5px);
  background-color: rgba(233, 237, 240, 0.8);
  /* background:linear-gradient(to bottom,#1468A4,#5ABEF2 );
  background-size: 100% ,100%; */
}

</style>

<style scoped>
#box{
  display: flex;
  flex-direction: column;
  justify-content: stretch;
  align-items: center;
  height: 100%;
}
#card{
  height: 100%;
  /* width: calc(100% - 600px); */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  margin: 100px;
  padding: 20px;
  overflow: hidden;
}

.el-card{
  page-break-inside:avoid;
  -webkit-column-break-inside: avoid;
}

#rooms{
  flex:1;
  margin: 40px;
  padding: 20px 20px 0 20px;
  width: 80%;
  border-radius: 1rem;
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: rgb(204, 204, 204) 0px 0px 8px;
  /* page-break-inside:avoid; */
  column-gap: 20px;
}

.v-input{
  margin-bottom: 20px;
}

/*手机 */
@media screen and (max-width:600px){
  #card{
    width: calc(100% - 20px);
  }
  #rooms{
    column-count: 2;
    -webkit-olumn-count: 2;
    -o-column-count: 2;
    -moz-column-count: 2;
  }
}
/*平板*/
@media screen and (min-width:600px) and (max-width:960px){
  #card{
    width: calc(100% - 80px);
  }
  #rooms{
    column-count: 2;
  }
}

/*PC 2*/
@media screen and (min-width:960px) and (max-width:1200px){
  #card{
    width: calc(100% - 80px);
  }
  #rooms{
    column-count: 4;
  }
}

@media screen and (min-width:1200px) and (max-width:1500px){
  #card{
    width: calc(100% - 200px);
  }
  #rooms{
    column-count: 4;
  }
}
/*PC*/
@media screen and (min-width:1500px){
  #card{
    width: calc(100% - 600px);
  }
  #rooms{
    column-count: 4;
    column-gap: 20px;
  }
}



#rooms>div{
  height: calc(100% / 2.2);
  margin-bottom: 20px;
}

.el-input:hover,.el-input:active{
  box-shadow: rgb(204, 204, 204) 0px 0px 10px;
  transform: scale(1.01,1.01);
}

.el-card:hover,.el-card:active{
  box-shadow: gray 0px 0px 10px;
  transform: scale(1.05,1.05);
  background-color: #132d59;
  transition-duration: 500ms;
  color: white;
}
.disabled:hover,.disabled:active{
  background-color: #910314 !important;
}

.card-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
}

#Room{
  height: calc(100% - 40px);
  overflow: hidden;
  word-wrap: break-word;
  font-size: 12px;
}

#addRoom{
  height: calc(100% - 40px);
  display: flex;
  justify-content: center;
  align-items: center;
}
#addRoom>.el-icon{
  font-size: 3rem;
}
#addRoom:hover>.el-icon{
  color: #409EFF;
}
/* a{
  text-decoration: none;
  color: gray;
} */

.label{
  font-size: small;
}
/* b{
  white-space:nowrap;
} */

.form{
    width: 100%;
    /* height: 100%; */
    min-height: 35vh;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
}
.form>div{
    overflow:hidden;
    margin-bottom: 35px;
}

</style>