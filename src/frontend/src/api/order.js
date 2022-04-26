import axios from "axios";

/* 1. HTTP Request & Response와 관련된 기본 설정*/
const config = {
    baseUrl: "/api/orders/"
};

function orderSave(cartId) {
    return new Promise((resolve, reject) => {
        axios
          .post(`${config.baseUrl}`, {
            orderStatement: cartId
          })
          .then(res => {
            resolve(res);
          })
          .catch(err => {
            reject(err);
          });
    });
}
export { orderSave };
