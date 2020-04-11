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

        <a-form-item label="区域名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaName', validatorRules.areaName]" placeholder="请输入区域名称"></a-input>
        </a-form-item>
        <a-form-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaCode', validatorRules.areaCode]" placeholder="请输入区域编码"></a-input>
        </a-form-item>
        <a-form-item label="区域地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'areaLoc', validatorRules.areaLoc]" placeholder="请输入区域地点"></a-input>
        </a-form-item>
        <a-form-item label="所属的仓库ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
          <a-input v-decorator="[ 'warehouseId', validatorRules.warehouseId]" placeholder="请选择所属的仓库ID"></a-input>
        </a-form-item>
        <a-form-item label="所属的仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'warehouseName', validatorRules.warehouseName]" placeholder="请选择所属的仓库"></a-input>
        </a-form-item>
        <a-form-item label="区域大小" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'areaSize', validatorRules.areaSize]" placeholder="请输入区域大小" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="区域用途" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list"  v-decorator="[ 'areaType', validatorRules.areaType]" placeholder="请输入区域用途"/>
        </a-form-item>
        <!--<a-form-item label="区域存放类别" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'areaKind', validatorRules.areaKind]" placeholder="请输入区域存放类别"></a-input>-->
        <!--</a-form-item>-->
        <a-form-item label="区域存放类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择区域存放类别"
            v-decorator="[ 'areaKind', validatorRules.areaKind]"
            dict="wms_goods_category,category_name,id"
            pidField="category_id"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select>
        </a-form-item>
        <a-form-item label="区域启停状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['areaState']" :trigger-change="true" dictCode="use_type" placeholder="请选择区域启停状态"/>
        </a-form-item>
        <a-form-item label="区域管理者" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['areaManager']" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'memo', validatorRules.areaType]" placeholder="请输入备注"></a-input>
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
  import JTreeSelect from '@/components/jeecg/JTreeSelect'

  export default {
    name: "WmsAreaModal",
    components: { 
      JSelectUserByDep,
      JDictSelectTag,
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
        areaName:{rules: [{ required: true, message: '请输入区域名称!' }]},
        areaLoc:{rules: [{ required: true, message: '请输入区域地点!' }]},
        warehouseId:{rules: [{ required: true, message: '请选择所属的仓库!' }]},
        areaSize:{},
        areaType:{},
        areaKind:{},
        areaState:{rules: [{ required: true, message: '请输入区域启停状态!' }]},
        areaManager:{},
        areaCode:{},
        },
        url: {
          add: "/area/wmsArea/add",
          edit: "/area/wmsArea/edit",
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
          this.form.setFieldsValue(pick(this.model,'areaName','areaLoc','warehouseId','areaSize','areaType','areaKind','areaState','areaManager','areaCode','memo'))
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
        this.form.setFieldsValue(pick(row,'areaName','areaLoc','warehouseId','areaSize','areaType','areaKind','areaState','areaManager','areaCode','memo'))
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