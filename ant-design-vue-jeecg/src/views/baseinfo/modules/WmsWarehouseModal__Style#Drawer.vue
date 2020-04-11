<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="仓库名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'warehouseName', validatorRules.warehouseName]" placeholder="请输入仓库名称"></a-input>
        </a-form-item>
        <a-form-item label="仓库编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'warehouseCode', validatorRules.warehouseCode]" placeholder="请输入仓库编码"></a-input>
        </a-form-item>
        <a-form-item label="仓库地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'warehouseLoc', validatorRules.warehouseLoc]" placeholder="请输入仓库地点"></a-input>
        </a-form-item>
        <a-form-item label="仓库容量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'warehouseVolume', validatorRules.warehouseVolume]" placeholder="请输入仓库容量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="仓库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-multi-select-tag type="list_multi" v-decorator="['warehouseType']" :trigger-change="true" dictCode="warehouse_type" placeholder="请选择仓库类型"/>
        </a-form-item>
        <a-form-item label="仓库启用状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isUse']" :trigger-change="true" dictCode="use_type" placeholder="请选择仓库启用状态"/>
        </a-form-item>
        <a-form-item label="仓库管理者" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['warehouseManager']" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="仓库编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'memo', validatorRules.memo]" placeholder="请输入备注"></a-input>
        </a-form-item>
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  
  export default {
    name: "WmsWarehouseModal",
    components: { 
      JSelectUserByDep,
      JDictSelectTag,
      JMultiSelectTag,
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
        warehouseName:{rules: [{ required: true, message: '请输入仓库名称!' }]},
        warehouseLoc:{rules: [{ required: true, message: '请输入仓库地点!' }]},
        warehouseVolume:{},
        warehouseType:{},
        isUse:{rules: [{ required: true, message: '请输入仓库启用状态!' }]},
        warehouseManager:{},
        warehouseCode:{},
        memo:{},
        },
        url: {
          add: "/warehouse/wmsWarehouse/add",
          edit: "/warehouse/wmsWarehouse/edit",
        }
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'warehouseName','warehouseCode','warehouseLoc','warehouseVolume','warehouseType','isUse','warehouseManager','memo'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'warehouseName','warehouseCode','warehouseLoc','warehouseVolume','warehouseType','isUse','warehouseManager','memo'))
      }
      
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>