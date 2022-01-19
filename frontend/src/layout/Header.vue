<template>
  <header>
    <div>
			<h1>
				<img src="@/assets/img/logo.png" @click="Gomain" width="250px" height="80px" style="margin-left: -30px">
			</h1>
      <!--<ul><input type="text" class="form_control" placeholder="검색 키워드를 입력하세요!" flat none></ul>-->
    </div>
    <div class="right-header">
			<v-container>
				<div v-if="isLogin">
					<div class="loginUser">
						<span>
							{{ userInfo.userId }}
						</span>님
						<v-btn @click="gotoMypage" text>마이페이지</v-btn>|
						<v-btn @click="logout" text>로그아웃</v-btn>
					</div>
				</div>
				<div style="float:right" v-else>
					<v-btn @click="gotoJoin" text>회원가입</v-btn>|
					<v-btn @click="gotoLogin" text>로그인</v-btn>
				</div>
			</v-container>
		</div>
  </header>
</template>

<script>
export default {
    name: 'Header',
    data() {
      return {
    }
  },
  methods: {
		//...mapActions(['fetchSession', 'fetchUserInfo']),
    gotoJoin() {
      this.$router.push({name: 'MemberJoinPage'})
    },
    gotoLogin() {
      this.$router.push('/login')
    },
    logout () {
      this.$store.commit('USER_LOGIN', false)
      this.fetchSession(this.$cookies.remove('session'))
      this.$store.commit('FETCH_USER_INFO', [])
      alert("로그아웃 되었습니다!")
      this.$router.push({ name: 'Home' })
    },
    gotoMypage () {
      this.$router.push({ name: 'Mypage' })
    },
		Gomain () {
			this.$router.push({ name: 'Home' })
		},
  }
}
</script>

<style scoped>
	@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

	* {
		font-family: 'Noto Sans KR', sans-serif;
		margin: 0 auto;
	}

	h1 {
		float: left;
		margin-right: 40px;
	}

  ol, ul {
		list-style: none;
	}

	a {
		color: #000;
		text-decoration: none;
	}

	header {
		max-height:100px;
		width: 1400px;
		padding: 15px 0px;
		/*margin: auto;*/
	}

	header:after {
		content: "";
		display: block;
		clear: both;
	}

	.right-header {
		width: 330px;
		margin-right: 0px;
	}

	.right-header .loginUser {
		font-size: 13px;
	}

	.right-header .loginUser > span {
		background: linear-gradient(to top, #a3f5a8 50%, transparent 50%);
	}

</style>