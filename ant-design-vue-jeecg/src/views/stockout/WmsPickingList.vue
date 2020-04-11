<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="拣选ID">
              <a-input placeholder="请输入拣选ID" v-model="queryParam.pickingId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="来源细单ID">
              <a-input placeholder="请输入来源细单ID" v-model="queryParam.sourcedtlId"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="拣选状态">
                <j-dict-select-tag placeholder="请选择拣选状态" v-model="queryParam.pickingState" dictCode="picking_state"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位编码">
                <a-input placeholder="请输入货位编码" v-model="queryParam.locCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物ID">
                <a-input placeholder="请输入货物ID" v-model="queryParam.goodsId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="16">
              <a-form-item label="出库时间">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.stockoutTime_begin"></a-input>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.stockoutTime_end"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('拣选表')">导出</a-button>
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="tableScroll"
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
                <a-popconfirm title="确认吗?" @confirm="() => handleConfirmPick(record)">
                  <a>拣选确认</a>
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

    <wmsPicking-modal ref="modalForm" @ok="modalFormOk"></wmsPicking-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WmsPickingModal from './modules/WmsPickingModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { httpAction } from "../../api/manage";

  export default {
    name: "WmsPickingList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      WmsPickingModal
    },
    data () {
      return {
        description: '拣选表管理页面',
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
            title:'拣选ID',
            align:"center",
            dataIndex: 'pickingId'
          },
          {
            title:'拣选编码',
            align:"center",
            dataIndex: 'pickingCode'
          },
          {
            title:'来源细单ID',
            align:"center",
            dataIndex: 'sourcedtlId'
          },
          {
            title:'拣选状态',
            align:"center",
            dataIndex: 'pickingState',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['pickingState'], text+"")
              }
            }
          },
          {
            title:'区域ID',
            align:"center",
            dataIndex: 'areaId'
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
            dataIndex: 'locId'
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
            dataIndex: 'goodsId'
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
            title:'货物规格',
            align:"center",
            dataIndex: 'goodsSize'
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
            dataIndex: 'goodsType'
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
            title:'出库时间',
            align:"center",
            dataIndex: 'stockoutTime'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/wmspicking/wmsPicking/list",
          delete: "/wmspicking/wmsPicking/delete",
          deleteBatch: "/wmspicking/wmsPicking/deleteBatch",
          exportXlsUrl: "/wmspicking/wmsPicking/exportXls",
          importExcelUrl: "wmspicking/wmsPicking/importExcel",
          confirmPickUrl: "/wmspicking/wmsPicking/confirmPicking",
        },
        dictOptions:{
         pickingState:[],
         goodsUnit:[],
         goodsColor:[],
         goodsLevel:[],
        },
        tableScroll:{x :22*147+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        initDictOptions('picking_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'pickingState', res.result)
          }
        })
        initDictOptions('goods_unit').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsUnit', res.result)
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
      handleConfirmPick(record) {
        httpAction(this.url.confirmPickUrl, record, 'post').then((res) => {
          if(res.success){
            this.$message.success(res.message);
            this.loadData();
          }else{
            this.$message.warning(res.message);
          }
        })
        console.log(record);
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>