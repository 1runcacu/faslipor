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
              <el-button type="success" class="button" text @click="handleSendMessage(item.description)" v-if="item.state&&(item.stats<item.limit)">进入</el-button>
            </div>
          </template>
          <div id="Room">
            &nbsp;&nbsp;&nbsp;&nbsp;{{item.description+item.description+item.description+item.description}}
          </div>
        </el-card>
        <el-card :body-style="{ height: '100%' }">
          <!-- <router-link to="/create" >
          </router-link> -->
          <div id="addRoom" @click="addRoomHandle">
            <el-icon><Plus/></el-icon>
          </div>
        </el-card>
      </div>
      <el-pagination background layout="prev, pager, next" :total="total" v-model:current-page="current"/>
    </div>
    <winUI :resizeAble="false" @close="closeWin" :closeShow="winShow" width="30vh" height="30vh">
      <template #head>
          <div><b>房间创建</b></div>
        </template>
      <el-form class="form" v-drag="dragAble">
        <vueInputVue v-model="roomName" hint="房间名称"/>
        <vueInputVue v-model="description" hint="房间简介"/>
        <el-button>快捷创建</el-button>
      </el-form>
    </winUI>
  </div>
</template>

<script setup>
import { ElPagination,ElMessageBox } from 'element-plus';
import {Search,Plus} from '@element-plus/icons-vue'
import { onMounted,ref,computed,inject,getCurrentInstance } from 'vue';
import vueInputVue from '@/components/vue-input.vue';
import winUI from '@/components/win-ui.vue';
import { useRouter } from 'vue-router'

const ctx = getCurrentInstance().appContext.config.globalProperties;

const search = ref("");
const total = ref(1);
const current = ref(1);

const roomName = ref("");
const description = ref("");

const dragAble = ref(true);

const socket = inject("socket");

const winShow = ref(false);
const closeWin = ()=>{
  winShow.value = false;
};

const router = useRouter();

const handleSendMessage = (evt) => {
  ElMessageBox.confirm('确定加入此房间?',"", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
    .then(v=>{
      console.log(evt)
      router.push({name:"panel"});
    }).catch(()=>{});
  // socket.emit("message", "客户端发送的消息");
};

const addRoomHandle = ()=>{
  winShow.value = true;
}

const ID = ()=>Date.now().toString(36)+Math.random().toString(36).substr(3,7);

const list = [];
for(let i=0;i<30;i++){
  list.push({
    rid:ID(),
    name:"房间"+i,
    state:Math.random()>0.2,
    description:ID(),
    stats:3,
    limit:10
  });
}

let rooms = computed(()=>{
  let key = search.value;
  let reg = RegExp(key);
  let result = list.filter(v=>{
    return !key||reg.test(v.name);
  })
  let i = (current.value - 1)*7;
  total.value = Math.floor((result.length/7)*10);
  return result.slice(i,i+7);
});

onMounted(() => {
  socket.emit("query",{
    event:"list"
  });
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
  align-items: center;
  height: 100%;
}
#card{
  height: 100%;
  width: calc(100% - 600px);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  margin: 100px;
  padding: 20px;
  overflow: hidden;
}
#rooms{
  flex:1;
  margin: 40px;
  padding: 20px 20px 0 20px;
  width: 80%;
  column-count: 4;
  column-gap: 20px;
  border-radius: 1rem;
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: rgb(204, 204, 204) 0px 0px 8px;
}
#rooms>div{
  height: calc(100% / 2.2);
  margin-bottom: 20px;
}

.el-input:hover{
  box-shadow: rgb(204, 204, 204) 0px 0px 10px;
  transform: scale(1.01,1.01);
}

.el-card:hover{
  box-shadow: gray 0px 0px 10px;
  transform: scale(1.05,1.05);
  background-color: #132d59;
  transition-duration: 500ms;
  color: white;
}
.disabled:hover{
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
    height: 100%;
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