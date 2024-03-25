<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top: 20px">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width: 100%" />
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top: 20px">
          <div class="card-header">
            <span style="font-size: 130%">我的Bot</span>
            <button
              type="button"
              class="btn btn-primary float-end"
              data-bs-toggle="modal"
              data-bs-target="#addBotModal"
            >
              创建Bot
            </button>
            <!-- Button trigger modal -->

            <!-- Modal -->
            <div class="modal fade" id="addBotModal" tabindex="-1">
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">
                      创建Bot
                    </h1>
                    <button
                      type="button"
                      class="btn-close"
                      data-bs-dismiss="modal"
                      aria-label="Close"
                    ></button>
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label for="exampleFormControlInput1" class="form-label"
                        >Bot名称</label
                      >
                      <input
                        v-model="addBot.title"
                        type="email"
                        class="form-control"
                        id="exampleFormControlInput1"
                        placeholder="请输入Bot名称"
                      />
                    </div>
                    <div class="mb-3">
                      <label
                        for="exampleFormControlTextarea1"
                        class="form-label"
                        >Bot简介</label
                      >
                      <textarea
                        v-model="addBot.description"
                        class="form-control"
                        id="exampleFormControlTextarea1"
                        rows="3"
                        placeholder="请输入Bot简介"
                      ></textarea>
                    </div>
                    <div class="mb-3">
                      <label
                        for="exampleFormControlTextarea1"
                        class="form-label"
                        >Bot代码</label
                      >
                      <VAceEditor
                        v-model:value="addBot.content"
                        @init="editorInit"
                        lang="java"
                        theme="textmate"
                        style="height: 300px"
                        :options="{
                          enableBasicAutocompletion: true, //启用基本自动完成
                          enableSnippets: true, // 启用代码段
                          enableLiveAutocompletion: true, // 启用实时自动完成
                          fontSize: 18, //设置字号
                          tabSize: 4, // 标签大小
                          showPrintMargin: false, //去除编辑器里的竖线
                          highlightActiveLine: true,
                        }"
                      />
                    </div>
                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{ addBot.error_message }}</div>
                    <button
                      type="button"
                      class="btn btn-secondary"
                      data-bs-dismiss="modal"
                    >
                      取消
                    </button>
                    <button
                      type="button"
                      class="btn btn-primary"
                      @click="add_bot"
                    >
                      创建
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">
            <table class="table table-hover">
              <thead>
                <th>Bot名称</th>
                <th>创建时间</th>
                <th>操作</th>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td>{{ bot.title }}</td>
                  <td>{{ bot.createTime }}</td>
                  <td>
                    <button
                      type="button"
                      class="btn btn-success"
                      style="margin-right: 10px"
                      data-bs-toggle="modal"
                      :data-bs-target="'#update-bot-modal' + bot.id"
                    >
                      修改
                    </button>
                    <button
                      type="button"
                      class="btn btn-danger"
                      @click="remove_bot(bot)"
                    >
                      删除
                    </button>
                    <!-- Modal -->
                    <div
                      class="modal fade"
                      :id="'update-bot-modal' + bot.id"
                      tabindex="-1"
                    >
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">
                              修改Bot
                            </h1>
                            <button
                              type="button"
                              class="btn-close"
                              data-bs-dismiss="modal"
                              aria-label="Close"
                            ></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label
                                for="exampleFormControlInput1"
                                class="form-label"
                                >Bot名称</label
                              >
                              <input
                                v-model="bot.title"
                                type="email"
                                class="form-control"
                                id="exampleFormControlInput1"
                                placeholder="请输入Bot名称"
                              />
                            </div>
                            <div class="mb-3">
                              <label
                                for="exampleFormControlTextarea1"
                                class="form-label"
                                >Bot简介</label
                              >
                              <textarea
                                v-model="bot.description"
                                class="form-control"
                                id="exampleFormControlTextarea1"
                                rows="3"
                                placeholder="请输入Bot简介"
                              ></textarea>
                            </div>
                            <div class="mb-3">
                              <label
                                for="exampleFormControlTextarea1"
                                class="form-label"
                                >Bot代码</label
                              >
                              <VAceEditor
                                v-model:value="bot.content"
                                @init="editorInit"
                                lang="java"
                                theme="textmate"
                                style="height: 300px"
                                :options="{
                                  enableBasicAutocompletion: true, //启用基本自动完成
                                  enableSnippets: true, // 启用代码段
                                  enableLiveAutocompletion: true, // 启用实时自动完成
                                  fontSize: 18, //设置字号
                                  tabSize: 4, // 标签大小
                                  showPrintMargin: false, //去除编辑器里的竖线
                                  highlightActiveLine: true,
                                }"
                              />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error-message">
                              {{ updateErrorMessage }}
                            </div>
                            <button
                              type="button"
                              class="btn btn-secondary"
                              data-bs-dismiss="modal"
                            >
                              取消
                            </button>
                            <button
                              type="button"
                              class="btn btn-primary"
                              @click="update_bot(bot)"
                            >
                              修改
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from "jquery";

import { reactive, ref } from "vue";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { VAceEditor } from "vue3-ace-editor";
import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/mode-json";
import "ace-builds/src-noconflict/theme-chrome";
import "ace-builds/src-noconflict/ext-language_tools";
import "ace-builds/src-noconflict/snippets/java";

import ace from "ace-builds";
ace.config.set(
  "basePath",
  "https://cdn.jsdelivr.net/npm/ace-builds@" +
    require("ace-builds").version +
    "/src-noconflict/"
);

export default {
  components: {
    VAceEditor,
  },
  setup() {
    const store = useStore();

    let addBot = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });

    let bots = ref([]);
    const refresh_bots = () => {
      $.ajax({
        url: "https://app6715.acapp.acwing.com.cn/api/user/bot/getlist",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
          console.log("bot list");
          console.log(bots.value);
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    refresh_bots();

    const add_bot = () => {
      addBot.error_message = "";
      $.ajax({
        url: "https://app6715.acapp.acwing.com.cn/api/user/bot/add/",
        type: "post",
        data: {
          title: addBot.title,
          description: addBot.description,
          content: addBot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "add success") {
            addBot.title = "";
            addBot.description = "";
            addBot.content = "";
            Modal.getInstance("#addBotModal").hide();
            refresh_bots();
          } else {
            addBot.error_message = resp;
          }
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    const remove_bot = (bot) => {
      $.ajax({
        url: "https://app6715.acapp.acwing.com.cn/api/user/bot/remove/",
        type: "post",
        data: {
          id: bot.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          refresh_bots();
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    let updateErrorMessage = ref("");
    const update_bot = (bot) => {
      updateErrorMessage.value = "";
      $.ajax({
        url: "https://app6715.acapp.acwing.com.cn/api/user/bot/update/",
        type: "post",
        data: {
          id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          if (resp.error_message === "update success") {
            Modal.getInstance("#update-bot-modal" + bot.id).hide();
            refresh_bots();
          } else {
            updateErrorMessage.value = resp.error_message;
          }
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    return {
      bots,
      addBot,
      add_bot,
      remove_bot,
      update_bot,
      updateErrorMessage,
    };
    /** 


       
    
     
    $.ajax({
      url: "https://app6715.acapp.acwing.com.cn/api/user/bot/getlist",
      type: "get",
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      },
    });
    */
  },
};
</script>
<style scoped>
div.error-message {
  color: red;
}
</style>
