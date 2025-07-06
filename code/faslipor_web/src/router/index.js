import { createWebHistory, createRouter } from "vue-router";

import index from '@/pages/app-index.vue';
import panel from '@/pages/app-panel.vue';
import mobile from '@/pages/app-mobile.vue';

import { getParams,getAllow, setAllow } from "@/state";

const routes = [
  {
    name:"index",
    path: "/",
    component:index
  },
  {
    name:"panel",
    path: "/panel",
    component:panel
  },
  {
    path:"/:pathMatch(.*)*",
    redirect: '/'
  }
];

const routesMobile = [
  {
    name:"index",
    path: "/",
    component:mobile
  },
  {
    name:"panel",
    path: "/panel",
    component:panel
  },
  {
    path:"/:pathMatch(.*)*",
    redirect: '/'
  }
];

let rt = [];

if (/(iPad|iPod)/i.test(navigator.userAgent)) {
  rt = routes;
} else if (/(iPhone|Android|iOS)/i.test(navigator.userAgent)) {
  rt = routesMobile;
} else {
  rt = routes;
}

export function getTerminalType(){
  if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
      return  'phone';
  } else if (/(Android)/i.test(navigator.userAgent)) {
      return  'mobile';
  } else {
      return 'pc';
  }
}

const router = createRouter({
  history: createWebHistory(),
  routes:rt,
});

async function tryExit(){
  const {room,user} = getParams();
  if(room&&user){
    const {rid} = room;
    const {uid} = user;
    if(rid&&uid){
      try {
        await window.confirm('确定退出房间?',"", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        })
        window.$socket.emit("query",{
          event:"exit",
          params:{
              uid,
              rid
          }
        });
        return Promise.resolve();
      }catch(e){
        console.log(e);
        return Promise.reject();
      }
    }
  }
}

router.beforeEach(async (to,from,next)=>{
  if(from.name==="panel"){
    try{
      await tryExit();
    }catch(e){
      return;
    }
  }
  next();
})

export default router;