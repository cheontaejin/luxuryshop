import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store/modules/users";

/* 명시적으로 router를 추가해야 사용 가능 */
Vue.use(VueRouter);
const Empty = () => import("../components/Empty.vue")
const DetailProduct = () => import("../views/DetailProduct.vue");
const Mypage = () => import("../views/Mypage.vue");
const Main = () => import("../views/Main.vue");
const Cart = () => import("../views/mycart.vue");
const Search = () => import("../views/Search.vue");
const Login = () => import("../views/login.vue");
const SignUp = () => import("../views/signUp.vue");
const EditUser = () => import("../views/editUser.vue");
const PaymentCheck = () => import("../views/PaymentCheck.vue");
const AdminPage = () => import("../views/admin/AdminPageHome.vue");
const AdminProductDetail = () => import("../views/admin/product/AdminProductDetail.vue");
const AdminProductUpdate = () => import("../views/admin/product/AdminProductUpdate.vue");
const Payment=()=> import("../views/Payment.vue")
const routes = [
    {
        /* path: url에 대한 정보 */
        path: "/",
        name: "Main",
        /* component: url 주소로 갔을 때 표시될 컴포넌트 */
        component: Main
    },
    {
        path: "/mypage",
        name: "Mypage",
        component: Mypage,
        meta: {authRequired: true}
    },
    {
        path: "/cart",
        name: "Cart",
        component: Cart,
        meta: {authRequired: true}
    },
    {
        path: "/detail/:id",
        name: "DetailProduct",
        component: DetailProduct,
        props: true
    },
    {
        path: "/login",
        name: "Login",
        component: Login
    },
    {
        path: "/singUp",
        name: "SignUp",
        component: SignUp
    },
    {
        path: "/editUser",
        name: "EditUser",
        component: EditUser,
        meta: {authRequired: true}
    },
    {
        path: "/search",
        name: "Search",
        component: Search,
    },
    {
        path: "/payment",
        name: "Payment",
        component: Payment,
        props: true
    },
    {
        path: "/payment/check",
        name: "PaymentCheck",
        component: PaymentCheck,
        props: true
    },
    {
        path: "/admin",
        component: Empty,
        children: [
            {
                path: "main",
                name: "AdminPage",
                component: AdminPage,

            },
            {
                path: "detail/:id",
                name: "AdminProductDetail",
                component: AdminProductDetail,
                props: true
            },
            {
                path: ":id/update",
                name: "AdminProductUpdate",
                component: AdminProductUpdate,
                props: true
            }
        ],
        meta: {authRequired: true}
    },
];
const router = new VueRouter({
    mode: "history",    //mode: "history"는 localhost뒤에 /#/을 삭제해주는 역할
    base: process.env.BASE_URL,
    routes
});

/* router.beforeEach를 사용해서 모든 라우터 객체에 가드를 적용합니다. */
router.beforeEach(function (to, from, next) {
    if (to.matched.some(routeInfo => routeInfo.meta.authRequired)) {
        /* === : Identity, 일치 연산자로, 형 변환을 하지 않고 두 피 연산자를 더 정확하게 비교한다. */
        if (store.state.jwt === null) {
            window.alert('로그인이 필요합니다.');
        } else {
            next();
        }
    } else {
        next();
    }
})

export default router;