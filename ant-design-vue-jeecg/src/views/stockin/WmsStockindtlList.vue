<template>
  <div>

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
          <a href="javascript:;" @click="handleDetail(record)">详情</a>

          <a-divider type="vertical"/>
        </span>

    </a-table>

    <wmsStockindtl-modal ref="modalForm" @ok="modalFormOk"></wmsStockindtl-modal>
  </div>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import WmsStockindtlModal from './modules/WmsStockindtlModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {getAction} from "../../api/manage";

  export default {
    name: "WmsStockindtlList",
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      WmsStockindtlModal
    },
    data() {
      return {
        description: '入库明细表管理页面',
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
          },
          {
            title: '入库ID',
            align: "center",
            dataIndex: 'stockinId'
          },
          {
            title: '入库编码',
            align: "center",
            dataIndex: 'stockinCode'
          },
          {
            title: '入库明细ID',
            align: "center",
            dataIndex: 'stockindtlId',
          },
          {
            title: '入库明细编码',
            align: "center",
            dataIndex: 'stockindtlCode',
          },
          {
            title: '入库状态',
            align: "center",
            dataIndex: 'stockinState',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['stockinState'], text + "")
              }
            }
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
            dataIndex: 'goodsType',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                let flag = false;
                this.arrayCategoryVal.forEach(function (item) {
                  if (item.value.toString() == text.toString()) {
                    text = item.text;
                    flag = true;
                  }
                })
                return flag ? text : "该货物类别已删除!!!";
                // return filterMultiDictText(this.dictOptions['areaState'], text+"")
              }
            }
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
            title: '创建人',
            align: "center",
            dataIndex: 'createBy'
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
            title: '更新人',
            align: "center",
            dataIndex: 'updateBy'
          },
          {
            title: '更新时间',
            align: "center",
            dataIndex: 'updateTime',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            fixed: "right",
            title: '操作',
            dataIndex: 'action',
            align: "center",
            width: 147,
            scopedSlots: {customRender: 'action'}
          }
        ],
        url: {
          list: "/wmsstockindtl/wmsStockindtl/list",
          delete: "/wmsstockindtl/wmsStockindtl/delete",
          deleteBatch: "/wmsstockindtl/wmsStockindtl/deleteBatch",
          exportXlsUrl: "/wmsstockindtl/wmsStockindtl/exportXls",
          importExcelUrl: "wmsstockindtl/wmsStockindtl/importExcel",
          queryCategoryList: "/goods/wmsGoods/queryLastCategoryList",
        },
        dictOptions: {
          stockinState: [],
          goodsUnit: [],
          goodsColor: [],
          goodsLevel: [],
        },
        tableScroll: {x: 21 * 147},
        arrayCategoryVal: [],
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    created() {
      getAction(this.url.queryCategoryList).then((res) => {
        if (res.success) {
          res.result.list.filter(item => {
            if (item.id != "") {
              this.arrayCategoryVal.push({text: item.categoryName, value: item.categoryId });
            }
          })
        }
      });
    },
    methods: {
      initDictConfig() {
        initDictOptions('stockin_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockinState', res.result)
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
      loadData(arg) {
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();
        //update-begin--Author:kangxiaolin  Date:20190905 for：[442]主子表分开维护，生成的代码子表的分页改为真实的分页--------------------
        getAction(this.url.list, {
          stockinId: params.stockinId, pageNo: this.ipagination.current,
          pageSize: this.ipagination.pageSize
        }).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          } else {
            this.dataSource = null;
          }
        })
        //update-end--Author:kangxiaolin  Date:20190905 for：[442]主子表分开维护，生成的代码子表的分页改为真实的分页--------------------
      },
      getOrderByStockinId(stockinId) {
        this.queryParam.stockinId = stockinId;
        this.loadData(1);
      },
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>