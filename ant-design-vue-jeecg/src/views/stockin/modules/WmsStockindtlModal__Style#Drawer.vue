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

        <a-form-item label="入库明细编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'stockindtlCode', validatorRules.stockindtlCode]" placeholder="请输入入库明细编码"></a-input>
        </a-form-item>
        <a-form-item label="入库ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'stockinId', validatorRules.stockinId]" placeholder="请输入入库ID"></a-input>
        </a-form-item>
        <a-form-item label="入库编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'stockinCode', validatorRules.stockinCode]" placeholder="请输入入库编码"></a-input>
        </a-form-item>
        <a-form-item label="入库状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['stockinState']" :trigger-change="true" dictCode="stockin_state" placeholder="请选择入库状态"/>
        </a-form-item>
        <a-form-item label="货物ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsId', validatorRules.goodsId]" placeholder="请输入货物ID"></a-input>
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
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新时间" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sysOrgCode', validatorRules.sysOrgCode]" placeholder="请输入所属部门"></a-input>
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
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "WmsStockindtlModal",
    components: { 
      JDate,
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
        stockindtlCode:{},
        stockinId:{},
        stockinCode:{},
        stockinState:{},
        goodsId:{},
        goodsName:{},
        goodsSize:{},
        goodsUnit:{},
        goodsType:{},
        goodsColor:{},
        goodsBatchnumber:{},
        goodsQuantity:{},
        goodsLevel:{},
        createBy:{},
        createTime:{},
        updateBy:{},
        updateTime:{},
        sysOrgCode:{},
        },
        url: {
          add: "/wmsstockindtl/wmsStockindtl/add",
          edit: "/wmsstockindtl/wmsStockindtl/edit",
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
          this.form.setFieldsValue(pick(this.model,'stockindtlId','stockindtlCode','stockinId','stockinCode','stockinState','goodsId','goodsName','goodsSize','goodsUnit','goodsType','goodsColor','goodsBatchnumber','goodsQuantity','goodsLevel','createBy','createTime','updateBy','updateTime','sysOrgCode'))
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
        this.form.setFieldsValue(pick(row,'stockindtlId','stockindtlCode','stockinId','stockinCode','stockinState','goodsId','goodsName','goodsSize','goodsUnit','goodsType','goodsColor','goodsBatchnumber','goodsQuantity','goodsLevel','createBy','createTime','updateBy','updateTime','sysOrgCode'))
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