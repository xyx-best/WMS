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
            <a-form-item label="库存状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['stockState']" :trigger-change="true"
                                 dictCode="stock_state" placeholder="请选择库存状态"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="入库时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择入库时间" v-decorator="[ 'stockinTime', validatorRules.stockinTime]"
                      :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="区域ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                showSearch
                placeholder="请选择区域ID"
                optionFilterProp="children"
                :filterOption="filterOption"
                v-decorator="[ 'areaId', validatorRules.areaId]"
                @change="handleAreaIdChange"
                :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                <a-select-option
                  v-for="(item,index) in arrayAreaValue"
                  :key="index"
                  :value="item.value">
                  {{ item.text || item.label }}
                </a-select-option>
              </a-select>
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
            <a-form-item label="货位ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-dict-select-tag type="list" v-decorator="['locId']" :trigger-change="true" dictCode=""-->
              <!--placeholder="请选择货位ID"/>-->
              <a-select
                showSearch
                placeholder="请选择货位ID"
                optionFilterProp="children"
                :filterOption="filterOption"
                v-decorator="[ 'locId']"
                @change="handleLocIdChange"
                :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                <a-select-option
                  v-for="(item,index) in arrayLocValue"
                  :key="index"
                  :value="item.value">
                  {{ item.text || item.label }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货位编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'locCode', validatorRules.locCode]" placeholder="请输入货位编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'locName', validatorRules.locName]" placeholder="请输入货位名称"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-dict-select-tag type="list" v-decorator="['goodsId']" :trigger-change="true" dictCode=""-->
                                 <!--placeholder="请选择货物ID"/>-->
              <a-select
                showSearch
                placeholder="请选择货物ID"
                optionFilterProp="children"
                :filterOption="filterOption"
                v-decorator="[ 'goodsId']"
                @change="handleGoodsIdChange"
                :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
                <a-select-option
                  v-for="(item,index) in arrayGoodsValue"
                  :key="index"
                  :value="item.value">
                  {{ item.text || item.label }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsCode', validatorRules.goodsCode]" placeholder="请输入货物编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsName', validatorRules.goodsName]" placeholder="请输入货物名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsUnit']" :trigger-change="true" dictCode="goods_unit"
                                 placeholder="请选择货物单位"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsType']" :trigger-change="true" dictCode="goods_type"
                                 placeholder="请选择货物类别"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物花色" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsColor']" :trigger-change="true" dictCode="color"
                                 placeholder="请选择货物花色"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsBatchnumber', validatorRules.goodsBatchnumber]"
                       placeholder="请输入货物批号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'goodsQuantity', validatorRules.goodsQuantity]" placeholder="请输入货物数量"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="6">
          <a-col :span="12">
            <a-form-item label="货物等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['goodsLevel']" :trigger-change="true" dictCode="goods_level"
                                 placeholder="请选择货物等级"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="货物规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'goodsSize', validatorRules.goodsSize]" placeholder="请输入货物规格"></a-input>
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
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {getAction} from "../../../api/manage";
  import JSearchSelectTag from "@/components/dict/JSearchSelectTag"

  export default {
    name: "WmsStockModal",
    components: {
      JDate,
      JDictSelectTag,
      JSearchSelectTag
    },
    data() {
      return {
        selectValueaa: "",
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
        visible: false,
        model: {},
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
          stockId: {},
          stockState: {},
          areaId: {},
          areaCode: {},
          locId: {},
          locCode: {},
          goodsId: {},
          goodsCode: {},
          goodsName: {},
          goodsUnit: {},
          goodsType: {},
          goodsColor: {},
          goodsBatchnumber: {},
          goodsQuantity: {},
          goodsLevel: {},
          stockinTime: {},
        },
        url: {
          add: "/stock/wmsStock/add",
          edit: "/stock/wmsStock/edit",
          queryAreaList: "/stock/wmsStock/queryAreaList",
          queryLocList: "/loc/wmsLoc/queryList",
          queryGoodsList: "/goods/wmsGoods/queryList",
        },
        arrayAreaValue: [],
        arrayLocValue: [],
        arrayGoodsValue: [],

      }
    },
    created() {
      getAction(this.url.queryAreaList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayAreaValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.areaId, value: item.areaId, code: item.areaCode}
              this.arrayAreaValue.push(temp);
            }
          })
        }
      });
      getAction(this.url.queryLocList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayLocValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {text: item.locId, value: item.locId, code: item.locCode, name: item.locName}
              this.arrayLocValue.push(temp);
            }
          })
        }
      });
      getAction(this.url.queryGoodsList).then((res) => {
        if (res.success) {
          let temp = {};
          this.arrayGoodsValue = [];
          res.result.list.filter(item => {
            if (item.id != "") {
              temp = {
                text: item.goodsId,
                value: item.goodsId,
                code: item.goodsCode,
                unit: item.goodsUnit,
                type: item.goodsType,
                color: item.goodsColor,
                size: item.goodsSize
              }
              this.arrayGoodsValue.push(temp);
            }
          })
        }
      });
    },
    methods: {
      add() {
        this.edit({});
        this.$nextTick(() => {
          this.form.setFieldsValue({
            stockState: '0',
          })
        });
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'stockState', 'areaId', 'areaCode', 'areaName', 'locId', 'locCode', 'locName', 'goodsId', 'goodsCode', 'goodsName', 'goodsUnit', 'goodsType', 'goodsColor', 'goodsBatchnumber', 'goodsQuantity', 'goodsLevel', 'stockinTime'))
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
        this.form.setFieldsValue(pick(row,  'stockState', 'areaId', 'areaCode', 'areaName', 'locId', 'locCode', 'locName', 'goodsId', 'goodsCode', 'goodsName', 'goodsUnit', 'goodsType', 'goodsColor', 'goodsBatchnumber', 'goodsQuantity', 'goodsLevel', 'stockinTime'))
      },
      handleFocus(url) {
      },
      filterOption(input, option) {
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      },

      handleAreaIdChange(value) {
        this.arrayAreaValue.filter(item => {
          if (item.value == value) {
            this.form.setFieldsValue({
              areaCode: item.code,
            });
          }
        })
        // let values = {'age': age}
        // that.triggleChangeValues(values)
      },
      handleLocIdChange(value) {
        this.arrayLocValue.filter(item => {
          if (item.value == value) {
            this.form.setFieldsValue({
              locCode: item.code, locName: item.name,
            });
          }
        })
        // let values = {'age': age}
        // that.triggleChangeValues(values)
      },
      handleGoodsIdChange(value) {
        this.arrayGoodsValue.filter(item => {
          if (item.value == value) {
            this.form.setFieldsValue({
              goodsCode: item.code, goodsName: item.name,goodsUnit:item.unit,goodsType:item.type,goodsColor:item.color,goodsSize:item.size
            });
          }
        })
        // let values = {'age': age}
        // that.triggleChangeValues(values)
      },

    }
  }
</script>