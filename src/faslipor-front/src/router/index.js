import { createWebHistory, createRouter } from "vue-router";

import create from '@/pages/app-create.vue';
import index from '@/pages/app-index.vue';
import canvas from '@/pages/app-canvas.vue';

const routes = [
  {
    path: "/",
    component:index
  },
  {
    path: "/create",
    component:create
  },
  {
    path: "/panel",
    component:canvas
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