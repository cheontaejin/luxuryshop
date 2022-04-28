import axios from "axios";

/* axios 인스턴스를 생성할 때, 인스턴스의 기본 URL 값을 정할 수 있는 속성 보통 기본 도메인으로 설정한다. */
const config = {
    baseUrl: "/api/users/"
};
/* 해당 데이터를 저장해 사용할 수 있도록 만듬 자바의 변수 선언 정도로 이해함 */
const state = {
    jwt: null,
    id: null,
    details: null,
    nickname: "로그인하세요.",
    userList: []
};
/* 데이터를 다루는 함수등을 작성(동기적 로직) */
const mutations = {
    setJwt: (state, jwt) => {
        state.jwt = jwt;
    },
    setId: (state, id) => {
        state.id = id;
    },
    setUserDetails: (state, data) => {
        state.details = data;
        state.nickname = data.nickname;
    },
    setUserList: (state, data) => {
        state.userList = data;
    },
    logout: state => {
        state.jwt = null;
        state.details = null;
        state.id = null;
        state.nickname = "로그인하세요.";
    }
};
/* api에서 데이터를 받아오는 역할, 사용자 입력에 따라 반응할 methods(비동기적 로직) */
const actions = {
    /* async = 현재 사용할 함수를 비동기로 처리하겠다. */
    async login({state, commit}, userSignupDto) {
        let isSuccess = true;
        /* axios = 뷰에서 권고하는 HTTP 통신 라이브러리 */
        /* axios.post 서버에서 데이터를 새로 생성할 때 사용하는 메서드 */
        /* await = 비동기로 순차 처리하기 위해서 수행할 API에 붙이는 선언자
        * request에 대한 response까지 모두 다 받은 다음에 다음 문장을 실행함 */
        await axios
            .post(`${config.baseUrl}login`, userSignupDto)
            /* 비동기 통신이 성공했을 경우 콜백을 인자로 받아 결과값을 처리할 수 있다. */
            /* 순차적으로 처리하는 부분은 .then()구문 내에서만 한정 된다. */
            .then(response => {
                commit("setJwt", response.data.token);
                commit("setId", response.data.id);
                alert("로그인 되었습니다.");
                console.log(state.jwt);
            })
            /* 오류 처리 */
            .catch(error => {
                alert(error.response.data);
                console.error(error);
                isSuccess = false;
            });
        return isSuccess;
    },

    async logout({commit}) {
        let isSuccess = true;

        if (state.jwt != null) {
            commit("logout");
            alert("로그아웃 되었습니다.");
        } else {
            alert("로그인 되어있지 않습니다.");
            isSuccess = false;
        }
        return isSuccess;
    },

    async signUp({commit}, userSignupDto) {
        let isSuccess = true;
        await axios
            .post(`${config.baseUrl}`, userSignupDto)
            .then(response => {
                alert("회원가입 성공!");
                console.log(response.data);
            })
            .catch(error => {
                alert(error.response.data);
                console.log(error);
                isSuccess = false;
            });
        return isSuccess;
    },
    isDuplicatedEmail({commit}, email) {
        return new Promise((resolve, reject) => {
            /* 서버에서 데이터를 가져올 때 사용하는 메서드 axios.get */
            axios
                .get(`${config.baseUrl}emails/${email}/exists`)
                .then(response => {
                    if (response.data) {
                        alert("중복되지 않은 이메일입니다.");
                    } else {
                        alert("중복된 이메일입니다.");
                    }
                    resolve(response);
                    console.log(response);
                })
                .catch(error => {
                    alert(error.response.data);
                    console.log(error);
                });
        });
    },
    async details({commit}, id) {
        let isSuccess = true;
        await axios
            .get(`${config.baseUrl}${id}/details`, {
                headers: {Authorization: `Bearer ${state.jwt}`}
            })
            .then(response => {
                commit("setUserDetails", response.data);
                console.log(response);
            })
            .catch(error => {
                alert(error.response.data);
                console.error(error);
                isSuccess = false;
            });
        return isSuccess;
    },
    async deleteUser({commit}) {
        let isSuccess = true;
        await axios
            .delete(`${config.baseUrl}${state.details.id}/delete`, {
                headers: {Authorization: `Bearer ${state.jwt}`}
            })
            .then(response => {
                commit("logout");
                alert("계정을 삭제했습니다.");
                console.log(response);
            })
            .catch(error => {
                alert(error.response.data);
                console.error(error);
                isSuccess = false;
            });
        return isSuccess;
    },
    async deleteUserWithId({commit}, id) {
        let isSuccess = true;
        /* axios.delete 특정 데이터나 값을 삭제할 때 요청하는 메서드 */
        await axios
            .delete(`${config.baseUrl}${id}/delete`, {
                headers: {Authorization: `Bearer ${state.jwt}`}
            })
            .then(response => {
                console.log(response);
            })
            .catch(error => {
                alert(error.response.data);
                console.error(error);
                isSuccess = false;
            });
        return isSuccess;
    },

    async update({commit}, userUpdateDto) {
        let isSuccess = true;
        /* axios.put 데이터를 수정할때 사용하는 메서드 */
        await axios
            .put(`${config.baseUrl}update`, userUpdateDto, {
                headers: {Authorization: `Bearer ${state.jwt}`}
            })
            .then(response => {
                alert("계정을 수정했습니다. 다시 로그인하세요");
                commit("logout");
                console.log(response);
            })
            .catch(error => {
                alert(error.response.data);
                console.error(error);
                isSuccess = false;
            });
        return isSuccess;
    },
    async findAll({commit}) {
        let isSuccess = true;
        await axios
            .get(`${config.baseUrl}list`, {
                headers: {Authorization: `Bearer ${state.jwt}`}
            })
            .then(response => {
                console.log(response);
                commit("setUserList", response.data);
            })
            .catch(error => {
                console.error(error);
                alert(error.response.data);
                isSuccess = false;
            });
        return isSuccess;
    }
};
export default {
    namespaced: true,
    state,
    mutations,
    actions
};