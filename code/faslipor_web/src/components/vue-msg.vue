<template>
    <div class="MSG" :class="{'LYOUT':uid===uuid}">
        <img src="@/assets/logo.png"/>
        <div class="ctx" :class="{'user':uid===uuid}">
            <div style="display: flex;margin-bottom: 4px;"><b>{{name||uid}}</b>&nbsp;&nbsp;<el-tag v-if="s">管理员</el-tag></div>
            <div class="message-right" v-if="uid===uuid">{{message}}</div>
            <div class="message-left" v-if="uid!==uuid">{{message}}</div>
        </div>
    </div>
</template>

<script>
import { ElTag } from 'element-plus';

export default{
    props:{
        message:String,
        uid:String,
        uuid:String,
        name:String,
        s:{
            type: Boolean,
            default:false,
        }
    },
    components:{
        ElTag
    },
}

</script>

<style>
.MSG{
    max-width: calc(100% - 20px);
    display: flex;
    align-items: flex-start;
    /* flex-direction: row-reverse; */
    padding: 10px;
}

.LYOUT{
    flex-direction: row-reverse;
}

.MSG>img{
    width: 30px;
    height: 30px;
    border: 1px solid gray;
    border-radius: 1rem;
}
.MSG>.ctx{
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    margin: 3px;
}

.MSG>.user{
    align-items: flex-end !important;
}

.message-right{
    border-radius:  10px 0 10px 10px;
    padding: 5px;
    box-shadow: 2px 2px 10px #55A8FD;
    background-color: #55A8FD;
}

.message-left{
    border-radius: 0 10px 10px 10px;
    padding: 5px;
    box-shadow: 2px 2px 10px #635a5a;
    background-color: #FFFFFF;
}


</style>