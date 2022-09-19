import { createRouter, createWebHistory } from 'vue-router'
import CommandView from "@/views/commandView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/:selectedCommand",
      name: 'command',
      component: CommandView,
      props: true
    },
    {
      path: "/spawn",
      name: 'spawn',
      component: () => import('../views/specificCommands/spawnCommandView.vue')
    },
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
})

export default router
