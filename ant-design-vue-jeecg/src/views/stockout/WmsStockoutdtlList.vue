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

    <wmsStockoutdtl-modal ref="modalForm" @ok="modalFormOk"></wmsStockoutdtl-modal>
  </div>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {getAction} from "../../api/manage";
  import WmsStockoutdtlModal from './modules/WmsStockoutdtlModal'

  export default {
    name: "WmsstockoutdtlList",
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      WmsStockoutdtlModal
    },
    data() {
      return {
        description: '出库明细表管理页面',
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
            title: '出库ID',
            align: "center",
            width:150,
            dataIndex: 'stockoutId'
          },
          {
            title: '出库编码',
            align: "center",
            width:150,
            dataIndex: 'stockoutCode'
          },
          {
            title: '出库明细ID',
            align: "center",
            width:150,
            dataIndex: 'stockoutdtlId',
          },
          {
            title: '出库明细编码',
            align: "center",
            width:150,
            dataIndex: 'stockoutdtlCode',
          },
          {
            title: '出库状态',
            align: "center",
            width:150,
            dataIndex: 'stockoutState',
            customRender: (text) => {
              if (!text) {
                return ''
              } else {
                return filterMultiDictText(this.dictOptions['stockoutState'], text + "")
              }
            }
          },
          {
            title: '货物ID',
            align: "center",
            width:150,
            dataIndex: 'goodsId'
          },
          {
            title: '货物编码',
            align: "center",
            width:150,
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
            width:150,
            dataIndex: 'goodsSize'
          },
          {
            title: '货物单位',
            align: "center",
            width:150,
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
            width:150,
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
            width:100,
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
            width:150,
            dataIndex: 'goodsBatchnumber'
          },
          {
            title: '货物数量',
            align: "center",
            width:100,
            dataIndex: 'goodsQuantity'
          },
          {
            title: '货物等级',
            align: "center",
            width:150,
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
            width:120,
            dataIndex: 'createBy'
          },
          {
            title: '创建时间',
            align: "center",
            width:150,
            dataIndex: 'createTime',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '更新人',
            align: "center",
            width:120,
            dataIndex: 'updateBy'
          },
          {
            title: '更新时间',
            align: "center",
            width:150,
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
          list: "/wmsstockoutdtl/wmsStockoutdtl/list",
          delete: "/wmsstockoutdtl/wmsStockoutdtl/delete",
          deleteBatch: "/wmsstockoutdtl/wmsStockoutdtl/deleteBatch",
          exportXlsUrl: "/wmsstockoutdtl/wmsStockoutdtl/exportXls",
          importExcelUrl: "wmsstockoutdtl/wmsStockoutdtl/importExcel",
          queryCategoryList: "/goods/wmsGoods/queryLastCategoryList",
        },
        dictOptions: {
          stockoutState: [],
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
        initDictOptions('stockout_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'stockoutState', res.result)
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
          stockoutId: params.stockoutId, pageNo: this.ipagination.current,
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
      getOrderBystockoutId(stockoutId) {
        this.queryParam.stockoutId = stockoutId;
        this.loadData(1);
      },
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>