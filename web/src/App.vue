<template>
  <router-view />
</template>

<script>
//import $ from "jquery";
//import {ref} from 'vue'

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";

export default {
  name: "app",
  setup: () => {
    /** 
    $.ajax({
      url: "http://127.0.0.1:3000/user/account/token/",
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
      url: "http://127.0.0.1:3000/user/account/info/",
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
      url: "http://127.0.0.1:3000/user/account/register/",
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
    //   url:"http://127.0.0.1:3000/pk/getbotinfo/",
    //   type:"get",
    //   success:resp=>{
    //     console.log(resp);
    //     x.value=resp;
    //     console.log(x.value);
    //   }
    // });
    // return {x};
  },
  components: {},
};
</script>

<style></style>
