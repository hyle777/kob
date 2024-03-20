<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
      <a class="navbar-brand" href="#">King of Bots</a>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :to="{ name: 'pk' }"
              class="nav-link active"
              aria-current="page"
              >对战pk</router-link
            >
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'record' }" class="nav-link"
              >对局列表</router-link
            >
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'ranklist' }" class="nav-link"
              >排行榜</router-link
            >
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              data-bs-toggle="dropdown"
              href="#"
              role="button"
              aria-expanded="false"
              >{{ $store.state.user.username }}</a
            >
            <ul class="dropdown-menu">
              <li>
                <router-link
                  :to="{ name: 'user_bot' }"
                  class="dropdown-item"
                  href="#"
                  >我的bot</router-link
                >
              </li>
              <li>
                <a class="dropdown-item" href="#" @click="logout">退出</a>
              </li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else-if="$store.state.user.is_show">
          <li class="nav-item">
            <router-link
              class="nav-link"
              :to="{ name: 'user_account_login' }"
              role="button"
              >登录</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              class="nav-link"
              :to="{ name: 'user_account_register' }"
              role="button"
              >注册</router-link
            >
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useStore } from "vuex";

export default {
  setup() {
    const store = new useStore();

    const logout = () => {
      store.dispatch("logout");
    };

    return {
      logout,
    };
  },
};
</script>

<style scoped></style>
