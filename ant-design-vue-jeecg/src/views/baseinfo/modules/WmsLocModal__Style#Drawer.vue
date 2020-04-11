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

        <a-form-item label="货位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'locName', validatorRules.locName]" placeholder="请输入货位名称"></a-input>
        </a-form-item>
        <a-form-item label="货位编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'locCode', validatorRules.locCode]" placeholder="请输入货位编码"></a-input>
        </a-form-item>
        <a-form-item label="货位地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'locLoc', validatorRules.locLoc]" placeholder="请输入货位地点"></a-input>
        </a-form-item>
        <a-form-item label="货位容量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'locVolume', validatorRules.locVolume]" placeholder="请输入货位容量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="所属的区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaId', validatorRules.areaId]" placeholder="请输入所属的区域"></a-input>
        </a-form-item>
        <a-form-item label="货位混放状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isMix']" :trigger-change="true" dictCode="mix_type" placeholder="请选择货位混放状态"/>
        </a-form-item>
        <a-form-item label="货位类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['locType']" :trigger-change="true" dictCode="" placeholder="请选择货位类型"/>
        </a-form-item>
        <a-form-item label="货位状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['locState']" :trigger-change="true" dictCode="" placeholder="请选择货位状态"/>
        </a-form-item>
        <a-form-item label="货位启停状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isUse']" :trigger-change="true" dictCode="use_type" placeholder="请选择货位启停状态"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "WmsLocModal",
    components: { 
      JDictSelectTag,
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
        locName:{rules: [{ required: true, message: '请输入货位名称!' }]},
        locLoc:{rules: [{ required: true, message: '请输入货位地点!' }]},
        locVolume:{},
        areaId:{rules: [{ required: true, message: '请输入所属的区域!' }]},
        locType:{},
        locState:{},
        isUse:{rules: [{ required: true, message: '请输入货位启停状态!' }]},
        locCode:{},
        memo:{},
        isMix:{},
        },
        url: {
          add: "/loc/wmsLoc/add",
          edit: "/loc/wmsLoc/edit",
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
          this.form.setFieldsValue(pick(this.model,'locName','locLoc','locVolume','areaId','locType','locState','isUse','locCode','memo','isMix'))
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
        this.form.setFieldsValue(pick(row,'locName','locLoc','locVolume','areaId','locType','locState','isUse','locCode','memo','isMix'))
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