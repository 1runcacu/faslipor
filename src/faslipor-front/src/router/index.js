import { createWebHistory, createRouter } from "vue-router";

import login from '@/pages/app-login.vue';

const routes = [
  {
      path: "/",
      component:login
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