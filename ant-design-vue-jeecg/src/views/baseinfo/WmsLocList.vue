<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="货位名称">
              <a-input placeholder="请输入货位名称" v-model="queryParam.locName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="货位地点">
              <a-input placeholder="请输入货位地点" v-model="queryParam.locLoc"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="货位容量">
                <a-input placeholder="请输入货位容量" v-model="queryParam.locVolume"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="所属的区域">
                <!--<a-input placeholder="请输入所属的区域" v-model="queryParam.areaId"></a-input>-->
                <a-select
                  showSearch
                  placeholder="请选择所属的区域"
                  optionFilterProp="children"
                  :filterOption="filterOption"
                  v-model="queryParam.areaId"
                  :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                  <a-select-option
                    v-for="(item,index) in arrayValue"
                    :key="index"
                    :value="item.value">
                    {{ item.text || item.label }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位混放状态">
                <j-dict-select-tag placeholder="请选择货位混放状态" v-model="queryParam.isMix" dictCode="mix_type"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位类型">
                <!--<j-dict-select-tag placeholder="请输入货位类型" v-model="queryParam.locType" dictCode="loc_type"></j-dict-select-tag>-->
                <a-select
                  showSearch
                  placeholder="请选择货位类型"
                  optionFilterProp="children"
                  :filterOption="filterOption"
                  v-model="queryParam.locType"
                  :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                  <a-select-option
                    v-for="(item,index) in arrayCategoryValue"
                    :key="index"
                    :value="item.value">
                    {{ item.text || item.label }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位状态">
                <j-dict-select-tag placeholder="请输入货位状态" v-model="queryParam.locState" dictCode="loc_state"></j-dict-select-tag>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位启停状态">
                <j-dict-select-tag placeholder="请选择货位启停状态" v-model="queryParam.isUse" dictCode="use_type"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位编码">
                <a-input placeholder="请输入货位编码" v-model="queryParam.locCode"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('货位管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button type="primary" @click="handleAddBatch" icon="plus">批量新增</a-button>
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
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
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

    <wmsLoc-modal ref="modalForm" @ok="modalFormOk"></wmsLoc-modal>
    <wms-loc-add-batch-modal ref="addbatchmodalForm" @ok="modalFormOk" @refresh="loadData"></wms-loc-add-batch-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WmsLocModal from './modules/WmsLocModal'
  import WmsLocAddBatchModal from './modules/WmsLocAddBatchModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {getAction} from "../../api/manage";

  export default {
    name: "WmsLocList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      WmsLocModal,
      WmsLocAddBatchModal,
    },
    data () {
      return {
        description: '货位管理管理页面',
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
            }
          },
          {
            title:'货位名称',
            align:"center",
            dataIndex: 'locName'
          },
          {
            title:'货位编码',
            align:"center",
            dataIndex: 'locCode'
          },
          {
            title:'货位地点(左右-列-行)',
            align:"center",
            dataIndex: 'locLoc'
          },
          {
            title:'货位容量',
            align:"center",
            dataIndex: 'locVolume'
          },
          {
            title:'所属的区域',
            align:"center",
            dataIndex: 'areaId',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                let flag = false;
                this.arrayValue.forEach(function (item) {
                  if (item.value.toString() == text.toString()) {
                    text = item.text;
                    flag = true;
                  }
                })
                return flag ? text : "区域已停用!!!";
                // return filterMultiDictText(this.dictOptions['areaState'], text+"")
              }
            }
          },
          {
            title:'货位混放状态',
            align:"center",
            dataIndex: 'isMix',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isMix'], text+"")
              }
            }
          },
          {
            title:'货位类型',
            align:"center",
            dataIndex: 'locType',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                let flag = false;
                this.arrayCategoryValue.forEach(function (item) {
                  if (item.value.toString() == text.toString()) {
                    text = item.text;
                    flag = true;
                  }
                })
                return flag ? text : "区域已停用!!!";
                // return filterMultiDictText(this.dictOptions['areaState'], text+"")
              }
            }
          },
          {
            title:'货位状态',
            align:"center",
            dataIndex: 'locState',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['locState'], text+"")
              }
            }
          },
          {
            title:'货位启停状态',
            align:"center",
            dataIndex: 'isUse',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isUse'], text+"")
              }
            }
          },
          {
            title:'上架策略',
            align:"center",
            dataIndex: 'rackingStrategy',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['rackingStrategy'], text+"")
              }
            }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'memo'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return text.split(" ")[0]
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        arrayValue:[],
        arrayCategoryValue:[],
        url: {
          list: "/loc/wmsLoc/list",
          delete: "/loc/wmsLoc/delete",
          deleteBatch: "/loc/wmsLoc/deleteBatch",
          exportXlsUrl: "/loc/wmsLoc/exportXls",
          importExcelUrl: "loc/wmsLoc/importExcel",
          queryAreaList: "/loc/wmsLoc/queryAreaList",
          queryCategoryList:"/goods/wmsGoods/queryLastCategoryList"
        },
        dictOptions:{
         locType:[],
         locState:[],
         isUse:[],
         isMix:[],
         rackingStrategy: []
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
        initDictOptions('loc_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'locType', res.result)
          }
        })
        initDictOptions('loc_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'locState', res.result)
          }
        })
        initDictOptions('use_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isUse', res.result)
          }
        })
        initDictOptions('mix_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isMix', res.result)
          }
        })
        initDictOptions('al_rk_strategy').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'rackingStrategy', res.result)
          }
        })
      },
      filterOption(input, option) {
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      },
      handleAddBatch(){
        this.$refs.addbatchmodalForm.edit();
        this.$refs.addbatchmodalForm.title = "批量新增";
        this.$refs.addbatchmodalForm.disableSubmit = false;
      }


    },
    created() {
      //获取区域信息
      getAction(this.url.queryAreaList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.areaName, value: item.areaId}
              this.arrayValue.push(temp);
            }
          })
        } else {
          this.$message.warning(res.error);
        }
      })
      getAction(this.url.queryCategoryList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayCategoryValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.categoryName, value: item.categoryId}
              this.arrayCategoryValue.push(temp);
            }
          })
        } else {
          this.$message.warning(res.error);
        }
      })
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>