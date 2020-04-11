<template>
  <a-select
    showSearch
    optionFilterProp="children"
    style="width: 200px"
    @focus="handleFocus"
    @blur="handleBlur"
    @change="handleChange"
    :filterOption="filterOption"
    :value="sValue"
    :getPopupContainer="(triggerNode)=>{ return triggerNode.parentNode}">
    <a-select-option
      v-for="(item,index) in options"
      :key="index"
      :value="item.value">
      {{ item.text || item.label }}
    </a-select-option>
  </a-select>
</template>
<script>

  export default {
  name: 'JSelectSearch',
  props:{
    options:{
      type: Array,
      required: true
    },
    readOnly:{
      type: Boolean,
      required: false,
      default: false
    },
  },
  data(){
    return {
      arrayValue:"",
      url:{
        list: "/area/wmsArea/queryList",
      },
      sValue:''
    }
  },
  watch:{
    value (val) {
      if(!val){
        this.arrayValue = []
      }else{
        this.arrayValue = this.value.split(",")
      }
    }
  },
  methods: {
    handleChange (value) {
      this.sValue = value;
      console.log("ddddaaa"+ this.sValue);
      console.log(`selected ${value}`);
    },
    handleBlur() {
      console.log('blur');
    },
    handleFocus() {
      // getAction(this.url.list).then((res)=>{
      //   if(res.success){
      //     console.log(res.result);
      //     let temp = {};
      //     this.options=[];
      //     res.result.list.filter(item => {
      //       if (item.id != "") {
      //         temp={text: item.warehouseName, value: item.id}
      //         this.options.push(temp);
      //       }
      //     });
      //     this.$emit('getaaa');
      //   }else{
      //
      //   }
      // });
      console.log('focus');
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
    }
  }
}
</script>