<template>
  <v-container>
    <form @submit.prevent="onSubmit">
			<v-card class="pa-10 mx-auto" width="500">
				
        <span>이메일</span>
				<div class="d-flex mt-3">
					<v-text-field v-model="email" :rules="emailRules" required
						solo @change="changeEmail"></v-text-field>
				</div>

				<span>이름</span>
				<v-text-field v-model="name" :rules="nameRules" required
					solo class="mt-3"></v-text-field>

				<span>비밀번호</span>
				<v-text-field v-model="password" :rules="pwRules"	type="password" required
					solo class="mt-3"></v-text-field>

				<span>비밀번호 확인</span>
				<v-text-field v-model="checkPassword" :rules="matchPwRules"	type="password" required
					solo class="mt-3"></v-text-field>

				<span>핸드폰</span>
				<v-text-field v-model="phone" :rules="phoneRules" required
					solo class="mt-3"></v-text-field>
         
				<v-card-actions>
					<v-btn @click="goHome" outlined>취소</v-btn>
					<v-spacer></v-spacer>
					<v-btn @click="onSubmit" class="secondary">회원가입</v-btn>
				</v-card-actions>
			</v-card>
    </form>

    <v-dialog v-model="idDialog" max-width="400">
      <v-card v-if="!canUseId" class="pa-3">
        <v-card-title>사용할 수 없는 아이디입니다.</v-card-title>
        <v-card-text>다른 아이디를 사용해주세요.</v-card-text>
        <v-text-field v-model="userId" :rules="idRules" required
				 label="아이디" class="mx-5" solo></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="checkId" class="white--text secondary">확인</v-btn>
        </v-card-actions>
      </v-card>

      <v-card v-else class="pa-3">
        <v-card-title class="mb-10">사용 가능한 아이디입니다.</v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="closeDialog" class="white--text secondary">사용하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="emailDialog" max-width="400">
      <v-card class="pa-3">
        <v-card-title>메일함을 확인해주세요.</v-card-title>
        <v-card-text>인증번호 6자리를 입력해주세요.</v-card-text>
        <v-text-field v-model="code"
          label="인증번호" class="mx-5" solo></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="ckeckCode" class="white--text secondary">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
			radioGroup: null,
			radioGroup2: null,
			password: '',
			email: '',
			name: '',
			phone: '',
			menu: false,
			date: null,
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
			completeEmail: false
		}
	},
	mounted() {
	},
	computed: {
		...mapState(['idRules', 'pwRules', 'emailRules', 'nameRules', 'birthRules', 'phoneRules'])
	},
	methods: {
		onSubmit () {
			const { password, email, name, phone, auth } = this
			if (this.completeId && this.completeEmail) { 
					this.$emit('submit', { password, email, name, phone, auth })
			} else {
				alert("아이디 또는 이메일 인증을 완료해주세요.")
			}
			//this.$emit('submit', { userId, password, email, name, birth, sex, phone, auth })
		},
		checkId () {
			const userId = this.userId
			if (userId) {
				axios.post(`http://localhost:7777/member/checkId/${userId}`).then(res => {
					this.canUseId = res.data
					this.idDialog = true
					if (this.canUseId) {
						this.completeId = true
					}
					
				})
			} else {
				alert('아이디를 입력해주세요.')
			}
		},
		closeDialog () {
			this.idDialog = false
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
		goHome () {
			this.$router.push({ name: 'Home' })
		}
	}
}
</script>