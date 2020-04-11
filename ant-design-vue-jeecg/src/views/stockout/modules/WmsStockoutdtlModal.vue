<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="出库明细编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'stockoutdtlCode', validatorRules.stockoutdtlCode]"
                       placeholder="请输入出库明细编码" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出库ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'stockoutId', validatorRules.stockoutId]" placeholder="请输入出库ID" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="出库编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'stockoutCode', validatorRules.stockoutCode]" placeholder="请输入出库编码" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出库状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['stockoutState']" :trigger-change="true"
                                 dictCode="stockout_state" placeholder="请选择出库状态" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsId', validatorRules.goodsId]" placeholder="请输入货物ID" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsCode', validatorRules.goodsCode]" placeholder="请输入货物编码" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsName', validatorRules.goodsName]" placeholder="请输入货物名称" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsSize', validatorRules.goodsSize]" placeholder="请输入货物规格" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsUnit']" :trigger-change="true" dictCode="goods_unit"
                                 placeholder="请选择货物单位" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsType', validatorRules.goodsType]" placeholder="请输入货物类别" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物花色" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsColor']" :trigger-change="true" dictCode="color"
                                 placeholder="请选择货物花色" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsBatchnumber', validatorRules.goodsBatchnumber]"
                       placeholder="请输入货物批号" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'goodsQuantity', validatorRules.goodsQuantity]" placeholder="请输入货物数量"
                              style="width: 100%" :readOnly="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsLevel']" :trigger-change="true" dictCode="goods_level"
                                 placeholder="请选择货物等级" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]"
                      :trigger-change="true" style="width: 100%" :readOnly="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人" :readOnly="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择更新时间" v-decorator="[ 'updateTime', validatorRules.updateTime]"
                      :trigger-change="true" style="width: 100%" :readOnly="disableSubmit"/>
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
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "WmsstockoutdtlModal",
    components: {
      JDate,
      JDictSelectTag,
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
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },

        confirmLoading: false,
        validatorRules: {
          stockoutdtlCode: {},
          stockoutId: {},
          stockoutCode: {},
          stockoutState: {},
          goodsId: {},
          goodsName: {},
          goodsSize: {},
          goodsUnit: {},
          goodsType: {},
          goodsColor: {},
          goodsBatchnumber: {},
          goodsQuantity: {},
          goodsLevel: {},
          createBy: {},
          createTime: {},
          updateBy: {},
          updateTime: {},
          sysOrgCode: {},
        },
        url: {
          add: "/wmsstockoutdtl/wmsstockoutdtl/add",
          edit: "/wmsstockoutdtl/wmsstockoutdtl/edit",
        },
        disableSubmit: false,

      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.disableSubmit = true;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'stockoutdtlCode', 'stockoutId', 'stockoutCode', 'stockoutState', 'goodsId', 'goodsName', 'goodsSize', 'goodsUnit', 'goodsType', 'goodsColor', 'goodsBatchnumber', 'goodsQuantity', 'goodsLevel', 'createBy', 'createTime', 'updateBy', 'updateTime'))
        });
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
        this.form.setFieldsValue(pick(row, 'stockoutdtlCode', 'stockoutId', 'stockoutCode', 'stockoutState', 'goodsId', 'goodsName', 'goodsSize', 'goodsUnit', 'goodsType', 'goodsColor', 'goodsBatchnumber', 'goodsQuantity', 'goodsLevel', 'createBy', 'createTime', 'updateBy', 'updateTime'))
      },
      getFormData(){
        let formdata_arr = []
        this.form.validateFields((err, values) => {
          if (!err) {
            let formdata = Object.assign(this.model, values)
            let isNullObj = true
            Object.keys(formdata).forEach(key=>{
              if(formdata[key]){
                isNullObj = false
              }
            })
            if(!isNullObj){
              formdata_arr.push(formdata)
            }
          }else{
            this.$emit("validateError","出库明细表表单校验未通过");
          }
        })
        console.log("出库明细表表单数据集",formdata_arr);
        return formdata_arr;
      },

    }
  }
</script>