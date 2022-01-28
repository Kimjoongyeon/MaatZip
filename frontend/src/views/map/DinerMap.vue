<template>
  <v-sheet class="fill-height">
    <NaverMap></NaverMap>
  </v-sheet>
</template>

<script>
import NaverMap from '@/components/map/NaverMap'
export default {
  name: 'DinerMap',
  components: {
    NaverMap
  },
}
</script>