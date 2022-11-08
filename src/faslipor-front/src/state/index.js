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
            innerHeight:window.innerHeight+"px",
            innerWidth:window.innerWidth+"px"
        }
    },
    mutations: {
        setParams(state,params={}){
            state.params = params;
        },
        setRooms(state,rooms=[]){
            state.rooms = rooms;
        },
        setWindow(state){
            const {innerHeight,innerWidth} = window;
            state.window.innerHeight = innerHeight + "px";
            state.window.innerWidth = innerWidth + "px";
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
