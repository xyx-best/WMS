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
            <a-form-item label="仓库名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'warehouseName', validatorRules.warehouseName]" placeholder="请输入仓库名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="仓库编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'warehouseCode', validatorRules.warehouseCode]" placeholder="请输入仓库编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-item label="仓库地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'warehouseLoc', validatorRules.warehouseLoc]" placeholder="请输入仓库地点"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="仓库容量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'warehouseVolume', validatorRules.warehouseVolume]" placeholder="请输入仓库容量"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-item label="仓库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-multi-select-tag type="list" v-decorator="['warehouseType']" :trigger-change="true"-->
                                  <!--dictCode="warehouse_type" placeholder="请选择仓库类型"/>  下拉多选-->
              <j-dict-select-tag type="list" v-decorator="['warehouseType']" :trigger-change="true" dictCode="warehouse_type"
                                 placeholder="请选择仓库类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="仓库启用状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isUse']" :trigger-change="true" dictCode="use_type"
                                 placeholder="请选择仓库启用状态"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-item label="仓库管理者" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-user-by-dep v-decorator="['warehouseManager']" :trigger-change="true"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'memo', validatorRules.memo]" placeholder="请输入备注"></a-input>
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
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import ACol from "ant-design-vue/es/grid/Col";

  export default {
    name: "WmsWarehouseModal",
    components: {
      ACol,
      JSelectUserByDep,
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
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span:24},
          sm: {span: 17},
        },

        confirmLoading: false,
        validatorRules: {
          warehouseName: {rules: [{required: true, message: '请输入仓库名称!'}]},
          warehouseLoc: {rules: [{required: true, message: '请输入仓库地点!'}]},
          warehouseVolume: {},
          warehouseType: {},
          isUse: {rules: [{required: true, message: '请输入仓库启用状态!'}]},
          warehouseManager: {},
          warehouseCode: {},
          memo: {},
        },
        url: {
          add: "/warehouse/wmsWarehouse/add",
          edit: "/warehouse/wmsWarehouse/edit",
        }

      }
    },
    created() {
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
          this.form.setFieldsValue(pick(this.model, 'warehouseName', 'warehouseLoc', 'warehouseVolume', 'warehouseType', 'isUse', 'warehouseManager', 'warehouseCode', 'memo'))
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
        this.form.setFieldsValue(pick(row, 'warehouseName', 'warehouseLoc', 'warehouseVolume', 'warehouseType', 'isUse', 'warehouseManager', 'warehouseCode', 'memo'))
      },


    }
  }
</script>