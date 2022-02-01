<template>
  <v-sheet class="fill-height" max-height="700">
    <!--
    <Search @selectStation="selectStation" @selectArea="selectArea"
      :guPolygons="guPolygons" :guMarkers="guMarkers" 
      :dongPolygons="dongPolygons" :dongMarkers="dongMarkers"
      style="position: fixed; z-index: 1;"></Search>-->

    <naver-map
      ref="maps"
      :center="center"
      :zoom="zoomLevel"
      style="width: 70%; height: 100%; float: left;"
      @idle="idle">

      <div v-if="zoomLevel >=13">
        <naver-map-marker-cluster :options="cluster.options">
          <naver-map-marker v-for="list in houseList" :key="list.index"
            :options="{ position: { lat: list.lat, lng: list.lng },
                        icon: iconContent(list.salesType,list.deposit, list.rent)}"
            @click="viewInfo(list)"/>
        </naver-map-marker-cluster>
      </div>
     
      <div v-if="zoomLevel >= 15">
        <naver-map-marker
          v-for="list in stationList" :key="list.index"
          :options="{
              position: { lat: list.lat, lng: list.lng },
              icon: {
                url: require('@/assets/img/metro-station.png')
              }
            }"
          @click="showCircle(list.name, list.lat, list.lng)">
        </naver-map-marker>  
      </div>
    </naver-map>

    <v-card width="400" style="float:right;" max-height="700"> 
      <v-card-title>
        <span>{{ selectHouseLength }}개의 매물</span> 
        <span class="ml-2" v-if="stationName">({{ stationName }}역 5분 거리)</span>
        <span class="ml-2" v-if="areaName">({{ areaName }})</span>
      </v-card-title>
      <!--
      <v-divider></v-divider>
      -->
      <v-card-subtitle v-if="!selectHouseLength">해당 지역에 매물이 없습니다.<br>지도를 이동&축소해 주세요.</v-card-subtitle>
      
      <v-virtual-scroll :bench="10" :items="selectHouseList" :item-height="120" height="630">
        <template v-slot:default="{ item }">
          <v-list-item three-line @click="viewInfo(item)" 
            @mouseover="openPosition(item.lat, item.lng)" @mouseout="closePosition">
            <img :src="imageList(item.image)" class="mr-3" width="40%">
            <v-list-item-content>
              <v-list-item-subtitle class="caption">{{item.roomType}} · {{item.sizeM2}}㎡</v-list-item-subtitle>
              <v-list-item-title v-if="item.salesType == '월세'" class="title my-2">{{item.salesType}} {{item.deposit}}/{{item.rent}}</v-list-item-title>
              <v-list-item-title v-else class="title my-2">{{item.salesType}} {{item.deposit}}</v-list-item-title>
              <v-list-item-subtitle>{{item.title}}</v-list-item-subtitle>
            </v-list-item-content> 
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-card>
    
    <!--
    <v-dialog v-model="dialog" max-width="400">
      <InfoDetail :info="houseInfo"></InfoDetail>
    </v-dialog>
    -->
  </v-sheet>
</template>

<script>
import seoulGu from '@/assets/data/seoul_gu'
import seoulDong from '@/assets/data/seoul_dong'
import axios from 'axios'
import point from 'point-in-polygon'

