<template>
  <div>
    <el-table
      :data="tableData.filter(data => !search || data.username.toLowerCase().includes(search.toLowerCase()))"
      style="width: 100%"
    >
      <el-table-column label="Name" prop="username" />
      <el-table-column label="Upload">
        <template slot-scope="scope">        
          <el-checkbox label="upload" :checked="scope.row.upload" @change="checked => handleUploadChange(checked, scope.row)">
            <span>{{scope.row.upload}}</span>
          </el-checkbox>
        </template>
      </el-table-column>
      <el-table-column label="Download">
        <template slot-scope="scope">        
          <el-checkbox label="download" :checked="scope.row.download" @change="checked => handleDownloadChange(checked, scope.row)">
            <span>{{scope.row.download}}</span>
          </el-checkbox>
        </template>
      </el-table-column>
      <el-table-column label="Delete">
        <template slot-scope="scope">        
          <el-checkbox label="delete" :checked="scope.row.delete" @change="checked => handleDownloadChange(checked, scope.row)">
            <span>{{scope.row.delete}}</span>
          </el-checkbox>
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template slot="header" slot-scope="scope">
          <el-input v-model="search" size="mini" placeholder="输入关键字搜索"/>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="success" round
    @click="handleChange"
    style="margin-top: 10px;">提交</el-button>
  </div>
</template>

<script>
import * as api from "@/api/api.js";
import axios from 'axios';

export default {
  data() {
    return {
      tableData: [],
      search: ""
    };
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    initTable() {
      api.userList().then(result => {
        this.tableData = result.data.userlist
        console.log(result);
        
      });
    },
    handleUploadChange(checked, r) {
      if (checked) {
        r.upload = true
      } else {
        r.upload = false
      }
    },
    handleDownloadChange(checked, r) {
      if (checked) {
        r.download = true
      } else {
        r.download = false
      }
    },
    handleChange() {
      axios({
        url: 'http://39.106.73.27/apis/change',
        method: 'post',
        data: {
          userlist: this.tableData
        }
      }).then(res => {
        this.$message("提交成功")
      })
    }
  },
  created() {
    this.initTable()
  }
};
</script>
