<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域   即    出库总表 表单 -->
      <a-form :form="form">

        <a-form-item label="出库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['stockoutType']" :trigger-change="true" dictCode="stockout_type" placeholder="请选择出库类型"/>
        </a-form-item>
        <a-form-item label="出库状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['stockoutState']" :trigger-change="true" dictCode="stockout_state" placeholder="请选择出库状态"/>
        </a-form-item>

      </a-form>

      <a-tabs v-show="isShow">
        <a-tab-pane tab="出库明细">

          <j-editable-table
            ref="stockoutdtlEdit"
            :loading="table1.loading"
            :columns="table1.columns"
            :dataSource="table1.dataSource"
            :disabledRows="{stockoutState:['1','2','3']}"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"
            @valueChange="handleValueChange"/>

        </a-tab-pane>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import InputElement from "ant-design-vue/es/auto-complete/InputElement"
  import {FormTypes, VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables} from '@/utils/JEditableTableUtil'


  export default {
    name: "WmsStockoutModal",
    components: {
      InputElement,
      JDictSelectTag,
      JEditableTable,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
        stockoutType:{rules: [{ required: true, message: '请输入出库类型!' }]},
        stockoutState:{},
        },
        url: {
          add: "/wmsstockout/wmsStockout/add",
          edit: "/wmsstockout/wmsStockout/edit",
          stockoutDetailList: "/wmsstockoutdtl/wmsStockoutdtl/list",
          queryGoodsList: "/goods/wmsGoods/queryList",
          queryCategoryList: "/goods/wmsGoods/queryLastCategoryList",
        },
        // 明细信息
        table1: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '入库状态',
              key: 'stockoutState',
              type: FormTypes.select,
              defaultValue: "0",
              options: [{text: "abc", value: "abc"}],
              dictCode: "stockout_state",
              disabled: true,
            },
            {
              title: '货物ID',
              key: 'goodsId',
              width: '150px',
              type: FormTypes.select,
              allowInput: true,
              allowSearch: true,
              // type: FormTypes.sel_search,
              options:[]
            },
            {
              title: '货物编码',
              align: "center",
              key: 'goodsCode',
              width: '150px',
              type: FormTypes.select,
              allowInput: true,
              allowSearch: true,
              // type: FormTypes.sel_search,
              options:[]
            },
            {
              title: '货物名称',
              align: "center",
              key: 'goodsName',
              width: '150px',
              type: FormTypes.select,
              allowInput: true,
              allowSearch: true,
              // type: FormTypes.sel_search,
              options:[]
            },
            {
              title: '货物规格',
              align: "center",
              key: 'goodsSize',
              type: FormTypes.input,
            },
            {
              title: '货物单位',
              align: "center",
              key: 'goodsUnit',
              type: FormTypes.select,
              options: [],
              dictCode: "goods_unit"
            },
            {
              title: '货物类别',
              align: "center",
              key: 'goodsType',
              width: '150px',
              // type: FormTypes.sel_search,
              type: FormTypes.select,
              allowInput: true,
              allowSearch: true,
              options:[]
            },
            {
              title: '货物花色',
              align: "center",
              key: 'goodsColor',
              options: [],
              dictCode: "color",
              type: FormTypes.select,
              allowInput: true,
              allowSearch: true,
            },
            {
              title: '货物批号',
              align: "center",
              key: 'goodsBatchnumber',
              width: '150px',
              type: FormTypes.input
            },
            {
              title: '货物数量',
              align: "center",
              key: 'goodsQuantity',
              type: FormTypes.inputNumber
            },
            {
              title: '货物等级',
              align: "center",
              key: 'goodsLevel',
              options: [],
              dictCode: "goods_level",
              type: FormTypes.select,
            },
            {
              title: '创建人',
              align: "center",
              key: 'createBy'
            },
            {
              title: '创建时间',
              align: "center",
              key: 'createTime',
            },
            {
              title: '更新人',
              align: "center",
              key: 'updateBy'
            },
            {
              title: '更新时间',
              align: "center",
              key: 'updateTime',
            },
          ]
        },
        isShow: true,
        arrayGoodsValue: [],
        arrayGoodsIdVal: [],
        arrayGoodsCodeVal: [],
        arrayGoodsNameVal: [],
        arrayCategoryVal: [],
        disSelect:false
      }
    },
    created() {
      getAction(this.url.queryGoodsList).then((res) => {
        if (res.success) {
          this.arrayGoodsValue = res.result.list;
          res.result.list.filter(item => {
            if (item.id != "") {
              this.arrayGoodsIdVal.push({text: item.goodsId, value: item.goodsId });
              this.arrayGoodsCodeVal.push({text: item.goodsCode, value: item.goodsCode});
              this.arrayGoodsNameVal.push({text: item.goodsName, value: item.goodsName});
            }
          })
          this.table1.columns[1].options = this.arrayGoodsIdVal
          this.table1.columns[2].options = this.arrayGoodsCodeVal
          this.table1.columns[3].options = this.arrayGoodsNameVal
        }
      });
      getAction(this.url.queryCategoryList).then((res) => {
        if (res.success) {
          res.result.list.filter(item => {
            if (item.id != "") {
              this.arrayCategoryVal.push({ text: item.categoryName, value: item.categoryId });
            }
          })
          this.table1.columns[6].options = this.arrayCategoryVal
        }
      });
    },
    methods: {
      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'stockoutdtlEdit'),
        ])
      },
      add () {
        this.edit({});
      },
      edit (record) {
        if (Object.keys(record).length == 0) {
          this.isShow = false
        } else {
          this.isShow = true
        }

        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'stockoutType','stockoutState'))
        })

        //加载子表数据
        if (this.model.stockoutId) {
          let params = {stockoutId: this.model.stockoutId}
          this.requestTableData(this.url.stockoutDetailList, params, this.table1)
        }
      },
      close () {
        this.visible = false
        this.getAllTable().then(editableTables => {
          editableTables[0].initialize()
        })
        this.$emit('close');
        this.$emit('refresh');
        this.visible = false;
      },
      /** 查询某个tab的数据 */
      requestTableData(url, params, tab) {
        tab.loading = true
        getAction(url, params).then(res => {
          tab.dataSource = res.result.records || []
        }).finally(() => {
          tab.loading = false
        })
      },
      handleOk () {
        this.validateFields();
      },
      /** 触发表单验证 */
      validateFields() {
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          return this.requestAddOrEdit(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : (e.index + 1).toString()
          } else {
            console.error(e)
          }
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          // wmsStockindtlList: this.$refs.stockindtlEdit.getFormData(),
          wmsStockoutdtlList: allValues.tablesValue[0].values,
        }
      },
      /** 发起新增或修改的请求 */
      requestAddOrEdit(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        this.confirmLoading = true
        httpAction(url, formData, method).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok')
            this.close()
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'stockoutType','stockoutState'))
      },
      handleInTypeChange(e) {
        if (e == '0') {
          this.disSelect=true
          this.$nextTick(() => {
            this.form.setFieldsValue({ 'stockoutSource':"0" })
          })
        } else{
          this.disSelect=false
        }
      },
      /** 当选项被改变时，联动其他组件 */
      handleValueChange(event) {
        const {type, row, column, value, target} = event
        let temp={}
        let flag = false
        //修改的列
        if (column.key === 'goodsId') {
          this.arrayGoodsValue.forEach(function (c) {
            if (c.goodsId == value) {
              temp = c
              flag=true
            }
          })
        }else if (column.key === 'goodsCode') {
          this.arrayGoodsValue.forEach(function (c) {
            if (c.goodsCode == value) {
              temp = c
              flag=true
            }
          })
        }else if (column.key === 'goodsName') {
          this.arrayGoodsValue.forEach(function (c) {
            if (c.goodsName == value) {
              temp = c
              flag=true
            }
          })
        }

        if (flag) {
          // 根据货物填写资料
          target.setValues([{
            rowKey: row.id,
            values: { 'goodsId': temp.goodsId,'goodsCode':temp.goodsCode,'goodsName':temp.goodsName,'goodsSize':temp.goodsSize, 'goodsUnit':temp.goodsUnit, 'goodsType':temp.goodsType,
              'goodsColor':temp.goodsColor, }
          }])
        }

      },
      validateError(msg){
        this.$message.error(msg)
      },
      
    }
  }
</script>