<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="最近7天总入库量" :total="totalStockin | NumberFormat">
          <a-tooltip title="入库趋势" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" :dataSource="miniStockinData"/>
          </div>
          <template slot="footer">今日入库量 &nbsp   <span> {{ todayStockin | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="最近7天总出库量" :total="totalStockout | NumberFormat">
          <a-tooltip title="出库趋势" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" :dataSource="miniStockoutData"/>
          </div>
          <template slot="footer">今日出库量 &nbsp<span>{{ todayStockout | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a @click="getStockInfoD('D')">本日</a>
              <a @click="getStockInfoM('M')">本月</a>
              <a @click="getStockInfoY('Y')">本年</a>
            </div>
            <a-range-picker showToday :style="{width: '256px'}" @change="onOk">
              <!--<template slot="renderExtraFooter">-->
                <!--extra footer-->
              <!--</template>-->
            </a-range-picker>
          </div>
          <a-tab-pane loading="true" tab="库存量" key="1">
            <a-row>
              <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
                <bar title="库存量" :dataSource="barData"/>
              </a-col>
              <!--<a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">-->
                <!--<rank-list title="区域利用率排行" :list="rankList"/>-->
              <!--</a-col>-->
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

  </div>
</template>

<script>
  import ChartCard from '@/components/ChartCard'
  import ACol from "ant-design-vue/es/grid/Col"
  import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
  import MiniArea from '@/components/chart/MiniArea'
  import MiniBar from '@/components/chart/MiniBar'
  import MiniProgress from '@/components/chart/MiniProgress'
  import RankList from '@/components/chart/RankList'
  import Bar from '@/components/chart/Bar'
  import LineChartMultid from '@/components/chart/LineChartMultid'
  import HeadInfo from '@/components/tools/HeadInfo.vue'
  import moment from 'dayjs'
  import { getAction } from "../../api/manage";

  import Trend from '@/components/Trend'
  import { getLoginfo,getVisitInfo } from '@/api/api'

  const rankList = []
  for (let i = 0; i < 7; i++) {
    rankList.push({
      name: '白鹭岛 ' + (i+1) + ' 号店',
      total: 1234.56 - i * 100
    })
  }
  export default {
    name: "Analysis",
    components: {
      ATooltip,
      ACol,
      ChartCard,
      MiniArea,
      MiniBar,
      MiniProgress,
      RankList,
      Bar,
      Trend,
      LineChartMultid,
      HeadInfo
    },
    data() {
      return {
        loading: true,
        center: null,
        rankList,
        barData: [],
        loginfo:{},
        visitFields:['ip','visit'],
        visitInfo:[],
        indicator: '<a-icon type="loading" style="font-size: 66px" spin />',
        totalStockin: 0,
        totalStockout: 0,
        todayStockin: 0,
        todayStockout: 0,
        miniStockinData: [],
        miniStockoutData: [],
        url:{
          getStockUrl: "/stock/wmsStock/getStockInfoByTime",
          getStockinUrl: "/wmsstockindtl/wmsStockindtl/getStockinQuantityLastDays",
          getStockoutUrl: "/wmsstockoutdtl/wmsStockoutdtl/getStockoutQuantityLastDays",
        }
      }
    },
    created() {
      setTimeout(() => {
        this.loading = !this.loading
      }, 1000)
      this.getStockinoutInfo();
      this.getStockInfoY();
    },
    // mounted() {
    //   setInterval(this.getStockinoutInfo, 5000);
    // },
    methods: {
      getStockInfoY(type="Y"){
        const param = { "type" : type}
        getAction(this.url.getStockUrl, param).then((res) => {
          if (res.success) {
            this.barData = [];
            for (let i = 0; i < 12; i += 1) {
              this.barData.push({
                x: `${i + 1}月`,
                y: 0
              })
            }
            res.result.filter((item,index) => {
              const month = parseInt(item.time) + '月';
              let ind = this.barData.findIndex(i => i.x ==  month); //根据 已知id（this.x） 获取在数组中的位置(index)；
              if (ind != -1){
                this.barData.splice(ind, 1, { x: month, y: item.quantity}); //从数据组移出当前数据；
              } else {
                this.barData.push({
                  x: month,
                  y: item.quantity
                })
              }
            })
          }
        });
      },
      getStockInfoM(type="M"){
        const param = { "type" : type}
        let date = new Date();
        date.setMonth(date.getMonth() + 1);
        date.setDate(0);
        let day = date.getDate();   //获取当月的天数
        getAction(this.url.getStockUrl, param).then((res) => {
          if (res.success) {
            this.barData = [];
            date.setMonth(date.getMonth() - 1);
            for (let i = 0; i < day; i += 1) {
              this.barData.push({
                x: moment(new Date(date.getTime() + 1000 * 60 * 60 * 24 * i)).format('MM-DD'),
                y: 0
              })
            }
            res.result.filter((item) => {
              let index = this.barData.findIndex(i => i.x == item.time); //根据 已知id（this.x） 获取在数组中的位置(index)；
              if (index != -1) {
                this.barData.splice(index, 1, { x: item.time, y: item.quantity}); //从数据组移出当前数据；
              }
              else {
                this.barData.push({
                  x: item.time,
                  y: item.quantity
                })
              }
            })
          }
        });
      },
      getStockInfoD(type="D"){
        const param = { "type" : type}
        getAction(this.url.getStockUrl, param).then((res) => {
          if (res.success) {
            this.barData = [];
            let date = new Date();
            date.setHours(0, 0, 0, 0)
            for (let i = 0; i < 24; i += 1) {
              this.barData.push({
                x: moment(new Date(date.getTime() + 1000 * 60 * 60 * i)).format('MM-DD HH:00'),
                y: 0
              })
            }
            res.result.filter((item) => {
              let index = this.barData.findIndex(i => i.x == item.time ); //根据 已知id（this.x） 获取在数组中的位置(index)；
              if (index != -1) {
                this.barData.splice(index, 1, { x: item.time, y: item.quantity}); //从数据组移出当前数据；
              }
              else {
                this.barData.push({
                  x: item.time,
                  y: item.quantity
                })
              }
            })
          }
        });
      },
      getStockinoutInfo(){
        const beginDay = moment(new Date()).format('MM-DD')
        getAction(this.url.getStockinUrl).then((res) => {
          if (res.success) {
            this.miniStockinData = [];
            this.totalStockin = 0;
            for (let i = -6; i < 1 ; i++) {
              this.miniStockinData.push({
                x: moment(new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * i)).format('MM-DD'),
                y: 0
              })
            }
            res.result.filter(item => {
              let index = this.miniStockinData.findIndex(i => i.x == item.date); //根据 已知id（this.x） 获取在数组中的位置(index)；
              if (index != -1) {
                this.miniStockinData.splice(index, 1, { x: item.date, y: item.quantity}); //从数据组移出当前数据；
                this.totalStockin+=item.quantity;
                if (item.date == beginDay){
                  this.todayStockin = item.quantity
                }
              }
            })
          }
        });
        getAction(this.url.getStockoutUrl).then((res) => {
          if (res.success) {
            this.miniStockoutData = [];
            this.totalStockout = 0;
            for (let i = -6; i < 1; i++) {
              this.miniStockoutData.push({
                x: moment(new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * i)).format('MM-DD'),
                y: 0
              })
            }
            res.result.filter(item => {
              let index = this.miniStockoutData.findIndex(i => i.x == item.date); //根据 已知id（this.x） 获取在数组中的位置(index)；
              if (index != -1) {
                this.miniStockoutData.splice(index, 1, { x: item.date, y: item.quantity}); //从数据组移出当前数据；
                this.totalStockout+=item.quantity;
                if (item.date == beginDay){
                  this.todayStockout = item.quantity
                }
              }
            });
          }
        })
      },
      onOk(date, dateString){
        const param = { "dateRange" : "R", "start" : dateString[0], "end" : dateString[1]}

        let dd = ((date[1]._d - date[0]._d)/1000/60/60/24) + 1

        const da = new Date(dateString[0]);
        const n= new Date()
        getAction(this.url.getStockUrl, param).then((res) => {
          if (res.success) {
            this.barData = [];
            for (let i = 0; i < dd; i+=1) {
              this.barData.push({
                x: moment(new Date(da.getTime() + 1000 * 60 * 60 * 24 * i)).format('MM-DD'),
                y: 0
              })
            }
            res.result.filter(item => {
              let index = this.barData.findIndex(i => i.x == item.time ); //根据 已知id（this.x） 获取在数组中的位置(index)；
              if (index != -1) {
                this.barData.splice(index, 1, { x: item.time, y: item.quantity}); //从数据组移出当前数据；
              }
              else {
                this.barData.push({
                  x: item.time,
                  y: item.quantity
                })
              }
            });
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .circle-cust{
    position: relative;
    top: 28px;
    left: -100%;
  }
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  /* 首页访问量统计 */
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      color: rgba(0, 0, 0, .45);
      display: inline-block;
      font-size: .95rem;
      line-height: 42px;
      margin-bottom: 4px;
    }
    p {
      line-height: 42px;
      margin: 0;
      a {
        font-weight: 600;
        font-size: 1rem;
      }
    }
  }
</style>