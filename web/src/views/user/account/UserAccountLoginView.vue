<template>
  <ContentFiled v-if="$store.state.user.is_show">
    登录
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">提交</button>
        </form>
      </div>
    </div>
  </ContentFiled>
</template>

<script>
import ContentFiled from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { ref } from "vue";
import router from "@/router/index";

export default {
  components: {
    ContentFiled,
  },
  setup() {
    const store = useStore();
    let username = ref("");
    let password = ref("");
    let error_message = ref("");

    const jwt_token = localStorage.getItem("jwt_token");
    if (jwt_token) {
      store.commit("updateToken", jwt_token);
      store.dispatch("getInfo", {
        success() {
          router.push({ name: "home" });
        },
        error() {
          store.commit("updateIsShow", true);
        },
      });
    } else {
      store.commit("updateIsShow", true);
    }
    const login = () => {
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success(resp) {
          console.log(resp);
          store.dispatch("getInfo", {
            success() {
              router.push({ name: "home" });
            },
          });
        },
        error(resp) {
          console.log(resp);
          error_message.value = "用户名或密码错误！";
        },
      });
    };

    return {
      username,
      password,
      error_message,
      login,
    };
  },
};
</script>

<style scoped>
button {
  width: 100%;
}
div.error-message {
  color: red;
}
</style>
