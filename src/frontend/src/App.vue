<template>
  <v-app>
    <div>
      <!-- 상단 bar -->
      <v-app-bar color="#00fa9a" dense dark height="60">
        <!-- 좌측 상단 네비게이션 아이콘 -->
        <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>
        <v-toolbar-title>Taejinimall</v-toolbar-title>
        <!-- 빈 공간 아이콘을 우측 끝으로 이동하기 위해 -->
        <v-spacer></v-spacer>
        <!-- 아이콘을 버튼으로 지정 to -> router의 Search와 연결 -->
        <v-btn icon :to="{ name: 'Search' }">
          <!-- mdi- 붙여서 쓰면 따로 설정안해도 아이콘 가져다 쓸 수 있음 -->
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
        <!-- 아이콘을 버튼으로 지정 to -> router의 Cart와 연결 -->
        <v-btn icon :to="{ name: 'Cart' }">
          <!-- mdi- 붙여서 쓰면 따로 설정안해도 아이콘 가져다 쓸 수 있음 -->
          <v-icon>mdi-cart</v-icon>
        </v-btn>
        <!-- 앱 바에서 메뉴 사용하기 left bottom -->
        <v-menu left bottom>
          <!-- v-slot이 v-btn의 on, attrs 속성을 가져다쓰고 attrs{전달 받은 속성이 다 들어있음(v-list)}을 사용하기 위해서는 activator를 사용해한다. -->
          <template v-slot:activator="{ on, attrs }">
            <!-- 버튼에 icon을 사용, v-bind = 태그의 속성을 동적으로 변경할 때 사용한다. "attrs" 전달 받은 속성이 다 들어있다(v-list), v-on="on" -> 클릭했을때 이벤트 발생 -->
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>

          <v-list>
            <!-- isLogin의 값이 존재하지 않으면 router의 'Login' 링크 -->
            <v-list-item v-if="!isLogin" :to="{ name: 'Login' }">
              <v-list-item-title>Login</v-list-item-title>
            </v-list-item>
            <!-- 클릭시 logout 함수 실행 후 Login으로 이동 -->
            <v-list-item v-if="isLogin" @click="logout" :to="{ name: 'Login' }">
              <v-list-item-title>Logout</v-list-item-title>
            </v-list-item>
            <v-list-item v-if="isLogin" to="/editUser">
              <v-list-item-title>회원정보 수정</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-app-bar>
      <!-- 네비게이션 아이콘 클릭시 동작 부분 -->
      <v-navigation-drawer v-model="drawer" absolute temporary>
        <v-list-item>
          <!-- 아이콘의 원본 이미지를 변경하지 않고 항목에 가장 적합한 모양의 디자인으로 바꿔서 표현해줌 -->
          <v-list-item-avatar>
            <!-- mdi- 붙여서 쓰면 따로 설정안해도 아이콘 가져다 쓸 수 있음 -->
            <v-icon>mdi-account</v-icon>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>{{
              this.$store.state.users.nickname
            }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <!-- 구분선 -->
        <v-divider></v-divider>
        <v-list dense>
          <v-list-item :to="{ name: 'Main' }">
            <v-list-item-icon>
              <v-icon>mdi-view-dashboard</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Home</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item :to="{ name: 'Mypage' }">
            <v-list-item-icon>
              <v-icon>mdi-forum</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Mypage</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item :to="{ name: 'AdminPage' }">
            <v-list-item-icon>
              <v-icon>mdi-forum</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>admin</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
    </div>
    <!-- 애플리케이션 구성 요소를 기반으로 콘텐츠 크기를 조정함 -->
    <v-main>
      <!-- 중앙 중심의 페이지에 fluid = 전체 너비를 이용 -->
      <v-container fluid>
        <!-- vue-router 사용 -->
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import { getSearchTitle } from "@/api/search";
import HelloWorld from "./components/HelloWorld";
import {mapActions} from "vuex";

export default {
  name: "App",

  components: {
    HelloWorld
  },
  mounted() {},
  data: () => ({
    searchModel: null,
    drawer: false,
    group: null,
    is: false,
    nickname: "로그인하세요.",
    items: [
      { title: "Home", icon: "mdi-view-dashboard" },
      { title: "Mypage", icon: "mdi-forum" }
    ]
  }),
  methods: {
    ...mapActions({logout: 'users/logout'}),
    async logoutUser() {
      if (await this.logout()) {
        await this.$router.push({name: "Main"})
      }
    }
  },
  computed: {
    isLogin() {
      return (
        this.$store.state.users.jwt != undefined ||
        this.$store.state.users.jwt == ""
      );
    }
  }
};
</script>
<style>
th {
  font-size: 1.2rem !important;
  font-weight: 100;
}

td {
  font-size: 1rem !important;
}
</style>
