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

        <a-form-item label="表名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'tableName', validatorRules.tableName]" placeholder="请输入表名" :read-only="isReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="长度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'length', validatorRules.length]" placeholder="请输入长度" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="前缀" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag ref="pp" type="list" v-decorator="['prefix']" :trigger-change="true" dictCode="prefix_type" placeholder="请选择前缀" @change="handleChange"/>
        </a-form-item>
        <a-form-item label="后缀" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag ref="ss" type="list" v-decorator="['suffix']" :trigger-change="true" dictCode="suffix_type" placeholder="请选择后缀"/>
        </a-form-item>
        <a-form-item label="计数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'count', validatorRules.count]" placeholder="请输入计数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'memo']" placeholder="请输入备注" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import { duplicateCheck } from "@/api/api"

  export default {
    name: "IdManageModal",
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
        tableName:{rules: [{ required: true, message: '请输入表名!' },{validator: this.validateTableName}]},
        length:{},
        prefix:{},
        suffix:{},
        count:{},
        },
        url: {
          add: "/sysmanage/idManage/add",
          edit: "/sysmanage/idManage/edit",
        },
        dictOs:[],
        isAdd:false,
        isReadOnly:true,
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
        this.isReadOnly=false;
        this.isAdd=true;
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'tableName','length','prefix','suffix','count','memo'))
        })
        this.isReadOnly=true;
        this.isAdd=false;
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
            formData["prefixText"] = this.$refs.pp.selectedText;
            formData["suffixText"] = this.$refs.ss.selectedText;
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
        this.form.setFieldsValue(pick(row,'tableName','length','prefix','suffix','count','memo'))
      },
      validateTableName(rule, value, callback){
        if (this.isAdd){
          if(!value){
            callback();
          }else{
            var params = {
              tableName: 'id_manage',
              fieldName: 'table_name',
              fieldVal: value,
            };
            duplicateCheck(params).then((res) => {
              if (res.success) {
                callback();
              } else {
                callback("表名已存在!")
              }
            })

          }
        } else {
          callback();
        }

      },
      handleChange(e){
        // console.log("父组件" + this.$refs.pp.selectedText)
        // console.log("父组件2222" + this.$refs.pp.dictOptions)
      }
      
    }
  }
</script>