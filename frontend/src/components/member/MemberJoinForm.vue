<template>
	<v-container>
		<form @submit.prevent="onSubmit">
    <header class="header">
				<img src="@/assets/img/logo.png" class="logo" @click="goMain" width="250px">
    </header>
		<div class="table">
			<div class="title">
				신규 회원가입하기
			</div>
			<table>
				<tr>
					<th>휴대전화인증</th>
					<td>
						<input class="txt_area" type="number" v-model="phone" placeholder="전화번호" :rules="phoneRules"><br><br>
						회원정보로 등록하실 이름을 입력해주세요. 영문 혹은 한글2~15자 이내로 입력해주세요.
					</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>
						<input class="txt_area" type="text" v-model="name" placeholder="이름" :rules="nameRules"><br><br>
						회원정보로 등록하실 이름을 입력해주세요. 영문 혹은 한글2~15자 이내로 입력해주세요.
					</td>
				</tr>
				<tr>
					<th>이메일</th>
					<td>
						<input class="txt_area" type="text" v-model="email" placeholder="이메일" :rules="emailRules"><br><br>
						이용하실 이메일을 입력해 주세요.
					</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td>
						<input class="txt_area" type="text" v-model="password" placeholder="비밀번호" :rules="pwRules"><br><br>
						<input class="txt_area" type="text" v-model="checkPassword" placeholder="비밀번호 확인" :rules="matchPwRules"><br><br>
						특수문자(예: !@#$ 등) 1자 이상을 포함한 10~15 글자의 비밀번호로 설정해주세요.
					</td>
				</tr>
			</table>
				<div class="terms_title">이용약관</div>
				
				<div class="terms_check">
					<div class="terms_block">
						<label >
							<span class="terms_txt">
								<input type="checkbox" v-model="Y" >이용약관</span>
									<v-dialog v-model="terms1_dialog">
									<template v-slot:activator="{ on, attrs }">
											<button v-bind="attrs" v-on="on" style="float:right">내용보기</button>
										</template>
										<v-card>
											<v-card-title class="text-h5">
												제목
											</v-card-title>

											<v-card-text>
												본문
											</v-card-text>
										</v-card>
									</v-dialog>
								</label>
							</div>
						<br>
					<div class="terms_block">
						<label>
							<span class="terms_txt">
								<input type="checkbox" v-model="Y" >개인정보 취급방침</span>
									<v-dialog v-model="terms2_dialog">
									<template v-slot:activator="{ on, attrs }">
											<button v-bind="attrs" v-on="on" style="float:right">내용보기</button>
										</template>
										<v-card>
											<v-card-title class="text-h5">
												제목2
											</v-card-title>

											<v-card-text>
												본문2
											</v-card-text>
										</v-card>
									</v-dialog>
								</label>
					</div>
					<br>
					<div class="terms_block">
						<label class="checkbox_label">
							<span class="terms_txt">
								<input type="checkbox" v-model="Y" >위치기반 서비스 이용약관</span>
									<v-dialog v-model="terms3_dialog">
									<template v-slot:activator="{ on, attrs }">
											<button v-bind="attrs" v-on="on" style="float:right">내용보기</button>
										</template>
										<v-card>
											<v-card-title class="text-h5">
												제목3
											</v-card-title>

											<v-card-text>
												본문3
											</v-card-text>
										</v-card>
									</v-dialog>
								</label>
							</div>
					</div>
				<div class="terms_allCheck">
					
				</div>
			<div class="row">
				<button class="join_btn" @submit.prevent="onSubmit">회원가입하기</button>
			</div>
		</div>
		</form>
	</v-container>	
</template>


<script>
import { mapState } from 'vuex'
import axios from 'axios'

export default {
	name: 'MemberJoinForm',
	props: {
	},
	data () {
		return {
			password: '',
			email: '',
			name: '',
			phone: '',
			checkPassword: null,
			matchPwRules: [
					pw => !!pw || '비밀번호를 입력해주세요!',
					pw => pw === this.password || '비밀번호가 일치하지 않습니다!'
			],
			canUseId: false,
			idDialog: false,
			emailDialog: false,
			code: null,
			randomCode: null,
			completeId: false,
			completeEmail: false,

			terms_dialog1: false,
			terms_dialog2: false,
			terms_dialog3: false,
		}
	},
	mounted() {
	},
	computed: {
		...mapState(['pwRules', 'emailRules', 'nameRules'])
	},
	methods: {
		onSubmit () {
			const { password, email, name, phone } = this
					this.$emit('submit', { password, email, name, phone }) 
			
		},
		checkEmail () {
			const email = this.email
			if (email) {
				axios.post(`http://localhost:7777/member/checkEmail/${email}`).then(res => {
					if (res.data == 'AlreadyUser') {
						alert('이미 사용중인 회원입니다.')
					} else {
						this.emailDialog = true
						this.randomCode = res.data
					}
				})
			} else {
				alert('이메일을 입력해주세요.')
			}
		},
		ckeckCode () {
			if (this.code == this.randomCode) {
					alert('이메일 인증이 완료되었습니다.')
					this.emailDialog = false
					this.code = null
					this.completeEmail = true
			} else {
					alert('인증번호가 일치하지 않습니다.')
			}
		},
		changeId () {
			this.completeId = false
		},
		changeEmail () {
			this.completeEmail = false
		},
		goMain () {
			this.$router.push({ name: 'Home' })
		}
	}
}
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
    cursor: pointer
  }

	th{
		width: 260px;
		height: 138px;
		font-weight: 700;
		text-align: left;
		font-size: 15px;
		
		background-color: #F9F9F9;
		border-bottom: 1px solid #E0E0E0;
		padding: 1px 1px 1px 38px;

		border-collapse: collapse;
		border-spacing: 2px 2px;
		
	}

	td{
		width: 700px;
		height: 138px;
		text-align: left;
		font-size: 13px;

		border-bottom: 1px solid  #E0E0E0;
		padding: 24px 48px 24px 48px;
		border-spacing: 2px 2px;
	}

	.title{
		height: 102px;
		width: 953px;
		padding: 50px 0px 25px 0px;
		text-align: left;

		font-size: 20px;
		font-weight: 700;
	}

	.table{
		padding: 0 0 24px 0px;
		text-align: center;
	}

	.txt_area{
		height: 40px;
		width: 400px;
		border: 1px solid  #D5D5D5;
		padding: 1px 2px 1px 10px;
	}

	.terms_title{
		font-size: 20px;
		font-weight: 700;
		text-align: left;

		height: 103px;
		width: 960px;
		border-bottom	: 1px solid  #E0E0E0;
		padding: 50px 12px 25px 12px;
	}

	.terms_check{
		height: 134px;
		width: 960px;
		padding: 30px 50px 30px 50px;
	}

	.checkbox_label{
		height: auto;
		width: auto;
		margin: 0 NaNpx 0 NaNpx;
	}

	.terms_allCheck{
		height: 80px;
		width: 960px;
		border-top: 1px solid  #E0E0E0;
		border-bottom: 1px solid  #E0E0E0;
		padding: 24px 50px 24px 50px;

		background-color:  #F9F9F9;
		text-align: right;
	}

	.terms_txt{
		font-size: 16px;
		font-weight: 500;
		cursor: pointer;

		float: left;
	}

	.row{
		height: 170px;
		width: 960px;
		
		padding: 50px 0 55px 0;
	}

	.join_btn{
		font-size: 16px;
		text-align: center;

		height: 65px;
		width: 400px;
		padding: 1px 6px 1px 6px;

		background-color:  #09D2E5;
		background-position: 0% 0%;
		color:  #FFFFFF;
		/*padding: 50px 0px 55px;*/
	}

</style>