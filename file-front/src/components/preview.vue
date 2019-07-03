<template>
  <div>
    <div>
      <div style="float: left;">
        <el-button round
          @click="back"
          style="margin-top: 10px;">back</el-button>
      </div>
      <p style="float: right;">
        <el-button round
          @click="changePdfPage(0)"
          style="margin-top: 10px;">preview</el-button>
          {{currentPage}} / {{pageCount}}
        <el-button round
          @click="changePdfPage(1)"
          style="margin-top: 10px;">next</el-button>
        <el-input placeholder="跳转至" style="width: 100px; margin-left: 10px;"
        @change="handleJump" v-model="inputPage"></el-input>
      </p>
    </div>
    <pdf
      :src="src"
      :page="currentPage"
      @num-pages="pageCount=$event"
      @page-loaded="currentPage=$event"
      @loaded="loadPdfHandler"
      style="width: 100%; height: 100%;"
    ></pdf>
  </div>
</template>
<script>
import pdf from "vue-pdf";
export default {
  components: { pdf },
  data() {
    return {
      currentPage: 0, // pdf文件页码
      pageCount: 0, // pdf文件总页数
      fileType: "pdf", // 文件类型
      inputPage: ''
    };
  },
  methods: {
    // 改变PDF页码,val传过来区分上一页下一页的值,0上一页,1下一页
    changePdfPage(val) {
      // console.log(val)
      if (val === 0 && this.currentPage > 1) {
        this.currentPage--;
        // console.log(this.currentPage)
      }
      if (val === 1 && this.currentPage < this.pageCount) {
        this.currentPage++;
        // console.log(this.currentPage)
      }
    },

    // pdf加载时
    loadPdfHandler(e) {
      this.currentPage = 1; // 加载的时候先加载第一页
    },
    back() {
      this.$router.push('/file')
    },
    handleChange(val) {
      this.currentPage = val
    },
    handleJump(val) {
      var count = parseInt(this.inputPage)
      if (this.inputPage === '') {
        return
      } else if (count < 1 || count > this.pageCount) {
        return
      } else {
        this.currentPage = count
      }
    }
  },
  computed: {
    src: function() {
      return this.$store.state.pdfUrl
    }
  }
};
</script>
<style <style lang="scss" scoped>
.pdf{
  height: 100%;
  width: 100%;
}
.el-input__inner{
  border-radius: 24px;
}
</style>