export default {
  components: {
  },
  props: {
    houseList: {
    }
  },
  data() {
    return {
      stationList: null,
      center: { lat: 37.5335, lng: 126.9896 },
      cluster: {
        options: {
          maxZoom: 17,
          icons: [
            {content: `<div class="cluster lv1"></div>`},
            {content: `<div class="cluster lv2"></div>`},
            {content: `<div class="cluster lv3"></div>`},
            {content: `<div class="cluster lv4"></div>`},
            {content: `<div class="cluster lv5"></div>`}
          ],
          gridSize: 200,
          averageCenter: true,
        }
      },
      houseInfo: null,
      zoomLevel: 12,
      infoWindow: null,
      dialog: false,
      selectHouseList: [],
      selectHouseLength: null,
      guMarkers: [],
      guPolygons: [],
      dongMarkers: [],
      dongPolygons: [],
      stationName: null,
      areaName: null
    }
  },
  watch: {
    zoomLevel: function () {
      if (this.zoomLevel <= 13) {
        if (this.guMarkers.length == 0) {
          this.addGuMarkerPolygon()
          this.guMarkerEvent()
        }
        this.removeDongMarkerPolygon()
      } else if (this.zoomLevel > 13) {
        this.removeGuMarkerPolygon()
        if (this.dongMarkers.length == 0) {
          this.addDongMarkerPolygon()
          this.dongMarkerEvent()
        }
      }
    }
  },
  mounted () {
    axios.get('http://localhost:7777/station/lists').then(res => {
      this.stationList = res.data
    }) 

    setTimeout(() => {
      this.markersOnMap()
      this.addGuMarkerPolygon()
      this.guMarkerEvent()
    }, 1000)
  },
  methods: {
    idle () {
      this.zoomLevel = this.$refs.maps.map.getZoom()

      this.markersOnMap()

      this.stationName = null
      this.areaName = null
    },
    markersOnMap () {
      let map = this.$refs.maps.map

      this.selectHouseList = []

      const list = this.houseList

      for (let i = 0; i < list.length; i++) {
        let coords = new window.naver.maps.LatLng(list[i].lat, list[i].lng)
        
        if (map.getBounds().hasLatLng(coords)) {
          this.selectHouseList.push(list[i])
        }
      }
      this.selectHouseLength = this.selectHouseList.length
    },
    viewInfo (info) {
      this.houseInfo = info
      console.log(this.houseInfo)
      this.dialog = true
    },
    iconContent (salesType, deposit, rent) {
      let leaseDeposit = (deposit / 10000).toString() + '억'
      
      if (salesType == '전세') {
        return { content: `<div class="marker-html" style="background: #CEE5D0;">${leaseDeposit}</div>` }
      } else if (salesType == '매매') {
        return { content: `<div class="marker-html" style="background: #F3F0D7;">${leaseDeposit}</div>` }
      } else {
        let rentDeposit

        if (deposit >= 1000) {
          rentDeposit = (deposit / 10000).toString() + '억'
        } else {
          rentDeposit = deposit
        }
        return { content: `<div class="marker-html" style="background: #FED2AA;">${rentDeposit}/${rent}</div>` }  
      }  
    },
    showCircle (name, lat, lng) {
      const coords = new window.naver.maps.LatLng(lat, lng)
      this.$refs.maps.map.setCenter(coords)
      this.$refs.maps.map.setZoom(17)

      const mapCircle = new window.naver.maps.Circle({
        map: this.$refs.maps.map,
        center: new window.naver.maps.LatLng(lat, lng),
        radius: 400,
        fillColor: 'gray',
        fillOpacity: 0.2,
        visible: this.circle
      })

      this.stationName = name

      this.selectHouseList = []

      const list = this.houseList

      for (let i = 0; i < list.length; i++) {
        let coords = new window.naver.maps.LatLng(list[i].lat, list[i].lng)
        if (mapCircle.getBounds().hasLatLng(coords)) {
          this.selectHouseList.push(list[i])
        }
      }
      this.selectHouseLength = this.selectHouseList.length
      
      setTimeout(() => {
        mapCircle.setMap(null)
      }, 2000)
    },
    selectStation (name, lat, lng) {
      this.showCircle(name, lat,lng)
    },
    imageList (imageList) {
      return imageList.split(',')[0]
    },
    // selectGu (lat, lng) {
    //   const coords = new window.naver.maps.LatLng(lat, lng)
    //   this.$refs.maps.map.setCenter(coords)
    //   this.$refs.maps.map.setZoom(14)
    // },
    // selectDong (lat, lng) {
    //   const coords = new window.naver.maps.LatLng(lat, lng)
    //   this.$refs.maps.map.setCenter(coords)
    //   this.$refs.maps.map.setZoom(16)
    // },
    addGuMarkerPolygon () {
      const list = seoulGu.features

      for (let i = 0; i < list.length; i++) {
        let position = new window.naver.maps.LatLng(list[i].properties.lat, list[i].properties.lng)
        
        let gu = list[i].properties.SGG_NM
        
        let marker = new window.naver.maps.Marker({
          map: this.$refs.maps.map,
          position: position,
          icon: { content: `<div class="marker-html">${gu}</div>` },
          title: `${gu}`
        })
        
        let coords = list[i].geometry.coordinates[0]
        let coordList = []
        
        for(let i = 0; i < coords.length; i++) {
          coordList.push(new window.naver.maps.LatLng(coords[i][1], coords[i][0]))
        }
        
        let polygon = new window.naver.maps.Polygon({
          map: this.$refs.maps.map,
          paths: [coordList],
          strokeColor: 'blue',
          strokeWeight: 2,
          visible: false
        })

        this.guPolygons.push(polygon)
        this.guMarkers.push(marker)
      }
    },
    guMarkerEvent() {
      for (let i = 0; i < this.guMarkers.length; i++) {
        window.naver.maps.Event.addListener(this.guMarkers[i], 'mouseover', () => {
          let polygon = this.guPolygons[i]
          
          polygon.setOptions({
            visible: true
          })
        })

        window.naver.maps.Event.addListener(this.guMarkers[i], 'mouseout', () => {
          let polygon = this.guPolygons[i]
          
          polygon.setOptions({
            visible: false
          })
        })

        window.naver.maps.Event.addListener(this.guMarkers[i], 'click', (e) => {
          this.selectArea(e.coord, 14, this.guPolygons[i], this.guMarkers[i])
        })
      }
    },
    removeGuMarkerPolygon() {
      for (let i = 0; i < this.guMarkers.length; i++) {
        this.guMarkers[i].setMap(null)
        this.guPolygons[i].setMap(null)
      }
      this.guMarkers = []
      this.guPolygons = []
    },
    openPosition (lat, lng) {
      this.infoWindow = new window.naver.maps.InfoWindow({
        disableAnchor: true,
        backgroundColor: 'transparent',
        borderWidth: 0,
        content: `<div class="infoWindow"></div>`
      })
      const coords = new window.naver.maps.LatLng(lat, lng)
      this.infoWindow.open(this.$refs.maps.map, coords)
    },
    closePosition () {
      this.infoWindow.close()
    },
    addDongMarkerPolygon () {
      const list = seoulDong.features

      for (let i = 0; i < list.length; i++) {
        
        let coords = list[i].geometry.coordinates[0]
        let coordList = []

        for(let i = 0; i < coords.length; i++) {
          coordList.push(new window.naver.maps.LatLng(coords[i][1], coords[i][0]))
        }
        
        let polygon = new window.naver.maps.Polygon({
          map: this.$refs.maps.map,
          paths: [coordList],
          strokeColor: 'blue',
          visible: false,
          strokeWeight: 2,
          strokeLineJoin: 'round'
        })

        let dong = list[i].properties.adm_nm.split(' ')[2]

        let position = polygon.getBounds().getCenter()

        let marker = new window.naver.maps.Marker({
          map: this.$refs.maps.map,
          position: position,
          icon: { content: `<div class="marker-html">${dong}</div>` },
          title: `${dong}`
        })

        this.dongPolygons.push(polygon)
        this.dongMarkers.push(marker)
      }
    },
    dongMarkerEvent() {
      for (let i = 0; i < this.dongMarkers.length; i++) {
        window.naver.maps.Event.addListener(this.dongMarkers[i], 'mouseover', () => {
          let polygon = this.dongPolygons[i]

          polygon.setOptions({
            visible: true
          })
        })

        window.naver.maps.Event.addListener(this.dongMarkers[i], 'mouseout', () => {
          let polygon = this.dongPolygons[i]

          polygon.setOptions({
            visible: false
          })
        })

        window.naver.maps.Event.addListener(this.dongMarkers[i], 'click', (e) => {
          this.selectArea(e.coord, 16, this.dongPolygons[i], this.dongMarkers[i])
        })
      }
    },
    removeDongMarkerPolygon() {
      for (let i = 0; i < this.dongMarkers.length; i++) {
        this.dongMarkers[i].setMap(null)
        this.dongPolygons[i].setMap(null)
      }
      this.dongMarkers = []
      this.dongPolygons = []
    },
    selectArea (center, zoom, areaPolygon, areaMarker) {
      this.$refs.maps.map.setCenter(center)
      this.$refs.maps.map.setZoom(zoom)

      let polygon = areaPolygon.getPath()._array
      let polygons = []

      for (let i = 0; i < polygon.length; i++) {
        polygons.push([polygon[i].y, polygon[i].x])
      }

      this.areaName = areaMarker.title

      this.selectHouseList = []

      const list = this.houseList

      for (let i = 0; i < list.length; i++) {
        let coord = [list[i].lat, list[i].lng]
        if (point(coord, polygons)) {
          this.selectHouseList.push(list[i])
        }
      }
      this.selectHouseLength = this.selectHouseList.length
    },
  }
}
</script>


