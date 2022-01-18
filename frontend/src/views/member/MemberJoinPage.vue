<template>
    <div>
        <div align="center">
            <h1>회원가입</h1>
        </div>
        
        <member-join-form v-bind:kakao_account="kakao_account" @submit="onSubmit"/>
   
     
    </div>
</template>

<script>
import MemberJoinForm from '@/components/member/MemberJoinForm'
import axios from 'axios'
export default {
    name: 'MemberRegisterPage',
    components: {
        MemberJoinForm
    },
    props: {
        kakao_account: {
            type: Object,
            require: true
        }
    },
    methods: {
        onSubmit (payload) {
            const { userId, password, email, name, birth, sex, phone, auth } = payload
            axios.post('http://localhost:7777/member/register', {
                        userId, password, email, name, birth, sex, phone, auth
                    })
                    .then(res => {
                        alert('회원가입이 완료되었습니다' + res)
                        
                        this.$router.push({
                            name: 'Home'
                            
                        })
                        
                    })
                    .catch(res => {
                        alert(res.response.data.message)
                    })
        }
    }
}
</script>


<style scoped>
h1 {
    font-size: 33px;
    font-weight: bold;
    width: 100%;
    color: #000;
    margin-top: 50px;
}
</style>