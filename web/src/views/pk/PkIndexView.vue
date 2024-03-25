<template>
  <ResultBoard v-if="$store.state.pk.loser !== 'none'" />
  <MatchGround v-if="$store.state.pk.status === 'matching'" />
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
</template>

<script>
import MatchGround from "@/components/MatchGround.vue";
import PlayGround from "@/components/PlayGround.vue";
import ResultBoard from "@/components/ResultBoard.vue";
import { useStore } from "vuex";
import { onMounted, onUnmounted } from "vue";

export default {
  components: { MatchGround, PlayGround, ResultBoard },
  setup() {
    const store = useStore();
    let socketUrl = `wss://app6715.acapp.acwing.com.cn/websocket/${store.state.user.token}/`;
    let socket = null;
    onMounted(() => {
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
          console.log("matched");
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo,
          });
          store.commit("updateGame", data.game);

          setTimeout(() => {
            store.commit("updateStatus", "playing");
          }, 200);
        } else if (data.event === "move") {
          console.log("66666====");
          console.log(data);
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          snake0.set_direction(data.a_direction);
          snake1.set_direction(data.b_direction);

          console.log(snake0);
          console.log(snake1);
        } else if (data.event === "result") {
          console.log(data);
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          if (data.loser === "all" || data.loser === "A") {
            snake0.status = "die";
          }
          if (data.loser === "all" || data.loser === "B") {
            snake1.status = "die";
          }
          store.commit("updateLoser", data.loser);
          console.log(store.pk);
          console.log(store.state.pk.loser === "A");
          console.log(typeof store.state.pk.a_id);
          console.log(typeof store.state.user.id);
          console.log(store.state.pk.a_id === store.state.user.id);
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
