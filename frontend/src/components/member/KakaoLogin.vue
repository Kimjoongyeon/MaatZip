<template>
  <div style="padding:15px">
    <div id="kakao_login">
    <a @click="kakaoLogin()">
      <img src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg" width="277px"/>
    </a>
    <!--<button type="button" @click="kakaoLogout">카카오 로그아웃</button>-->
    </div>
  </div>
</template>

<script>
export default {
  name: "KakaoLogin",
  data() {
    return {
      
    }
  },
  methods: {
    kakaoLogin() {
      const params = {
          redirectUri: "http://localhost:8080/login",
      };
      window.Kakao.Auth.authorize(params);
    },
    kakaoLogout() {
      if (!window.Kakao.Auth.getAccessToken()) {
        console.log("Not logged in.");
        return;
      }
      window.Kakao.Auth.logout((response) => {
        //로그아웃
        console.log("access token:", window.Kakao.Auth.getAccessToken());
        console.log("log out:", response);
      });
    },
  }
}
</script>

<style scoped>
  *{
		margin: 0 auto;
	}
  #kakao_login{
    background-position: 0% 0%;
    background-repeat: no-repeat;
    text-align: center;
  }
</style>