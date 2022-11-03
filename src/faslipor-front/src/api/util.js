// 函数节流
export const throttle = function(fn, delay){
    var timer;
    return function () {
        var _this = this;
        var args = arguments;
        if (timer) {
            clearTimeout(timer);
        }
        timer = setTimeout(function () {
            fn.apply(_this, args);
        }, delay)
    }
}
 
// 函数防抖
export const shakeProof = (fn, time) => {
    var timer;
    return function(){
        var _this = this;
        var args = arguments;
        if(timer){
            clearTimeout(timer);
        }
        timer = setTimeout(function(){
            fn.apply(_this, args);// 用 apply 指向调用 shakeProof 方法的对象，相当于 _this.fn(args);
        }, time);
    }
}
 
export default {
    throttle,
    shakeProof
}