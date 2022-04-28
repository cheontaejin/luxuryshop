import axios from 'axios';

/* 1. HTTP Request & Response와 관련된 기본 설정*/
/* axios 인스턴스를 생성할 때, 인스턴스의 기본 URL 값을 정할 수 있는 속성 보통 기본 도메인으로 설정한다. */
const config = {
    baseUrl: '/api/users/'
};

function isDuplicatedEmail(email) {
    /* axios 모듈에서 request, response는 비동기로 처리되기 때문에 처리 순서를 지정해 줘야한다
    * 순차처리를 위해 Promise를 사용한다. */
    return new Promise((resolve, reject) => {
        axios.get(`${config.baseUrl}emails/${email}/exists`)
            .then(response => {
                resolve(response);
                console.log(response);
            })
            .catch(error => {
                reject(error);
                console.log(error);
            })
    })
}

function signUp(userSignupDto) {
    return new Promise((resolve, reject) => {
        axios.post(`${config.baseUrl}`, userSignupDto)
            .then(response => {
                resolve(response)
                console.log(response)
            })
            .catch(error => {
                reject(error)
                console.error(error)
            })
    })
}


function details() {
    return new Promise((resolve, reject) => {
        axios.get(`${config.baseUrl}/details`)
            .then(response => {
                resolve(response)
                console.log(response)
            })
            .catch(error => {
                reject(error)
                console.error(error)
            })
    })
}


function deleteUser() {
    return new Promise((resolve, reject) => {
        axios.post(`${config.baseUrl}/delete`)
            .then(response => {
                resolve(response)
                console.log(response)
            })
            .catch(error => {
                reject(error)
                console.error(error)
            })
    })
}

function update(userUpdateDto) {
    return new Promise((resolve, reject) => {
        axios.post(`${config.baseUrl}update`, userUpdateDto)
            .then(response => {
                resolve(response)
                console.log(response)
            })
            .catch(error => {
                reject(error)
                console.error(error)
            })
    })
}


function findAll() {
    return new Promise((resolve, reject) => {
        axios.get(`${config.baseUrl}login`)
            .then(response => {
                resolve(response)
                console.log(response)
            })
            .catch(error => {
                reject(error)
                console.error(error)
            })
    })
}

export {
    isDuplicatedEmail,
    signUp,
    details,
    deleteUser,
    update,
    findAll
}