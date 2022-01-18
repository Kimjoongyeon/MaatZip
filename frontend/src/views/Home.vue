<template>
  <div>
    <Header />
    <Footer />
  </div>
</template>

<script>
import Header from '../layout/Header'
import Footer from '../layout/Footer'
  export default {
    name: 'Home',
    components: {
      Header, Footer
    },
  }
</script>
