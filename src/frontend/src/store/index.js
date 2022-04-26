import Vue from "vue";
import Vuex from "vuex";
import products from "./modules/products";
import users from "./modules/users";
import carts from "./modules/cart";

/* Vue가 Vuex를 사용할 수 있게 선언*/
Vue.use(Vuex);

export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules: {products, users, carts}
});