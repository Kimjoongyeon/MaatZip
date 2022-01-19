<template>
  <member-login-form></member-login-form>
</template>

<script>
import MemberLoginForm from '../../components/member/MemberLoginForm.vue'

export default {
  name: 'MemberLoginPage',
  components: {
    MemberLoginForm
  }
}
</script>

<style scoped>
  * {
		font-family: 'Noto Sans KR', sans-serif;
		margin: auto;
	}
</style>