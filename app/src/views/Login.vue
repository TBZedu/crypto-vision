<template>
  <div>
    <h2>Login</h2>
    <div>
      <form action="" @submit.prevent="login">
        <div>
          <input type="text" v-model="email" />
          <input type="text" v-model="password" />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data: () => ({
    email: '',
    password: ''
  }),
  methods: {
    login() {
      this.$store.fetch('loginUser');
      this.$router.go('/');
    }
  }
};
</script>
