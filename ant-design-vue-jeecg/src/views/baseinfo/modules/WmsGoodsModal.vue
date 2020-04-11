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
      <a-form :form="form">

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsName', validatorRules.goodsName]" placeholder="请输入货物名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsCode', validatorRules.goodsCode]" placeholder="请输入货物编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsSize', validatorRules.goodsSize]" placeholder="请输入货物规格"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-tree-select-->
                <!--ref="treeSelect"-->
                <!--placeholder="请选择货物类别"-->
                <!--v-decorator="['goodsType', validatorRules.goodsType]"-->
                <!--dict="wms_goods_category,category_name,id"-->
                <!--pidField="category_id"-->
                <!--pidValue="0"-->
                <!--hasChildField="has_child">-->
              <!--</j-tree-select>-->
              <a-select
                showSearch
                placeholder="请选择货物类别"
                optionFilterProp="children"
                :filterOption="filterOption"
                v-decorator="['goodsType', validatorRules.goodsType]"
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
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsUnit']" :trigger-change="true" dictCode="goods_unit"
                                 placeholder="请选择货物单位"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物启停状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isUse']" :trigger-change="true" dictCode="use_type"
                                 placeholder="请选择货物启停状态"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物花色" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-multi-select-tag type="list_multi" v-decorator="['goodsColor']" :trigger-change="true" dictCode="color"-->
                                  <!--placeholder="请选择货物花色"/>-下拉多选-->
              <j-dict-select-tag type="list" v-decorator="['goodsColor']" :trigger-change="true" dictCode="color"
                                 placeholder="请选择货物花色"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'memo', validatorRules.memo]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- JSelectMultiple -->
        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="上架策略" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-multi-select-tag v-decorator="['rackingStrategy',{ initialValue: '0'}]" dictCode="racking_strategy" placeholder="请选择上架策略"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  import { getAction } from "../../../api/manage";
  import JSelectMultiple from '@/components/jeecg/JSelectMultiple'


  export default {
    name: "WmsGoodsModal",
    components: {
      JDictSelectTag,
      JMultiSelectTag,
      JTreeSelect,
      JSelectMultiple,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 7},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        validatorRules: {
          goodsName: {rules: [{required: true, message: '请输入货物名称!'}]},
          goodsSize: {},
          goodsType: {},
          isUse: {rules: [{required: true, message: '请输入货物启停状态!'}]},
          goodsColor: {},
          goodsCode: {},
          memo: {},
          goodsUnit: {},
        },
        url: {
          add: "/goods/wmsGoods/add",
          edit: "/goods/wmsGoods/edit",
          queryLastCategoryList:"/goods/wmsGoods/queryLastCategoryList",
        },
        arrayValue:[],
        rackingStrategy: {
          options: [
            { text: '类别', value: '0' },
            { text: '规格', value: '1' },
            { text: '花色', value: '2' },
            { text: '等级', value: '3', disabled: true }
          ],
          value: ''
        },

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

        }
      });
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'goodsName', 'goodsSize', 'goodsType', 'isUse', 'goodsColor', 'goodsCode', 'memo', 'goodsUnit', 'rackingStrategy'))
        })
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据", formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'goodsName', 'goodsSize', 'goodsType', 'isUse', 'goodsColor', 'goodsCode', 'memo', 'goodsUnit', 'rackingStrategy'))
      },
      filterOption(input, option) {
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      }

    }
  }
</script>