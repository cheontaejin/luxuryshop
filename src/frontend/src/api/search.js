import axios from "axios";

/* 1. HTTP Request & Response와 관련된 기본 설정*/
const config = {
  baseUrl: "/api/products/"
};

function getSearchTitle(name, category) {
  return new Promise((resolve, reject) => {
    axios
      .get(`${config.baseUrl}search`, {
        params: { name: name, category: category }
      })
      .then(res => {
        resolve(res);
      })
      .catch(err => {
        reject(err);
      });
  });
}
export { getSearchTitle };
