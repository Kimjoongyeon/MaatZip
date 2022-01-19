<template>
  <v-container>
    <header>
      
    </header>
    <v-divider style="margin-bottom: 20px"></v-divider>
    <div>
      <a id="kakao_login" @click="kakaoLogin()">
        <img src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg" width="277"/>
      </a>
      <!--<button type="button" @click="kakaoLogout">카카오 로그아웃</button>-->
      <div>
        <div id="naverIdLogin"></div>
      </div>
      <!--<button type="button" @click="naverLogout">네이버 로그아웃</button>-->
    </div>
    <div class="member_join">
      <span class="fs-15 fw-b">아직 가입하지 않으셨나요?</span><br>
      <spam>지금 바로 가입하여 더 많은 정보와 혜택을 누리세요.</spam>
      <v-btn outlined style="margin-top: 20px">회원가입</v-btn>
    </div>
  </v-container>
</template>

<script>
import axios from 'axios'
export default {
  name: "MemberLoginForm",
  components: {},
  data() {
    return {
      code: "",
    };
  },
  mounted() {
    //네이버 로그인에서 사용하는 코드
    this.naverLogin = new window.naver.LoginWithNaverId({
      clientId: "xP1nq_zE4QPSy2WJiyZ6", //개발자센터에 등록한 ClientID
      callbackUrl: "http://localhost:8080/login", //개발자센터에 등록한 callback Url
      isPopup: false, //팝업을 통한 연동처리 여부
      loginButton: { color: "green", type: 3, height: 60 }, //로그인 버튼의 타입을 지정
    });
    //설정정보를 초기화하고 연동을 준비
    this.naverLogin.init();
    this.naverLogin.getLoginStatus((status) => {
      if (status) {
        console.log(status);
        console.log(this.naverLogin.user);
        //필수적으로 받아야하는 프로필 정보가 있다면 callback처리 시점에 체크
        var email = this.naverLogin.user.getEmail();
        if (email == undefined || email == null) {
          alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
          //사용자 정보 재동의를 위하여 다시 네아로 동의페이지로 이동함
          this.naverLogin.reprompt();
          return;
        }
      } else {
        console.log("callback 처리에 실패하였습니다.");
      }
    });
  },
  methods: {
    kakaoLogin() {
      window.Kakao.Auth.login({
        scope: "profile_nickname, profile_image, account_email",
        success: this.getKakaoAccount,
      });
    },
    getKakaoAccount() {
      window.Kakao.API.request({
        url: "/v2/user/me",
        success: (res) => {
          const kakao_account = res.kakao_account;
          const nickname = kakao_account.profile_nickname.nickname; //카카오 닉네임
          const email = kakao_account.email; //카카오 이메일
          console.log("nickname", nickname);
          console.log("email", email);
          //로그인 처리 구현
          console.log(kakao_account);
          this.$store.commit("user", kakao_account);
          alert("로그인 성공!");
        },
        fail: (error) => {
          // this.$router.push("/errorPage");
          console.log(error);
        },
      });
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
    naverLogout() {
      const accessToken = this.naverLogin.accessToken.accessToken;
      const url = `/oauth2.0/token?grant_type=delete&client_id=xP1nq_zE4QPSy2WJiyZ6&client_secret=jWblxJytJD&access_token=${accessToken}&service_provider=NAVER`;
      axios.get(url).then((res) => {
        console.log(res.data);
      });
    },
  },
};
</script>

<style scoped>
  *{
		font-family: 'Noto Sans KR', sans-serif;
		margin: auto;
	}

  #kakao_login{
    width: 300px;
    margin-left: 430px;
  }

  #naverIdLogin{
    width: 300px;
  }

  .member_join{
    text-align: center;
    width: 400px;
    height: 150px;
    padding: 24px 0px 24px 0px;
    border: 2px solid grey;
  }


</style>