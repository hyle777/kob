export class Cell {
  constructor(r, c) {
    this.r = r;
    this.c = c;
    this.x = c + 0.5;
    this.y = r + 0.5;
  }
  setCell(r, c, x, y) {
    this.r = r;
    this.c = c;
    this.x = x;
    this.y = y;
  }
}
