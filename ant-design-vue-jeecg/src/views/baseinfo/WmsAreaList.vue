<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="区域名称">
              <a-input placeholder="请输入区域名称" v-model="queryParam.areaName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="区域地点">
              <a-input placeholder="请输入区域地点" v-model="queryParam.areaLoc"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="所属的仓库">
                <!--<a-input placeholder="请选择所属的仓库" v-model="queryParam.warehouseId"></a-input>-->
                <a-select
                  showSearch
                  placeholder="请选择所属的仓库"
                  optionFilterProp="children"
                  :filterOption="filterOption"
                  v-model="queryParam.warehouseId"
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
              <a-form-item label="区域大小">
                <a-input placeholder="请输入区域大小" v-model="queryParam.areaSize"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="区域用途">
                <j-dict-select-tag placeholder="请选择区域用途" v-model="queryParam.areaType" dictCode="area_type"></j-dict-select-tag>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="区域存放类别">
                <!--<a-input placeholder="请输入区域存放类别" v-model="queryParam.areaKind"></a-input>-->
                <a-select
                  showSearch
                  placeholder="请输入区域存放类别"
                  optionFilterProp="children"
                  :filterOption="filterOption"
                  v-model="queryParam.areaKind"
                  :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                  <a-select-option
                    v-for="(item,index) in arrayValueCategory"
                    :key="index"
                    :value="item.value">
                    {{ item.text || item.label }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="区域启停状态">
                <j-dict-select-tag placeholder="请选择区域启停状态" v-model="queryParam.areaState" dictCode="use_type"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="区域管理者">
                <a-input placeholder="请输入区域管理者" v-model="queryParam.areaManager"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="区域编码">
                <a-input placeholder="请输入区域编码" v-model="queryParam.areaCode"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('区域管理')">导出</a-button>
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
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wmsArea-modal ref="modalForm" @ok="modalFormOk"></wmsArea-modal>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import WmsAreaModal from './modules/WmsAreaModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getAction } from "../../api/manage";

  export default {
    name: "WmsAreaList",
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      WmsAreaModal
    },
    data() {
      return {
        description: '区域管理管理页面',
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
            }
          },
          {
            title: '区域名称',
            align: "center",
            dataIndex: 'areaName'
          },
          {
            title: '区域编码',
            align: "center",
            dataIndex: 'areaCode'
          },
          {
            title: '区域地点',
            align: "center",
            dataIndex: 'areaLoc'
          },
          {
            title: '所属的仓库',
            align: "center",
            dataIndex: 'warehouseId',
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
                return flag ? text : "仓库已停用!!!";
                // return filterMultiDictText(this.dictOptions['areaState'], text+"")
              }
            }
          },
          {
            title: '区域大小',
            align: "center",
            dataIndex: 'areaSize'
          },
          {
            title: '区域用途',
            align: "center",
            dataIndex: 'areaType',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['areaType'], text + "")
              }
            }
          },
          {
            title: '区域存放类别',
            align: "center",
            dataIndex: 'areaKind',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                let flag = false;
                this.arrayValueCategory.forEach(function (item) {
                  if (item.value.toString() == text.toString()) {
                    text = item.text;
                    flag = true;
                  }
                })
                return flag ? text : "存放的类别已删除!!!";
              }
            }
          },
          {
            title: '区域启停状态',
            align: "center",
            dataIndex: 'areaState',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['areaState'], text + "")
              }
            }
          },
          {
            title: '区域管理者',
            align: "center",
            dataIndex: 'areaManager'
          },
          {
            title:'上架策略',
            align:"center",
            dataIndex: 'rackingStrategy',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['rackingStrategy'], text + "")
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
            align: "center",
            scopedSlots: {customRender: 'action'}
          }
        ],
        url: {
          list: "/area/wmsArea/list",
          delete: "/area/wmsArea/delete",
          deleteBatch: "/area/wmsArea/deleteBatch",
          exportXlsUrl: "/area/wmsArea/exportXls",
          importExcelUrl: "area/wmsArea/importExcel",
          queryList: "/area/wmsArea/queryList",
          queryCategoryList: "/area/wmsArea/queryCategoryList",
        },
        dictOptions: {
          areaState: [],
          rackingStrategy:[],
        },
        arrayValue: [],
        arrayValueCategory: [],
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig() {
        initDictOptions('use_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'areaState', res.result)
          }
        })
        initDictOptions('area_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'areaType', res.result)
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
    }

    },
    created() {
      getAction(this.url.queryList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.warehouseName, value: item.warehouseId}
              this.arrayValue.push(temp);
            }
          })
        } else {
          conloge.log(res.error);
        }
      })
      getAction(this.url.queryCategoryList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayValueCategory = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.categoryName, value: item.categoryId}
              this.arrayValueCategory.push(temp);
            }
          })
        } else {
          conloge.log(res.error);
        }
      })
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>