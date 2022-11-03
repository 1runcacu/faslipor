export function debounce(func, wait) {
    let timeout;

    return function () {
        let context = this;
        let args = arguments;

        clearTimeout(timeout)
        timeout = setTimeout(function(){
            func.apply(context, args)
        }, wait);
    }
}

export function throttle(func, wait) {
    let context, args;
    let previous = 0;
  
    return function() {
        let now = +new Date();
        context = this;
        args = arguments;
        if (now - previous > wait) {
            func.apply(context, args);
            previous = now;
        }
    }
}