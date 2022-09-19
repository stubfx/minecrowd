import { createRouter, createWebHistory } from 'vue-router'
import CommandView from "@/views/commandView.vue";
import HomeView from "@/views/HomeView.vue";
import SpawnCommandView from "@/views/specificCommands/spawnCommandView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/:selectedCommand",
      name: 'genericCommand',
      component: CommandView,
      props: true
    },
    {
      path: "/spawn",
      name: 'spawn',
      component: SpawnCommandView
    },
    {
      path: "/documentation",
      name: 'homepage',
      component: HomeView,
      props: true
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
