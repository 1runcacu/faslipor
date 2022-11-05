import { createStore } from "vuex";
 

const store = {
    state: {
        info:{
            rid:"",
            uid:""
        },
        params:{},
        rooms:[]
    },
    mutations: {
        setParams(state,params={}){
            state.params = params;
        },
        setRooms(state,rooms=[]){
            state.rooms = rooms;
        }
    },
    actions: {

    },
    getters: {

    },
    modules: {
        
    }
}

export default createStore(store)
