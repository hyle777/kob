import PkIndexView from "@/views/pk/PkIndexView.vue";
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView.vue";
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView.vue";
import RecordIndexView from "@/views/record/RecordIndexView.vue";
import RecordContentView from "@/views/record/RecordContentView";
import RankListView from "@/views/ranklist/RankListView";
import { createRouter, createWebHistory } from "vue-router";
import store from "@/store/index";
import MyBot from "@/views/user/bot/BotIndexView.vue";

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
  },
  {
    path: "/user/bot/",
    name: "user_bot",
    component: MyBot,
    meta: {
      requestAuth: true, //是否需要先登录再访问
    },
  },
  {
    path: "/pk/",
    name: "pk",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/record/",
    name: "record",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/record/:recordId",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/ranklist/",
    name: "ranklist",
    component: RankListView,
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({ name: "user_account_login" });
  } else {
    next();
  }
});
export default router;
