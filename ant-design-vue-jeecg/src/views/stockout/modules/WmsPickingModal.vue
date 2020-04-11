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

        <a-form-item label="来源细单ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sourcedtlId', validatorRules.sourcedtlId]" placeholder="请输入来源细单ID"></a-input>
        </a-form-item>
        <a-form-item label="拣选状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['pickingState']" :trigger-change="true" dictCode="picking_state" placeholder="请选择拣选状态"/>
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
          <a-input v-decorator="[ 'goodsQuantity', validatorRules.goodsQuantity]" placeholder="请输入货物数量"></a-input>
        </a-form-item>
        <a-form-item label="货物等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['goodsLevel']" :trigger-change="true" dictCode="goods_level" placeholder="请选择货物等级"/>
        </a-form-item>
        <a-form-item label="出库时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'stockoutTime', validatorRules.stockoutTime]" placeholder="请输入出库时间"></a-input>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新时间" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sysOrgCode', validatorRules.sysOrgCode]" placeholder="请输入所属部门"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "WmsPickingModal",
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
        sourcedtlId:{},
        pickingState:{},
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
        stockoutTime:{},
        createBy:{},
        createTime:{},
        updateBy:{},
        updateTime:{},
        sysOrgCode:{},
        },
        url: {
          add: "/wmspicking/wmsPicking/add",
          edit: "/wmspicking/wmsPicking/edit",
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
          this.form.setFieldsValue(pick(this.model,'pickingId','pickingCode','sourcedtlId','pickingState','areaId','areaCode','areaName','locId','locCode','locName','goodsId','goodsCode','goodsName','goodsSize','goodsUnit','goodsType','goodsColor','goodsBatchnumber','goodsQuantity','goodsLevel','stockoutTime','createBy','createTime','updateBy','updateTime','sysOrgCode'))
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
        this.form.setFieldsValue(pick(row,'pickingId','pickingCode','sourcedtlId','pickingState','areaId','areaCode','areaName','locId','locCode','locName','goodsId','goodsCode','goodsName','goodsSize','goodsUnit','goodsType','goodsColor','goodsBatchnumber','goodsQuantity','goodsLevel','stockoutTime','createBy','createTime','updateBy','updateTime','sysOrgCode'))
      },

      
    }
  }
</script>