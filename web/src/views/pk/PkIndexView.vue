<template>
  <NavBar />
  <img v-bind:src="$store.state.user.photo" />
</template>

<script>
import NavBar from "@/components/NavBar.vue";

export default {
  components: {
    NavBar,
  },
};
</script>
<style scoped></style>
