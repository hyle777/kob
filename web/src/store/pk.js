export default {
  state: {
    status: "matching", // matching表示正在匹配
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    gamemap: null,
  },
  getters: {},
  mutations: {
    updateGameMap(state, data) {
      state.gamemap = data.gamemap;
    },
    updateStatus(state, status) {
      state.status = status;
    },
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateOpponent(state, data) {
      state.opponent_username = data.username;
      state.opponent_photo = data.photo;
    },
  },
  actions: {},
  modules: {},
};
