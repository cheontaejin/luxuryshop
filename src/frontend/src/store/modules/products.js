import axios from "axios";

const state = {
    productList: null,
    productDetail: null
};
const getters = {};
const actions = {
    getProductsList({commit}) {
        return axios
            /* 서버에서 데이터를 가져올 때 사용하는 메서드 axios.get */
            .get("/api/products")
            .then(data => {
                commit("setProductList", data);
            })
            .catch(err => {
                console.log(err);
            });
    },
    getProduct({commit}, id) {
        console.log("detail...." +id)
        return axios
            .get("/api/products/", {id:id})
            .then(data => {
                console.log(data)
                commit("setProduct", data);
            })
            .catch(err => {
                console.log(err);
            });
    }
};

const mutations = {
    setProductList(state, data) {
        state.productList = data;
    },
    setProduct(state,data){
        console.log(data)
        state.productDetail=data;
    }
};
export default {
    namespaced: true,
    getters,
    state,
    actions,
    mutations
};
