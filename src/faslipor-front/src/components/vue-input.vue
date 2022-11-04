<template>
    <div class="wrapper">
        <div class="input-data">
            <input type="text" required="" v-model="modelValue"/>
            <div class="underline"></div>
            <label>{{hint}}</label>
        </div>
    </div>
</template>

<script>
export default{
    props:{
        modelValue: String,
        hint:{
            type:String,
            default:""
        }
    }
}

</script>

<style>

.wrapper{
    width: calc(100% - 30px);
    background-color: rgba(255, 255, 255, 0.2);
    padding: 10px;
    box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
    border-radius: 8px;
}
.wrapper .input-data{
    width: 100%;
    height: 40px;
    position: relative;
}
.wrapper .input-data input{
    width: 100%;
    height: 100%;
    border: none;
    border-radius: 0 10px 0 0;
    border-bottom: 2px solid silver;
    font-size: 17px;
    background-color: rgba(255,255,255,0);
    outline: none;
}
.input-data input:focus ~ label,
.input-data input:valid ~ label{
    transform: translateY(-20px);
    font-size: 15px;
    color: #421a1a;
}
.wrapper .input-data label{
    position: absolute;
    bottom: 10px;
    left: 0;
    color: grey;
    pointer-events: none;
    transition: all 0.3s ease;
}
.wrapper .input-data .underline{
    position: absolute;
    bottom: 0px;
    height: 2px;
    width: 100%;
    background-color: rgba(255, 255, 255, 0);
}
.input-data .underline:before{
    position: absolute;
    content: "";
    height: 100%;
    width: 100%;
    background: #572e2e;
    transform: scaleX(0);
    transition:transform 0.3s ease;
}
.input-data input:focus ~ .underline:before,
.input-data input:valid ~ .underline:before{
    transform: scaleX(1);
}

</style>