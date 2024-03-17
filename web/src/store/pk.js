export default {
  state: {
    status: "matching", // matching表示正在匹配
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    gamemap: null,
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
    gameObject: null,
    loser: "none", //all表示平局 A表示左下角的输， B表示右上角的输
  },
  getters: {},
  mutations: {
    updateLoser(state, loser) {
      state.loser = loser;
    },
    updateGameObject(state, gameObject) {
      state.gameObject = gameObject;
    },
    updateGame(state, data) {
      state.gamemap = data.gamemap;
      state.a_id = data.a_id;
      state.a_sx = data.a_sx;
      state.a_sy = data.a_sy;
      state.b_id = data.b_id;
      state.b_sx = data.b_sx;
      state.b_sy = data.b_sy;
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
