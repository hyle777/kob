<template>
  <div
    class="tip"
    v-if="parseInt($store.state.user.id) === $store.state.pk.a_id"
  >
    你是A蛇
  </div>
  <div v-else class="tip">你是B蛇</div>
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
      store.commit(
        "updateGameObject",
        new GameMap(canvas.value.getContext("2d"), parent.value, store)
      );
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
div.tip {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
}
</style>
