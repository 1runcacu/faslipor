import { throttle,lock,unlock } from "@/api/util"; //节流函数不再展示，不要直接去除即可，在下面样式引用去除即可

const map = {};
const Layout = [];

function sortLayout(id){
  let index = Layout.indexOf(id);
  Layout.splice(index,1);
  Layout.push(id);
  Layout.forEach((key,index)=>{
    map[key].el.style.zIndex = index;
  });
}

function changeDevice(e){
  if(/touch/.test(e.type)){
    if(e.targetTouches&&e.targetTouches.length>0){
      return e.targetTouches[0];
    }
  }
  return e; 
}

let MODE = 1;

export function setMODE(v=0){
  MODE = v;
}

const directives = {
  drag: {
    mounted(el, binding) {
      // 如果传递了false就不启用指令，反之true undefined null 不传 则启动
      if (!binding.value && (binding.value ?? "") !== "") return;
      // 拖拽实现
      const odiv = el.parentNode;
      const id = Math.random().toString(36).substr(3,8);
      map[id] = {
        el:odiv
      }
      Layout.push(id);
      odiv.style.zIndex = Layout.length;
      const onmousedown = (eve) => {
        lock();
        sortLayout(id);
        eve = eve || window.event;
        eve = changeDevice(eve);
        const mx = eve.pageX; //鼠标点击时的坐标
        const my = eve.pageY; //鼠标点击时的坐标
        const dleft = odiv.offsetLeft; //窗口初始位置
        const dtop = odiv.offsetTop;
        const clientWidth = document.documentElement.clientWidth; //页面的宽
        const oWidth = odiv.clientWidth; //窗口的宽
        const maxX = clientWidth - oWidth; // x轴能移动的最大距离
        const clientHeight = document.documentElement.clientHeight; //页面的高
        const oHeight = odiv.clientHeight; //窗口的高度
        const maxY = clientHeight - oHeight; //y轴能移动的最大距离
        // console.log(mx,my,dleft,dtop);
        const onmousemove = (e) => {
          // const x = e.pageX;
          // const y = e.pageY;
          e = changeDevice(e);
          const [x,y] = [e.pageX,e.pageY];
          let left = x - mx + dleft; //移动后的新位置
          let top = y - my + dtop; //移动后的新位置
          if (left < 0) left = 0;
          if (left > maxX) left = maxX;
          if (top < 0) top = 0;
          if (top > maxY) top = maxY;
          if(left < 3) left = 0;
          if(top < 3) top = 0;
          odiv.style.left = left + "px";
          odiv.style.top = top + "px";
          odiv.style.marginLeft = 0;
          odiv.style.marginTop = 0;
        };
        // document.addEventListener("mousemove",onmousemove,false);
        // document.addEventListener("touchmove",onmousemove,false);
        document.onmousemove = onmousemove;
        document.ontouchmove = onmousemove;
         const onmouseup = () => {
          // document.removeEventListener("mousemove",onmousemove);
          // document.removeEventListener("touchmove",onmousemove);
          document.onmousemove = null;
          document.ontouchmove = null;
          unlock();
        };
        document.onmouseup = onmouseup;
        document.ontouchend = onmouseup;
      };
      // el.addEventListener("mousedown",onmousedown,false);
      // el.addEventListener("touchstart",onmousedown,false);
      el.onmousedown = onmousedown;
      el.ontouchstart = onmousedown;
    }
  },
  resize: {
    mounted(el, binding) {
      // 如果传递了false就不启用指令，反之true undefined null 不传 则启动
      if (!binding.value && (binding.value ?? "") !== "") return;
      // 给选定的元素绑定name属性 设置name为resize区分只有该元素可以缩放
      el.name = "resize";
      // 八个方位对应
      const mouseDir = {
        top: "n-resize", //上
        bottom: "s-resize", //下
        left: "w-resize", //左
        right: "e-resize", //右
        topright: "ne-resize", //右上
        topleft: "nw-resize", //左上
        bottomleft: "sw-resize", //左下
        bottomright: "se-resize" //右下
      };
      // 记录被修改元素的原始位置大小，以及变更方向
      const pos = { width: 0, height: 0, top: 0, left: 0, x: 0, y: 0, dir: "" };
      // 获取鼠标所在方位
      const getDirection = (ev) => {
        let dir = "";
        ev = changeDevice(ev);
        const xP = ev.offsetX;
        const yP = ev.offsetY;
        const offset = 8; //内边距为多少时触发
        // 计算是那个方位
        if (yP < offset) dir += "top";
        else if (yP > ev.toElement.clientHeight - offset) dir += "bottom";
        if (xP < offset) dir += "left";
        else if (xP > ev.toElement.clientWidth - offset) dir += "right";
        return dir;
      };
      // 计算移动距离
      const computedDistance = (pre, cur) => {
        return [cur.x - pre.x, cur.y - pre.y];
      };
      // 变更尺寸方法
      const changeSize = (e) => {
        e = changeDevice(e);
        // 两个点之间的差值，计算鼠标位移数值
        const [disX, disY] = computedDistance(
          { x: pos.x, y: pos.y },
          { x: e.pageX, y: e.pageY }
        );
        const addWid = pos.width + disX;
        const subWid = pos.width - disX;
        const addHig = pos.height + disY;
        const subHig = pos.height - disY;
        const minX = 200;
        const minY = 200;
        //上下左右的变更方法
        const top = () => {
          if (subHig <= minY) return; //不能小于最小最高
          let t = pos.top + disY;
          if(t>=0){
            el.style.height = subHig + "px";
            el.style.top = t + "px";
          }else{
            el.style.top = 0 + "px";
          }
        }; // 上
        const bottom = () => {
          el.style.height = addHig + "px";
        }; // 下
        const left = () => {
          if (subWid <= minX) return; //不能小于最小宽度
          let t = pos.left + disX;
          if(t>=0){
            el.style.left = t + "px";
            el.style.width = subWid + "px";
          }else{
            el.style.left = 0 + "px";
          }
        }; // 左
        const right = () => {
          el.style.width = addWid + "px";
        }; // 右
        // 变更方位及其修改方法映射
        const doFn = {
          top, //上
          bottom, //下
          left, //左
          right, //右
          topright: () => {
            top();
            right();
          }, //右上
          topleft: () => {
            top();
            left();
          }, //左上
          bottomleft: () => {
            bottom();
            left();
          }, //左下
          bottomright: () => {
            bottom();
            right();
          } //右下
        };
        doFn[pos.dir]();
      };
      //鼠标按下 触发变更事件
      const onmousedown = (e) => {
        if (e.target.name !== "resize") return;
        e = changeDevice(e);
        let d = getDirection(e);
        //当位置为四个边和四个角才开启尺寸修改
        if (mouseDir[d]) {
          pos.width = el.clientWidth;
          pos.height = el.clientHeight;
          pos.top = el.offsetTop;
          pos.left = el.offsetLeft;
          pos.x = e.pageX;
          pos.y = e.pageY;
          pos.dir = d;
          // document.addEventListener("mousemove",changeSize,false);
          // document.addEventListener("touchmove",changeSize,false);
          document.onmousemove = changeSize;
          document.ontouchmove = changeSize;
        }
        document.onmouseup = resetData;
        document.ontouchend = resetData;
      };
      // el.addEventListener("mousedown",onmousedown,false);
      // el.addEventListener("touchstart",onmousedown,false);
      el.onmousedown = onmousedown;
      el.ontouchstart = onmousedown;
      /** 鼠标样式变更 */
      const changeShowCursor = throttle((e) => {
        e.preventDefault();
        el.style.cursor = "default"; //先恢复鼠标默认
        if (e.target.name !== "resize") return;
        // 修改鼠标显示效果
        let d = getDirection(e);
        // 确定是某个方位的动向
        el.style.cursor = mouseDir[d] || "default";
      }, 0); //节流0.2s
      // el.addEventListener("mousemove",changeShowCursor,false);
      // el.addEventListener("touchmove",changeShowCursor,false);
      el.onmousemove = changeShowCursor; //监听根元素上移动的鼠标事件
      el.ontouchmove = changeShowCursor;
      //数据重置
      var resetData = () => {
        pos.width = 0;
        pos.height = 0;
        pos.top = 0;
        pos.left = 0;
        pos.x = 0;
        pos.y = 0;
        pos.dir = "";
        // document.removeEventListener("mousemove",changeSize);
        // document.removeEventListener("touchmove",changeSize);
        document.onmousemove = null;
        document.ontouchmove = null;
      };
    }
  },
  click2:{
    mounted:(()=>{
      let flag = 0;
      return function(el,binding){
        el.addEventListener("click",()=>{
          switch(MODE){
            case 0:
              flag++;
              setTimeout(() => {
                flag = 0;
              }, 500);
              if(flag==2){
                try{
                  binding.value();
                }catch(err){}
              }
              break;
            default:
              try{
                binding.value();
              }catch(err){}
          }
        })
      }
    })()
  }
};

export default (app) => {
  //批量注册指令
  Object.entries(directives).forEach(([key, fn]) => {
    app.directive(key, fn);
  });
};
