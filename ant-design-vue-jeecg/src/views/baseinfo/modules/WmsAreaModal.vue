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
            <a-form-item label="区域名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'areaName', validatorRules.areaName]" placeholder="请输入区域名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="区域编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'areaCode', validatorRules.areaCode]" placeholder="请输入区域编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="区域地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'areaLoc', validatorRules.areaLoc]" placeholder="请输入区域地点"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="所属的仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                placeholder="请选择所属仓库"
                optionFilterProp="children"
                @focus="handleFocus"
                @blur="handleBlur"
                @change="handleChange"
                :filterOption="filterOption"
                v-decorator="[ 'warehouseId', validatorRules.warehouseId]"
                :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                <a-select-option
                  v-for="(item,index) in arrayValue"
                  :key="index"
                  :value="item.value">
                  {{ item.text || item.label }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="区域大小" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'areaSize', validatorRules.areaSize]" placeholder="请输入区域大小"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="区域用途" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list"  v-decorator="[ 'areaType', validatorRules.areaType]" placeholder="请选择区域用途"  :trigger-change="true" dictCode="area_type"/>
            </a-form-item>
          </a-col>
        </a-row>


        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="区域存放类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-tree-select
                showSearch
                ref="treeSelect"
                placeholder="请选择区域存放类别"
                v-decorator="[ 'areaKind', validatorRules.areaKind]"
                dict="wms_goods_category,category_name,category_id"
                pidField="parent_category_id"
                pidValue="0"
                :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                hasChildField="has_child">
              </j-tree-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="区域启停状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['areaState']" :trigger-change="true" dictCode="use_type"
                                 placeholder="请选择区域启停状态"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="区域管理者" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-user-by-dep v-decorator="['areaManager']" :trigger-change="true"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'memo', validatorRules.areaType]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="上架策略" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-multi-select-tag v-decorator="['rackingStrategy',{ initialValue: '0'}]" dictCode="al_rk_strategy" placeholder="请选择上架策略"/>
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
  import JSelectSearch from '@/components/jeecg/JSelectSearch'
  import {getAction} from "../../../api/manage";
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"

  export default {
    name: "WmsAreaModal",
    components: {
      JSelectUserByDep,
      JDictSelectTag,
      JSelectSearch,
      JTreeSelect,
      JMultiSelectTag,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
        visible: false,
        model: {},
        arrayValue: [],
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 17},
        },
        confirmLoading: false,
        validatorRules: {
          areaName: {rules: [{required: true, message: '请输入区域名称!'}]},
          areaLoc: {rules: [{required: false, message: '请输入区域地点!'}]},
          warehouseId: {rules: [{required: true, message: '请选择所属的仓库!'}]},
          areaSize: {},
          areaType: {},
          areaKind: {},
          areaState: {rules: [{required: true, message: '请输入区域启停状态!'}]},
          areaManager: {},
          areaCode: {},
        },
        url: {
          queryList: "/area/wmsArea/queryList",
          add: "/area/wmsArea/add",
          edit: "/area/wmsArea/edit",
        }

      }
    },
    created() {
      getAction(this.url.queryList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.warehouseName, value: item.warehouseId}
              this.arrayValue.push(temp);
            }
          })
        } else {

        }
      });
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
          this.form.setFieldsValue(pick(this.model, 'areaName', 'areaLoc', 'warehouseId', 'areaSize', 'areaType', 'areaKind', 'areaState', 'areaManager', 'areaCode', 'memo','rackingStrategy'))
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
        this.form.setFieldsValue(pick(row, 'areaName', 'areaLoc', 'warehouseId', 'areaSize', 'areaType', 'areaKind', 'areaState', 'areaManager', 'areaCode', 'memo','rackingStrategy'))
      },
      handleChange(value) {
        //this.sValue=value;
        // console.log(this.sValue);
        // console.log(`selected ${value}`);
      },
      handleBlur() {
      },
      handleFocus() {
      },
      filterOption(input, option) {
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      }
    }
  }
</script>