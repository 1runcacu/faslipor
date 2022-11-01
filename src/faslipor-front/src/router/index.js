import { createWebHistory, createRouter } from "vue-router";

import login from '@/pages/app-login.vue';
import index from '@/pages/app-index.vue';

const routes = [
  {
    path: "/",
    component:login
  },
  {
    path: "/panel",
    component:index
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to,from,next)=>{
  next();
})

export default router;