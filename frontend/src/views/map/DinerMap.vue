<template>
  <v-sheet class="fill-height">
    <Map></Map>
  </v-sheet>
</template>

<script>
import Map from '@/components/map/Map'
export default {
  name: 'DinerMap',
  components: {
    Map
  },
}
</script>