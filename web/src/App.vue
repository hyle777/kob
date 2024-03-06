<template>
  <div>后端的数据：{{ x }}</div>
  <NavBar/>
  <router-view/>
</template>

<script>
import $ from 'jquery'
import {ref} from 'vue'

import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  name:"app",
  setup:() => {
    let x = ref("");
    $.ajax({
      url:"http://127.0.0.1:3000/pk/getbotinfo/",
      type:"get",
      success:resp=>{
        console.log(resp);
        x.value=resp;
        console.log(x.value);
      }
    });
    return {x};
  },
  components:{
    NavBar
  }
}
</script>

<style>

</style>
