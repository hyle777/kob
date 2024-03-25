<template>
  <NavBar></NavBar>
  <router-view />
</template>

<script>
//import $ from "jquery";
//import {ref} from 'vue'

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
import NavBar from "@/components/NavBar.vue";

export default {
  name: "app",
  setup: () => {
    /** 
    $.ajax({
      url: "https://app6715.acapp.acwing.com.cn/api/user/account/token/",
      type: "post",
      data: {
        username: "yxc",
        password: "pyxc",
      },
      success(resp) {
        console.log("登录");
        console.log(resp);
      },
      error(resp) {
        console.log("登录");
        console.log(resp);
      },
    });

    $.ajax({
      url: "https://app6715.acapp.acwing.com.cn/api/user/account/info/",
      type: "get",
      headers: {
        Authorization:
          "Bearer " +
          "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlZTY3MDc5NGFmY2E0NjM5YjJiNmU5NGFkYzFjNGU3NyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcwOTcxOTk0MSwiZXhwIjoxNzEwOTI5NTQxfQ.asbdEt7vsnnejqXZ6BqtSyuIgV2h4hE3W2_dacvugLs",
      },
      success(resp) {
        console.log("获取");
        console.log(resp);
      },
      error(resp) {
        console.log("获取");
        console.log(resp);
      },
    });

    $.ajax({
      url: "https://app6715.acapp.acwing.com.cn/api/user/account/register/",
      type: "post",
      data: {
        username: "lzm",
        password: "plzm",
        confirmedPassword: "plzm",
      },
      success(resp) {
        console.log("注册");
        console.log(resp);
      },
      error(resp) {
        console.log("注册");
        console.log(resp);
      },
    });
    */
    // let x = ref("");
    // $.ajax({
    //   url:"https://app6715.acapp.acwing.com.cn/api/pk/getbotinfo/",
    //   type:"get",
    //   success:resp=>{
    //     console.log(resp);
    //     x.value=resp;
    //     console.log(x.value);
    //   }
    // });
    // return {x};
  },
  components: { NavBar },
};
</script>

<style>
body {
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>
