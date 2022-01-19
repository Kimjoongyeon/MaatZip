<template>
  <div>
    <Header />
    <div class="banner">
      <swiper>
				<swiper-slide><img class="swiper_bn" src="../assets/img/banner.jpg"></swiper-slide>
			</swiper>
				<div class=tabHouse>
          <h1 class="main_text">솔직한 리뷰, 믿을 수 있는 평점</h1><p class="main_text">Yami!</p>
        <fieldset class="main_search">
          <input type="text" class="form_control" placeholder="지역, 식당 또는 음식" flat none>
            <v-btn class="search-icon" height="50px" width="180px" color="#FF7100">
              검색
            </v-btn>
        </fieldset>
		  </div>
    </div>
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

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

	* {
		font-family: 'Noto Sans KR', sans-serif;
		margin: 0 auto;
	}

.banner {
  position: relative;
}

.search-icon {
		/* Box */
		height: 45px;
		width: 180px;
		margin: 150px 250px;
		padding: 0px;
    font-size: 21px;
    line-height: 52px;


		/* Positioning */
		position: absolute;
    top: 40px;
    bottom: 0px;
    right: -1px;
    left: 261px;
    float: none;
    display: block;
    color: white;

    border-top-left-radius: 50px;
		border-top-right-radius: 50px;
		border-bottom-left-radius: 50px;
		border-bottom-right-radius: 50px;
	}

.main_search {
  width: 650px;
  height: 50px;
  border: 2px solid #FF7100;
  margin: 100px 397px 0 0px;
  background-color: white;
  border-top-left-radius: 50px;
  border-top-right-radius: 50px;
  border-bottom-left-radius: 50px;
  border-bottom-right-radius: 50px;

}

.form_control {
  width: 400px;
  height: 45px;
  margin-left: 50px;
}

.main_text {
		text-align: center;
    line-height: 45px;
		font-size: 30px;
		font-weight: 450;
		background: 0% 0%;
		white-space: pre-wrap;
		display: block;
		/*margin: 0 129px 0 129px;*/
    width: 720px;
		color: white;
	}

  .swiper_bn {
		height: 450px;
		position: relative;
    width: 100%;
    display: flex;
		justify-content: center;
		align-items: center;
		text-align: center;
		font-weight: bold;
	}
  .tabHouse::after {
		display: block;
		content: "";
		position: absolute;
		z-index: -1;
		top: 80%;
		left: 50%;
		transform: translate( -50%, 0% );
		min-width: 700px;
		height: 300px;
		opacity: 0.6;
	}

	.tabHouse {
		align-content: center;
		height: 174px;
		width: 704px;
		font-size: 16px;
		display: block;
		margin: 0 auto;
		position: absolute;
		top: 40%;
		left: 50%;
		transform: translate( -50%, -50% );
	}

</style>