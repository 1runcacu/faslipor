import { createWebHistory, createRouter } from "vue-router";

import index from '@/pages/app-index.vue';
import panel from '@/pages/app-panel.vue';

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

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to,from,next)=>{
  next();
  // if(to.name==="panel"){
  //   if(from&&from.name==="index"){
  //     next();
  //   }else{
  //     next({name:"index"});
  //   }
  // }else{
  //   next();
  // }
})

export default router;