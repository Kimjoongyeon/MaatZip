export default {
  // Login
  isAuth: false,
  isLogin: false,
  
  emailRules: [
    id => !!id || '아이디를 입력해주세요!',
    id => /^[a-zA-Z0-9]*$/.test(id) || '아이디는 영문 + 숫자만 입력 가능합니다.',
    id => !(id && id.length >= 15) || '아이디는 15자 이상 입력할 수 없습니다.'
  ],
  pwRules: [
      pw => !!pw || '비밀번호를 입력해주세요!',
  ],
};
