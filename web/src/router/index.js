import PkIndexView from "@/views/pk/PkIndexView.vue";
import UserAccountLoginView from "@/views/account/UserAccountLoginView.vue";
import UserAccountRegisterView from "@/views/account/UserAccountRegisterView.vue";
import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
  },
  {
    path: "/pk/",
    name: "pk",
    component: PkIndexView,
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
