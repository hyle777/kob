import { AcGameObject } from "./AcGameObjects";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
  constructor(ctx, parent) {
    super();
    this.ctx = ctx;
    this.parent = parent;

    this.L = 0;
    this.rows = 13;
    this.cols = 14;

    this.inner_walls_count = 20;
    this.walls = [];
    this.snakes = [
      new Snake({ id: 0, r: this.rows - 2, c: 1, color: "#4285F4" }, this),
      new Snake({ id: 1, r: 1, c: this.cols - 2, color: "#EA4335" }, this),
    ];
  }

  /**
   * 检测目标位置是否合法，有没有撞到两条蛇的身体和墙
   */
  check_valid(cell) {
    for (const wall of this.walls) {
      if (wall.r === cell.r && wall.c === cell.c) {
        return false;
      }
    }
    for (const snake of this.snakes) {
      let k = snake.cells.length;
      if (!snake.check_tail_increasing()) {
        k--;
      }
      for (let i = 0; i < k; i++) {
        if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
          return false;
        }
      }
    }
    return true;
  }
  add_listening_event() {
    this.ctx.canvas.focus();
    const [snake1, snake2] = this.snakes;
    this.ctx.canvas.addEventListener("keydown", (e) => {
      if (e.key === "w") snake1.set_direction(0);
      else if (e.key === "d") snake1.set_direction(1);
      else if (e.key === "s") snake1.set_direction(2);
      else if (e.key === "a") snake1.set_direction(3);
      else if (e.key === "ArrowUp") snake2.set_direction(0);
      else if (e.key === "ArrowRight") snake2.set_direction(1);
      else if (e.key === "ArrowDown") snake2.set_direction(2);
      else if (e.key === "ArrowLeft") snake2.set_direction(3);
    });
  }
  //让两条蛇进入下一回合
  next_step() {
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }
  check_ready() {
    //判断两条蛇是否准备好下一回合了
    for (const snake of this.snakes) {
      if (snake.status !== "idle") {
        return false;
      }
      if (snake.direction === -1) {
        return false;
      }
    }
    return true;
  }
  check_connectivity(g, sx, sy, tx, ty) {
    if (sx === tx && sy === ty) return true;
    g[sx][sy] = true;
    let dx = [-1, 0, 1, 0],
      dy = [0, 1, 0, -1];
    for (let i = 0; i < 4; i++) {
      let nx = sx + dx[i],
        ny = sy + dy[i];
      if (!g[nx][ny] && this.check_connectivity(g, nx, ny, tx, ty)) {
        return true;
      }
    }
    return false;
  }
  create_walls() {
    const g = []; //墙为false
    for (let r = 0; r < this.rows; r++) {
      g[r] = [];
      for (let c = 0; c < this.cols; c++) {
        g[r][c] = false;
      }
    }
    //四周加上墙
    for (let r = 0; r < this.rows; r++) {
      g[r][0] = true;
      g[r][this.cols - 1] = true;
    }
    for (let c = 0; c < this.cols; c++) {
      g[0][c] = true;
      g[this.rows - 1][c] = true;
    }

    for (let i = 0; i < this.inner_walls_count; i++) {
      for (let j = 0; j < 1000; j++) {
        let r = parseInt(Math.random() * this.rows);
        let c = parseInt(Math.random() * this.cols);
        if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) {
          continue;
        }
        if (
          (r === this.rows - 2 && c === 1) ||
          (r === 1 && c === this.cols - 2)
        ) {
          continue;
        }
        //轴对称
        //g[r][c] = g[c][r] = true;
        //中心对称
        g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
        break;
      }
    }
    let copy_g = JSON.parse(JSON.stringify(g));
    if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) {
      return false;
    }
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (g[r][c]) {
          this.walls.push(new Wall(r, c, this));
        }
      }
    }
    return true;
  }

  start() {
    for (let i = 0; i < 1000; i++) {
      if (this.create_walls()) break;
    }
    this.add_listening_event();
  }
  //求出划分单元格的实际大小
  update_size() {
    this.L = parseInt(
      Math.min(
        this.parent.clientWidth / this.cols,
        this.parent.clientHeight / this.rows
      )
    );
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  update() {
    this.update_size();
    if (this.check_ready()) {
      this.next_step();
    }
    this.render();
  }
  render() {
    const color_even = "#AAD751",
      color_odd = "#A2D149";
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even;
        } else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
      }
    }
  }
}
