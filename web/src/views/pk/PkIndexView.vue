<template>
  <MatchGround v-if="$store.state.pk.status === 'matching'" />
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
</template>

<script>
import MatchGround from "@/components/MatchGround.vue";
import PlayGround from "@/components/PlayGround.vue";
import { useStore } from "vuex";
import { onMounted, onUnmounted } from "vue";

export default {
  components: { MatchGround, PlayGround },
  setup() {
    const store = useStore();
    let socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
    let socket = null;
    onMounted(() => {
      console.log(store);
      store.commit("updateOpponent", {
        username: "我的对手",
        photo:
          "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      });
      socket = new WebSocket(socketUrl);

      /**
       * 建立连接
       */
      socket.onopen = () => {
        console.log("connected");
        store.commit("updateSocket", socket);
      };

      /**
       * 断开连接
       */
      socket.onclose = () => {
        console.log("disconnected");
      };

      /**
       * 接受消息
       */
      socket.onmessage = (msg) => {
        const data = JSON.parse(msg.data);
        if (data.event === "start-matching") {
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo,
          });
          store.commit("updateGameMap", {
            gamemap: data.gamemap,
          });
          setTimeout(() => {
            store.commit("updateStatus", "playing");
          }, 2000);
        }
      };
    });
    onUnmounted(() => {
      socket.close();
      store.commit("updateStatus", "matching");
    });
  },
};
</script>
<style scoped></style>
