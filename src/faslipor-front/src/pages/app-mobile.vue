<template>
    <div id="box" @click="searchShow=false;winShow=false">
      <div id="search" >
        <el-icon @click.stop="searchShow=true;$refs.sc.focus()" :class="{selected:searchShow}"><Search/></el-icon>
        <transition name="drag-win">
            <input v-model="search" v-show="searchShow" @blur="searchShow=false" @click.stop ref="sc" placeholder="搜索房间"/>
        </transition>
      </div>
    <transition name="drag-win">
        <div id="create" v-show="winShow" @click.stop>
            <h3>创建房间</h3>
            <input v-model="roomName" placeholder="房间名称"/>
            <input v-model="description" placeholder="房间简介"/>
            <div>
                <el-button link type="danger" @click="winShow = false">取消创建</el-button>
                <el-button link type="primary" @click="createRoomHandle">创建房间</el-button>
            </div>
        </div>
    </transition>
    <el-scrollbar class="rooms">
        <div id="cards">
            <el-card :body-style="{ height: '200px','page-break-inside':'avoid' }">
                <div id="addRoom" @click.stop="addRoomHandle">
                <el-icon><Plus/></el-icon>
                </div>
            </el-card>
            <el-card :body-style="{ 'min-height': '150px','page-break-inside':'avoid' }" v-for="(item,index) in rooms" :class="{disabled:!item.state||(item.stats>=item.limit)}" :key="index" :disabled="!item.state||(item.stats>=item.limit)">
                <template #header>
                <div class="card-header">
                    <span><b>{{item.name}}</b><div style="font-size:0.7rem;">[{{item.stats}}/{{item.limit}}]</div></span>
                    <el-button :disabled="!lock" type="success" class="button" text @click="handleSendMessage(item)" v-if="item.state&&(item.stats<item.limit)">进入</el-button>
                </div>
                </template>
                <div id="Room">
                    &nbsp;&nbsp;&nbsp;&nbsp;{{item.description}}
                </div>
            </el-card>
        </div>
    </el-scrollbar>
    </div>
    
  </template>
  
  <script setup>
  import { ElMessageBox } from 'element-plus';
  import {Search,Plus} from '@element-plus/icons-vue'
  import { onMounted,ref,computed,inject,getCurrentInstance } from 'vue';
  import { useRouter } from 'vue-router'
  import { useStore } from 'vuex';
  // import { getTerminalType } from '@/router';
  
  const searchShow = ref(false);

const test = ()=>{
    console.log(2333)
    searchShow.value = true;
}

  const ctx = getCurrentInstance().appContext.config.globalProperties;
  
  const search = ref("");
  
  let roomName = ref("FASLIPOR");
  let description =ref("");
  
  const socket = inject("socket");
  const store = useStore();
  
  const winShow = ref(false);
  
  let lock = ref(false);
  
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
    if(roomName.value===""){
      ctx.$message({
        message:"房间名称不为空~",
        type:"error"
      });return;
    }
    if(roomName.value.split('').length>20){
      ctx.$message({
        message:"房间名称太长啦~",
        type:"warning"
      });return;
    }
    if(roomName.value!=""){
      ElMessageBox.confirm(`确定创建【${roomName.value}】?`,"", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        })
      .then(v=>{
        lockHandle();
        socket.emit("query",{
          event:"add",
          params:{
            roomName:roomName.value,
            description:description.value||"主人很懒，什么都没有写喔~"
          }
        });
      }).catch(()=>{});
    }
  }
  
  const H = computed(()=>store.state.window.innerHeight - 80 - (winShow.value?233:0) + "px");
  
  const ID = ()=>Date.now().toString(36)+Math.random().toString(36).substr(3,7);
  
function check(str,max=5){
    return str.length>max?`${str.substr(0,max)}...`:str;
}

  let rooms = computed(()=>{
    let key = search.value;
    let reg = RegExp(key,'i');
    return store.state.rooms.filter(v=>{
      return !key||reg.test(v.name);
    }).map(v=>{
        let obj = Object.assign({},v);
        obj.name = check(obj.name);
        obj.description = check(obj.description,400);
        return obj;
    });
  });
  


  onMounted(() => {
    socket.emit("query",{
      event:"list"
    });
    lock.value = true;
  });

  
  // const result = {
  //   event:"list",
  //   state:200,
  //   data:[
  //     {
  //       rid:"xxx",
  //       name:"xxx",
  //       state:"xxx",
  //       description:"xxx",
  //       stats:233,
  //       limit:10
  //     },{
  //       rid:"xxx",
  //       name:"xxx",
  //       state:"xxx",
  //       description:"xxx",
  //       stats:233,
  //       limit:10
  //     }
  //   ]
  // }
  
  </script>
  
  <style scoped>
  #box{
    display: flex;
    flex-direction: column;
    justify-content: stretch;
    align-items: flex-start;
    position: absolute;
    width: 100%;
  }

  #search{
    color: gray;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 15px;
    font-size: 1.3rem;
  }

  .selected{
    color: white;
  }

  #search>input{
    margin-left: 10px;
    min-width: 10rem;
    border: 0px;
    border-radius: 1rem;
    backdrop-filter: blur(5px);
    background-color: rgba(255, 255, 255, 0.1);
    padding: 8px;
    color: white;
  }
  .rooms{
    width: 100%;
    height: v-bind(H);
  }
  .el-scrollbar{
    width: 100%;
  }
  #cards{
    width: calc(100% - 20px);
    flex-direction: column;
    column-gap: 10px;
    margin: 10px;
  }
  #cards>*{
    page-break-inside:avoid;
    -webkit-column-break-inside: avoid;
    -o-column-break-inside: avoid;
    -moz-column-break-inside: avoid;
  }

@media screen and (max-width:600px){
  #cards{
    column-count: 2;
  }
}

@media screen and (min-width:600px) and (max-width:960px){
  #cards{
    column-count: 3;
  }
}

@media screen and (min-width:960px){
  #cards{
    column-count: 4;
  }
}

  .el-card{
    page-break-inside:avoid;
    -webkit-column-break-inside: avoid;
    min-height: 200px;
    margin-bottom: 10px;
    height: auto;
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
    column-gap: 20px;
  }
  #create{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
  }
  #create>h3{
    color: rgb(163, 151, 151);
  }
  #create>input{
    width: calc(100% - 40px);
    border: 0px;
    border-radius: .3rem;
    backdrop-filter: blur(5px);
    background-color: rgba(255, 255, 255, 0.1);
    padding: 8px;
    color: white;
    margin-bottom: 15px;
  }
  #create>div{
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 15px;
    margin-bottom: 40px;
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
    /* transform: scale(1.01,1.01); */
    padding: 10px;
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
    height: 100%;
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
  </style>