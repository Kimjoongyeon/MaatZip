<template>
  <v-container>
    <header class="header">
				<img src="@/assets/img/logo.png" class="logo" @click="Gomain" width="250px">
    </header>
    
    <div class="title">로그인 및 회원가입</div>
    <div class="subTitle">로그인을 하여 다양한 혜택을 누리세요.</div>
      <v-col sm="5">
        <v-text-field v-model="email" :rules="emailRules" placeholder="이메일을 입력해주세요." max-width="400" required 
          outlined ></v-text-field>
      </v-col>
			<v-col sm="5">
        <v-text-field type="password" v-model="pw" :rules="pwRules" placeholder="비밀번호를 입력해주세요." required
          outlined ></v-text-field>
      </v-col>
      <v-col sm="5">
			  <v-btn width="400px" @click="onSubmit" class="white--text secondary" large block >로그인하기</v-btn>
      </v-col>
    <div class="float_line">
      <div class="fl"></div><div class="fm">또는</div><div class="fr"></div>
    </div>
    <kakao-login/>
    <naver-login/>
    <div class="member_join">
      <span>아직 가입하지 않으셨나요?</span><br>
      <spam>지금 바로 가입하여 더 많은 정보와 혜택을 누리세요.</spam>
      <v-btn outlined style="margin-top: 20px" @click="Gomemberjoin">회원가입</v-btn>
    </div>
  </v-container>
</template>

<script>

import KakaoLogin from '../member/KakaoLogin.vue'
import NaverLogin from '../member/NaverLogin.vue'

export default {
  name: "MemberLoginForm",
  components: {
    KakaoLogin, NaverLogin
  },
  data() {
    return {
      code: "",
    };
  },
  methods: {
    Gomain () {
			this.$router.push({ name: 'Home' })
		},
    Gomemberjoin () {
			this.$router.push({ name: 'MemberJoinPage' })
		},
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

  *{
		font-family: 'Noto Sans KR', sans-serif;
		margin: 0 auto;
	}

  .header{
    text-align: center;
  }

  .logo{
    text-align: center;
    width: 250px;
    display: inline;
    cursor: pointer
  }

  .member_join{
    text-align: center;
    width: 400px;
    height: 150px;
    padding: 24px 0px 24px 0px;
    border: 2px solid grey;
  }

  .title{
    width: 400px;
    height: 100px;
    font-size: 20px;
    font-weight: 700;
    text-align: center;
    border-bottom: 1px solid#E0E0E0;
    padding: 50px 12px 25px 12px
  }

  .subTitle{
    font-weight: 700;
    width: 400px;
    height: 68px;
    text-align: center;
    padding: 24px 0 24px 0;
  }

  .fl{
    height: 7px;
    width: 180px;
    border-bottom: 1px solid  #E0E0E0;
    float: left;
    text-align: center;

  }

  .fm{
    height: 17px;
    width: 40px;
    float: left;
    text-align: center;
  }

  .fr {
    height: 7px;
    width: 180px;
    border-bottom: 1px solid  #E0E0E0;
    float: right;
    text-align: center;
  }

  .float_line {
    height: 45px;
    width: 400px;
    padding: 14px 0 14px 0;
    
  }

</style>