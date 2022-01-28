import store from '@/store';
import { NavigationGuardWithThis, RouteRecord } from 'vue-router';

export default (to: RouteRecord, from: RouteRecord, next: Function) => { // disable-line
  if (to.path !== '/login' && !store.getters.isLoggedIn) {
    return next({ name: 'Login' });
  }

  return next();
};
