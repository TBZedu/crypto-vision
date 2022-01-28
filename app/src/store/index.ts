import { createStore } from 'vuex';

export default createStore({
  state: {
    isLoggedIn: false
  },
  mutations: {
    LOGIN(state) {
      state.isLoggedIn = true;
    }
  },
  actions: {
    loginUser(context, credentials) {
      context.commit('LOGIN');
    }
  },
  getters: {
    isLoggedIn: (state) => state.isLoggedIn
  }
});
