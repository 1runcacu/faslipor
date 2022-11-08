import { createStore } from "vuex";
 

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
        }
    },
    mutations: {
        setParams(state,params={}){
            state.params = params;
        },
        setRooms(state,rooms=[]){
            state.rooms = rooms;
        },
        setWindow(state,data=window){
            const {innerHeight,innerWidth} = data;
            state.window.innerHeight = innerHeight;
            state.window.innerWidth = innerWidth;
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
