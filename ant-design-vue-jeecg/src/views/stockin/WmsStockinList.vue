<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="入库id">
              <a-input placeholder="请输入入库id" v-model="queryParam.stockinId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="入库编码">
              <a-input placeholder="请输入入库编码" v-model="queryParam.stockinCode"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="入库类型">
                <j-dict-select-tag placeholder="请选择入库类型" v-model="queryParam.stockinType" dictCode="stockin_type"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="入库来源">
                <j-dict-select-tag placeholder="请输入入库来源" v-model="queryParam.stockinSource" dictCode="stockin_source"></j-dict-select-tag>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="表单状态">
                <j-dict-select-tag placeholder="请选择表单状态" v-model="queryParam.stockinState" dictCode="stockin_state"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="创建人">
                <a-input placeholder="请输入创建人" v-model="queryParam.createBy"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="16">
              <a-form-item label="创建日期">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
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
      <a-button type="primary" icon="download" @click="handleExportXls('入库表')">导出</a-button>
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
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: type}"
        @change="handleTableChange"
        :customRow="clickThenCheck">

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
                <a-popconfirm title="确定生成吗?" @confirm="() => handleCreateRecord(record)">
                  <a>生成上架和库存记录</a>
                </a-popconfirm>
              </a-menu-item>
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

    <div>
      <Wms-Stockindtl-List ref="StockinDetail"></Wms-Stockindtl-List>
    </div>

    <wmsStockin-modal ref="modalForm" @ok="modalFormOk" @refresh="loadData1"></wmsStockin-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WmsStockinModal from './modules/WmsStockinModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { httpAction } from '@/api/manage'
  import WmsStockindtlList from './WmsStockindtlList'

  export default {
    name: "WmsStockinList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      WmsStockinModal,
      WmsStockindtlList
    },
    data () {
      return {
        description: '入库表管理页面',
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
            title:'入库ID',
            align:"center",
            dataIndex: 'stockinId'
          },
          {
            title:'入库编码',
            align:"center",
            dataIndex: 'stockinCode'
          },
          {
            title:'入库类型',
            align:"center",
            dataIndex: 'stockinType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['stockinType'], text+"")
              }
            }
          },
          {
            title:'入库来源',
            align:"center",
            dataIndex: 'stockinSource',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['stockinSource'], text+"")
              }
            }
          },
          {
            title:'表单状态',
            align:"center",
            dataIndex: 'stockinState',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['stockinState'], text+"")
              }
            }
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
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
            scopedSlots: { customRender: 'action' }
          }
        ],
        type: "radio",
        url: {
          list: "/wmsStockin/wmsStockin/list",
          delete: "/wmsStockin/wmsStockin/delete",
          deleteBatch: "/wmsStockin/wmsStockin/deleteBatch",
          exportXlsUrl: "/wmsStockin/wmsStockin/exportXls",
          importExcelUrl: "wmsStockin/wmsStockin/importExcel",
          createRecordUrl: "/wmsStockin/wmsStockin/createRecord",
        },
        dictOptions:{
         stockinType:[],
         stockinSource:[],
         stockinState:[],
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
        initDictOptions('stockin_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockinType', res.result)
          }
        })
        initDictOptions('stockin_source').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockinSource', res.result)
          }
        })
        initDictOptions('stockin_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockinState', res.result)
          }
          console.log(this.dictOptions)
        })
      },
      handleCreateRecord(record){
        httpAction(this.url.createRecordUrl,record,'post').then((res)=>{
          if(res.success){
            this.$message.success(res.message);
          }else{
            this.$message.warning(res.message);
          }
        })
      },
      clickThenCheck(record) {
        return {
          on: {
            click: () => {
              this.onSelectChange(record.id.split(","), [record]);
            }
          }
        };
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        this.$refs.StockinDetail.getOrderByStockinId(this.selectionRows[0].stockinId);
      },
      loadData1(){
        this.loadData();
        this.$refs.StockinDetail.loadData();
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>