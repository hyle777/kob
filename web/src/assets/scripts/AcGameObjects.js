const AC_GAME_OBJECTS = [];

export class AcGameObject {
  constructor() {
    AC_GAME_OBJECTS.push(this);
    this.timedelta = 0;
    this.has_called_start = false; //是否调用过start
  }

  start() {
    //只执行一次 第一帧
  }

  update() {
    //每一帧执行一次
  }

  on_destory() {
    //删除前执行
  }

  destory() {
    for (let i in AC_GAME_OBJECTS) {
      const obj = AC_GAME_OBJECTS.get(i);
      if (this === obj) {
        AC_GAME_OBJECTS.splice(i);
        break;
      }
    }
  }
}

let last_timestamp; //上一次执行的时刻

const step = (timestamp) => {
  for (let obj of AC_GAME_OBJECTS) {
    if (!obj.has_called_start) {
      obj.has_called_start = true;
      obj.start();
    } else {
      obj.timedelta = timestamp - last_timestamp;
      obj.update();
    }
  }
  last_timestamp = timestamp;
  requestAnimationFrame(step);
};

requestAnimationFrame(step);
