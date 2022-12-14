import { createApp } from 'vue'
import App from './App.vue'
import router from './router';
import state from './state';
import axios from 'axios';
import { ElButton,ElIcon,ElScrollbar,ElCard,ElNotification,ElMessage,ElInput,ElMessageBox } from 'element-plus';
import 'element-plus/dist/index.css'
import config from './api/config';
import SocketIO from './api/socket';
import {SOCKET} from '@/api/config';
import registerDirectives from '@/components/directives'

//https://antonreshetov.github.io/vue-unicons/

const app = createApp(App);
    app.config.globalProperties.$axios=axios;
    app.config.globalProperties.$app_config=config;
    app.config.globalProperties.$log=ElNotification;
    app.config.globalProperties.$message=ElMessage;
    app.config.globalProperties.$confirm=ElMessageBox.confirm;

    window.confirm = ElMessageBox.confirm;

axios.defaults.headers.common['Content-Type'] = 'application/json;charset=UTF-8';

registerDirectives(app);

app
    .use(state)
    .use(router)
    .use(ElButton)
    .use(ElIcon)
    .use(ElScrollbar)
    .use(ElCard)
    .use(ElInput)
    .use(SocketIO,{
        connection: SOCKET,
        options:{
            query: 'userName=test&appKey=test&roomId=rabbit',
            transports:['websocket']
        }
    })
    .mount('#app');
