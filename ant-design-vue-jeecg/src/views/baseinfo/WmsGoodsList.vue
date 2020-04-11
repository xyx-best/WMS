<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="货物名称">
              <a-input placeholder="请输入货物名称" v-model="queryParam.goodsName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="货物规格">
              <a-input placeholder="请输入货物规格" v-model="queryParam.goodsSize"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <!--<a-form-item label="货物类别">-->
                <!--<a-input placeholder="请输入货物类别" v-model="queryParam.goodsType" dictCode="goods_type"></a-input>-->
              <!--</a-form-item>-->
              <a-form-item label="货物类别">
                <!--<a-input placeholder="请输入货物类别" v-model="queryParam.goodsType"></a-input>-->
                <a-select
                  showSearch
                  placeholder="请选择货物类别"
                  optionFilterProp="children"
                  :filterOption="filterOption"
                  v-model="queryParam.goodsType"
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
              <a-form-item label="货物单位">
                <j-dict-select-tag placeholder="请选择货物单位" v-model="queryParam.goodsUnit" dictCode="goods_unit"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物启停状态">
                <j-dict-select-tag placeholder="请选择货物启停状态" v-model="queryParam.isUse" dictCode="use_type"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物花色">
                <j-dict-select-tag placeholder="请输入货物花色" v-model="queryParam.goodsColor" dictCode="color"></j-dict-select-tag>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="货物编码">
                <a-input placeholder="请输入货物编码" v-model="queryParam.goodsCode"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('货物管理')">导出</a-button>
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

    <wmsGoods-modal ref="modalForm" @ok="modalFormOk"></wmsGoods-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WmsGoodsModal from './modules/WmsGoodsModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getAction } from "../../api/manage";

  export default {
    name: "WmsGoodsList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      WmsGoodsModal
    },
    data () {
      return {
        description: '货物管理管理页面',
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
            title:'货物名称',
            align:"center",
            dataIndex: 'goodsName'
          },
          {
            title:'货物编码',
            align:"center",
            dataIndex: 'goodsCode'
          },
          {
            title:'货物规格',
            align:"center",
            dataIndex: 'goodsSize'
          },
          // {
          //   title:'货物类别',
          //   align:"center",
          //   dataIndex: 'goodsType',
          //   customRender:(text)=>{
          //     if(!text){
          //       return ''
          //     }else{
          //       return filterMultiDictText(this.dictOptions['goodsType'], text+"")
          //     }
          //   }
          // },
          {
            title:'货物类别',
            align:"center",
            dataIndex: 'goodsType',
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
                return flag ? text : "该货物类别已删除!!!";
                // return filterMultiDictText(this.dictOptions['areaState'], text+"")
              }
            }
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
            title:'货物启停状态',
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
        url: {
          list: "/goods/wmsGoods/list",
          delete: "/goods/wmsGoods/delete",
          deleteBatch: "/goods/wmsGoods/deleteBatch",
          exportXlsUrl: "/goods/wmsGoods/exportXls",
          importExcelUrl: "goods/wmsGoods/importExcel",
          queryLastCategoryList: "/goods/wmsGoods/queryLastCategoryList",
        },
        dictOptions:{
         // goodsType:[],
         isUse:[],
         goodsColor:[],
         goodsUnit:[],
         rackingStrategy:[]
        },
        arrayValue:[],
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        initDictOptions('goods_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsType', res.result)
          }
        })
        initDictOptions('use_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isUse', res.result)
          }
        })
        initDictOptions('color').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsColor', res.result)
          }
        })
        initDictOptions('goods_unit').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'goodsUnit', res.result)
          }
        })
        initDictOptions('racking_strategy').then((res) => {
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
        getAction(this.url.queryLastCategoryList).then((res) => {
          if (res.success) {
            let temp = {};
            this.arrayValue = [];
            res.result.list.filter(item => {
              if (item.id != "") {
                temp = {text: item.categoryName, value: item.categoryId}
                this.arrayValue.push(temp);
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