<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="上架ID">
              <a-input placeholder="请输入上架ID" v-model="queryParam.rackingId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="上架编码">
              <a-input placeholder="请输入上架编码" v-model="queryParam.rackingCode"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="上架状态">
                <j-dict-select-tag placeholder="请选择上架状态" v-model="queryParam.rackingState" dictCode="racking_state"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="来源单号">
                <a-input placeholder="请输入来源单号" v-model="queryParam.sourcedtlId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="区域ID">
                <a-input placeholder="请输入区域ID" v-model="queryParam.areaId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货位ID">
                <a-input placeholder="请输入货位ID" v-model="queryParam.locId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物ID">
                <a-input placeholder="请输入货物ID" v-model="queryParam.goodsId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="16">
              <a-form-item label="入库时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间"
                        class="query-group-cust" v-model="queryParam.stockinTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间"
                        class="query-group-cust" v-model="queryParam.stockinTime_end"></j-date>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <a-button type="primary" icon="download" @click="handleExportXls('上架表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确认吗?" @confirm="() => handleConfirmRack(record)">
                  <a>上架确认</a>
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

    <wmsRacking-modal ref="modalForm" @ok="modalFormOk"></wmsRacking-modal>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import WmsRackingModal from './modules/WmsRackingModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { httpAction } from "../../api/manage";

  export default {
    name: "WmsRackingList",
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      WmsRackingModal
    },
    data() {
      return {
        description: '上架表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            },
            fixed: 'left',
          },
          {
            title: '上架ID',
            align: "center",
            dataIndex: 'rackingId',
            fixed: 'left',
          },
          {
            title: '上架编码',
            align: "center",
            dataIndex: 'rackingCode',
            fixed: 'left',
          },
          {
            title: '上架状态',
            align: "center",
            dataIndex: 'rackingState',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['rackingState'], text + "")
              }
            }
          },
          {
            title: '来源细单ID',
            align: "center",
            dataIndex: 'sourcedtlId'
          },
          {
            title: '区域ID',
            align: "center",
            dataIndex: 'areaId'
          },
          {
            title: '区域编码',
            align: "center",
            dataIndex: 'areaCode'
          },
          {
            title: '区域名称',
            align: "center",
            dataIndex: 'areaName'
          },
          {
            title: '货位ID',
            align: "center",
            dataIndex: 'locId'
          },
          {
            title: '货位编码',
            align: "center",
            dataIndex: 'locCode'
          },
          {
            title: '货位名称',
            align: "center",
            dataIndex: 'locName'
          },
          {
            title: '货物ID',
            align: "center",
            dataIndex: 'goodsId'
          },
          {
            title: '货物编码',
            align: "center",
            dataIndex: 'goodsCode'
          },
          {
            title: '货物名称',
            align: "center",
            dataIndex: 'goodsName'
          },
          {
            title: '货物规格',
            align: "center",
            dataIndex: 'goodsSize'
          },
          {
            title: '货物单位',
            align: "center",
            dataIndex: 'goodsUnit',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['goodsUnit'], text + "")
              }
            }
          },
          {
            title: '货物类别',
            align: "center",
            dataIndex: 'goodsType'
          },
          {
            title: '货物花色',
            align: "center",
            dataIndex: 'goodsColor',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['goodsColor'], text + "")
              }
            }
          },
          {
            title: '货物批号',
            align: "center",
            dataIndex: 'goodsBatchnumber'
          },
          {
            title: '货物数量',
            align: "center",
            dataIndex: 'goodsQuantity'
          },
          {
            title: '货物等级',
            align: "center",
            dataIndex: 'goodsLevel',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['goodsLevel'], text + "")
              }
            }
          },
          {
            title: '入库时间',
            align: "center",
            dataIndex: 'stockinTime'
          },
          {
            title: '创建时间',
            align: "center",
            dataIndex: 'createTime',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
            fixed: 'right',
          }
        ],
        url: {
          list: "/wmsracking/wmsRacking/list",
          delete: "/wmsracking/wmsRacking/delete",
          deleteBatch: "/wmsracking/wmsRacking/deleteBatch",
          exportXlsUrl: "/wmsracking/wmsRacking/exportXls",
          importExcelUrl: "wmsracking/wmsRacking/importExcel",
          confirmRackUrl: "/wmsracking/wmsRacking/confirmRacking",
        },
        dictOptions: {
          rackingState: [],
          goodsUnit: [],
          goodsColor: [],
          goodsLevel: [],
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig() {
        initDictOptions('racking_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'rackingState', res.result)
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
      handleConfirmRack(record) {
        httpAction(this.url.confirmRackUrl, record, 'post').then((res) => {
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