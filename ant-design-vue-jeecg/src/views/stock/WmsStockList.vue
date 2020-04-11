<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="库存状态">
              <j-dict-select-tag placeholder="请选择库存状态" v-model="queryParam.stockState" dictCode="stock_state"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="货物名称">
              <a-input placeholder="请输入货物名称" v-model="queryParam.goodsName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="货物类别">
                <j-dict-select-tag placeholder="请选择货物类别" v-model="queryParam.goodsType" dictCode="goods_type"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物批号">
                <a-input placeholder="请输入货物批号" v-model="queryParam.goodsBatchnumber"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物等级">
                <j-dict-select-tag placeholder="请选择货物等级" v-model="queryParam.goodsLevel" dictCode="goods_level"/>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="16">
              <a-form-item label="入库时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.stockinTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.stockinTime_end"></j-date>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('库存表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        :scroll="{x:'max-content'}"
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="ant-table"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wmsStock-modal ref="modalForm" @ok="modalFormOk"></wmsStock-modal>
  </a-card>
</template>
<style>
  .ant-table-thead > tr > th, .ant-table-tbody > tr{
    padding: 16px 16px;
    word-break: break-word;
    -ms-word-break: break-all;
  }
  .ant-table td {
    white-space: nowrap;}
</style>
<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WmsStockModal from './modules/WmsStockModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "WmsStockList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      WmsStockModal,
    },
    data () {
      return {
        description: '库存表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            },
            fixed: "left",
          },
          {
            title:'库存ID',
            align:"center",
            dataIndex: 'stockId',
            fixed: "left",
          },
          {
            title:'库存状态',
            align:"center",
            dataIndex: 'stockState',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['stockState'], text+"")
              }
            }
          },
          {
            title:'区域ID',
            align:"center",
            dataIndex: 'areaId',
            // customRender:(text)=>{
            //   if(!text){
            //     return ''
            //   }else{
            //     return filterMultiDictText(this.dictOptions['areaId'], text+"")
            //   }
            // }
          },
          {
            title:'区域编码',
            align:"center",
            dataIndex: 'areaCode'
          },
          {
            title:'区域名称',
            align:"center",
            dataIndex: 'areaName'
          },
          {
            title:'货位ID',
            align:"center",
            dataIndex: 'locId',
            // customRender:(text)=>{
            //   if(!text){
            //     return ''
            //   }else{
            //     return filterMultiDictText(this.dictOptions['locId'], text+"")
            //   }
            // }
          },
          {
            title:'货位编码',
            align:"center",
            dataIndex: 'locCode'
          },
          {
            title:'货位名称',
            align:"center",
            dataIndex: 'locName'
          },
          {
            title:'货物ID',
            align:"center",
            dataIndex: 'goodsId',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['goodsId'], text+"")
              }
            }
          },
          {
            title:'货物编码',
            align:"center",
            dataIndex: 'goodsCode'
          },
          {
            title:'货物名称',
            align:"center",
            dataIndex: 'goodsName'
          },
          {
            title:'货物单位',
            align:"center",
            dataIndex: 'goodsUnit',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['goodsUnit'], text+"")
              }
            }
          },
          {
            title:'货物类别',
            align:"center",
            dataIndex: 'goodsType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['goodsType'], text+"")
              }
            }
          },
          {
            title:'货物花色',
            align:"center",
            dataIndex: 'goodsColor',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['goodsColor'], text+"")
              }
            }
          },
          {
            title:'货物批号',
            align:"center",
            dataIndex: 'goodsBatchnumber'
          },
          {
            title:'货物数量',
            align:"center",
            dataIndex: 'goodsQuantity'
          },
          {
            title:'货物等级',
            align:"center",
            dataIndex: 'goodsLevel',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['goodsLevel'], text+"")
              }
            }
          },
          {
            title:'入库时间',
            align:"center",
            dataIndex: 'stockinTime'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
            fixed: "right",
          }
        ],
        url: {
          list: "/stock/wmsStock/list",
          delete: "/stock/wmsStock/delete",
          deleteBatch: "/stock/wmsStock/deleteBatch",
          exportXlsUrl: "/stock/wmsStock/exportXls",
          importExcelUrl: "stock/wmsStock/importExcel",
        },
        dictOptions:{
         stockState:[],
         areaId:[],
         locId:[],
         goodsId:[],
         goodsUnit:[],
         goodsType:[],
         goodsColor:[],
         goodsLevel:[],
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        initDictOptions('stock_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockState', res.result)
          }
        })
        initDictOptions('').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'areaId', res.result)
          }
        })
        // initDictOptions('').then((res) => {
        //   if (res.success) {
        //     this.$set(this.dictOptions, 'locId', res.result)
        //   }
        // })
        initDictOptions('').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsId', res.result)
          }
        })
        initDictOptions('goods_unit').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsUnit', res.result)
          }
        })
        initDictOptions('goods_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsType', res.result)
          }
        })
        initDictOptions('color').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsColor', res.result)
          }
        })
        initDictOptions('goods_level').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsLevel', res.result)
          }
        })
      },

       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>