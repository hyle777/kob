import { AcGameObject } from "./AcGameObjects";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
  constructor(info, gameMap) {
    super();
    this.id = info.id;
    this.color = info.color;
    this.gameMap = gameMap;
    this.cells = [new Cell(info.r, info.c)]; //cells存放蛇的身体 cells[0]存放蛇头

    this.speed = 5; //每秒中移动5格

    //根据用户操作来更新状态和获取方向
    this.direction = -1; //-1表示没有指令 0123表示上右下左
    this.status = "idle"; // idle表示静止 move表示正在移动 die表示死亡

    this.next_cell = null; //下一步目标位置
    this.dr = [-1, 0, 1, 0]; //4个方向的偏移量
    this.dc = [0, 1, 0, -1];
    this.step = 0; //回合数

    this.eps = 1e-2; //判断两个浮点数是否相等的误差

    // 蛇眼睛朝向
    if (this.id === 0) this.eye_direction = 0;
    else this.eye_direction = 2;

    //蛇眼睛偏移量
    this.eye_dx = [
      [-1, 1],
      [1, 1],
      [-1, 1],
      [-1, -1],
    ];
    this.eye_dy = [
      [-1, -1],
      [1, -1],
      [1, 1],
      [1, -1],
    ];
  }
  check_tail_increasing() {
    if (this.step <= 3) {
      return true;
    }
    if (this.step % 3 === 1) {
      return true;
    }
    return false;
  }
  set_direction(d) {
    this.direction = d;
    this.eye_direction = d;
  }
  //获取蛇的下一步
  next_step() {
    const d = this.direction;
    this.next_cell = new Cell(
      this.cells[0].r + this.dr[d],
      this.cells[0].c + this.dc[d]
    );
    this.direction = -1; //清空方向
    this.status = "move";
    this.step++;
    if (!this.gameMap.check_valid(this.next_cell)) {
      //操作非法
      this.status = "die";
    }
  }
  update_move() {
    const k = this.cells.length;
    for (let i = k; i >= 1; i--) {
      //蛇的每格往后移动一位 使用json实现深拷贝
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
    }
    if (this.next_cell !== null) {
      this.cells[0] = new Cell(this.next_cell.r, this.next_cell.c);
      this.status = "idle";
      this.next_cell = null;
    }
    if (!this.check_tail_increasing()) {
      this.cells.pop();
    }
  }
  update() {
    if (this.status === "move") {
      this.update_move();
    }
    this.render();
  }
  render() {
    const ctx = this.gameMap.ctx;
    const L = this.gameMap.L;
    ctx.fillStyle = this.color;
    if (this.status === "die") {
      ctx.fillStyle = "white";
    }
    for (let cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, (L / 2) * 0.8, 0, Math.PI * 2);
      ctx.fill();
    }
    //矩形填充
    for (let i = 1; i < this.cells.length; i++) {
      let a = this.cells[i],
        b = this.cells[i - 1];
      if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) {
        continue;
      }
      if (Math.abs(a.x - b.x) < this.eps) {
        //横坐标相同 竖的矩形
        ctx.fillRect(
          (a.x - 0.5 + 0.1) * L,
          Math.min(a.y, b.y) * L,
          L * 0.8,
          Math.abs(a.y - b.y) * L
        );
      }
      if (Math.abs(a.y - b.y) < this.eps) {
        ctx.fillRect(
          Math.min(a.x, b.x) * L,
          (a.y - 0.5 + 0.1) * L,
          Math.abs(a.x - b.x) * L,
          L * 0.8
        );
      }
    }

    ctx.fillStyle = "black";
    for (let i = 0; i < 2; i++) {
      const eye_x = this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15;
      const eye_y = this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15;
      ctx.beginPath();
      ctx.arc(eye_x * L, eye_y * L, (L / 2) * 0.1, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}
