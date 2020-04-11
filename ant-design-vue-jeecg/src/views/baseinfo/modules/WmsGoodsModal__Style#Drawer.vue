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

        <a-form-item label="货物名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsName', validatorRules.goodsName]" placeholder="请输入货物名称"></a-input>
        </a-form-item>
        <a-form-item label="货物编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsCode', validatorRules.goodsCode]" placeholder="请输入货物编码"></a-input>
        </a-form-item>
        <a-form-item label="货物规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'goodsSize', validatorRules.goodsSize]" placeholder="请输入货物规格"></a-input>
        </a-form-item>
        <!--<a-form-item label="货物品种" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<j-dict-select-tag type="list" v-decorator="['goodsType']" :trigger-change="true" dictCode="" placeholder="请选择货物品种"/>-->
        <!--</a-form-item>-->
        <a-form-item label="货物类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择货物类别"
            v-decorator="['goodsType', validatorRules.goodsType]"
            dict="wms_goods_category,category_name,id"
            pidField="category_id"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select>
        </a-form-item>
        <a-form-item label="货物单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['goodsUnit']" :trigger-change="true" dictCode="goods_unit" placeholder="请选择货物单位"/>
        </a-form-item>
        <a-form-item label="货物启停状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isUse']" :trigger-change="true" dictCode="use_type" placeholder="请选择货物启停状态"/>
        </a-form-item>
        <a-form-item label="货物花色" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-multi-select-tag type="list_multi" v-decorator="['goodsColor']" :trigger-change="true" dictCode="" placeholder="请选择货物花色"/>
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
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  
  export default {
    name: "WmsGoodsModal",
    components: { 
      JDictSelectTag,
      JMultiSelectTag,
      JTreeSelect,
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
        goodsName:{rules: [{ required: true, message: '请输入货物名称!' }]},
        goodsSize:{},
        goodsType:{},
        isUse:{rules: [{ required: true, message: '请输入货物启停状态!' }]},
        goodsColor:{},
        goodsCode:{},
        memo:{},
        goodsUnit:{},
        },
        url: {
          add: "/goods/wmsGoods/add",
          edit: "/goods/wmsGoods/edit",
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
          this.form.setFieldsValue(pick(this.model,'goodsName','goodsSize','goodsType','isUse','goodsColor','goodsCode','memo','goodsUnit'))
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
        this.form.setFieldsValue(pick(row,'goodsName','goodsSize','goodsType','isUse','goodsColor','goodsCode','memo','goodsUnit'))
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