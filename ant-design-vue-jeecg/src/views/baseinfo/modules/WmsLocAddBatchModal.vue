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
            <a-form-item label="货位数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'locLoc', validatorRules.locLoc]" placeholder="请输入新增货位数量"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货位容量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'locVolume', validatorRules.locVolume]" placeholder="请输入单个货位容量"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="所属区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                placeholder="请选择所属区域"
                optionFilterProp="children"
                @focus="handleFocus"
                @blur="handleBlur"
                @change="handleChange"
                :filterOption="filterOption"
                v-decorator="[ 'areaId', validatorRules.areaId]"
                :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                <a-select-option
                  v-for="(item,index) in arrayValue"
                  :key="index"
                  :value="item.value">
                  {{ item.text || item.label }}
                </a-select-option>
              </a-select>
              <!--<a-input v-decorator="[ 'areaId', validatorRules.areaId]" placeholder="请输入所属的区域"></a-input>-->
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货位混放状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isMix', validatorRules.isMix]" :trigger-change="true" dictCode="mix_type"
                                 placeholder="请选择货位混放状态"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货位类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-dict-select-tag type="list" v-decorator="['locType']" :trigger-change="true" dictCode="loc_type"-->
                                 <!--placeholder="请选择货位类型"/>-->
              <a-select
                showSearch
                placeholder="请选择货位类型"
                optionFilterProp="children"
                :filterOption="filterOption"
                v-decorator="['locType', validatorRules.locType]"
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
          <a-col :span="12">
            <a-form-item label="货位状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['locState', validatorRules.locState]" :trigger-change="true" dictCode="loc_state"
                                 placeholder="请选择货位状态"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货位启停状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isUse', validatorRules.isUse]" :trigger-change="true" dictCode="use_type"
                                 placeholder="请选择货位启停状态"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'memo', validatorRules.memo]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="上架策略" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-multi-select-tag v-decorator="['rackingStrategy',{ initialValue: '0'}]" dictCode="al_rk_strategy" placeholder="请选择上架策略"/>
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
  import {getAction} from "../../../api/manage";
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"

  export default {
    name: "WmsLocModal",
    components: {
      JDictSelectTag,
      JMultiSelectTag,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
        visible: false,
        model: {},
        arrayValue: [],
        arrayCategoryValue:[],
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 17},
        },

        confirmLoading: false,
        validatorRules: {
          locName: {rules: [{required: true, message: '请输入货位名称!'}]},
          locLoc: {rules: [{required: true, message: '请按“L/R-row-col”输入货位数量!',pattern: /^[LR]-[0-9]+-[0-9]+$/, }]},// 正则
          locVolume: {rules: [{required: true, message: '请输入货位容量!'}]},
          areaId: {rules: [{required: true, message: '请输入所属的区域!'}]},
          locType: {rules: [{required: true, message: '请输入所属的类型!'}]},
          locState: {rules: [{required: true, message: '请输入货位状态!'}]},
          isUse: {rules: [{required: true, message: '请输入货位启停状态!'}]},
          locCode: {},
          memo: {},
          isMix: {rules: [{required: true, message: '请输入货位混放状态!'}]},
        },
        url: {
          queryList: "/loc/wmsLoc/queryAreaList",
          add: "/loc/wmsLoc/addBatch",
          edit: "/loc/wmsLoc/edit",
          queryCategoryList:"/goods/wmsGoods/queryLastCategoryList",
        }

      }
    },
    created() {
      getAction(this.url.queryList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayValue = [];
          res.result.list.filter(item => {
            if (item.id != null) {
              temp = {text: item.areaName, value: item.areaId}
              this.arrayValue.push(temp);
            }
          })
        }
      });
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
          this.form.setFieldsValue(pick(this.model, 'locLoc', 'locVolume', 'areaId', 'locType', 'locState', 'isUse', 'memo', 'isMix','rackingStrategy'))
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
                that.$emit('refresh');
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
        this.form.setFieldsValue(pick(row, 'locLoc', 'locVolume', 'areaId', 'locType', 'locState', 'isUse', 'memo', 'isMix','rackingStrategy'))
      },
      handleChange(value) {
        //this.sValue=value;
        // console.log(this.sValue);
        // console.log(`selected ${value}`);
      },
      handleBlur() {
      },
      handleFocus() {
      },
      filterOption(input, option) {
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      }
    }
  }
</script>