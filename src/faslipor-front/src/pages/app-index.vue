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
        <el-card v-for="item in rooms">
          <template #header>
            <div class="card-header">
              <span>{{item.name}}</span>
              <el-button class="button" text @click="handleSendMessage">进入</el-button>
            </div>
          </template>
          描述:{{item.description}}
        </el-card>
        <el-card :body-style="{ height: '100%' }">
          <router-link to="/create" >
            <div id="addRoom" >
              <el-icon><Plus/></el-icon>
            </div>
          </router-link>
        </el-card>
      </div>
      <el-pagination background layout="prev, pager, next" :total="total" />
    </div>
  </div>
</template>

<script setup>
import { ElPagination } from 'element-plus';
import {Search,Plus} from '@element-plus/icons-vue'
import { ref,computed,inject,getCurrentInstance } from 'vue';

const ctx = getCurrentInstance().appContext.config.globalProperties;

let search = ref("");
let total = ref(1000);


const socket = inject("socket");

socket.on("connection", (res) => {
  console.log("#connection: ", res);
});

socket.on("connected", (res) => {
  console.log("#connected: ", res);
});

socket.on("message", (res) => {
  console.log("#message: ", res);
  ctx.$message({
    message: res.msg,
    type: 'success',
  });
});

const handleSendMessage = () => {
  socket.emit("message", "{ text:  '客户端发送的消息'}");
};






const ID = ()=>Date.now().toString(36)+Math.random().toString(36).substr(3,7);

const list = [];
for(let i=0;i<20;i++){
  list.push({
    name:"房间"+i,
    description:ID()
  });
}

let rooms = computed(()=>{
  let key = search.value;
  let reg = RegExp(key);
  return list.filter(v=>{
    return !key||reg.test(v.name);
  }).slice(0,7);
});

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
}

.card-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
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
a{
  text-decoration: none;
  color: gray;
}
</style>