<style lang="scss">
  .marker-html {
    white-space: nowrap;
    position: relative;
    background: white;
    line-height: 30px;
    text-align: center;
    font-weight: bold;
    border-radius: 15px;
    transition: 0.5s;
    padding: 0 8px;
    box-shadow: rgba(0, 0, 0, 0.08) 0px 0px 0px 1px,
      rgba(0, 0, 0, 0.18) 0px 1px 2px;
    overflow-y: auto;
    transform: translate(-40%, -60%);
    &:hover {
      background: #77ACF1;
      color: white;
      border-color: white;
      z-index: 1;
    }
  }
  .cluster {
    cursor:pointer;
    width:40px;
    height:40px;
    line-height:42px;
    font-size:12px;
    color:white;
    text-align:center;
    font-weight:bold;
    border-radius:20px;
    opacity:0.8;
    border:2px solid;
  }
  .lv1 {
    border-color:#3EDBF0;
    background-color:#3EDBF0;
  }
  .lv2 {
    border-color:#77ACF1;
    background-color:#77ACF1;
  }
  .lv3 {
    border-color:#185ADB;
    background-color:#185ADB;
  }
  .lv4 {
    border-color:#04009A;
    background-color:#04009A;
  }
  .lv5 {
    border-color:#04009A;
    background-color:#04009A;
  }
  .infoWindow {
    background: #F3950D;
    width: 30px;
    height: 30px;
    border-radius: 50%;
    opacity: 0.6;
    animation: scale 0.8s infinite alternate;
  }
  @keyframes scale {
    0% { transform: scale(1); }
    100%  { transform: scale(2.5); opacity: 0.4; }
  }
</style>