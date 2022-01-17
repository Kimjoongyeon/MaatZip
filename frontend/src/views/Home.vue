<template>
  <div>
    <Header />
    <Footer />
  </div>
</template>

<script>
import Header from '../src/layout/Header'
import Footer from '../src/layout/Footer'
  export default {
    name: 'Home',
    components: {
      Header, Footer
    },
  }
</script>
