<template>
  <div ref="parent" class="gamemap">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { onMounted } from "vue";
import { ref } from "vue";
import { GameMap } from "@/assets/scripts/GameMap.js";
import { useStore } from "vuex";

export default {
  setup() {
    let parent = ref(null);
    let canvas = ref(null);

    let store = useStore();

    onMounted(() => {
      new GameMap(canvas.value.getContext("2d"), parent.value, store);
    });
    return {
      parent,
      canvas,
    };
  },
};
</script>

<style>
div.gamemap {
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
