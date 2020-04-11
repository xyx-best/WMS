<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="父级类别节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择父级类别节点"
                v-decorator="['parentCategoryId', validatorRules.parentCategoryId]"
                dict="wms_goods_category,category_name,category_id"
                pidField="parent_category_id"
                pidValue="0"
                hasChildField="has_child">
              </j-tree-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="类别名字" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'categoryName', validatorRules.categoryName]" placeholder="请输入类别名字"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="预留字段1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'udf1', validatorRules.udf1]" placeholder="请输入预留字段1"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="预留字段2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'udf2', validatorRules.udf2]" placeholder="请输入预留字段2"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="预留字段3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'udf3', validatorRules.udf3]" placeholder="请输入预留字段3"></a-input>
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
  import JTreeSelect from '@/components/jeecg/JTreeSelect'

  export default {
    name: "WmsGoodsCategoryModal",
    components: {
      JTreeSelect
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
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },

        confirmLoading: false,
        validatorRules: {
          parentCategoryId: {},
          categoryName: {},
          udf1: {},
          udf2: {},
          udf3: {},
        },
        url: {
          add: "/goodsCategory/wmsGoodsCategory/add",
          edit: "/goodsCategory/wmsGoodsCategory/edit",
        },
        expandedRowKeys: [],
        pidField: "parentCategoryId"

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
          this.form.setFieldsValue(pick(this.model, 'parentCategoryId', 'categoryName', 'udf1', 'udf2', 'udf3'))
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
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values);
            let new_pid = this.model[this.pidField]
            console.log("表单提交数据", formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.submitSuccess(formData, old_pid == new_pid)
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
        this.form.setFieldsValue(pick(row, 'parentCategoryId', 'categoryName', 'udf1', 'udf2', 'udf3'))
      },
      submitSuccess(formData, flag) {
        if (!formData.id) {
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys = []
          this.getExpandKeysByPid(formData[this.pidField], treeData, treeData)
          this.$emit('ok', formData, this.expandedRowKeys.reverse());
        } else {
          this.$emit('ok', formData, flag);
        }
      },
      getExpandKeysByPid(pid, arr, all) {
        if (pid && arr && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].key == pid) {
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'], all, all)
            } else {
              this.getExpandKeysByPid(pid, arr[i].children, all)
            }
          }
        }
      }


    }
  }
</script>