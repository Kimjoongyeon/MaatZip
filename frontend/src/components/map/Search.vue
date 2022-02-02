<template>
  <v-card max-width="450" class="pa-2">
    <v-row class="mb-n10 mt-n3">
      <v-col>
        <v-autocomplete :items="guList" label="구" item-text="gu" solo>
          <template v-slot:item="{ item }">
            <v-list-item-content>
              <v-list-item-subtitle @click="selectGu(item.gu)">
                {{ item.gu }}
              </v-list-item-subtitle>
            </v-list-item-content>
          </template>
        </v-autocomplete>
      </v-col>
      <v-col>
        <v-autocomplete :items="dongList" label="동" item-text="dong" solo>
          <template v-slot:no-data>
            <v-list-item>
              <v-list-item-subtitle>
                구를 선택해주세요.
              </v-list-item-subtitle>
            </v-list-item>
          </template>
          <template v-slot:item="{ item }">
            <v-list-item-content>
              <v-list-item-subtitle @click="selectDong(item.dong)">
                {{ item.dong }}
              </v-list-item-subtitle>
            </v-list-item-content>
          </template>
        </v-autocomplete>
      </v-col>
      <v-col>
        <v-menu offset-y>
          <template v-slot:activator="{on}">
            <v-text-field v-model="search" v-on="on" @keyup="searchStation" label="역" 
               append-icon="mdi-magnify" solo hide-details></v-text-field>
          </template>
          <v-list v-if="searchList" id="scrolling-techniques-7" class="overflow-y-auto" 
            max-height="350">
            <v-list-item-group v-for="list in searchList" :key="list.index">
              <v-list-item @click="selectStation(list.name, list.lat, list.lng)">
                <v-list-item-content>
                  <v-list-item-subtitle>
                    {{list.name}}
                  </v-list-item-subtitle>
                </v-list-item-content>   
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-menu>
      </v-col>
    </v-row>
  </v-card>
</template>


<script>
import axios from 'axios'

export default {
  props: {
    guPolygons: {},
    guMarkers: {},
    dongPolygons: {},
    dongMarkers: {}
  },
  data () {
    return {
      search: null,
      searchList: null,
      guList: [],
      dongList: [],
      gu: null,
      dong: null,
      guPolygon: [],
      guMarker: [],
    }
  },
  mounted() {
    axios.get('http://localhost:7777/gu/lists').then(res => {
      for (let i = 0; i < res.data.length; i++) {
        this.guList.push({ gu: res.data[i].gu, lat: res.data[i].lat, lng: res.data[i].lng })
      }
    })

    this.guPolygon = this.guPolygons
    this.guMarker = this.guMarkers
  },
  methods: {
    searchStation () {
      if (this.search) {
        axios.get(`http://localhost:7777/station/${this.search}`).then(res => {
          if (res.data.length != 0) {
            this.searchList = res.data
          } else {
            this.searchList = null
          }
        }) 
      }   
    },
    selectStation (name, lat, lng) {
      this.search = name
      this.$emit('selectStation', name, lat, lng)
      this.searchList = null
    },
    selectGu (gu) {
      this.gu = gu

      let num
      let coord

      for (let i = 0; i < this.guMarker.length; i++) {
        if (this.gu == this.guMarker[i].title) {
          num = i
          coord = this.guMarker[i].position
        }
      }
      this.$emit('selectArea', coord, 14, this.guPolygon[num], this.guMarker[num])

      this.dong = null
      this.dongList = []

      axios.get(`http://localhost:7777/dong/lists/${gu}`).then(res => {
        for (let i = 0; i < res.data.length; i++) {
          this.dongList.push({ dong: res.data[i].dong, lat: res.data[i].lat, lng: res.data[i].lng })
        }
      })
    },
    selectDong (dong) {
      this.dong = dong
      
      let num
      let coord

      for (let i = 0; i < this.dongMarkers.length; i++) {
        if (this.dong == this.dongMarkers[i].title) {
          num = i
          coord = this.dongMarkers[i].position
        }
      }
      this.$emit('selectArea', coord, 16, this.dongPolygons[num], this.dongMarkers[num])
    }
  }
}
</script>