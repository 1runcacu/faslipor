import { createStore } from "vuex";

const PMS = {};

let allow = false;

const store = {
    state: {
        info:{
            rid:"",
            uid:""
        },
        params:{},
        rooms:[],
        window:{
            innerHeight:window.innerHeight,
            innerWidth:window.innerWidth
        },
        uid:null
    },
    mutations: {
        setParams(state,params={}){
            state.params = params;
            if(params&&params.user&&state.uid){
                params.user.uid = state.uid;
            }
            Object.assign(PMS,params);
        },
        setRooms(state,rooms=[]){
            state.rooms = rooms;
        },
        setWindow(state,data=window){
            const {innerHeight,innerWidth} = data;
            state.window.innerHeight = innerHeight;
            state.window.innerWidth = innerWidth;
        },
        lock(state,uid){
            state.uid = uid;
        }
    },
    actions: {

    },
    getters: {

    },
    modules: {
        
    }
}

export const getParams = ()=>PMS;

export const setAllow = (v=true)=>{
    allow = v;
}

export const getAllow = ()=>{
    return allow;
}

export default createStore(store)
