<template>
  <div>
    <div id="naverIdLogin"></div>
  </div>
      <!--<button type="button" @click="naverLogout">네이버 로그아웃</button>-->
</template>

<script>

import axios from 'axios'

export default {
  name: 'NaverLogin',
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
    naverLogout() {
      const accessToken = this.naverLogin.accessToken.accessToken;
      const url = `/oauth2.0/token?grant_type=delete&client_id=xP1nq_zE4QPSy2WJiyZ6&client_secret=jWblxJytJD&access_token=${accessToken}&service_provider=NAVER`;
      axios.get(url).then((res) => {
        console.log(res.data);
      });
    },
  }
}
</script>

<style scoped>
  *{
		margin: 0 auto;
	}

  #naverIdLogin{
    width: 290px;
    text-align: center;
  }
</style>

