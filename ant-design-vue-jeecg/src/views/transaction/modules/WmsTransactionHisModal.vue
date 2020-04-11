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

        <a-form-item label="交易ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'transactionId', validatorRules.transactionId]" placeholder="请输入交易ID"></a-input>
        </a-form-item>
        <a-form-item label="交易编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'transactionCode', validatorRules.transactionCode]" placeholder="请输入交易编码"></a-input>
        </a-form-item>
        <a-form-item label="上架/拣选单ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'moveId', validatorRules.moveId]" placeholder="请输入上架/拣选单ID"></a-input>
        </a-form-item>
        <a-form-item label="库存ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'stockId', validatorRules.stockId]" placeholder="请输入库存ID"></a-input>
        </a-form-item>
        <a-form-item label="来源类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sourceType']" :trigger-change="true" dictCode="source_typev" placeholder="请选择来源类型"/>
        </a-form-item>
        <a-form-item label="交易状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['transactionState']" :trigger-change="true" dictCode="transaction_state" placeholder="请选择交易状态"/>
        </a-form-item>
        <a-form-item label="区域ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaId', validatorRules.areaId]" placeholder="请输入区域ID"></a-input>
        </a-form-item>
        <a-form-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaCode', validatorRules.areaCode]" placeholder="请输入区域编码"></a-input>
        </a-form-item>
        <a-form-item label="区域名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaName', validatorRules.areaName]" placeholder="请输入区域名称"></a-input>
        </a-form-item>
        <a-form-item label="货位ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'locId', validatorRules.locId]" placeholder="请输入货位ID"></a-input>
        </a-form-item>
        <a-form-item label="货位编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'locCode', validatorRules.locCode]" placeholder="请输入货位编码"></a-input>
        </a-form-item>
        <a-form-item label="货位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'locName', validatorRules.locName]" placeholder="请输入货位名称"></a-input>
        </a-form-item>
        <a-form-item label="货物ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsId', validatorRules.goodsId]" placeholder="请输入货物ID"></a-input>
        </a-form-item>
        <a-form-item label="货物编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsCode', validatorRules.goodsCode]" placeholder="请输入货物编码"></a-input>
        </a-form-item>
        <a-form-item label="货物名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsName', validatorRules.goodsName]" placeholder="请输入货物名称"></a-input>
        </a-form-item>
        <a-form-item label="货物规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsSize', validatorRules.goodsSize]" placeholder="请输入货物规格"></a-input>
        </a-form-item>
        <a-form-item label="货物单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['goodsUnit']" :trigger-change="true" dictCode="goods_unit" placeholder="请选择货物单位"/>
        </a-form-item>
        <a-form-item label="货物类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsType', validatorRules.goodsType]" placeholder="请输入货物类别"></a-input>
        </a-form-item>
        <a-form-item label="货物花色" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['goodsColor']" :trigger-change="true" dictCode="color" placeholder="请选择货物花色"/>
        </a-form-item>
        <a-form-item label="货物批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsBatchnumber', validatorRules.goodsBatchnumber]" placeholder="请输入货物批号"></a-input>
        </a-form-item>
        <a-form-item label="货物数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'goodsQuantity', validatorRules.goodsQuantity]" placeholder="请输入货物数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="货物等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['goodsLevel']" :trigger-change="true" dictCode="goods_level" placeholder="请选择货物等级"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "WmsTransactionHisModal",
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
        transactionId:{},
        transactionCode:{},
        moveId:{},
        stockId:{},
        sourceType:{},
        transactionState:{},
        areaId:{},
        areaCode:{},
        areaName:{},
        locId:{},
        locCode:{},
        locName:{},
        goodsId:{},
        goodsCode:{},
        goodsName:{},
        goodsSize:{},
        goodsUnit:{},
        goodsType:{},
        goodsColor:{},
        goodsBatchnumber:{},
        goodsQuantity:{},
        goodsLevel:{},
        },
        url: {
          add: "/transactionHis/wmsTransactionHis/add",
          edit: "/transactionHis/wmsTransactionHis/edit",
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
          this.form.setFieldsValue(pick(this.model,'transactionId','transactionCode','moveId','stockId','sourceType','transactionState','areaId','areaCode','areaName','locId','locCode','locName','goodsId','goodsCode','goodsName','goodsSize','goodsUnit','goodsType','goodsColor','goodsBatchnumber','goodsQuantity','goodsLevel'))
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
        this.form.setFieldsValue(pick(row,'transactionId','transactionCode','moveId','stockId','sourceType','transactionState','areaId','areaCode','areaName','locId','locCode','locName','goodsId','goodsCode','goodsName','goodsSize','goodsUnit','goodsType','goodsColor','goodsBatchnumber','goodsQuantity','goodsLevel'))
      },

      
    }
  }
</script>